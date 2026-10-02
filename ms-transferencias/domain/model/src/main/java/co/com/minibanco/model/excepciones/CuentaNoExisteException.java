package co.com.minibanco.model.excepciones;

public class CuentaNoExisteException extends NegocioException {

    public CuentaNoExisteException(Long id) {
        super("La cuenta " + id + " no existe");
    }
}