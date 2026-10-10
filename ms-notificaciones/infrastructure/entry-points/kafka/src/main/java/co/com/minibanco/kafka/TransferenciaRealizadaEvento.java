package co.com.minibanco.kafka;

import java.math.BigDecimal;

public record TransferenciaRealizadaEvento(
        String eventoId,
        Long origen,
        Long destino,
        BigDecimal monto,
        String fecha) {
}