package co.com.minibanco.api;

import co.com.minibanco.model.excepciones.CuentaNoExisteException;
import co.com.minibanco.model.excepciones.MontoInvalidoException;
import co.com.minibanco.model.excepciones.SaldoInsuficienteException;
import co.com.minibanco.usecase.consultarcuenta.ConsultarCuentaUseCase;
import co.com.minibanco.usecase.crearcuenta.CrearCuentaUseCase;
import co.com.minibanco.usecase.transferir.TransferirUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    // Los tres casos de uso: Spring los inyecta solo
    private final CrearCuentaUseCase crearCuentaUseCase;
    private final ConsultarCuentaUseCase consultarCuentaUseCase;
    private final TransferirUseCase transferirUseCase;

    // POST /api/cuentas
    public Mono<ServerResponse> crearCuenta(ServerRequest request) {
        return request.bodyToMono(CrearCuentaRequest.class)                                   // 1. lee el JSON
                .flatMap(r -> crearCuentaUseCase.crear(r.id(), r.titular(), r.saldoInicial())) // 2. caso de uso
                .flatMap(cuenta -> ServerResponse.status(HttpStatus.CREATED).bodyValue(cuenta)) // 3. respuesta 201
                .onErrorResume(this::manejarError);                                           // 4. plan B
    }

    // GET /api/cuentas/{id}
    public Mono<ServerResponse> consultarCuenta(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));     // saca el {id} de la URL
        return consultarCuentaUseCase.porId(id)
                .flatMap(cuenta -> ServerResponse.ok().bodyValue(cuenta))
                .onErrorResume(this::manejarError);
    }

    // POST /api/transferencias
    public Mono<ServerResponse> transferir(ServerRequest request) {
        return request.bodyToMono(TransferenciaRequest.class)
                .flatMap(r -> transferirUseCase.transferir(r.origen(), r.destino(), r.monto()))
                .flatMap(t -> ServerResponse.status(HttpStatus.CREATED).bodyValue(t))
                .onErrorResume(this::manejarError);
    }

    // Traduce los errores del NEGOCIO a códigos HTTP
    private Mono<ServerResponse> manejarError(Throwable error) {
        if (error instanceof CuentaNoExisteException) {
            return responder(HttpStatus.NOT_FOUND, "CUENTA_NO_EXISTE", error);
        }
        if (error instanceof SaldoInsuficienteException) {
            return responder(HttpStatus.UNPROCESSABLE_CONTENT, "SALDO_INSUFICIENTE", error);
        }
        if (error instanceof MontoInvalidoException) {
            return responder(HttpStatus.BAD_REQUEST, "MONTO_INVALIDO", error);
        }
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue(new ErrorResponse("ERROR_INTERNO", "Ocurrió un error inesperado"));
    }

    private Mono<ServerResponse> responder(HttpStatus estado, String codigo, Throwable error) {
        return ServerResponse.status(estado).bodyValue(new ErrorResponse(codigo, error.getMessage()));
    }
}