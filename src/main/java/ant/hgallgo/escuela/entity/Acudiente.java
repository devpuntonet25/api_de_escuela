package ant.hgallgo.escuela.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_acudiente")
public class Acudiente implements Comparable<Acudiente>{

    @Id
    @SequenceGenerator(
            name = "secuencia_acudiente",
            sequenceName = "secuencia_acudiente",
            allocationSize = 10
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "secuencia_acudiente"
    )
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private Long numeroCelular;

//    @JsonIgnore
//    @OneToMany(
//            mappedBy = "acudiente",
//            fetch =  FetchType.EAGER
//    )
//    private List<Estudiante> estudiantes;
//
//    public List<Estudiante> getEstudiantes() {
//        return estudiantes;
//    }
//
//    public void setEstudiantes(List<Estudiante> estudiantes) {
//        this.estudiantes = estudiantes;
//    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Long getNumeroCelular() {
        return numeroCelular;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setNumeroCelular(Long numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    @Override
    public int compareTo(Acudiente a) {
        return id.compareTo(a.getId());
    }
}
