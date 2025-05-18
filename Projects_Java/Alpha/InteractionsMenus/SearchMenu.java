package Alpha.InteractionsMenus;

import java.util.Scanner;

import Alpha.Validation;
import Alpha.InventoryLogic.Inventory;

public class SearchMenu {
    public static void mostrarMenuBusqueda(Scanner scanner, Inventory inventario) { // Método para mostrar el menú de opciones de búsqueda
        boolean regresarAlMenu = false;
        while (!regresarAlMenu) {
            System.out.println("\n=== BÚSQUEDA EN INVENTARIO ===");
            System.out.println("1. Buscar productos por categoría");
            System.out.println("2. Buscar producto por nombre");
            System.out.println("3. Regresar al menú principal");
            int opcionBusqueda = Validation.leerEnteroPositivo(scanner, "Seleccione una opción válida.");
            switch (opcionBusqueda) {
                case 1 -> {
                    String categoria = Validation.leerTextoNoVacio(scanner, "La categoría no puede estar vacía.");
                    inventario.buscarPorCategoria(categoria);
                }
                case 2 -> {
                    String nombreProducto = Validation.leerTextoNoVacio(scanner, "El nombre del producto no puede estar vacío.");
                    inventario.buscarPorNombre(nombreProducto);
                }
                case 3 -> regresarAlMenu = true;
                default -> System.out.println("Opción no válida.");
            }
        }
    }
}
