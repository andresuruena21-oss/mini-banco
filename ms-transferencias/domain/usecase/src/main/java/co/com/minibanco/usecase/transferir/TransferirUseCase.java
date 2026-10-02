package co.com.minibanco.usecase.transferir;

import co.com.minibanco.model.cuenta.Cuenta;
import co.com.minibanco.model.cuenta.gateways.CuentaRepository;
import co.com.minibanco.model.excepciones.CuentaNoExisteException;
import co.com.minibanco.model.excepciones.MontoInvalidoException;
import co.com.minibanco.model.excepciones.SaldoInsuficienteException;
import co.com.minibanco.model.transferencia.Transferencia;
import co.com.minibanco.model.transferencia.gateways.EventoGateway;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TransferirUseCase {

    private final CuentaRepository cuentas;
    private final EventoGateway eventos;

    public Transferencia transferir(Long origenId, Long destinoId, BigDecimal monto) {

        // 1. El monto debe ser mayor a cero
        if (monto == null || monto.signum() <= 0) {
            throw new MontoInvalidoException();
        }

        // 2. No se puede transferir a la misma cuenta
        if (origenId.equals(destinoId)) {
            throw new MontoInvalidoException();
        }

        // 3 y 4. Buscar las dos cuentas
        Cuenta origen = cuentas.buscarPorId(origenId)
                .orElseThrow(() -> new CuentaNoExisteException(origenId));
        Cuenta destino = cuentas.buscarPorId(destinoId)
                .orElseThrow(() -> new CuentaNoExisteException(destinoId));

        // 5. Revisar el saldo
        if (!origen.tieneSaldoPara(monto)) {
            throw new SaldoInsuficienteException();
        }

        // 6. Mover el dinero y guardar
        cuentas.guardar(origen.debitar(monto));
        cuentas.guardar(destino.acreditar(monto));

        // 7. Avisar
        // 7. Avisar que hubo una transferencia

        // Ya no usamos "new Transferencia(...)".
        // El builder crea el objeto, y nosotros solo llenamos cada campo por su nombre.
        Transferencia transferencia = Transferencia.builder()   // abro el "formulario" para crear una transferencia
                .origen(origenId)                               // casilla origen: el número de la cuenta que envía
                .destino(destinoId)                             // casilla destino: el número de la cuenta que recibe
                .monto(monto)                                   // casilla monto: cuánto se transfiere
                .fecha(LocalDateTime.now())                     // casilla fecha: la fecha y hora de este momento
                .build();                                       // "listo, créalo": aquí, por dentro, se hace el new

        // El builder solo CREA la transferencia; quien avisa es esta línea:
        eventos.publicarTransferencia(transferencia);

        // 8. Entregar el comprobante
        return transferencia;
    }
}