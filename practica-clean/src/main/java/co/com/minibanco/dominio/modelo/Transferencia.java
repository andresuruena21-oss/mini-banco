package co.com.minibanco.dominio.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transferencia(Long origen, Long destino, BigDecimal monto, LocalDateTime fecha) {}