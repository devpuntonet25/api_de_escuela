package ant.hgallgo.escuela.repository;

import ant.hgallgo.escuela.entity.Acudiente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AcudienteRepository extends JpaRepository<Acudiente, Long> {

    @Query(
            value = "SELECT * FROM tbl_acudiente a WHERE a.numero_celular LIKE :primerValor% " +
                    "OR a.numero_celular LIKE :segundoValor%",
            nativeQuery = true
    )
    List<Acudiente> encontrarPorNumeroCelularStartingWithOrNumeroCelularStartingWith(
            @Param("primerValor") Long primerValor,
            @Param("segundoValor") Long segundoValor
    );

    List<Acudiente> findByNombreContaining(String valor);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE tbl_acudiente a SET " +
            "a.nombre = :nombre , " +
            "a.apellido = :apellido , " +
            "a.numero_celular = :numeroCelular " +
            "WHERE a.id = :id",
            nativeQuery = true
    )
    void actualizarAcudiente(
           @Param("id") Long id,
           @Param("nombre") String nombre,
           @Param("apellido") String apellido,
           @Param("numeroCelular") Long numeroCelular);
}
