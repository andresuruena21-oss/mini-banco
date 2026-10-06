package co.com.minibanco.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> rutas(Handler handler) {
        return route(POST("/api/cuentas"), handler::crearCuenta)               // crear cuenta
                .andRoute(GET("/api/cuentas/{id}"), handler::consultarCuenta)  // consultar cuenta
                .andRoute(POST("/api/transferencias"), handler::transferir);   // transferir
    }
}