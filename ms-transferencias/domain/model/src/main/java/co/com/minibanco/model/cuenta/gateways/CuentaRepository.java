package co.com.minibanco.model.cuenta.gateways;

import co.com.minibanco.model.cuenta.Cuenta;

import java.util.Optional;

public interface CuentaRepository {

    Optional<Cuenta> buscarPorId(Long id);

    void guardar(Cuenta cuenta);
}