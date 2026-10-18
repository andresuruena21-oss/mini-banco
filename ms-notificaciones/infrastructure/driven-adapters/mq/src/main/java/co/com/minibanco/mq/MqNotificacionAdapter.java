package co.com.minibanco.mq;

import co.com.minibanco.model.notificacion.Notificacion;
import co.com.minibanco.model.notificacion.gateways.CanalNotificacionGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class MqNotificacionAdapter implements CanalNotificacionGateway {

    private static final String COLA_SMS = "DEV.QUEUE.1";   // el "buzón" del sistema de SMS

    private final JmsTemplate jmsTemplate;   // Spring lo crea con la configuración de IBM MQ

    @Override
    public Mono<Void> enviar(Notificacion n) {
        String texto = "Su cuenta " + n.getDestino() + " recibió $" + n.getMonto()
                + " de la cuenta " + n.getOrigen();

        return Mono.fromRunnable(() -> jmsTemplate.convertAndSend(COLA_SMS, texto))   // 1. envía a la cola
                .subscribeOn(Schedulers.boundedElastic())                             // 2. ¡en los mensajeros!
                .then();
    }
}