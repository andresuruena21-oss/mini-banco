package co.com.minibanco.usecase.crearcuenta;

import co.com.minibanco.model.cuenta.Cuenta;
import co.com.minibanco.model.cuenta.gateways.CuentaRepository;
import co.com.minibanco.model.excepciones.MontoInvalidoException;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class CrearCuentaUseCase {

    private final CuentaRepository cuentas;

    public Cuenta crear(Long id, String titular, BigDecimal saldoInicial) {
        // Una cuenta no puede empezar con saldo negativo
        if (saldoInicial == null || saldoInicial.signum() < 0) {
            throw new MontoInvalidoException();
        }

        Cuenta cuenta = Cuenta.builder()
                .id(id)
                .titular(titular)
                .saldo(saldoInicial)
                .build();

        cuentas.guardar(cuenta);
        return cuenta;
    }
}