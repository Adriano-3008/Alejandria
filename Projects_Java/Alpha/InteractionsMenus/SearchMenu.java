package Alpha.InteractionsMenus;

import java.util.Scanner;

import Alpha.Validation;
import Alpha.InventoryLogic.Inventory;

public class SearchMenu {
    public static void showSearchSubmenu(Scanner scanner, Inventory inventario) { // Método para mostrar el menú de opciones de búsqueda
        boolean regresarAlMenu = false;
        while (!regresarAlMenu) {
            System.out.println("\n=== BÚSQUEDA EN INVENTARIO ===");
            System.out.println("1. Buscar productos por categoría");
            System.out.println("2. Buscar producto por nombre");
            System.out.println("3. Regresar al menú principal");
            System.out.print("Seleccione una opción: ");
            int opcionBusqueda = Integer.parseInt(scanner.nextLine());
            switch (opcionBusqueda) {
                case 1 -> {
                    System.out.print("Ingrese la categoría a buscar: ");
                    inventario.buscarPorCategoria(scanner.nextLine());
                }
                case 2 -> {
                    System.out.print("Ingrese el nombre del producto a buscar: ");
                    String nombreProducto = Validation.leerTextoNoVacio(scanner, "El nombre del producto no puede estar vacío. Intente nuevamente.");
                    inventario.buscarPorNombre(nombreProducto);
                }
                case 3 -> {
                    regresarAlMenu = true;
                    System.out.println("Regresando al menú principal...");
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }
}
