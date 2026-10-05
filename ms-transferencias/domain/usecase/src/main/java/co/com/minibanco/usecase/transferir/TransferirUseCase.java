package co.com.minibanco.usecase.transferir;

import co.com.minibanco.model.cuenta.Cuenta;
import co.com.minibanco.model.cuenta.gateways.CuentaRepository;
import co.com.minibanco.model.excepciones.CuentaNoExisteException;
import co.com.minibanco.model.excepciones.MontoInvalidoException;
import co.com.minibanco.model.excepciones.SaldoInsuficienteException;
import co.com.minibanco.model.transferencia.Transferencia;
import co.com.minibanco.model.transferencia.gateways.EventoGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TransferirUseCase {

    private final CuentaRepository cuentas;
    private final EventoGateway eventos;

    public Mono<Transferencia> transferir(Long origenId, Long destinoId, BigDecimal monto) {

        // 1 y 2. Validaciones rápidas: en vez de "throw", se DEVUELVE un Mono con el error
        if (monto == null || monto.signum() <= 0) {
            return Mono.error(new MontoInvalidoException());
        }
        if (origenId.equals(destinoId)) {
            return Mono.error(new MontoInvalidoException());
        }

        // 3 y 4. Buscar las dos cuentas AL MISMO TIEMPO con zip
        return Mono.zip(buscar(origenId), buscar(destinoId))
                // 5 y 6. Con las dos cuentas: revisar saldo y mover el dinero (devuelve otro Mono → flatMap)
                .flatMap(par -> moverDinero(par.getT1(), par.getT2(), monto))
                // 7. Cuando termine de mover el dinero, crear el comprobante
                .then(Mono.fromSupplier(() -> Transferencia.builder()
                        .origen(origenId)
                        .destino(destinoId)
                        .monto(monto)
                        .fecha(LocalDateTime.now())
                        .build()))
                // 8. Publicar el evento y, cuando termine, entregar el comprobante
                .flatMap(transferencia -> eventos.publicarTransferencia(transferencia)
                        .thenReturn(transferencia));
    }

    // Busca una cuenta; si la caja llega vacía, error (el orElseThrow reactivo)
    private Mono<Cuenta> buscar(Long id) {
        return cuentas.buscarPorId(id)
                .switchIfEmpty(Mono.error(new CuentaNoExisteException(id)));
    }

    // Revisa el saldo y mueve el dinero
    private Mono<Void> moverDinero(Cuenta origen, Cuenta destino, BigDecimal monto) {
        if (!origen.tieneSaldoPara(monto)) {
            return Mono.error(new SaldoInsuficienteException());
        }
        return cuentas.actualizarSaldos(origen.debitar(monto), destino.acreditar(monto));
    }
}