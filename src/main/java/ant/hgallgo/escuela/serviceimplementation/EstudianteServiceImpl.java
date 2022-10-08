package ant.hgallgo.escuela.serviceimplementation;

import ant.hgallgo.escuela.entity.Estudiante;
import ant.hgallgo.escuela.repository.EstudianteRepository;
import ant.hgallgo.escuela.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EstudianteServiceImpl implements EstudianteService {


    @Autowired
    private EstudianteRepository estudianteRepository;

    @Override
    public Estudiante save(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    @Override
    public List<Estudiante> getMayoresDe(Integer edad) {
        return estudianteRepository.findByEdadGreaterThanEqual(edad);
    }

    @Override
    public Set<Estudiante> getEstudiantes() {
        return new HashSet<>(estudianteRepository.findAll());
    }

    @Override
    public Integer getNumeroEstudiantes() {
        return estudianteRepository.getNumeroEstudiantes();
    }

    @Override
    public Collection<Estudiante> getEstudiantesDondeNombreEmpieceConAndApellidoContenga(String starting, String containing) {
        return estudianteRepository.findByNombreStartingWithAndApellidoContaining(starting, containing);
    }

    @Override
    public List<Estudiante> getEstudiantesDondeEmailContengaAndOrderBy(String containing, String column) {
        return estudianteRepository.getEstudiantesDondeEmailContengaAndOrderBy(containing, column);
    }

    @Override
    public List<Estudiante> getEstudiantesDondeEmailContengaAndOrderByEdad(String containing) {
        return estudianteRepository.findByEmailContainingOrderByEdadAsc(containing);
    }

    @Override
    public List<Estudiante> getEstudiantesConPaginacionAndOrdenadosPor(Integer pagina, Integer numeroRegistros,
                                                                       String campoOrdenamiento, String orden) {

        Pageable pag = PageRequest.of(
                pagina,
                numeroRegistros
        );
        List<Estudiante> estudiantesPaginadosOriginal = estudianteRepository.findAll(pag).getContent();
        List<Estudiante> listaModificable = new ArrayList<>(estudiantesPaginadosOriginal);
        Pageable paginacion;
        switch(orden.toLowerCase()){
            case "descendente":
                //el siguiente switch escoge la forma adecuada de ordenar los estudiantes ya paginados en la linea anterior
                switch (campoOrdenamiento.toLowerCase()){
                    case "edad" -> listaModificable.sort(Comparator.comparing(Estudiante::getEdad).reversed());
                    case "nombre" -> listaModificable.sort(Comparator.comparing(Estudiante::getNombre).reversed());
                    case "apellido" -> listaModificable.sort(Comparator.comparing(Estudiante::getApellido).reversed());
                }
                break;
            case "ascendente":
                //el siguiente switch escoge la forma adecuada de ordenar los estudiantes ya paginados en la linea anterior
                switch (campoOrdenamiento.toLowerCase()){
                    case "edad" -> listaModificable.sort(Comparator.comparing(Estudiante::getEdad));

                    //si es por nombre, no es necesario sobrescribir el método, porque el compareTo que tiene ya ordena por el nombre
                    case "nombre" -> listaModificable.sort(Comparator.comparing(Estudiante::getNombre));

                    case "apellido" -> listaModificable.sort(Comparator.comparing(Estudiante::getApellido));
                }
                    break;
        }
        return listaModificable;
    }

    @Override
    public Estudiante getEstudiantePorId(Long id) {
        try{
            Estudiante e = estudianteRepository.findById(id).get();
            return e;
        }catch(NoSuchElementException ex){
            return null;
        }
    }

    @Override
    public String actualizar(Estudiante e) {
        if( estudianteRepository.existsById(e.getId()) ){
            estudianteRepository.actualizarEstudiante(
                    e.getId(), e.getNombre(), e.getApellido(),
                    e.getEmail(), e.getEdad()
            );
            return "se modificó exitosamente el estudiante con id " + e.getId();
        }else{
            return "no se puede modificar el estudiante porque no existe un estudiante con el id "
                    + e.getId() + " en la base de datos";
        }
    }

    @Override
    public Estudiante eliminar(Long id) {
        try {
            Estudiante est = estudianteRepository.findById(id).get();
            estudianteRepository.deleteById(id);

            return est;
        }catch (NoSuchElementException ex){
            return null;
        }
    }



}
