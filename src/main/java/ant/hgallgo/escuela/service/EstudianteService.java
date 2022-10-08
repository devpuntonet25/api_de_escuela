package ant.hgallgo.escuela.service;

import ant.hgallgo.escuela.entity.Estudiante;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface EstudianteService {
    Estudiante save(Estudiante estudiante);

    List<Estudiante> getMayoresDe(Integer edad);

    Set<Estudiante> getEstudiantes();

    Integer getNumeroEstudiantes();

    Collection<Estudiante> getEstudiantesDondeNombreEmpieceConAndApellidoContenga(String starting, String containing);

    List<Estudiante> getEstudiantesDondeEmailContengaAndOrderBy(String containing, String column);

    List<Estudiante> getEstudiantesDondeEmailContengaAndOrderByEdad(String containing);

    List<Estudiante> getEstudiantesConPaginacionAndOrdenadosPor(Integer pagina, Integer numeroRegistros,
                                                                String campoOrdenamiento, String orden);

    Estudiante getEstudiantePorId(Long id);

    String actualizar(Estudiante e);

    Estudiante eliminar(Long id);
}
