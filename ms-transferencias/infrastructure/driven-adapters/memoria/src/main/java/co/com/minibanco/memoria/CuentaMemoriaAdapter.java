package co.com.minibanco.memoria;

import co.com.minibanco.model.cuenta.Cuenta;
import co.com.minibanco.model.cuenta.gateways.CuentaRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository   // "Spring: esta es la toma para el enchufe CuentaRepository"
public class CuentaMemoriaAdapter implements CuentaRepository {

    // ConcurrentHashMap: un HashMap seguro cuando llegan muchas peticiones al tiempo
    private final Map<Long, Cuenta> datos = new ConcurrentHashMap<>();

    @Override
    public Mono<Cuenta> buscarPorId(Long id) {
        return Mono.justOrEmpty(datos.get(id));
    }

    @Override
    public Mono<Void> guardar(Cuenta cuenta) {
        return Mono.fromRunnable(() -> datos.put(cuenta.getId(), cuenta));
    }

    @Override
    public Mono<Void> actualizarSaldos(Cuenta origen, Cuenta destino) {
        return guardar(origen).then(guardar(destino));
    }
}