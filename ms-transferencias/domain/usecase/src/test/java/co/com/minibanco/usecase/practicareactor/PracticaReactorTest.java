package co.com.minibanco.usecase.practicareactor;
import reactor.test.StepVerifier;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import co.com.minibanco.model.excepciones.CuentaNoExisteException;
import java.time.Duration;
class PracticaReactorTest {

    @Test
    void nadaPasaSinSuscripcion() {
        Flux<Integer> receta = Flux.just(1, 2, 3)        // una banda con 1, 2 y 3
                .map(n -> {
                    System.out.println("procesando " + n);
                    return n * 10;
                });

        System.out.println("--- receta escrita, todavía no pasa nada ---");

        receta.subscribe(n -> System.out.println("recibí " + n));   // ahora sí arranca
    }

    @Test
    void crearMonoYFlux() {
        Mono<String> uno = Mono.just("Ana");                        // un Mono con un valor
        Mono<String> vacio = Mono.empty();                          // un Mono vacío
        Flux<String> varios = Flux.just("Ana", "Luis", "Marta");    // un Flux con tres valores

        uno.subscribe(nombre -> System.out.println("Mono con valor: " + nombre));
        vacio.subscribe(nombre -> System.out.println("Esto nunca se imprime"));
        varios.subscribe(nombre -> System.out.println("Flux: " + nombre));
    }

    // EJEMPLO RESUELTO: map y filter, igual que en los Streams
    @Test
    void mapYFilter() {
        Flux<Integer> resultado = Flux.just(1, 2, 3, 4, 5)
                .filter(n -> n > 2)          // deja pasar 3, 4 y 5
                .map(n -> n * 10);           // los convierte en 30, 40 y 50

        StepVerifier.create(resultado)
                .expectNext(30, 40, 50)
                .verifyComplete();
    }
    // EJERCICIO 1: de los números del 1 al 10, quédate con los pares y multiplícalos por 3
    @Test
    void paresPorTres() {
        Flux<Integer> resultado = Flux.range(1, 10)     // banda con 1, 2, 3, ..., 10
                .filter(n -> n % 2 == 0)                // solo pasan los pares: 2, 4, 6, 8, 10
                .map(n -> n * 3);                       // cada uno se multiplica por 3: 6, 12, 18, 24, 30

        StepVerifier.create(resultado)
                .expectNext(6, 12, 18, 24, 30)          // espero estos valores, en este orden
                .verifyComplete();                      // y que la banda termine bien
    }
    // Simula una base de datos: dado un id, "promete" el nombre del cliente
    private Mono<String> buscarNombre(Long id) {
        if (id == 1L) return Mono.just("Ana");
        if (id == 2L) return Mono.just("Luis");
        return Mono.empty();                          // si no existe, la caja llega vacía
    }

    // EJEMPLO RESUELTO: map vs flatMap
    @Test
    void mapVsFlatMap() {
        Mono<Long> id = Mono.just(1L);

        // Con map: caja dentro de caja (fíjate en el tipo)
        Mono<Mono<String>> conMap = id.map(i -> buscarNombre(i));

        // Con flatMap: una sola caja, con el nombre adentro
        Mono<String> conFlatMap = id.flatMap(i -> buscarNombre(i));

        StepVerifier.create(conFlatMap)
                .expectNext("Ana")
                .verifyComplete();
    }

    // EJERCICIO 2: tengo un Flux con los ids 1 y 2. Quiero un Flux con sus nombres.
    // Pista: buscarNombre devuelve un Mono... ¿map o flatMap?
    @Test
    void nombresDeLosClientes() {
        Flux<String> nombres = Flux.just(1L, 2L)
                .flatMap(id->buscarNombre(id))
                ;

        StepVerifier.create(nombres)
                .expectNext("Ana", "Luis")
                .verifyComplete();
    }

    // EJEMPLO RESUELTO 1: si no existe, usar un valor por defecto
    @Test
    void defaultIfEmpty() {
        Mono<String> nombre = buscarNombre(99L)              // no existe: Mono vacío
                .defaultIfEmpty("Cliente desconocido");      // si está vacío, usa esto

        StepVerifier.create(nombre)
                .expectNext("Cliente desconocido")
                .verifyComplete();
    }

    // EJEMPLO RESUELTO 2: si no existe, lanzar un error del negocio
    @Test
    void switchIfEmptyConError() {
        Mono<String> nombre = buscarNombre(99L)
                .switchIfEmpty(Mono.error(new CuentaNoExisteException(99L)));

        StepVerifier.create(nombre)
                .expectError(CuentaNoExisteException.class)
                .verify();
    }

    // EJEMPLO RESUELTO 3: recuperarse de un error con un plan B
    @Test
    void onErrorResume() {
        Mono<String> nombre = buscarNombre(99L)
                .switchIfEmpty(Mono.error(new CuentaNoExisteException(99L)))   // genera el error...
                .onErrorResume(error -> Mono.just("Plan B"));                 // ...y aquí lo atrapa

        StepVerifier.create(nombre)
                .expectNext("Plan B")
                .verifyComplete();
    }

    // EJERCICIO 3: busca el cliente 2. Si no existe, error CuentaNoExisteException.
    // Como el 2 sí existe, debe llegar "Luis".
    @Test
    void clienteExisteNoDaError() {
        Mono<String> nombre = buscarNombre(2L)                               // 1. busca el cliente 2: la promesa trae "Luis"
                .switchIfEmpty(Mono.error(new CuentaNoExisteException(2L))); // 2. SOLO si llegara vacía: error

        StepVerifier.create(nombre)                                          // 3. me suscribo y reviso
                .expectNext("Luis")                                          //    espero que llegue "Luis"
                .verifyComplete();                                           //    y que termine bien, sin error
    }

    // EJEMPLO RESUELTO 1: zip junta dos promesas
    @Test
    void zipJuntaDosPromesas() {
        Mono<String> pareja = Mono.zip(buscarNombre(1L), buscarNombre(2L))   // busca los dos a la vez
                .map(par -> par.getT1() + " y " + par.getT2());               // une los dos nombres

        StepVerifier.create(pareja)
                .expectNext("Ana y Luis")
                .verifyComplete();
    }

    // EJEMPLO RESUELTO 2: zip trabaja en paralelo
    @Test
    void zipEsParalelo() {
        Mono<String> lento1 = Mono.just("A").delayElement(Duration.ofMillis(500));  // tarda medio segundo
        Mono<String> lento2 = Mono.just("B").delayElement(Duration.ofMillis(500));  // tarda medio segundo

        long inicio = System.currentTimeMillis();

        StepVerifier.create(Mono.zip(lento1, lento2))
                .expectNextCount(1)            // espero que llegue 1 resultado (el par)
                .verifyComplete();

        long tardo = System.currentTimeMillis() - inicio;
        System.out.println("Tardó " + tardo + " ms");   // ¿cerca de 500 o de 1000?
    }

    // EJEMPLO RESUELTO 3: then y thenReturn
    @Test
    void thenYThenReturn() {
        Mono<Void> guardarOrigen = Mono.fromRunnable(() -> System.out.println("guardé el origen"));
        Mono<Void> guardarDestino = Mono.fromRunnable(() -> System.out.println("guardé el destino"));

        Mono<String> resultado = guardarOrigen
                .then(guardarDestino)                     // cuando termine el origen, guarda el destino
                .thenReturn("comprobante 001");           // cuando termine todo, entrega el comprobante

        StepVerifier.create(resultado)
                .expectNext("comprobante 001")
                .verifyComplete();
    }
}