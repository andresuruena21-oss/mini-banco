package co.com.minibanco.memoria;

import co.com.minibanco.model.transferencia.Transferencia;
import co.com.minibanco.model.transferencia.gateways.EventoGateway;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class EventoLogAdapter implements EventoGateway {

    @Override
    public Mono<Void> publicarTransferencia(Transferencia t) {
        return Mono.fromRunnable(() -> System.out.println(
                "EVENTO: transferencia de " + t.getMonto()
                        + " de la cuenta " + t.getOrigen()
                        + " a la cuenta " + t.getDestino()));
    }
}