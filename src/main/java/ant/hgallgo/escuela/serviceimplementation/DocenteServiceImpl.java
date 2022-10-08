package ant.hgallgo.escuela.serviceimplementation;

import ant.hgallgo.escuela.entity.Docente;
import ant.hgallgo.escuela.repository.DocenteRepository;
import ant.hgallgo.escuela.service.DocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import static java.util.Comparator.*;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DocenteServiceImpl implements DocenteService {

    @Autowired
    private DocenteRepository docenteRepository;

    @Override
    public List<Docente> getTodosLosDocentes() {
        return docenteRepository.findAll();
    }

    @Override
    public Docente getDocentePorId(Long id) {
        try{
            return docenteRepository.findById(id).get();
        }catch(NoSuchElementException e){
            return null;
        }
    }

    @Override
    public List<Docente> getDocentesCuyoSalarioEsMayorOrIgualQue(Long salario) {
        return docenteRepository.findBySalarioGreaterThanEqual(salario);
    }

    @Override
    public Docente guardarDocente(Docente docente) {
        return docenteRepository.save(docente);
    }

    @Override
    public List<Docente> docentesOrdenados(String campoOrdenamiento, String orden) {
        //List<Passenger> passengers = repository.findAll(Sort.by(Sort.Direction.ASC, "seatNumber"));
        List<Docente> docentesOrdenados = new ArrayList<>();
        switch(orden.toLowerCase()) {
            case "ascendente", "asc" -> docentesOrdenados =  ordenamientoAscendente(campoOrdenamiento);
            case "descendente", "desc" -> docentesOrdenados =  ordenamientoDescendente(campoOrdenamiento);
        }

        return docentesOrdenados;
    }

    @Override
    public List<Docente> paginacionAndOrdenamiento(Integer pagina, Integer numeroRegistros, String campoOrdenamiento, String orden) {
        Pageable paginacionDocentes = PageRequest.of(pagina, numeroRegistros);
        List<Docente> docentesPaginados = docenteRepository.findAll(paginacionDocentes).getContent();
        List<Docente> docentesPaginadosParaOrdenar = new ArrayList<>(docentesPaginados);

        switch(orden.toUpperCase()) {
            case "ASCENDENTE", "ASC":
                    switch(campoOrdenamiento.toUpperCase()) {
                        case "ID" -> docentesPaginadosParaOrdenar.sort(comparing(Docente::getId));
                        case "NOMBRE" -> docentesPaginadosParaOrdenar.sort((a, b) -> a.getNombre().compareTo(b.getNombre()));
                        case "APELLIDO" -> docentesPaginadosParaOrdenar.sort(comparing(Docente::getApellido));
                        case "SALARIO" -> docentesPaginadosParaOrdenar.sort((a, b) -> a.getSalario().compareTo(b.getSalario()));
                    }
                    break;
            case "DESCENDENTE", "DESC":
                switch(campoOrdenamiento.toUpperCase()) {
                    case "ID" -> docentesPaginadosParaOrdenar.sort(comparing(Docente::getId).reversed());
                    case "NOMBRE" -> docentesPaginadosParaOrdenar.sort((a, b) -> b.getNombre().compareTo(a.getNombre()));
                    case "APELLIDO" -> docentesPaginadosParaOrdenar.sort(comparing(Docente::getApellido).reversed());
                    case "SALARIO" -> docentesPaginadosParaOrdenar.sort((a, b) -> b.getSalario().compareTo(a.getSalario()));
                }
                break;
        }

        return docentesPaginadosParaOrdenar;

    }

    @Override
    public Docente eliminar(Long id) {
        try {
            Docente docente = docenteRepository.findById(id).get();
            docenteRepository.deleteById(id);
            return docente;
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public String actualizar(Docente docente) {
        if ( docenteRepository.existsById(docente.getId()) ) {
            docenteRepository.actualizar(docente.getId(), docente.getNombre(),
                    docente.getApellido(), docente.getSalario());
            return "Se actualizó satisfactoriamente el docente con id \"" + docente.getId() + "\".";
        } else {
            return "No se pudo actualizar el docente con id \"" + docente.getId() + "\", porque no existe " +
                    "en la base de datos.";
        }
    }

    public List<Docente> ordenamientoAscendente(String campoOrdenamiento){
        List<Docente> docentes = new ArrayList<>();
        switch(campoOrdenamiento.toLowerCase()) {
            case "id" -> docentes = docenteRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
            case "nombre" -> docentes = docenteRepository.findAll(Sort.by(Sort.Direction.ASC, "nombre"));
            case "apellido" -> docentes = docenteRepository.findAll(Sort.by(Sort.Direction.ASC, "apellido"));
            case "salario" -> docentes = docenteRepository.findAll(Sort.by(Sort.Direction.ASC, "salario"));

        };

        return docentes;
    }
    public List<Docente> ordenamientoDescendente(String campoOrdenamiento){
        List<Docente> docentes = new ArrayList<>();
        switch(campoOrdenamiento.toLowerCase()) {
            case "id" -> docentes = docenteRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            case "nombre" -> docentes = docenteRepository.findAll(Sort.by(Sort.Direction.DESC, "nombre"));
            case "apellido" -> docentes = docenteRepository.findAll(Sort.by(Sort.Direction.DESC, "apellido"));
            case "salario" -> docentes = docenteRepository.findAll(Sort.by(Sort.Direction.DESC, "salario"));

        };

        return docentes;
    }
}
