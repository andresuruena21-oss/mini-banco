package co.com.minibanco.api;

import java.math.BigDecimal;

public record CrearCuentaRequest(Long id, String titular, BigDecimal saldoInicial) {}