package co.com.minibanco.model.notificacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Notificacion {

    private String eventoId;      // para no notificar dos veces lo mismo
    private Long origen;
    private Long destino;
    private BigDecimal monto;
}