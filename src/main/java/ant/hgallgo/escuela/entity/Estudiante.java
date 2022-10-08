package ant.hgallgo.escuela.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "tbl_estudiante",
        uniqueConstraints = @UniqueConstraint(//hace que el email sea único para cada estudiante
                name = "email_unico",
                columnNames = "email"
        )
)
public class Estudiante implements Comparable<Estudiante>{
    @Id
    @Getter
    @SequenceGenerator(
            name = "secuencia_estudiante",
            sequenceName = "secuencia_estudiante",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "secuencia_estudiante"
    )
    private Long id;

    @Column(nullable = false)
    @Getter
    @Setter
    private String nombre;

    @Column(nullable = false)
    @Getter
    @Setter
    private String apellido;

    @Column(nullable = false)
    @Getter
    @Setter
    private Integer edad;

    @Column(
            name = "email",
            nullable = false
    )
    @Getter
    @Setter
    private String email;

//    @JsonIgnore
    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER//al hacer un fetch, hace un join con la tabla acudiente
            //optional = false//hace que al guardar la info de un estudiante, también se tenga que guardar la info del acudiente

    )
    @JoinColumn(
            name = "id_acudiente",
            referencedColumnName = "id"
    )
    @Getter
    @Setter
    private Acudiente acudiente;

    @Override
    public int compareTo(Estudiante e) {
        return nombre.toLowerCase().compareTo(e.getNombre().toLowerCase());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estudiante that = (Estudiante) o;
        return Objects.equals(id, that.id) && Objects.equals(nombre, that.nombre) && Objects.equals(apellido, that.apellido) && Objects.equals(edad, that.edad) && Objects.equals(email, that.email) && Objects.equals(acudiente, that.acudiente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, apellido, edad, email, acudiente);
    }
}
