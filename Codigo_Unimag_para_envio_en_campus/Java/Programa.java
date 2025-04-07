/*
@startuml
class carrera {
- nombreCarrera: String
- listaMaterias.Materias[]
+ agregarMateria(nombre: String)
+ mostrarMaterias()
}

class materia{
- materia: String
+ getNombre(): String
}

carrera *-- materia: "composición"

@enduml

 */

package Java;
import java.util.ArrayList;
import java.util.List;

class Materia {
    private String nombre;

    public Materia(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}

class Carrera {
    private String nombreCarrera;
    private List<Materia> listaMaterias;

    public Carrera(String nombreCarrera) {
        this.nombreCarrera = nombreCarrera;
        this.listaMaterias = new ArrayList<>();
    }

    public void agregarMateria(String nombre) {
        listaMaterias.add(new Materia(nombre));
    }

    public void mostrarMaterias() {
        System.out.println("Materias de la carrera " + nombreCarrera + ":");
        for (Materia materia : listaMaterias) {
            System.out.println("- " + materia.getNombre());
        }
    }
}

public class Programa {
    public static void main(String[] args) {
        Carrera carrera = new Carrera("Ingeniería en Software");
        carrera.agregarMateria("Programación Orientada a Objetos");
        carrera.agregarMateria("Estructuras de Datos");
        carrera.agregarMateria("Bases de Datos");

        carrera.mostrarMaterias();
    }
}