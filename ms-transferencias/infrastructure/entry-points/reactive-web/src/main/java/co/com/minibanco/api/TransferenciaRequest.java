package co.com.minibanco.api;

import java.math.BigDecimal;

public record TransferenciaRequest(Long origen, Long destino, BigDecimal monto) {}