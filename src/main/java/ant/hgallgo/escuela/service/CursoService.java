package ant.hgallgo.escuela.service;

import ant.hgallgo.escuela.entity.Curso;

import java.util.List;

public interface CursoService {
   Curso guardar(Curso curso);

   List<Curso> getTodosLosCursos();

   Curso getCursoPorId(Long id);

   String asignarDocenteAlCurso(Long idCurso, Long idDocente);

   String matricularEstudianteAlCurso(Long idCurso, Long idEstudiante);

   List<Curso> getCursosOrdenados(String campoOrdenamiento, String orden);

   List<Curso> paginacionAndOrdenamiento(Integer pagina, Integer numeroRegistrosPorPagina, String campoOrdenamiento, String orden);

   String actualizarCurso(Curso curso);

   Curso eliminar(Long idCurso);
}
