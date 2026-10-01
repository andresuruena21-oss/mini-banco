package co.com.minibanco.practica_java;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class SaludoController {
    private final SaludoService servicio;
    public SaludoController(SaludoService servicio){
        this.servicio=servicio;
    }
    @GetMapping("/hola/{nombre}")
    public String hola(@PathVariable String nombre) {
        return servicio.saludar(nombre);
    }
}
