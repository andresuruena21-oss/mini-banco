package co.com.minibanco.usecase.consultarcuenta;

import co.com.minibanco.model.cuenta.Cuenta;
import co.com.minibanco.model.cuenta.gateways.CuentaRepository;
import co.com.minibanco.model.excepciones.CuentaNoExisteException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ConsultarCuentaUseCase {

    private final CuentaRepository cuentas;

    public Mono<Cuenta> porId(Long id) {
        return cuentas.buscarPorId(id)
                .switchIfEmpty(Mono.error(new CuentaNoExisteException(id)));
    }
}