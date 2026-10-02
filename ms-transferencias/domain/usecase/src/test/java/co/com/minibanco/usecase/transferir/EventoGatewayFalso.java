package co.com.minibanco.usecase.transferir;

import co.com.minibanco.model.transferencia.Transferencia;
import co.com.minibanco.model.transferencia.gateways.EventoGateway;

import java.util.ArrayList;
import java.util.List;

public class EventoGatewayFalso implements EventoGateway {

    public final List<Transferencia> publicados = new ArrayList<>();

    @Override
    public void publicarTransferencia(Transferencia transferencia) {
        publicados.add(transferencia);
    }
}