package co.com.minibanco.model.transferencia.gateways;

import co.com.minibanco.model.transferencia.Transferencia;

public interface EventoGateway {

    void publicarTransferencia(Transferencia transferencia);
}