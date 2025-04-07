/*
@startuml
class estudiante{
- nombre: String
+ getNombre(): void
}

class curso{
- nombre curso: String
+ listaEstudiante.Estudiante[]
+ agregarEstudiante(Estudiante: estudiante): void
+ mostrarEstudiantes(): void
}

curso o--> estudiante
@enduml
*/

package Java;
import java.util.ArrayList;
import java.util.List;

class Estudiante {
    private String nombre;

    public Estudiante(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}

class Curso {
    private String nombreCurso;
    private List<Estudiante> listaEstudiantes;

    public Curso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
        this.listaEstudiantes = new ArrayList<>();
    }

    public void agregarEstudiante(Estudiante estudiante) {
        listaEstudiantes.add(estudiante);
    }

    public void mostrarEstudiantes() {
        System.out.println("Estudiantes en el curso " + nombreCurso + ":");
        for (Estudiante estudiante : listaEstudiantes) {
            System.out.println("- " + estudiante.getNombre());
        }
    }
}

public class Alumno {
    public static void main(String[] args) {
        Curso curso = new Curso("Programación en Java");

        curso.agregarEstudiante(new Estudiante("Juan Pérez"));
        curso.agregarEstudiante(new Estudiante("María López"));
        curso.agregarEstudiante(new Estudiante("Carlos Gómez"));

        curso.mostrarEstudiantes();
    }
}
