package co.com.minibanco.kafka;

import co.com.minibanco.model.transferencia.Transferencia;
import co.com.minibanco.model.transferencia.gateways.EventoGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaEventoAdapter implements EventoGateway {

    private static final String TOPIC = "transferencias";

    private final KafkaTemplate<String, String> kafkaTemplate;    // Spring lo crea con la configuración del YAML
    private final ObjectMapper objectMapper = new ObjectMapper();  // convierte objetos a JSON

    @Override
    public Mono<Void> publicarTransferencia(Transferencia t) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(aEvento(t)))   // 1. el evento → JSON
                .flatMap(json -> Mono.fromFuture(
                        kafkaTemplate.send(TOPIC, t.getOrigen().toString(), json)))   // 2. lo envía a Kafka
                .then();                                                              // 3. "listo, terminé"
    }

    private TransferenciaRealizadaEvento aEvento(Transferencia t) {
        return new TransferenciaRealizadaEvento(
                UUID.randomUUID().toString(),     // un id único e irrepetible
                t.getOrigen(),
                t.getDestino(),
                t.getMonto(),
                t.getFecha().toString());
    }
}