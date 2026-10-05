package co.com.minibanco.usecase.transferir;

import co.com.minibanco.model.cuenta.Cuenta;
import co.com.minibanco.model.cuenta.gateways.CuentaRepository;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class CuentaRepositoryEnMemoria implements CuentaRepository {

    private final Map<Long, Cuenta> datos = new HashMap<>();

    @Override
    public Mono<Cuenta> buscarPorId(Long id) {
        return Mono.justOrEmpty(datos.get(id));    // si es null, Mono vacío
    }

    @Override
    public Mono<Void> guardar(Cuenta cuenta) {
        return Mono.fromRunnable(() -> datos.put(cuenta.getId(), cuenta));
    }

    @Override
    public Mono<Void> actualizarSaldos(Cuenta origen, Cuenta destino) {
        return guardar(origen).then(guardar(destino));   // guarda una y después la otra
    }

    // Métodos de ayuda SOLO para las pruebas: acceso directo, sin Mono
    public void agregar(Cuenta cuenta) {
        datos.put(cuenta.getId(), cuenta);
    }

    public Cuenta obtener(Long id) {
        return datos.get(id);
    }
}