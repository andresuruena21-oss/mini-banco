package co.com.minibanco.dominio.casosdeuso;

import co.com.minibanco.dominio.gateways.EventoGateway;
import co.com.minibanco.dominio.modelo.Transferencia;

import java.util.ArrayList;
import java.util.List;

public class EventoGatewayFalso implements EventoGateway {

    public final List<Transferencia> publicados = new ArrayList<>();

    @Override
    public void publicarTransferencia(Transferencia transferencia) {
        publicados.add(transferencia);
    }
}