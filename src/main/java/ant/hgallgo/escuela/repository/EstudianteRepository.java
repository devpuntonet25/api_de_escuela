package ant.hgallgo.escuela.repository;

import ant.hgallgo.escuela.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    List<Estudiante> findByEdadGreaterThanEqual(Integer edad);

    @Query(
            value = "SELECT COUNT(*) FROM tbl_estudiante",
            nativeQuery = true
    )
    Integer getNumeroEstudiantes();

    Collection<Estudiante> findByNombreStartingWithAndApellidoContaining(String starting, String containing);

    @Query(
            value = "SELECT * FROM tbl_estudiante e WHERE e.email like %:containing% " +
                    "\r\nORDER BY :column",
            nativeQuery = true
    )
    List<Estudiante> getEstudiantesDondeEmailContengaAndOrderBy(
           @Param("containing") String containing,
           @Param("column") String column
    );

    List<Estudiante> findByEmailContainingOrderByEdadAsc(String containing);


    @Modifying
    @Transactional
    @Query(
            value = "UPDATE tbl_estudiante " +
            "SET nombre = :nombre , " +
            "apellido = :apellido , " +
            "email = :email , " +
            "edad = :edad " +
            "WHERE id = :id",
            nativeQuery = true
    )
    void actualizarEstudiante(
            @Param("id") Long id,
            @Param("nombre") String nombre,
            @Param("apellido") String apellido,
            @Param("email") String email,
            @Param("edad") Integer edad
            );

}
