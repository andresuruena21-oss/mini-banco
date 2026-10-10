package co.com.minibanco.model.notificacion.gateways;

import reactor.core.publisher.Mono;

public interface EventoProcesadoRepository {

    Mono<Boolean> yaProcesado(String eventoId);     // ¿ya vi este evento?

    Mono<Void> marcarProcesado(String eventoId);    // anotarlo como visto
}