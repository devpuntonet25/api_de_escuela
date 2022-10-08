package ant.hgallgo.escuela.validaciones;

public class ValidacionesParaLaEntidadCurso {
    public static int validarCampoOrdenamientoAndOrden(String campoOrdenamiento, String orden) {
        if( !campoOrdenamiento.equalsIgnoreCase("id") && !campoOrdenamiento.equalsIgnoreCase("nombre")
                && !campoOrdenamiento.equalsIgnoreCase("numerocreditos") ) {
            return 1;
        }

        if( !orden.equalsIgnoreCase("ascendente") && !orden.equalsIgnoreCase("descendente") ) {
            return 2;
        }

        return 0;
    }
}
