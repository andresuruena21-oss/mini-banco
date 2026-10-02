package co.com.minibanco.dominio.modelo;

import java.math.BigDecimal;

public record Cuenta(Long id, String titular, BigDecimal saldo) {

    // Resta el monto: devuelve una cuenta NUEVA con el saldo menor
    public Cuenta debitar(BigDecimal monto) {
        return new Cuenta(id, titular, saldo.subtract(monto));
    }

    // Suma el monto: devuelve una cuenta NUEVA con el saldo mayor
    public Cuenta acreditar(BigDecimal monto) {
        return new Cuenta(id, titular, saldo.add(monto));
    }

    // Pregunta: ¿el saldo alcanza para este monto?
    public boolean tieneSaldoPara(BigDecimal monto) {
        return saldo.compareTo(monto) >= 0;
    }
}