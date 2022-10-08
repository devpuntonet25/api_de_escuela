package ant.hgallgo.escuela.controller;

import ant.hgallgo.escuela.entity.Estudiante;
import ant.hgallgo.escuela.service.EstudianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/estudiante")
@CrossOrigin(origins = "http://localhost:3000/")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    @PostMapping("/save")
    public ResponseEntity<Estudiante> save(@RequestBody Estudiante estudiante){
        Estudiante estGuardado = estudianteService.save(estudiante);

        return ResponseEntity.ok(estGuardado);
    }

    @Operation(
            summary = "Retorna los estudiantes que tienen una edad mayor o igual a la indicada en el parámetro"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "petición realizada sin errores, retorna la lista de estudiantes que cumplen la condición" +
                            " o no retorna nada porque ningún estudiante cumplió la condición",
                            content = { @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Estudiante.class)) }),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @GetMapping("/mayorde/{edad}")
    public ResponseEntity<List<Estudiante>> getMayoresDe(
            @Parameter(description = "edad para construir la condición de la petición")
            @PathVariable("edad") Integer edad
    ){

        return ResponseEntity.ok(estudianteService.getMayoresDe(edad));
    }

    @GetMapping("/estudiantes")
    public ResponseEntity<Set<Estudiante>> getEstudiantes(){
        return ResponseEntity.ok(estudianteService.getEstudiantes());
    }

    @GetMapping("/numeroestudiantes")
    public ResponseEntity<Integer> getNumeroEstudiantes(){
        return ResponseEntity.ok(estudianteService.getNumeroEstudiantes());
    }

    @Operation(
            summary = "Retorna una lista de estudiantes que cumplen con la condición de que" +
                    " su nombre empieza con una cadena de caracteres determinada y además que su apellido" +
                    " contiene otra cadena de caracteres determinada"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "petición realizada sin errores, retorna la lista de estudiantes que cumplen la condición" +
                            " o no retorna nada porque ningún estudiante cumplió la condición",
                            content = { @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Estudiante.class)) }),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @GetMapping("/nombreempiezaconyapellidocontiene")
    public ResponseEntity<List<Estudiante>> getEstudiantesQueNombreEmpiezaConAndApellidoContiene(
            @Parameter(description = "cadena de caracteres para la condición de que el nombre empiece por estos")
            @RequestParam(value = "starting", defaultValue = "a") String starting,
            @Parameter(description = "cadena de caracteres para la condición de que el apellido contenga estos caracteres")
            @RequestParam(value = "containing", defaultValue = "a") String containing
    ){
        return ResponseEntity.ok(
                new ArrayList<>(
                        estudianteService.getEstudiantesDondeNombreEmpieceConAndApellidoContenga(starting, containing)
                )
        );
    }

    @Operation(
            summary = "Retorna una lista de estudiantes que cumplen con la condición de que" +
                    " su email contenga una cadena de caracteres, además ordena esa lista de estudiantes por el campo especificado"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "petición realizada sin errores, retorna la lista de estudiantes que cumplen la condición" +
                            " o no retorna nada porque ningún estudiante cumplió la condición",
                            content = { @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Estudiante.class)) }),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @GetMapping("/emailcontieneyordenadopor")
    public ResponseEntity<List<Estudiante>> getEstudiantesDondeEmailContengaAndOrdenadoPor(
            @Parameter(description = "cadena de caracteres por los cuales se quiere seleccionar " +
                    "los estudiantes que los contengan en su campo email (por defecto su valor es \'a\')"
            )
            @RequestParam(value = "containing", defaultValue = "a") String containing,
            @Parameter(description = "propiedad de la entidad estudiante por el cual se quiere aplicar el order by")
            @RequestParam(value = "orderBy" , defaultValue = "id") String orderBy
    ){
        return ResponseEntity.ok(
                estudianteService.getEstudiantesDondeEmailContengaAndOrderBy(containing, orderBy)
        );
    }

    @Operation(
            summary = "Retorna una lista de estudiantes que cumplen con la condición de que" +
                    " su email contenga una cadena de caracteres, además ordena esa lista de estudiantes por el campo edad"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "petición realizada sin errores, retorna la lista de estudiantes que cumplen la condición" +
                            " o no retorna nada porque ningún estudiante cumplió la condición",
                            content = { @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Estudiante.class)) }),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @GetMapping("/emailcontieneyordenadoporedad")
    public ResponseEntity<List<Estudiante>> getEstudiantesDondeEmailContengaAndOrdenadoPor(
            @Parameter(
                    description = "cadena de caracteres por los cuales se quiere seleccionar " +
                            "los estudiantes que los contengan en su campo email"
            )
            @RequestParam(value = "containing", defaultValue = "a") String containing
    ){
        return ResponseEntity.ok(
                estudianteService.getEstudiantesDondeEmailContengaAndOrderByEdad(containing)
        );
    }


    @Operation(
            summary = "Se utiliza para recibir un número de registros de la tabla estudiantes, correspondientes " +
                    "a la página especificada y que serán ordenados en orden ascendente o descendente y por el campo que se " +
                    "determina al hacer la petición"
    )
    @ApiResponses(
            value = {
            @ApiResponse(responseCode = "200", description = "Se toman los estudiantes con paginación",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Estudiante.class)) }),
            @ApiResponse(responseCode = "500", description = "error interno del servidor",
                    content = @Content)
                    }
    )
    @GetMapping("/paginacion")
    public ResponseEntity<List<Estudiante>> getPaginacion(
            @Parameter(description = "número de página para la paginación")
            @RequestParam(value = "pagina", defaultValue = "0") String pagina,
            @Parameter(description = "número de registros que se quieren obtener para la página")
            @RequestParam(value = "numeroRegistros", defaultValue = "4") String numeroRegistros,
            @Parameter(description = "campo de la entidad estudiante por el cual se hará el ordenamiento")
            @RequestParam(value = "campoOrdenamiento", defaultValue = "edad") String campoOrdenamiento,
            @Parameter(description = "ascendente/descendente, por defecto es ascendente y este campo es para indicar el tipo de ordenamiento")
            @RequestParam(value = "orden", defaultValue = "ascendente") String orden
    ){
        return ResponseEntity.ok(
                estudianteService.getEstudiantesConPaginacionAndOrdenadosPor(
                        Integer.parseInt(pagina),
                        Integer.parseInt(numeroRegistros),
                        campoOrdenamiento,
                        orden
                        )
        );
    }


    @Operation(
            summary = "Se utiliza para obtener un estudiante por su id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Se retorna el estudiante con el id especificado",
                            content = { @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Estudiante.class)) }),
                    @ApiResponse(responseCode = "204", description = "no se encontró un estudiante con el id especificado",
                            content = @Content),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @GetMapping("/estudiante/{id}")
    public ResponseEntity<?> getEstudiante(
            @Parameter(description = "id del estudiante que se quiere obtener")
            @PathVariable("id") String id
    ){
        try{
            Long llave = Long.parseLong(id);
            Estudiante e = estudianteService.getEstudiantePorId(llave);

            if(e == null){
                return new ResponseEntity<>(
                        "no existe un estudiante con el id " + id,
                        HttpStatus.NO_CONTENT
                );
            }else{
                return ResponseEntity.ok(e);
            }

        }catch(NumberFormatException e){
            return new ResponseEntity<>("el id ingresado no es un valor numérico", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Operation(
            summary = "Se utiliza para actualizar una o todas las propiedades de un estudiante"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Se actualizó el estudiante o no se pudo porque no existe un estudiante con el id indicado, sin embargo en ambos casos la petición no presenta error",
                            content = { @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Estudiante.class)) }),
                    @ApiResponse(responseCode = "400", description = "no se envío la entidad del estudiante para su actualización",
                            content = @Content),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarEstudiante(
            @Parameter(description = "Objeto en formato json del estudiante a actualizar")
            @RequestBody Estudiante e
    ){
        if(e == null){
            return new ResponseEntity<>(
                    "no se puede actualizar un estudiante porque no ha sido enviado al servidor",
                    HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(estudianteService.actualizar(e),
                    HttpStatus.OK);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarEstudiantePorId(@PathVariable("id") String id){
        try{
            Long llave = Long.parseLong(id);
            Estudiante est = estudianteService.eliminar(llave);

            if(est == null){
                return new ResponseEntity<>(
                        "No existe un estudiante con el id " + id,
                        HttpStatus.OK
                );
            }else{
                return new ResponseEntity<>(est, HttpStatus.OK);
            }
        }catch (NumberFormatException error){
            return new ResponseEntity<>(
                    "El id enviado no es de tipo numérico",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}
