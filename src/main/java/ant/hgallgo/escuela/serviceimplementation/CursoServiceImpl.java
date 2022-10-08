package ant.hgallgo.escuela.serviceimplementation;

import ant.hgallgo.escuela.entity.Curso;
import ant.hgallgo.escuela.repository.CursoRepository;
import ant.hgallgo.escuela.repository.DocenteRepository;
import ant.hgallgo.escuela.repository.EstudianteRepository;
import ant.hgallgo.escuela.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private DocenteRepository docenteRepositoryNivelDeCurso;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Override
    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Override
    public List<Curso> getTodosLosCursos() {
        return cursoRepository.findAll();
    }

    @Override
    public Curso getCursoPorId(Long id) {
        try{
            return cursoRepository.findById(id).get();
        }catch(NoSuchElementException error){
            return null;
        }
    }

    @Override
    public String asignarDocenteAlCurso(Long idCurso, Long idDocente) {
        if(!cursoRepository.existsById(idCurso)){
            return "No existe un curso con el id " + idCurso;
        }

        if(!docenteRepositoryNivelDeCurso.existsById(idDocente)){
            return "No existe un docente con el id " + idDocente;
        }

        cursoRepository.asignarDocenteAlCurso(idCurso, idDocente);
        return "Se asignó exitosamente el docente con id \"" + idDocente + "\" al curso con id \"" + idCurso + "\"";

    }

    @Override
    public String matricularEstudianteAlCurso(Long idCurso, Long idEstudiante) {
        if ( !cursoRepository.existsById(idCurso) ) {
            return "No existe un curso con el id " + idCurso;
        }
        if( !estudianteRepository.existsById(idEstudiante) ) {
            return "No existe un estudiante con el id " + idEstudiante;
        }

        cursoRepository.matricularEstudianteAlCurso(idCurso, idEstudiante);
        return "El estudiante con id " + idEstudiante + " ha sido matriculado satisfactoriamente al curso con id " + idCurso;

    }

    @Override
    public List<Curso> getCursosOrdenados(String campoOrdenamiento, String orden) {
        //List<Passenger> passengers = repository.findAll(Sort.by(Sort.Direction.ASC, "seatNumber"));
        List<Curso> cursosOrdenados = new ArrayList<>();
        switch(orden.toLowerCase()) {
            case "ascendente":
                switch(campoOrdenamiento.toLowerCase()) {
                    case "id" ->
                            cursosOrdenados = cursoRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
                    case "nombre" ->
                            cursosOrdenados = cursoRepository.findAll(Sort.by(Sort.Direction.ASC, "nombre"));
                    case "numerocreditos" ->
                            cursosOrdenados = cursoRepository.findAll(Sort.by(Sort.Direction.ASC, "numeroCreditos"));
                }
                break;
            case "descendente":
                switch(campoOrdenamiento.toLowerCase()) {
                    case "id" ->
                            cursosOrdenados = cursoRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
                    case "nombre" ->
                            cursosOrdenados = cursoRepository.findAll(Sort.by(Sort.Direction.DESC, "nombre"));
                    case "numerocreditos" ->
                            cursosOrdenados = cursoRepository.findAll(Sort.by(Sort.Direction.DESC, "numeroCreditos"));
                }
                break;
        }

        return cursosOrdenados;
    }

    @Override
    public List<Curso> paginacionAndOrdenamiento(Integer pagina, Integer numeroRegistrosPorPagina, String campoOrdenamiento, String orden) {
        Pageable paginacion = PageRequest.of(
          pagina,
          numeroRegistrosPorPagina
        );
        List<Curso> cursosPaginados = cursoRepository.findAll(paginacion).getContent();
        List<Curso> cursosOrdenados = new ArrayList<>(cursosPaginados);
        switch (orden.toLowerCase()) {
            case "ascendente":
                switch (campoOrdenamiento.toLowerCase()) {
                    case "id" -> cursosOrdenados.sort(Comparator.comparing(Curso::getId));
                    case "nombre" -> cursosOrdenados.sort(Comparator.comparing(Curso::getNombre));
                    case "numeroCreditos" -> cursosOrdenados.sort(Comparator.comparing(Curso::getNumeroCreditos));
                }
                break;
            case "descendente":
                switch (campoOrdenamiento.toLowerCase()) {
                    case "id" -> cursosOrdenados.sort(Comparator.comparing(Curso::getId).reversed());
                    case "nombre" -> cursosOrdenados.sort(Comparator.comparing(Curso::getNombre).reversed());
                    case "numeroCreditos" -> cursosOrdenados.sort(Comparator.comparing(Curso::getNumeroCreditos).reversed());
                }
                break;
        }

        return cursosOrdenados;
    }

    @Override
    public String actualizarCurso(Curso curso) {
        cursoRepository.actualizarCurso(curso.getId(), curso.getNombre(), curso.getNumeroCreditos());
        return "el curso " + curso.getId() + ", " + curso.getNombre() + " se modificó exitosamente";
    }

    @Override
    public Curso eliminar(Long idCurso) {
        try {
            Curso c = cursoRepository.findById(idCurso).get();
            cursoRepository.deleteById(idCurso);
            return c;
        }catch (NoSuchElementException error){
            return null;
        }
    }
}
