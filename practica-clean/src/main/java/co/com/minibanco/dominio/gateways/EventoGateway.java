package co.com.minibanco.dominio.gateways;

import co.com.minibanco.dominio.modelo.Transferencia;

public interface EventoGateway {

    void publicarTransferencia(Transferencia transferencia);
}