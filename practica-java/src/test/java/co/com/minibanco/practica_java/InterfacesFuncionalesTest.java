package co.com.minibanco.practica_java;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InterfacesFuncionalesTest {

    Movimiento debito = new Movimiento("111", "DEBITO", new BigDecimal("50000"));
    Movimiento credito = new Movimiento("222", "CREDITO", new BigDecimal("120000"));

    // PREDICATE: una pregunta que responde sí o no
    @Test
    void predicate() {
        Predicate<Movimiento> esDebito = m -> m.tipo().equals("DEBITO");

        assertTrue(esDebito.test(debito));     // .test() le hace la pregunta: ¿es débito? → sí
        assertFalse(esDebito.test(credito));   // ¿es débito? → no
    }

    // FUNCTION: recibe algo y lo transforma en otra cosa
    @Test
    void function() {
        Function<Movimiento, String> sacarCuenta = m -> m.cuenta();
        //       ↑ recibe     ↑ devuelve

        assertEquals("111", sacarCuenta.apply(debito));  // .apply() aplica la transformación
    }

    // CONSUMER: recibe algo, hace algo con él, y no devuelve nada
    @Test
    void consumer() {
        Consumer<Movimiento> imprimir = m -> System.out.println("Movimiento de la cuenta " + m.cuenta());

        imprimir.accept(debito);   // .accept() lo ejecuta: imprime en la consola, no devuelve nada
    }

    // SUPPLIER: no recibe nada y entrega un valor cuando se lo piden
    @Test
    void supplier() {
        Supplier<LocalDate> hoy = () -> LocalDate.now();
        //                        ↑ paréntesis vacíos: no recibe nada

        System.out.println("Hoy es " + hoy.get());   // .get() le pide el valor
    }

    record Cliente(String nombre, List<String> cuentas) {}

    @Test
    void mapVsFlatMap() {
        List<Cliente> clientes = List.of(
                new Cliente("Ana", List.of("111", "112")),
                new Cliente("Luis", List.of("222")),
                new Cliente("Marta", List.of("333", "334", "335"))
        );

        // Con map: cada cliente se convierte en SU lista → lista de listas
        List<List<String>> conMap = clientes.stream()
                .map(c -> c.cuentas())
                .toList();
        assertEquals(3, conMap.size());   // 3 elementos: las 3 listas

        // Con flatMap: se convierten en su lista Y se aplanan → una sola lista
        List<String> conFlatMap = clientes.stream()
                .flatMap(c -> c.cuentas().stream())
                .toList();
        assertEquals(6, conFlatMap.size());   // 6 elementos: todas las cuentas
        assertEquals(List.of("111", "112", "222", "333", "334", "335"), conFlatMap);
    }
}