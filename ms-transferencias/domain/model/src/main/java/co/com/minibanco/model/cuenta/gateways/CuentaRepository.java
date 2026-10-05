package co.com.minibanco.model.cuenta.gateways;

import co.com.minibanco.model.cuenta.Cuenta;
import reactor.core.publisher.Mono;

public interface CuentaRepository {

    Mono<Cuenta> buscarPorId(Long id);                         // antes: Optional<Cuenta>

    Mono<Void> guardar(Cuenta cuenta);                         // antes: void

    Mono<Void> actualizarSaldos(Cuenta origen, Cuenta destino); // NUEVO: guardar las dos juntas
}