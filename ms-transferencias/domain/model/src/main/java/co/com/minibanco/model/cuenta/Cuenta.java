package co.com.minibanco.model.cuenta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Cuenta {

    private Long id;
    private String titular;
    private BigDecimal saldo;

    public Cuenta debitar(BigDecimal monto) {
        return this.toBuilder().saldo(saldo.subtract(monto)).build();
    }

    public Cuenta acreditar(BigDecimal monto) {
        return this.toBuilder().saldo(saldo.add(monto)).build();
    }

    public boolean tieneSaldoPara(BigDecimal monto) {
        return saldo.compareTo(monto) >= 0;
    }
}