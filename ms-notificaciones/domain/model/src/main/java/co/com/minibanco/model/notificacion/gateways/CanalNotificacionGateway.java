package co.com.minibanco.model.notificacion.gateways;

import co.com.minibanco.model.notificacion.Notificacion;
import reactor.core.publisher.Mono;

public interface CanalNotificacionGateway {

    Mono<Void> enviar(Notificacion notificacion);   // SMS, correo, push... al dominio no le importa
}