package co.com.minibanco.practica_java;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SaludoServiceTest {
    @Test
    public void saludaConElNombre(){
        SaludoService servicio = new SaludoService();
        String resultado = servicio.saludar("Ana");
        assertEquals("Hola Ana", resultado);
    }
}
