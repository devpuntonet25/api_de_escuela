package ant.hgallgo.escuela.repository;

import ant.hgallgo.escuela.entity.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {
    List<Docente> findBySalarioGreaterThanEqual(Long salario);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE tbl_docente d SET " +
                    "d.nombre = :nombre , " +
                    "d.apellido = :apellido , " +
                    "d.salario = :salario " +
                    "WHERE d.id = :id",
            nativeQuery = true
    )
    void actualizar(
           @Param("id") Long id,
           @Param("nombre") String nombre,
           @Param("apellido") String apellido,
           @Param("salario") Long salario
    );
}
