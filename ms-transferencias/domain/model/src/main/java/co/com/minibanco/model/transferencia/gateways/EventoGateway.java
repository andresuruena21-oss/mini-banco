package co.com.minibanco.model.transferencia.gateways;

import co.com.minibanco.model.transferencia.Transferencia;
import reactor.core.publisher.Mono;

public interface EventoGateway {

    Mono<Void> publicarTransferencia(Transferencia transferencia);   // antes: void
}