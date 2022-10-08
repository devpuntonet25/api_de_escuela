package ant.hgallgo.escuela.validaciones;

public class ValidacionesNumericas {
    public static boolean esEntero(String numero){
        boolean numeroEntero = true;

        try{
            Integer.parseInt(numero);
        }catch(NumberFormatException error){
            numeroEntero = false;
        }

        return numeroEntero;
    }
}
