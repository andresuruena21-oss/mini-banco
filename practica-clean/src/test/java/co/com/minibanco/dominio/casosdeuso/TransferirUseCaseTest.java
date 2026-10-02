package co.com.minibanco.dominio.casosdeuso;

import co.com.minibanco.dominio.excepciones.CuentaNoExisteException;
import co.com.minibanco.dominio.excepciones.MontoInvalidoException;
import co.com.minibanco.dominio.excepciones.SaldoInsuficienteException;
import co.com.minibanco.dominio.modelo.Cuenta;
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

        cuentas.guardar(new Cuenta(1L, "Ana", new BigDecimal("1000")));
        cuentas.guardar(new Cuenta(2L, "Luis", new BigDecimal("0")));
    }

    // ESCENARIO 1: todo sale bien
    @Test
    void transferenciaExitosaMueveElDinero() {
        useCase.transferir(1L, 2L, new BigDecimal("300"));

        assertEquals(new BigDecimal("700"), cuentas.buscarPorId(1L).get().saldo());   // Ana quedó con 700
        assertEquals(new BigDecimal("300"), cuentas.buscarPorId(2L).get().saldo());   // Luis quedó con 300
        assertEquals(1, eventos.publicados.size());                                    // se anotó 1 aviso
    }

    // ESCENARIO 2: no hay saldo suficiente
    @Test
    void saldoInsuficienteNoMueveElDinero() {
        assertThrows(SaldoInsuficienteException.class,
                () -> useCase.transferir(1L, 2L, new BigDecimal("5000")));

        assertEquals(new BigDecimal("1000"), cuentas.buscarPorId(1L).get().saldo());  // Ana sigue con 1000
        assertEquals(0, eventos.publicados.size());                                     // no se anotó nada
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