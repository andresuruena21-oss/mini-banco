package co.com.minibanco.sms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

// Simula al sistema externo de mensajes de texto, que lee el buzón DEV.QUEUE.1
@Component
public class SmsListener {

    @JmsListener(destination = "DEV.QUEUE.1")
    public void recibir(String mensaje) {
        System.out.println("📲 SMS ENVIADO AL CLIENTE: " + mensaje);
    }
}