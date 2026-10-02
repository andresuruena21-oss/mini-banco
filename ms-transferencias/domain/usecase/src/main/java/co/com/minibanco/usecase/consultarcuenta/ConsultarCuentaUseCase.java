package co.com.minibanco.usecase.consultarcuenta;

import co.com.minibanco.model.cuenta.Cuenta;
import co.com.minibanco.model.cuenta.gateways.CuentaRepository;
import co.com.minibanco.model.excepciones.CuentaNoExisteException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConsultarCuentaUseCase {

    private final CuentaRepository cuentas;

    public Cuenta porId(Long id) {
        return cuentas.buscarPorId(id)
                .orElseThrow(() -> new CuentaNoExisteException(id));
    }
}