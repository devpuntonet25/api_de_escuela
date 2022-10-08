package ant.hgallgo.escuela.controller;

import ant.hgallgo.escuela.entity.Acudiente;
import ant.hgallgo.escuela.entity.Estudiante;
import ant.hgallgo.escuela.service.AcudienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static ant.hgallgo.escuela.validaciones.ValidacionesNumericas.*;

import java.util.List;

@RestController
@RequestMapping("/api/acudiente")
@CrossOrigin(origins = "http://localhost:3000/")
public class AcudienteController {

    @Autowired
    private AcudienteService acudienteService;

    @PostMapping("/save")
    public ResponseEntity<Acudiente> save(@RequestBody Acudiente acudiente){
        return ResponseEntity.ok(acudienteService.save(acudiente));
    }

    @GetMapping("/acudientes")
    public ResponseEntity<List<Acudiente>> getAcudientes(){
        return ResponseEntity.ok(acudienteService.acudientes());
    }

    @Operation(
            summary = "Se utiliza para recibir un número de registros de la tabla acudientes, correspondientes " +
                    "a la página especificada y que serán ordenados en orden ascendente o descendente y por el campo que se " +
                    "determina al hacer la petición"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Se toman los acudientes con paginación",
                            content = { @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Acudiente.class)) }),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @GetMapping("/paginacion")
    public ResponseEntity<?> getAcudientesPaginados(
            @Parameter(description = "número de página para hacer la paginación. Por defecto es 0, es decir, la página 1")
            @RequestParam(value = "pagina", defaultValue = "0") String pagina,
            @Parameter(description = "número de registros a obtener para la página especificada. Por defecto es 3")
            @RequestParam(value = "numeroregistros", defaultValue = "3") String numeroRegistros,
            @Parameter(description = "campo de la entidad acudiente por el cual se quiere ordenar. Por defecto es el id")
            @RequestParam(value = "campoordenamiento", defaultValue = "id") String campoOrdenamiento,
            @Parameter(description = "orden para realizar el ordenamiento de la paginación. Por defecto es ascendente")
            @RequestParam(value = "orden", defaultValue = "ascendente") String orden
    ){
        if(!esEntero(pagina)){
            return new ResponseEntity<>(
                    "la página enviada \'" + pagina + "\' no corresponde a un número entero",
                    HttpStatus.NOT_ACCEPTABLE
                    );
        }
        if(!esEntero(numeroRegistros)){
            return new ResponseEntity<>(
                    "el número de registros enviado \'" + numeroRegistros + "\' no corresponde a un número entero",
                    HttpStatus.NOT_ACCEPTABLE
            );
        }

        return new ResponseEntity<>(
                acudienteService.paginacion(
                        Integer.parseInt(pagina),
                        Integer.parseInt(numeroRegistros),
                        campoOrdenamiento,
                        orden
                        ),
                HttpStatus.OK
        );

    }//fin método get acudientes paginados

    @Operation(
            summary = "Retorna los acudientes cuyo número de celular empieza con un valor o con un segundo valor"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Se toman los acudientes que cumplen la condición",
                            content = { @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Acudiente.class)) }),
                    @ApiResponse(responseCode = "400", description = "el primer o segundo valor enviado no corresponde a un número entero",
                            content = @Content),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @GetMapping("/numerocelularempiezacon")
    public ResponseEntity<?> obtenerAcudientesCuyoNumeroEmpiezaPorPrimerValorOrPorSegundoValor(
            @Parameter(description = "primer valor para la condición. Por defecto es 300")
            @RequestParam(value = "primerValor", defaultValue = "300") String primerValor,
            @Parameter(description = "segundo valor para la condición. Por defecto es 300")
            @RequestParam(value = "segundoValor", defaultValue = "300") String segundoValor
    ){
       if (!esEntero(primerValor)) {
           return new ResponseEntity<>(
                   primerValor + " ingresado en el campo primerValor, no es un número entero",
                   HttpStatus.BAD_REQUEST
           );
       }

       if (!esEntero(segundoValor)) {
            return new ResponseEntity<>(
                    segundoValor + " ingresado en el campo segundoValor, no es un número entero",
                    HttpStatus.BAD_REQUEST
            );
       }

       return new ResponseEntity<>(
               acudienteService.obtenerAcudientesCuyoNumeroEmpiezaPorPrimerValorOrPorSegundoValor(
                       Long.parseLong(primerValor),
                       Long.parseLong(segundoValor)
               ),
               HttpStatus.OK
       );
    }//fin método obtenerAcudientesCuyoNumeroEmpiezaPorPrimerValorOrPorSegundoValor


    @Operation(
            summary = "Retorna los acudientes cuyo nombre contiene el valor especificado"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna los acudientes que cumplen con la condición" +
                            " o un mensaje que indica que no hay acudientes cuyo nombre contenga ese valor enviado",
                            content = { @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Acudiente.class)) }),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @GetMapping("/acudientespornombre/{valor}")
    public ResponseEntity<?> obtenerAcudientesCuyoNombreContenga(
            @Parameter(description = "valor o cadena de caracteres por los cuales se quiere buscar" +
                    " los acudientes que contengan este valor en su nombre")
            @PathVariable("valor") String valor
    ){
        List<Acudiente> lista = acudienteService.findByNombreContaining(valor);

        if(lista.isEmpty()){
            return new ResponseEntity<>(
                    "no se encontraron acudientes cuyo nombre contenga el valor " + valor,
                    HttpStatus.OK
            );
        }

        return new ResponseEntity<>(
                lista,
                HttpStatus.OK
                );
    }//obtenerAcudientesCuyoNombreContenga

    @Operation(
            summary = "Recibe un objeto json de la entidad acudiente, lo convierte a la entidad acudiente" +
                    " y lo actualiza en la base de datos"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Actualizó de forma correcta al acudiente " +
                            "o no lo hizo porque no existe un acudiente con ese id en la base de datos",
                            content =  @Content),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarAcudiente(
            @Parameter(description = "objeto json respresentando la entidad acudiente a actualizar")
            @RequestBody Acudiente acudiente
    ){
        return new ResponseEntity<>(
                acudienteService.actualizarAcudiente(acudiente),
                HttpStatus.OK
        );
    }//fin método actualizar acudiente

    @Operation(
            summary = "Recibe el id de un acudiente que de existir lo eliminar en la base de datos"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Se elimina el acudiente con el id especificado, " +
                            "o no se elimina si no existe un acudiente con ese id",
                            content = { @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Acudiente.class)) }),
                    @ApiResponse(responseCode = "400", description = "el id ingresado no es un número entero",
                            content = @Content),
                    @ApiResponse(responseCode = "500", description = "error interno del servidor",
                            content = @Content)
            }
    )
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarAcudiente(
            @Parameter(description = "id del acudiente que se necesita eliminar de la base de datos")
            @PathVariable("id") String id
    ){
        if(!esEntero(id)){
            return new ResponseEntity<>(
                    "Error! el id " + id + " ingresado no es un número entero",
                    HttpStatus.BAD_REQUEST
            );
        }

        Acudiente acudiente = acudienteService.eliminarAcudiente(Long.parseLong(id));

        if(acudiente == null){
            return new ResponseEntity<>(
                    "NO se pudo eliminar el acudiente con el id " + id +
                            " ingresado, porque no existe para ningún acudiente en la base de datos",
                    HttpStatus.OK
            );
        }else{
            return new ResponseEntity<>(acudiente, HttpStatus.OK);
        }
    }//fin del método eliminar acudiente

}
