package co.com.minibanco.kafka;

import java.math.BigDecimal;

public record TransferenciaRealizadaEvento(
        String eventoId,     // identificador único de este evento (lo usaremos en la sesión 2)
        Long origen,
        Long destino,
        BigDecimal monto,
        String fecha) {
}