package co.com.minibanco.dominio.casosdeuso;

import co.com.minibanco.dominio.gateways.CuentaRepository;
import co.com.minibanco.dominio.modelo.Cuenta;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CuentaRepositoryEnMemoria implements CuentaRepository {

    private final Map<Long, Cuenta> datos = new HashMap<>();

    @Override
    public Optional<Cuenta> buscarPorId(Long id) {
        return Optional.ofNullable(datos.get(id));
    }

    @Override
    public void guardar(Cuenta cuenta) {
        datos.put(cuenta.id(), cuenta);
    }
}