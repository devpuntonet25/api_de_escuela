package ant.hgallgo.escuela.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_curso")
public class Curso {

    @Id
    @SequenceGenerator(
            name = "secuencia_curso",
            sequenceName = "secuencia_curso",
            allocationSize = 5
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "secuencia_curso"
    )
    private Long id;
    private String nombre;
    private Integer numeroCreditos;

    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "tbl_curso_estudiantes_mapping",
            joinColumns = @JoinColumn(
                    name = "id_curso",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "id_estudiante",
                    referencedColumnName = "id"
            )
    )
    private List<Estudiante> estudiantesMatriculados;

    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "id_docente",
            referencedColumnName = "id"
    )
    private Docente docente;
}
