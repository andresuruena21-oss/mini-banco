package co.com.minibanco.usecase.transferir;

import co.com.minibanco.model.cuenta.Cuenta;
import co.com.minibanco.model.excepciones.CuentaNoExisteException;
import co.com.minibanco.model.excepciones.MontoInvalidoException;
import co.com.minibanco.model.excepciones.SaldoInsuficienteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransferirUseCaseTest {

    private CuentaRepositoryEnMemoria cuentas;
    private EventoGatewayFalso eventos;
    private TransferirUseCase useCase;

    @BeforeEach
    void preparar() {
        cuentas = new CuentaRepositoryEnMemoria();            // base de datos de mentira, vacía
        eventos = new EventoGatewayFalso();                   // Kafka de mentira, vacío
        useCase = new TransferirUseCase(cuentas, eventos);    // le entrego los enchufes de mentira

        // Con Lombok, las cuentas se crean con el builder
        cuentas.guardar(Cuenta.builder().id(1L).titular("Ana").saldo(new BigDecimal("1000")).build());
        cuentas.guardar(Cuenta.builder().id(2L).titular("Luis").saldo(new BigDecimal("0")).build());
    }

    // ESCENARIO 1: todo sale bien
    @Test
    void transferenciaExitosaMueveElDinero() {
        useCase.transferir(1L, 2L, new BigDecimal("300"));

        assertEquals(new BigDecimal("700"), cuentas.buscarPorId(1L).get().getSaldo());   // Ana quedó con 700
        assertEquals(new BigDecimal("300"), cuentas.buscarPorId(2L).get().getSaldo());   // Luis quedó con 300
        assertEquals(1, eventos.publicados.size());                                       // se anotó 1 aviso
    }

    // ESCENARIO 2: no hay saldo suficiente
    @Test
    void saldoInsuficienteNoMueveElDinero() {
        assertThrows(SaldoInsuficienteException.class,
                () -> useCase.transferir(1L, 2L, new BigDecimal("5000")));

        assertEquals(new BigDecimal("1000"), cuentas.buscarPorId(1L).get().getSaldo());  // Ana sigue con 1000
        assertEquals(0, eventos.publicados.size());                                        // no se anotó nada
    }

    // ESCENARIO 3: la cuenta origen no existe
    @Test
    void cuentaOrigenNoExisteLanzaError() {
        assertThrows(CuentaNoExisteException.class,
                () -> useCase.transferir(45L, 2L, new BigDecimal("100")));
    }

    // ESCENARIO 4: el monto es negativo
    @Test
    void montoNegativoLanzaError() {
        assertThrows(MontoInvalidoException.class,
                () -> useCase.transferir(1L, 2L, new BigDecimal("-2333")));
    }
}