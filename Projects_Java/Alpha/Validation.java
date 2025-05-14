package Alpha;

// Import necesario para trabajar con colecciones y Scanner
import java.util.*; 

public class Validation{ // Clase para validar las entradas del usuario en el sistema
        
    public static int leerEnteroPositivo(Scanner scanner, String mensajeError) { // Método para validar un número entero positivo
        while (true) {
            try {
                int numero = Integer.parseInt(scanner.nextLine());
                if (numero > 0) {
                    return numero;
                } else {
                    System.out.println("Por favor, ingrese un número positivo.");
                }
            } catch (NumberFormatException e) {
                System.out.println(mensajeError);
            }
        }
    }

    public static double leerDoublePositivo(Scanner scanner, String mensajeError) { // Método para validar un número decimal positivo
        while (true) {
            try {
                double numero = Double.parseDouble(scanner.nextLine());
                if (numero > 0) {
                    return numero;
                } else {
                    System.out.println("Por favor, ingrese un número positivo.");
                }
            } catch (NumberFormatException e) {
                System.out.println(mensajeError);
            }
        }
    }

    public static String leerTextoNoVacio(Scanner scanner, String mensajeError) { // Método para validar una cadena de texto no vacía
        while (true) {
            String entrada = scanner.nextLine().trim();
            if (!entrada.isEmpty()) {
                return entrada;
            }
            System.out.println(mensajeError);
        }
    }
}
