package co.com.minibanco.r2dbc;

import co.com.minibanco.model.cuenta.Cuenta;
import co.com.minibanco.model.cuenta.gateways.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class CuentaR2dbcAdapter implements CuentaRepository {

    private final CuentaDataRepository repository;                // el de Spring Data
    private final TransactionalOperator transactionalOperator;    // para las transacciones

    @Override
    public Mono<Cuenta> buscarPorId(Long id) {
        return repository.findById(id)        // busca en la tabla → Mono<CuentaEntity>
                .map(this::aDominio);         // la traduce al modelo del dominio
    }

    @Override
    public Mono<Void> guardar(Cuenta cuenta) {
        return repository.save(aEntidad(cuenta, true))   // true = cuenta NUEVA → INSERT
                .then();
    }

    @Override
    public Mono<Void> actualizarSaldos(Cuenta origen, Cuenta destino) {
        return repository.save(aEntidad(origen, false))
                .then(repository.save(aEntidad(destino, false)))
                .then()
                .as(transactionalOperator::transactional);
    }

    // Traductores entre la tabla y el dominio
    private Cuenta aDominio(CuentaEntity e) {
        return Cuenta.builder().id(e.getId()).titular(e.getTitular()).saldo(e.getSaldo()).build();
    }

    private CuentaEntity aEntidad(Cuenta c, boolean nueva) {
        return CuentaEntity.builder()
                .id(c.getId()).titular(c.getTitular()).saldo(c.getSaldo())
                .nueva(nueva)
                .build();
    }
}