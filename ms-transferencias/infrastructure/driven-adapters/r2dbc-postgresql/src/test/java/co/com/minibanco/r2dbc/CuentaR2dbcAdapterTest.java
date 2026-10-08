package co.com.minibanco.r2dbc;

import co.com.minibanco.model.cuenta.Cuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CuentaR2dbcAdapterTest {

    private CuentaDataRepository repository;
    private TransactionalOperator transactionalOperator;
    private CuentaR2dbcAdapter adapter;

    @BeforeEach
    void preparar() {
        repository = mock(CuentaDataRepository.class);
        transactionalOperator = mock(TransactionalOperator.class);

        // Transacción de mentira: devuelve el mismo Mono que recibe, sin hacer nada extra
        when(transactionalOperator.transactional(any(Mono.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        // Guardar de mentira: devuelve la misma entidad que le pasaron
        when(repository.save(any(CuentaEntity.class)))
                .thenAnswer(invocacion -> Mono.just(invocacion.getArgument(0)));

        adapter = new CuentaR2dbcAdapter(repository, transactionalOperator);
    }

    @Test
    void buscarPorIdTraduceLaEntidadAlDominio() {
        when(repository.findById(1L)).thenReturn(Mono.just(
                CuentaEntity.builder().id(1L).titular("Ana").saldo(new BigDecimal("1000")).build()));

        StepVerifier.create(adapter.buscarPorId(1L))
                .expectNextMatches(cuenta -> cuenta.getTitular().equals("Ana")
                        && cuenta.getSaldo().compareTo(new BigDecimal("1000")) == 0)
                .verifyComplete();
    }

    @Test
    void buscarPorIdInexistenteLlegaVacio() {
        when(repository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.buscarPorId(99L))
                .verifyComplete();             // termina sin ningún valor: caja vacía
    }

    @Test
    void guardarHaceUnInsert() {
        Cuenta ana = Cuenta.builder().id(1L).titular("Ana").saldo(new BigDecimal("1000")).build();

        StepVerifier.create(adapter.guardar(ana))
                .verifyComplete();

        verify(repository).save(argThat(entidad -> entidad.isNew()));   // se guardó como NUEVA
    }

    @Test
    void actualizarSaldosGuardaLasDosCuentasEnUnaTransaccion() {
        Cuenta origen = Cuenta.builder().id(1L).titular("Ana").saldo(new BigDecimal("700")).build();
        Cuenta destino = Cuenta.builder().id(2L).titular("Luis").saldo(new BigDecimal("300")).build();

        StepVerifier.create(adapter.actualizarSaldos(origen, destino))
                .verifyComplete();

        verify(repository, times(2)).save(argThat(entidad -> !entidad.isNew()));   // 2 UPDATE
        verify(transactionalOperator).transactional(any(Mono.class));              // dentro de una transacción
    }
}