package ant.hgallgo.escuela.validaciones;

public class ValidacionesParaLaEntidadDocente {
    public static int validarCampoOrdenamientoAndOrden(String campoOrdenamiento, String orden){

        if( !campoOrdenamiento.equalsIgnoreCase("id") && !campoOrdenamiento.equalsIgnoreCase("nombre")
        && !campoOrdenamiento.equalsIgnoreCase("apellido") && !campoOrdenamiento.equalsIgnoreCase("salario") ) {
            return 1;
        }

        if( !orden.equalsIgnoreCase("ascendente") && !orden.equalsIgnoreCase("descendente")
        && !orden.equalsIgnoreCase("asc") && !orden.equalsIgnoreCase("desc") ) {
            return 2;
        }

        return 0;

    }
}
