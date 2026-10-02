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

**Pregunta:** ...