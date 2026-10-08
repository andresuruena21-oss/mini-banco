package co.com.minibanco.r2dbc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("cuenta")                 // esta clase representa una fila de la tabla "cuenta"
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaEntity implements Persistable<Long> {

    @Id                          // esta columna es la llave primaria
    private Long id;
    private String titular;
    private BigDecimal saldo;

    @Transient                   // este campo NO es una columna de la tabla
    private boolean nueva;

    @Override
    public boolean isNew() {     // le dice a Spring si debe hacer INSERT o UPDATE
        return nueva;
    }
}