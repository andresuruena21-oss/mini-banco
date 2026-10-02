package co.com.minibanco.model.excepciones;
public class NegocioException extends RuntimeException{

    public NegocioException(String mensaje){
        super(mensaje);
    }
}
