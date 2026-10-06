package co.com.minibanco.api.config;

import co.com.minibanco.api.Handler;
import co.com.minibanco.api.RouterRest;
import co.com.minibanco.model.cuenta.Cuenta;
import co.com.minibanco.usecase.consultarcuenta.ConsultarCuentaUseCase;
import co.com.minibanco.usecase.crearcuenta.CrearCuentaUseCase;
import co.com.minibanco.usecase.transferir.TransferirUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})   // arranca solo la API
@WebFluxTest                                                         // prueba de la capa web
@Import({CorsConfig.class, SecurityHeadersConfig.class})             // ⚠️ revisa estos nombres en tu carpeta config
class ConfigTest {

    // Casos de uso de mentira: el Handler los necesita para poder crearse
    @MockitoBean
    private CrearCuentaUseCase crearCuentaUseCase;

    @MockitoBean
    private ConsultarCuentaUseCase consultarCuentaUseCase;

    @MockitoBean
    private TransferirUseCase transferirUseCase;

    @Autowired
    private WebTestClient webTestClient;   // Spring lo crea, conectado a la API de prueba

    @BeforeEach
    void preparar() {
        // Cuando pidan la cuenta 1, el actor de mentira responde con Ana
        when(consultarCuentaUseCase.porId(1L)).thenReturn(Mono.just(
                Cuenta.builder().id(1L).titular("Ana").saldo(new BigDecimal("1000")).build()));
    }

    @Test
    void laApiDevuelveLasCabecerasDeSeguridad() {
        webTestClient.get()
                .uri("/api/cuentas/1")                    // una ruta real de tu API
                .exchange()
                .expectStatus().isOk()
                .expectHeader().exists("Content-Security-Policy")     // evita cargar contenido de otros sitios
                .expectHeader().exists("Strict-Transport-Security")   // obliga a usar HTTPS
                .expectHeader().exists("X-Content-Type-Options")      // evita que el navegador "adivine" tipos
                .expectHeader().exists("Cache-Control");              // evita guardar respuestas en caché
    }
}