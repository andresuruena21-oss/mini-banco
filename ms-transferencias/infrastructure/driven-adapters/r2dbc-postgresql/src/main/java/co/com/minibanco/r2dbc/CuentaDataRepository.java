package co.com.minibanco.r2dbc;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CuentaDataRepository extends ReactiveCrudRepository<CuentaEntity, Long> {
}
