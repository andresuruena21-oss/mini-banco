package co.com.minibanco.memoria;

import co.com.minibanco.model.notificacion.Notificacion;
import co.com.minibanco.model.notificacion.gateways.CanalNotificacionGateway;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class NotificacionLogAdapter implements CanalNotificacionGateway {

    @Override
    public Mono<Void> enviar(Notificacion n) {
        return Mono.fromRunnable(() -> System.out.println(
                "📱 NOTIFICACIÓN: la cuenta " + n.getDestino() + " recibió " + n.getMonto()
                        + " de la cuenta " + n.getOrigen() + " (evento " + n.getEventoId() + ")"));
    }
}