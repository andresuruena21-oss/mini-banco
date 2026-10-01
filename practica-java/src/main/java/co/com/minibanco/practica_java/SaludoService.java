package co.com.minibanco.practica_java;
import org.springframework.stereotype.Service;
@Service
public class SaludoService {
    public String saludar (String nombre) {
        return "Hola " + nombre;
    }
}
