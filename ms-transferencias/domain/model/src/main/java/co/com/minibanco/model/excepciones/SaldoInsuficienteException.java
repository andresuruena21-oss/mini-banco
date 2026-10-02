package co.com.minibanco.model.excepciones;
public class SaldoInsuficienteException extends NegocioException{
    public SaldoInsuficienteException(){
        super("El saldo de la cuenta de origen no es suficiente");
    }
}
