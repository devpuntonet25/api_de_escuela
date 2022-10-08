package ant.hgallgo.escuela.repository;

import ant.hgallgo.escuela.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    @Modifying
    @Transactional
    @Query(
            value = "UPDATE tbl_curso c SET " +
            "c.id_docente = :idDocente " +
            "WHERE c.id = :idCurso",
            nativeQuery = true
    )
    void asignarDocenteAlCurso(
            @Param("idCurso")
            Long idCurso,
            @Param("idDocente")
            Long idDocente
    );

    @Modifying
    @Transactional
    @Query(
            value = "INSERT INTO tbl_curso_estudiantes_mapping (id_curso, id_estudiante) " +
            "VALUES (:idCurso, :idEstudiante)",
            nativeQuery = true
    )
    void matricularEstudianteAlCurso(
            @Param("idCurso") Long idCurso,
            @Param("idEstudiante") Long idEstudiante);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE tbl_curso c SET " +
            "c.nombre = :nombre , " +
            "c.numero_creditos = :numeroCreditos " +
            "WHERE c.id = :idCurso",
            nativeQuery = true
    )
    void actualizarCurso(
           @Param("idCurso") Long idCurso,
           @Param("nombre") String nombre,
           @Param("numeroCreditos") Integer numeroCreditos);
}
