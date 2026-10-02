package co.com.minibanco.usecase.transferir;

import co.com.minibanco.model.cuenta.gateways.CuentaRepository;
import co.com.minibanco.model.cuenta.Cuenta;

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
        datos.put(cuenta.getId(), cuenta);
    }
}