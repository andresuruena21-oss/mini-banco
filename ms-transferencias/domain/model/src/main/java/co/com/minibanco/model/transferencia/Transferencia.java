package co.com.minibanco.model.transferencia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Transferencia {

    private Long origen;
    private Long destino;
    private BigDecimal monto;
    private LocalDateTime fecha;
}