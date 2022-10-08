package ant.hgallgo.escuela.controller;

import ant.hgallgo.escuela.entity.Docente;
import ant.hgallgo.escuela.service.DocenteService;
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
import static ant.hgallgo.escuela.validaciones.ValidacionesParaLaEntidadDocente.validarCampoOrdenamientoAndOrden;

import java.util.List;

@RestController
@RequestMapping("/api/docente")
@CrossOrigin(origins = "http://localhost:3000/")
public class DocenteController {

    @Autowired
    private DocenteService docenteService;

    @Operation(
            summary = "Retorna una lista con todos los docentes en la base de datos"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "petición realizada sin errores, retorna la lista de todos los docentes",
                            content = { @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Docente.class)
                            )
                            }
                    ),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @GetMapping("/docentes")
    public ResponseEntity<?> getTodosLosDocentes(){
        List<Docente> docentes = docenteService.getTodosLosDocentes();

        if ( docentes.isEmpty() ) {
            return new ResponseEntity<>("No hay docentes en la tbl_docente de la base de datos", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(docentes, HttpStatus.OK);
        }
    }//Fin método getTodosLosDocentes

    @Operation(
            summary = "Retorna un docente con el id ingresado para su busqueda"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "petición realizada sin errores, retorna el docente con el id especificado",
                            content = { @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Docente.class)
                            )
                            }
                    ),
                    @ApiResponse(responseCode = "406", description = "ERROR! el idDocente debe ser un número entero",
                            content = @Content),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @GetMapping("/docente/{idDocente}")
    public ResponseEntity<?> getDocentePorId(
            @Parameter(description = "id del docente que se quiere buscar y obtener")
            @PathVariable("idDocente") String idDocente
    ){
        if( !esEntero(idDocente) ) {
            return new ResponseEntity<>(
                    "Error! el id \"" + idDocente + "\" ingresado no es un número entero",
                    HttpStatus.NOT_ACCEPTABLE
                    );
        }

        Docente d = docenteService.getDocentePorId(Long.parseLong(idDocente));
        if(d == null) {
            return new ResponseEntity<>("No existe un docente con el id \"" + idDocente + "\" ingresado.", HttpStatus.OK);
        }else{
            return new ResponseEntity<>(d, HttpStatus.OK);
        }
    }//Fin método getDocentePorId

    @Operation(
            summary = "Retorna una lista de docentes cuyo salario sea >= que el especificado"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "petición realizada sin errores, retorna la lista de docentes que cumplen la condición " +
                            "o nada si ningún docente cumplió con la condición.",
                            content = { @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Docente.class)
                            )
                            }
                    ),
                    @ApiResponse(responseCode = "406", description = "ERROR! el salario debe ser un número entero, ejemplo 5000000",
                            content = @Content),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @GetMapping("/salariomayorigual/{salario}")
    public ResponseEntity<?> salarioMayorIgualQue(
            @Parameter(description = "salario para realizar la condición, docente.salario >= salario")
            @PathVariable("salario") String salario
    ){
        if ( !esEntero(salario) ) {
            return new ResponseEntity<>(
                    "Error! el salario indicado \"" + salario + "\" no es un número entero.",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        List<Docente> docentes = docenteService.getDocentesCuyoSalarioEsMayorOrIgualQue(Long.parseLong(salario));

        if ( docentes.isEmpty() ) {
            return new ResponseEntity<>(
                    "No hay docentes cuyo salario sea mayor o igual que " + salario,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(docentes, HttpStatus.OK);
        }
    }//Fin método salarioMayorIgualQue

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarDocente(
            @Parameter(description = "Objeto json de la entidad Docente a guardar")
            @RequestBody Docente docente
    ){
        return new ResponseEntity<>(
                docenteService.guardarDocente(docente),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Retorna una lista de docentes ordenados en el orden y por el campo indicado"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "petición realizada sin errores, retorna la lista de docentes ordenados " +
                                    "en el orden y por el campo indicado.",
                            content = { @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Docente.class)
                            )
                            }
                    ),
                    @ApiResponse(responseCode = "406", description = "ERROR! el campoOrdenamiento o el campo orden son inválidos",
                            content = @Content),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @GetMapping("/docentesordenados")
    public ResponseEntity<?> obtenerDocentesOrdenados(
            @Parameter(description = "campo por el cual se quiere ordenar la lista de los docentes. Por defecto es \"salario\".")
            @RequestParam(value = "campoOrdenamiento", defaultValue = "salario") String campoOrdenamiento,
            @Parameter(description = "orden en que se quiere ordenar la lista de docentes." +
                    " ascendente o descendente son los valores permitidos. Por defect es \"descendente\".")
            @RequestParam(value = "orden", defaultValue = "descendente") String orden
    ){
        if ( validarCampoOrdenamientoAndOrden(campoOrdenamiento, orden) == 1 ) {
            return new ResponseEntity<>(
                    "El valor asignado \"" + campoOrdenamiento + "\" al campoOrdenamiento no es válido, los valores admitidos son: "
                    + "id, nombre, apellido, salario",
                    HttpStatus.NOT_ACCEPTABLE
            );
        }

        if ( validarCampoOrdenamientoAndOrden(campoOrdenamiento, orden) == 2 ) {
            return new ResponseEntity<>(
                    "el valor asignado \"" + orden + "\" al campo orden no es válido, los valores admitidos son: "
                    + "ascendente, descendente, asc, desc. también pueden ser enviados en mayúsculas.",
                    HttpStatus.NOT_ACCEPTABLE
            );
        }

        List<Docente> docentes = docenteService.docentesOrdenados(campoOrdenamiento, orden);
        return new ResponseEntity<>(docentes, HttpStatus.OK);
    }//Fin método obtenerDocentesOrdenados

    @Operation(
            summary = "Retorna una lista de docentes ordenados en el orden y por el campo indicado," +
                    " y paginados por la página y con el número de registros indicados"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "petición realizada sin errores, retorna la lista de docentes ordenados " +
                                    "en el orden y por el campo indicado, y paginados.",
                            content = { @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Docente.class)
                            )
                            }
                    ),
                    @ApiResponse(responseCode = "406", description = "ERROR! el campoOrdenamiento o el campo orden son inválidos o " +
                            "la pagina o el numeroRegistros no son números enteros",
                            content = @Content),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @GetMapping("/paginacionyordenamiento")
    public ResponseEntity<?> paginacionAndOrdenamiento(
            @Parameter(description = "página para realizar la paginación. Por defecto es 0, es decir, página 1.")
            @RequestParam(value = "pagina", defaultValue = "0") String pagina,
            @Parameter(description = "número de registros que se quiere obtener para la página indicada." +
            " Por defecto es 3.")
            @RequestParam(value = "numeroRegistros", defaultValue = "3") String numeroRegistros,
            @Parameter(description = "campo por el cual se quiere ordenar. Por defecto es \"salario\"")
            @RequestParam(value = "campoOrdenamiento", defaultValue = "salario") String campoOrdenamiento,
            @RequestParam(value = "orden", defaultValue = "descendente") String orden
    ){
        if( !esEntero(pagina) ) {
            return new ResponseEntity<>(
                    "ERROR! la página \"" + pagina + "\", ingresada, no es un número entero",
                    HttpStatus.NOT_ACCEPTABLE
            );
        }

        if( !esEntero(numeroRegistros) ) {
            return new ResponseEntity<>(
                    "ERROR! el número de registros \"" + numeroRegistros + "\", ingresados, no es un número entero",
                    HttpStatus.NOT_ACCEPTABLE
            );
        }

        if ( validarCampoOrdenamientoAndOrden(campoOrdenamiento, orden) == 1 ) {
            return new ResponseEntity<>(
                    "El valor asignado \"" + campoOrdenamiento + "\" al campoOrdenamiento no es válido, los valores admitidos son: "
                            + "id, nombre, apellido, salario",
                    HttpStatus.NOT_ACCEPTABLE
            );
        }

        if ( validarCampoOrdenamientoAndOrden(campoOrdenamiento, orden) == 2 ) {
            return new ResponseEntity<>(
                    "el valor asignado \"" + orden + "\" al campo orden no es válido, los valores admitidos son: "
                            + "ascendente, descendente, asc, desc. también pueden ser enviados en mayúsculas.",
                    HttpStatus.NOT_ACCEPTABLE
            );
        }

        List<Docente> docentesPaginadosAndOrdenados = docenteService.paginacionAndOrdenamiento(
                Integer.parseInt(pagina),
                Integer.parseInt(numeroRegistros),
                campoOrdenamiento,
                orden
        );
        return new ResponseEntity<>(docentesPaginadosAndOrdenados, HttpStatus.OK);

    }//Fin método paginacionAndOrdenamiento

    @DeleteMapping("/eliminar/{idDocente}")
    public ResponseEntity<?> eliminarDocente(@PathVariable("idDocente") String idDocente){
        if ( !esEntero(idDocente) ) {
            return new ResponseEntity<>(
                    "ERROR! el id \"" + idDocente + "\" del docente ingresado, no es un número entero"
                    ,HttpStatus.NOT_ACCEPTABLE
            );
        }

        Docente d = docenteService.eliminar(Long.parseLong(idDocente));

        if ( d == null ) {
            return new ResponseEntity<>(
                    "No se encontró y por tanto no se eliminó el docente con id \"" + idDocente + "\""
                    ,HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(d, HttpStatus.OK);
        }

    }//Fin método eliminarDocente

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizar(@RequestBody Docente docente){
        return new ResponseEntity<>(docenteService.actualizar(docente), HttpStatus.OK);
    }
}
