package co.com.minibanco.practica_java;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class MovimientosTest {
    List<Movimiento> movimientos = List.of(
            new Movimiento("111", "DEBITO",  new BigDecimal("50000")),
            new Movimiento("222", "CREDITO", new BigDecimal("120000")),
            new Movimiento("111", "CREDITO", new BigDecimal("8000")),
            new Movimiento("333", "DEBITO",  new BigDecimal("300000")),
            new Movimiento("222", "DEBITO",  new BigDecimal("15000"))
    );

    // EJERCICIO 1 (resuelto): quedarse solo con los débitos
    @Test
    void ejercicio1_soloDebitos() {
        List<Movimiento> debitos = movimientos.stream()        // pongo los 5 movimientos en la banda
                .filter(m -> m.tipo().equals("DEBITO"))            // solo pasan los que son DEBITO
                .toList();                                         // los empaco en una lista

        assertEquals(3, debitos.size());                       // deben quedar 3
    }
    // EJERCICIO 2: lista de cuentas sin repetir
    // Pista: usa map(m -> m.cuenta()) para sacar la cuenta de cada movimiento, y luego distinct()
    @Test
    void ejercicio2_cuentasSinRepetir() {
        List<String> cuentas = movimientos.stream()
                .map(m ->m.cuenta())
                .distinct()
                .toList();

        assertEquals(List.of("111", "222", "333"), cuentas);
    }

    // EJERCICIO 3: sumar todos los créditos
    // Pista: filter para los CREDITO, map para sacar el monto, y reduce(BigDecimal.ZERO, BigDecimal::add)
    @Test
    void ejercicio3_sumaCreditos() {
        BigDecimal total = movimientos.stream()
                .filter(m->m.tipo().equals("CREDITO"))
                .map(m->m.monto())
                .reduce(BigDecimal.ZERO, BigDecimal::add);// tu código aquí

        assertEquals(new BigDecimal("128000"), total);
    }




    // EJERCICIO 4: la cuenta del movimiento más grande
    // Pista: max(Comparator.comparing(Movimiento::monto)) devuelve un Optional.
    //        Luego usa .map(Movimiento::cuenta).orElse("ninguna")
    @Test
    void ejercicio4_cuentaDelMayorMovimiento() {
        String cuenta = movimientos.stream()
                .max(Comparator.comparing(Movimiento::monto))   // busca el de mayor monto → Optional<Movimiento>
                .map(Movimiento::cuenta)                        // si existe, saca su cuenta → Optional<String>
                .orElse("ninguna");
        assertEquals("333", cuenta);
    }
    // EJERCICIO 5: agrupar los movimientos por cuenta
    // Pista: collect(Collectors.groupingBy(Movimiento::cuenta))
    @Test
    void ejercicio5_agruparPorCuenta() {
        Map<String, List<Movimiento>> porCuenta = movimientos.stream()
                .collect(Collectors.groupingBy(Movimiento::cuenta));// tu código aquí

        assertEquals(2, porCuenta.get("111").size());
    }


    // EJERCICIO 6: convertir cada movimiento en un texto como "111: DEBITO 50000"
    // Pista: map(m -> m.cuenta() + ": " + ...) y luego toList()
    @Test
    void ejercicio6_comoTexto() {
        List<String> textos = movimientos.stream()
                .map(m-> m.cuenta() + ": "+ m.tipo() + " " + m.monto())
                .toList();// tu código aquí

        assertEquals("111: DEBITO 50000", textos.get(0));
    }
}

