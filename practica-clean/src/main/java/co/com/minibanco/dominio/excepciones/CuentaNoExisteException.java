package co.com.minibanco.dominio.excepciones;

public class CuentaNoExisteException extends NegocioException {

    public CuentaNoExisteException(Long id) {
        super("La cuenta " + id + " no existe");
    }
}