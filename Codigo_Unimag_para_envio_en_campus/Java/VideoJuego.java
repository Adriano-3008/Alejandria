/*
@startuml
class jugador{
- Nombre: string
- juego: juego
+ mostrarInformacion(): void
}

class juego{
-titulo String
+getTitulo()
}

jugador --> juego
@enduml
 */

package Java;

class Juego {
    private String titulo;

    public Juego(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }
}

class Jugador {
    private String nombre;
    private Juego juego; 

    public Jugador(String nombre, Juego juego) {
        this.nombre = nombre;
        this.juego = juego;
    }

    public void mostrarInformacion() {
        System.out.println("Jugador: " + nombre);
        System.out.println("Juego: " + juego.getTitulo());
    }
}

public class VideoJuego {
    public static void main(String[] args) {
        Juego juego = new Juego("The Legend of Zelda");
        Jugador jugador = new Jugador("Carlos", juego);

        jugador.mostrarInformacion();
    }
}
