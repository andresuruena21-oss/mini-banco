package co.com.minibanco.usecase.crearcuenta;

import co.com.minibanco.model.cuenta.Cuenta;
import co.com.minibanco.model.cuenta.gateways.CuentaRepository;
import co.com.minibanco.model.excepciones.MontoInvalidoException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class CrearCuentaUseCase {

    private final CuentaRepository cuentas;

    public Mono<Cuenta> crear(Long id, String titular, BigDecimal saldoInicial) {
        if (saldoInicial == null || saldoInicial.signum() < 0) {
            return Mono.error(new MontoInvalidoException());
        }

        Cuenta cuenta = Cuenta.builder().id(id).titular(titular).saldo(saldoInicial).build();

        return cuentas.guardar(cuenta)      // guarda (Mono<Void>)...
                .thenReturn(cuenta);        // ...y cuando termine, entrega la cuenta
    }
}