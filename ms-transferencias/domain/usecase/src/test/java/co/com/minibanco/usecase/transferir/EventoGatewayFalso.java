package co.com.minibanco.usecase.transferir;

import co.com.minibanco.model.transferencia.Transferencia;
import co.com.minibanco.model.transferencia.gateways.EventoGateway;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class EventoGatewayFalso implements EventoGateway {

    public final List<Transferencia> publicados = new ArrayList<>();

    @Override
    public Mono<Void> publicarTransferencia(Transferencia transferencia) {
        return Mono.fromRunnable(() -> publicados.add(transferencia));
    }
}