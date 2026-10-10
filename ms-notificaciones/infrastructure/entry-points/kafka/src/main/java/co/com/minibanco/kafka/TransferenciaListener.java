package co.com.minibanco.kafka;

import co.com.minibanco.model.notificacion.Notificacion;
import co.com.minibanco.usecase.notificartransferencia.NotificarTransferenciaUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransferenciaListener {

    private final NotificarTransferenciaUseCase useCase;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "transferencias", groupId = "ms-notificaciones")
    public void escuchar(String mensaje) throws Exception {
        // 1. JSON → evento
        TransferenciaRealizadaEvento evento = objectMapper.readValue(mensaje, TransferenciaRealizadaEvento.class);

        // 2. evento → modelo del dominio
        Notificacion notificacion = Notificacion.builder()
                .eventoId(evento.eventoId())
                .origen(evento.origen())
                .destino(evento.destino())
                .monto(evento.monto())
                .build();

        // 3. ejecuta el caso de uso y ESPERA a que termine
        useCase.notificar(notificacion).block();
    }
}