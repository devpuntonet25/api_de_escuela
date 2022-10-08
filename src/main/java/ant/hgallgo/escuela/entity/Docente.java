package ant.hgallgo.escuela.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_docente")
public class Docente implements Comparable<Docente>{
    @Id
    @SequenceGenerator(
            name = "secuencia_docente",
            sequenceName = "secuencia_docente",
            allocationSize = 100
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "secuencia_docente"
    )
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private Long salario;

    @Override
    public int compareTo(Docente d) {
        return id.compareTo(d.getId());
    }
}
