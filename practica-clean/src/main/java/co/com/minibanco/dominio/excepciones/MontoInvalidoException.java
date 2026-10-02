package co.com.minibanco.dominio.excepciones;

public class MontoInvalidoException extends NegocioException {

    public MontoInvalidoException() {
        super("El monto debe ser mayor a cero");
    }
}