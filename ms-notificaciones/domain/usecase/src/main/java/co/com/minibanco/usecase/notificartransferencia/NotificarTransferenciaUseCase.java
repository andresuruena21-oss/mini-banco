package co.com.minibanco.usecase.notificartransferencia;

import co.com.minibanco.model.notificacion.Notificacion;
import co.com.minibanco.model.notificacion.gateways.CanalNotificacionGateway;
import co.com.minibanco.model.notificacion.gateways.EventoProcesadoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class NotificarTransferenciaUseCase {

    private final EventoProcesadoRepository procesados;
    private final CanalNotificacionGateway canal;

    public Mono<Void> notificar(Notificacion notificacion) {
        return procesados.yaProcesado(notificacion.getEventoId())           // 1. ¿ya lo procesé?
                .filter(yaProcesado -> !yaProcesado)                         // 2. solo sigue si es NUEVO
                .flatMap(nuevo -> canal.enviar(notificacion)                 // 3. envía la notificación...
                        .then(procesados.marcarProcesado(notificacion.getEventoId())));  // 4. ...y lo anota
    }
}