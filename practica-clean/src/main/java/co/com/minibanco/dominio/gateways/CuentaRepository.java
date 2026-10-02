package co.com.minibanco.dominio.gateways;
import co.com.minibanco.dominio.modelo.Cuenta;

import java.util.Optional;
public interface CuentaRepository {
    Optional<Cuenta> buscarPorId(Long id);//Con Optional, el contrato le avisa a
    // quien lo use: "ojo, puede que no encuentre nada; tienes que decidir qué hacer en
    // ese caso". Y en tu caso de uso, si la caja llega vacía, lanzarás la CuentaNoExisteException
    // que acabas de crear.

    void guardar (Cuenta cuenta);

}
