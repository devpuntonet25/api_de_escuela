package ant.hgallgo.escuela.controller;

import ant.hgallgo.escuela.entity.Curso;
import ant.hgallgo.escuela.service.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static ant.hgallgo.escuela.validaciones.ValidacionesNumericas.esEntero;
import static ant.hgallgo.escuela.validaciones.ValidacionesParaLaEntidadCurso.validarCampoOrdenamientoAndOrden;

import java.util.List;

@RestController
@RequestMapping("/api/curso")
@CrossOrigin(origins = "http://localhost:3000/")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarCurso(
            @Parameter(description = "objeto json del curso que se quiere guardar en la base de datos")
            @RequestBody Curso curso
    ){
        return new ResponseEntity<>(
                cursoService.guardar(curso),
                HttpStatus.OK
                );
    }

    @Operation(
            summary = "Retorna una lista con todos los cursos en la base de datos"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "petición realizada sin errores, retorna la lista de todos los cursos",
                            content = { @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Curso.class)
                            )
                            }
                    ),
                    @ApiResponse(responseCode = "204", description = "No hay registros en tbl_curso",
                            content = @Content),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @GetMapping("/cursos")
    public ResponseEntity<?> getCursos(){
        List<Curso> cursos = cursoService.getTodosLosCursos();

        if(cursos.isEmpty()){
            return new ResponseEntity<>(
                    "No hay cursos en la base de datos",
                    HttpStatus.NO_CONTENT
            );
        }else{
            return new ResponseEntity<>(cursos, HttpStatus.OK);
        }
    }//fin método getCursos

    @Operation(
            summary = "Busca y retorna el curso con el id enviado"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "petición realizada sin errores, retorna el curso buscado",
                            content = { @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Curso.class)
                            )
                            }
                    ),
                    @ApiResponse(responseCode = "204", description = "No existe un curso con el id especificado en la base de datos",
                            content = @Content),
                    @ApiResponse(responseCode = "406", description = "Error, el id ingresado no es un número entero",
                            content = @Content),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @GetMapping("/buscarcursoporid/{idCurso}")
    public ResponseEntity<?> buscarCursoPorId(
            @Parameter(description = "id del curso que se necesita buscar y obtener")
            @PathVariable("idCurso") String idCurso
            ){
        if(!esEntero(idCurso)){
            return new ResponseEntity<>(
                    "ERROR! el id \"" + idCurso + "\" ingresado, no es un número entero",
                    HttpStatus.NOT_ACCEPTABLE
            );
        }

        Curso curso = cursoService.getCursoPorId(Long.parseLong(idCurso));

        if(curso == null){
            return new ResponseEntity<>(
                    "no se encontró un curso con el id " + idCurso + " ingresado",
                    HttpStatus.NO_CONTENT
            );
        }else{
            return new ResponseEntity<>(curso, HttpStatus.OK);
        }
    }//fin método buscarCursoPorId

    @Operation(
            summary = "Asigna el un docente a un curso por sus llaves primarias o id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "petición realizada sin errores",
                            content = @Content),
                    @ApiResponse(responseCode = "400", description = "Error, los parámetros idCurso y idDocente son obligatorios",
                            content = @Content),
                    @ApiResponse(responseCode = "406", description = "Error, idCurso o idDocente no son números enteros",
                            content = @Content),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @PutMapping("/asignardocente")
    public ResponseEntity<?> asignarDocente(
            @RequestParam(value = "idCurso") String idCurso,
            @RequestParam(value = "idDocente") String idDocente
    ){
        if(idCurso.isEmpty() || idDocente.isEmpty()){
            return new ResponseEntity<>("los parámetros idCurso y idDocente son obligatorios", HttpStatus.BAD_REQUEST);
        }

        if(!esEntero(idCurso)){
            return new ResponseEntity<>(
                    "ERROR! el id \"" + idCurso + "\" ingresado, no es un número entero",
                    HttpStatus.NOT_ACCEPTABLE
            );
        }

        if(!esEntero(idDocente)){
            return new ResponseEntity<>(
                    "ERROR! el id \"" + idDocente + "\" ingresado, no es un número entero",
                    HttpStatus.NOT_ACCEPTABLE
            );
        }

        return new ResponseEntity<>(
                cursoService.asignarDocenteAlCurso(
                Long.parseLong(idCurso),
                Long.parseLong(idDocente)
                ),
                HttpStatus.OK
        );

    }//fin método asignar docente

    @Operation(
            summary = "Matricula un estudiante en un curso por sus llaves primarias o id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "petición realizada sin errores",
                            content = @Content),
                    @ApiResponse(responseCode = "400", description = "Error, los parámetros idCurso y idEstudiante son obligatorios",
                            content = @Content),
                    @ApiResponse(responseCode = "406", description = "Error, idCurso o idEstudiante no son números enteros",
                            content = @Content),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @PostMapping("/matricularestudiante")
    public ResponseEntity<?> matricularEstudianteAlCurso(
            @Parameter(description = "id del curso al cual se va a matricular el estudiante")
            @RequestParam(value = "idCurso") String idCurso,
            @Parameter(description = "id del estudiante que se va a matricular en el curso")
            @RequestParam(value = "idEstudiante") String idEstudiante
    ){
        if ( idCurso.isEmpty() || idEstudiante.isEmpty() ) {
            return new ResponseEntity<>(
                    "los parámetros idCurso y idEstudiante son obligatorios",
                    HttpStatus.BAD_REQUEST
            );
        }

        if( !esEntero(idCurso) ) {
            return new ResponseEntity<>(
                    "ERROR! el idCurso \"" + idCurso + "\" ingresado, no es un número entero",
                    HttpStatus.NOT_ACCEPTABLE
            );
        }

        if( !esEntero(idEstudiante) ) {
            return new ResponseEntity<>(
                    "ERROR! el idEstudiante \"" + idEstudiante + "\" ingresado, no es un número entero",
                    HttpStatus.NOT_ACCEPTABLE
            );
        }

        String respuesta = cursoService.matricularEstudianteAlCurso(
                Long.parseLong(idCurso),
                Long.parseLong(idEstudiante)
        );

        return new ResponseEntity<>(respuesta, HttpStatus.OK);

    }//fin método matricularEstudianteAlCurso

    @Operation(
            summary = "Retorna una lista de todos los cursos ordenados por el campo y orden indicado"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "petición realizada sin errores, retorna la lista de cursos ordenada",
                            content = { @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Curso.class)
                            )
                            }
                    ),
                    @ApiResponse(responseCode = "406", description = "Error, los campos, campoOrdenamiento y orden, deben ser valores válidos",
                            content = @Content),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @GetMapping("cursosordenados")
    public ResponseEntity<?> getCursosOrdenados(
            @Parameter(description = "campo por el cual se quieren ordenar los cursos. Por defecto es el id")
            @RequestParam(value = "campoOrdenamiento", defaultValue = "id") String campoOrdenamiento,
            @Parameter(description = "Orden en que se quieren ordenar los cursos. Por defecto es descendente")
            @RequestParam(value = "orden", defaultValue = "descendente") String orden
    ){
        if(validarCampoOrdenamientoAndOrden(campoOrdenamiento, orden) == 1 ) {
            return new ResponseEntity<>(
                    "ERROR! el campoOrdenamiento " + campoOrdenamiento +
                            " enviado, no es válido para ordenar, debe ser uno de los siguientes:\r\n " +
                            "id, nombre, numeroCreditos",
                    HttpStatus.NOT_ACCEPTABLE
            );
        }

        if( validarCampoOrdenamientoAndOrden(campoOrdenamiento, orden) == 2 ) {
            return new ResponseEntity<>(
                    "ERROR! el campo orden " + orden + " enviado, no es válido para ordenar, debe ser :\r\n " +
                    "ascendente o descendente",
                    HttpStatus.NOT_ACCEPTABLE
            );
        }


        return new ResponseEntity<>(
                cursoService.getCursosOrdenados(campoOrdenamiento, orden),
                HttpStatus.OK
        );
    }//Fin método getCursosOrdenados

    @Operation(
            summary = "Retorna una lista de todos los cursos ordenados por el campo y orden indicado" +
                    " y paginados por la página y según el número de registros por esa página indicados"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "petición realizada sin errores, retorna la lista de cursos ordenada y paginada",
                            content = { @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Curso.class)
                            )
                            }
                    ),
                    @ApiResponse(responseCode = "406", description = "Error, los campos, campoOrdenamiento y orden, deben ser valores válidos" +
                            " y los campos página y numeroRegistrosPorPagina deben ser números enteros",
                            content = @Content),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @GetMapping("/paginacionyordenamiento")
    public ResponseEntity<?> getCursosPaginadosAndOrdenados(
            @Parameter(description = "número de página para hacer la paginación. Por defecto es 0, es decir, la página 1.")
            @RequestParam(value = "pagina", defaultValue = "0") String pagina,
            @Parameter(description = "número de registros que se quieren traer para la página indicada en la paginación." +
            " Por defecto es 3.")
            @RequestParam(value = "numeroRegistrosPorPagina", defaultValue = "3") String numeroRegistrosPorPagina,
            @Parameter(description = "campo de la entidad curso por el cual se quiere ordenar. Por defecto es \"nombre\".")
            @RequestParam(value = "campoOrdenamiento", defaultValue = "nombre") String campoOrdenamiento,
            @Parameter(description = "orden por el cual se quiere hacer el ordenamiento. Por defecto es \"ascendente\".")
            @RequestParam(value = "orden", defaultValue = "ascendente") String orden
    ) {
        if(validarCampoOrdenamientoAndOrden(campoOrdenamiento, orden) == 1 ) {
            return new ResponseEntity<>(
                    "ERROR! el campoOrdenamiento " + campoOrdenamiento +
                            " enviado, no es válido para ordenar, debe ser uno de los siguientes:\r\n " +
                            "id, nombre, numeroCreditos",
                    HttpStatus.NOT_ACCEPTABLE
            );
        }

        if( validarCampoOrdenamientoAndOrden(campoOrdenamiento, orden) == 2 ) {
            return new ResponseEntity<>(
                    "ERROR! el campo orden " + orden + " enviado, no es válido para ordenar, debe ser :\r\n " +
                            "ascendente o descendente",
                    HttpStatus.NOT_ACCEPTABLE
            );
        }

        if ( !esEntero(pagina) ) {
            return new ResponseEntity<>(
                    "ERROR! el número de página \"" + pagina + "\" ingresado, no es un número entero",
                    HttpStatus.NOT_ACCEPTABLE
            );
        }

        if ( !esEntero(numeroRegistrosPorPagina) ) {
            return new ResponseEntity<>(
                    "ERROR! el campo numeroRegistrosPorPagina \"" + numeroRegistrosPorPagina
                            + "\" ingresado, no es un número entero",
                    HttpStatus.NOT_ACCEPTABLE
            );
        }

        return new ResponseEntity<>(cursoService.paginacionAndOrdenamiento(
                Integer.parseInt(pagina),
                Integer.parseInt(numeroRegistrosPorPagina),
                campoOrdenamiento,
                orden),
                HttpStatus.OK);
    }// Fin método getCursosPaginadosAndOrdenados

    @Operation(
            summary = "Permite actualizar las propiedades de una entidad curso a excepción del id_docente y del id del curso." +
                    " Para actualizar el docente se debe utilizar el endpoint o servicio de \"asignardocente\""
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "petición realizada sin errores",
                            content = @Content),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarCurso(
            @RequestBody Curso curso
    ) {
        return new ResponseEntity<>(
          cursoService.actualizarCurso(curso),
          HttpStatus.OK
        );
    }//Fin método actualizarCurso

    @DeleteMapping("/eliminar/{idCurso}")
    public ResponseEntity<?> eliminar(@PathVariable("idCurso") String idCurso){
        if ( !esEntero(idCurso) ) {
            return new ResponseEntity<>(
                    "ERROR! el id \"" + idCurso + "\" ingresado no es un número entero"
                    , HttpStatus.NOT_ACCEPTABLE);
        }

        Curso c = cursoService.eliminar(Long.parseLong(idCurso));

        if(c == null){
            return new ResponseEntity<>("No existe un curso con el id \"" + idCurso + "\" ingresado", HttpStatus.OK);
        }else{
            return new ResponseEntity<>(c, HttpStatus.OK);
        }
    }

}
