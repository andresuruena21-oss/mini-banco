package co.com.minibanco.usecase.transferir;

import co.com.minibanco.model.cuenta.Cuenta;
import co.com.minibanco.model.excepciones.CuentaNoExisteException;
import co.com.minibanco.model.excepciones.MontoInvalidoException;
import co.com.minibanco.model.excepciones.SaldoInsuficienteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransferirUseCaseTest {

    private CuentaRepositoryEnMemoria cuentas;
    private EventoGatewayFalso eventos;
    private TransferirUseCase useCase;

    @BeforeEach
    void preparar() {
        cuentas = new CuentaRepositoryEnMemoria();
        eventos = new EventoGatewayFalso();
        useCase = new TransferirUseCase(cuentas, eventos);

        // agregar (directo), NO guardar: guardar devuelve un Mono que nadie suscribiría
        cuentas.agregar(Cuenta.builder().id(1L).titular("Ana").saldo(new BigDecimal("1000")).build());
        cuentas.agregar(Cuenta.builder().id(2L).titular("Luis").saldo(new BigDecimal("0")).build());
    }

    // ESCENARIO 1: todo sale bien
    @Test
    void transferenciaExitosaMueveElDinero() {
        StepVerifier.create(useCase.transferir(1L, 2L, new BigDecimal("300")))
                .expectNextCount(1)                 // llega 1 comprobante
                .verifyComplete();

        assertEquals(new BigDecimal("700"), cuentas.obtener(1L).getSaldo());   // Ana quedó con 700
        assertEquals(new BigDecimal("300"), cuentas.obtener(2L).getSaldo());   // Luis quedó con 300
        assertEquals(1, eventos.publicados.size());                            // se publicó 1 evento
    }

    // ESCENARIO 2: no hay saldo suficiente
    @Test
    void saldoInsuficienteNoMueveElDinero() {
        StepVerifier.create(useCase.transferir(1L, 2L, new BigDecimal("5000")))
                .expectError(SaldoInsuficienteException.class)
                .verify();

        assertEquals(new BigDecimal("1000"), cuentas.obtener(1L).getSaldo());  // Ana sigue con 1000
        assertEquals(0, eventos.publicados.size());                             // no se publicó nada
    }

    // ESCENARIO 3: la cuenta origen no existe
    @Test
    void cuentaOrigenNoExisteLanzaError() {
        StepVerifier.create(useCase.transferir(45L, 2L, new BigDecimal("100")))
                .expectError(CuentaNoExisteException.class)
                .verify();
    }

    // ESCENARIO 4: el monto es negativo
    @Test
    void montoNegativoLanzaError() {
        StepVerifier.create(useCase.transferir(1L, 2L, new BigDecimal("-2333")))
                .expectError(MontoInvalidoException.class)
                .verify();
    }
}