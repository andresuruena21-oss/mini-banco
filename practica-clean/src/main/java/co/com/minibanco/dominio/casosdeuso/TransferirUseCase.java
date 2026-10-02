package co.com.minibanco.dominio.casosdeuso;
import co.com.minibanco.dominio.excepciones.CuentaNoExisteException;
import co.com.minibanco.dominio.excepciones.MontoInvalidoException;
import co.com.minibanco.dominio.excepciones.SaldoInsuficienteException;
import co.com.minibanco.dominio.gateways.CuentaRepository;
import co.com.minibanco.dominio.gateways.EventoGateway;
import co.com.minibanco.dominio.modelo.Cuenta;
import co.com.minibanco.dominio.modelo.Transferencia;

import java.math.BigDecimal;
import java.time.LocalDateTime;
public class TransferirUseCase {
    //pongo los dos contratos que ya creamos (interfaces)
    private final CuentaRepository cuentas;
    private final EventoGateway eventos;

    //se hace inyeccion de dependencias , me los entregan desde afuera
    public TransferirUseCase(CuentaRepository cuentas, EventoGateway eventos){
        this.cuentas=cuentas;
        this.eventos=eventos;
    }

    public Transferencia transferir(Long origenId, Long destinoId, BigDecimal monto) {

        //para el monto debe ser mayor a cero
        if (monto ==null || monto.signum()<=0){ //signun dice : si es positivo =1 si es cero = 0 si es negativo -1 , por eso es el <= 0
            throw new MontoInvalidoException();
        }

        //no se puede transferir a la misma cuenta
        if(destinoId.equals(origenId)){
            throw new MontoInvalidoException();
        }

        // buscar las dos cuentas , si alguna no existe error
        Cuenta origen =cuentas.buscarPorId(origenId)
                .orElseThrow(()-> new CuentaNoExisteException(origenId));
        Cuenta destino = cuentas.buscarPorId(destinoId)
                .orElseThrow(()-> new CuentaNoExisteException(destinoId));
        if(!origen.tieneSaldoPara(monto)){
            throw new SaldoInsuficienteException();
        }

        //mover el dinero y guardar
        cuentas.guardar(origen.debitar(monto));
        cuentas.guardar(destino.acreditar(monto));
        //avisar a todo el sistema que se hizo una transferencia
        Transferencia transferencia = new Transferencia(origenId, destinoId, monto, LocalDateTime.now());
        eventos.publicarTransferencia(transferencia);

        //entregar el comprobante
        return transferencia;
    }
}
