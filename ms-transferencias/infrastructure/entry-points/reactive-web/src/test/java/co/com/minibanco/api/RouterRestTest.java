package co.com.minibanco.api;

import co.com.minibanco.model.cuenta.Cuenta;
import co.com.minibanco.model.excepciones.CuentaNoExisteException;
import co.com.minibanco.model.excepciones.SaldoInsuficienteException;
import co.com.minibanco.model.transferencia.Transferencia;
import co.com.minibanco.usecase.consultarcuenta.ConsultarCuentaUseCase;
import co.com.minibanco.usecase.crearcuenta.CrearCuentaUseCase;
import co.com.minibanco.usecase.transferir.TransferirUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RouterRestTest {

    private ConsultarCuentaUseCase consultarCuentaUseCase;
    private TransferirUseCase transferirUseCase;
    private WebTestClient cliente;

    @BeforeEach
    void preparar() {
        // Casos de uso de mentira
        CrearCuentaUseCase crearCuentaUseCase = mock(CrearCuentaUseCase.class);
        consultarCuentaUseCase = mock(ConsultarCuentaUseCase.class);
        transferirUseCase = mock(TransferirUseCase.class);

        // Armo la API real (router + handler) con los casos de uso de mentira
        Handler handler = new Handler(crearCuentaUseCase, consultarCuentaUseCase, transferirUseCase);
        cliente = WebTestClient.bindToRouterFunction(new RouterRest().rutas(handler)).build();
    }

    @Test
    void consultarCuentaExistenteResponde200() {
        when(consultarCuentaUseCase.porId(1L)).thenReturn(Mono.just(
                Cuenta.builder().id(1L).titular("Ana").saldo(new BigDecimal("700")).build()));

        cliente.get().uri("/api/cuentas/1")      // simulo un GET
                .exchange()                      // "envío" la petición
                .expectStatus().isOk()           // espero 200
                .expectBody()
                .jsonPath("$.titular").isEqualTo("Ana")    // reviso campos del JSON
                .jsonPath("$.saldo").isEqualTo(700);
    }

    @Test
    void cuentaInexistenteResponde404() {
        when(consultarCuentaUseCase.porId(99L)).thenReturn(Mono.error(new CuentaNoExisteException(99L)));

        cliente.get().uri("/api/cuentas/99")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().jsonPath("$.codigo").isEqualTo("CUENTA_NO_EXISTE");
    }

    @Test
    void transferenciaExitosaResponde201() {
        when(transferirUseCase.transferir(any(), any(), any())).thenReturn(Mono.just(
                Transferencia.builder().origen(1L).destino(2L)
                        .monto(new BigDecimal("300")).fecha(LocalDateTime.now()).build()));

        cliente.post().uri("/api/transferencias")
                .bodyValue(new TransferenciaRequest(1L, 2L, new BigDecimal("300")))   // el JSON que envío
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.monto").isEqualTo(300);
    }

    @Test
    void transferenciaSinSaldoResponde422() {
        when(transferirUseCase.transferir(any(), any(), any()))
                .thenReturn(Mono.error(new SaldoInsuficienteException()));

        cliente.post().uri("/api/transferencias")
                .bodyValue(new TransferenciaRequest(1L, 2L, new BigDecimal("99999")))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody().jsonPath("$.codigo").isEqualTo("SALDO_INSUFICIENTE");
    }
}