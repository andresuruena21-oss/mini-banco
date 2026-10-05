# Aprendizajes



\## Dia 1 

Estuvo interesante , aprendí sobre los lambda que es lo que nosotros le pedimos a la maquina que haga por nosotros , aprendí de los métodos filter y demás que están en el stream , el stream es todo lo que esta en la banda , ósea todas las transferencias pasan por ahí y hace que recorra , luego se les aplican los métodos y las lambda que va toda dentro del metodo que son las instrucciones 



Me costo al principio porque no tenia mas ejemplos y eran cosas muy nuevas , me costo entender cuando colocar tipo y las mayúsculas y ya además las diferencias del filter y del map al principio, pero ya aprendí que filter espera algo asi como un si y un no mientras que el map convierte una cosa en otra por ejemplo en un string



No escribir el tipo dentro de la lambda: se escribe m -> m.cuenta(), no Movimiento m -> ... (lo gris era ayuda de IntelliJ).

Elegir el tipo correcto de la variable: List<Movimiento> cuando quedan movimientos, List<String> cuando map los convirtió en textos.



## Día 2

**Aprendí:** Clean Architecture divide el proyecto en capas, en vez de hacer un revoltijo
de cosas. Las reglas del negocio quedan en el centro y no conocen la tecnología. Cuando el
negocio necesita algo de afuera, define un gateway (una interfaz o contrato), y otra clase
lo implementa, por ejemplo con una base de datos. Así, si cambia la tecnología, solo se
cambia esa implementación y no todo el proyecto. También aprendí los principios SOLID,
que son cinco consejos para escribir clases e interfaces fáciles de cambiar y de probar.

**Me costó:** entender las excepciones. Aprendí que no son interfaces sino clases que
heredan de RuntimeException (con extends), y que sirven para representar cada error del
negocio con su propio nombre, como SaldoInsuficienteException, para saber exactamente
qué salió mal.

**Pregunta:** …


## Día 3

**Aprendí:** que el Scaffold de Bancolombia es un plugin de Gradle que genera
el microservicio con Clean Architecture por comandos, en vez de crear las carpetas
a mano como hice el día 2. Cada capa es un módulo separado (model, usecase,
driven-adapters, entry-points y app-service), y Gradle no deja que el dominio use
cosas de infraestructura, o sea la regla de dependencia queda protegida.
Usé gm para generar los modelos, guc para los casos de uso y vs para validar
la estructura. También aprendí Lombok: @Getter para leer los datos, @Builder para
crear objetos nombrando cada campo sin escribir new, y toBuilder para crear una
copia modificada, como en debitar. Y que Spring encuentra los casos de uso solo
porque terminan en UseCase, sin ponerles @Service.

**Me costó:** pasar el código de un proyecto al otro, porque había que cambiar
los package e imports, y que el record usa saldo() pero con Lombok es getSaldo().
También aprendí que si una clase depende de otra que tiene errores, toca arreglar
primero la otra (me pasó con el EventoGatewayFalso). Y que el código real va en
main y las pruebas en test.

**Pregunta:** ¿cómo se ve el caso de uso cuando lo pasemos a Mono y Flux?



## Día 4

**Aprendí:** la programación reactiva es como un mesero que no se queda esperando
a la cocina, sino que atiende otras mesas mientras tanto; así con pocos hilos se
atienden muchas peticiones. Mono es una promesa de 0 o 1 valor y Flux de varios.
Nada pasa hasta que alguien se suscribe, como el Stream sin operación final.
map es para transformar con un valor normal y flatMap cuando la lambda devuelve
otro Mono, porque si no queda un Mono dentro de otro y nunca se ejecuta.
switchIfEmpty es como el orElseThrow, zip busca dos cosas al tiempo, then sigue
con otra cosa y thenReturn entrega un valor al final. Nunca usar .block().

**Me costó:** entender el flatMap en Reactor y que en las pruebas no podía usar
guardar() para preparar datos, porque nadie se suscribía.

**Pregunta:** ¿cómo recibe WebFlux una petición HTTP y devuelve un Mono?