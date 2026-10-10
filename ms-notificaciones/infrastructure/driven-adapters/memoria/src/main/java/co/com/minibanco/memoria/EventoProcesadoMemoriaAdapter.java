package co.com.minibanco.memoria;

import co.com.minibanco.model.notificacion.gateways.EventoProcesadoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EventoProcesadoMemoriaAdapter implements EventoProcesadoRepository {

    // Un conjunto (Set) no admite repetidos: perfecto para anotar ids
    private final Set<String> procesados = ConcurrentHashMap.newKeySet();

    @Override
    public Mono<Boolean> yaProcesado(String eventoId) {
        return Mono.fromSupplier(() -> procesados.contains(eventoId));
    }

    @Override
    public Mono<Void> marcarProcesado(String eventoId) {
        return Mono.fromRunnable(() -> procesados.add(eventoId));
    }
}