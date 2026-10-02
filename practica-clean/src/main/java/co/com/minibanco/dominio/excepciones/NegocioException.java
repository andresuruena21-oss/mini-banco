package co.com.minibanco.dominio.excepciones;

public class NegocioException extends RuntimeException{

    public NegocioException(String mensaje){
        super(mensaje);
    }
}
