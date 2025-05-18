package Alpha.InteractionsMenus;

import java.io.File;
import java.util.Scanner;

import Alpha.InventoryManagementSystem;
import Alpha.InventoryLogic.Inventory;
import Alpha.InventoryLogic.InventoryReport;

public class InventoryModificationMenu{ // Clase para mostrar el menú de opciones de modificación del inventario
    public static void mostrarMenuModificacionInventario (Scanner scanner, Inventory inventario, InventoryReport reporteInventario) { // Método para mostrar el menú de opciones del inventario 
        boolean regresarAlMenu = false;
        while (!regresarAlMenu) {
            System.out.println("\n=== MODIFICACIONES DE INVENTARIO ===");
            System.out.println("1. Agregar Categoría");
            System.out.println("2. Agregar Producto(s)");
            System.out.println("3. Modificar cantidad de un producto");
            System.out.println("4. Modificar precio de un producto");
            System.out.println("5. Eliminar un producto");
            System.out.println("6. Eliminar una categoría");
            System.out.println("7. Exportar inventario a CSV");
            System.out.println("8. Importar inventario desde CSV");
            System.out.println("9. Regresar al menú principal");
            System.out.print("Seleccione una opción: ");
            int opcionModificacion = Integer.parseInt(scanner.nextLine());
            switch (opcionModificacion) {
                case 1 -> InventoryManagementSystem.agregarCategoria(scanner, inventario);
                case 2 -> InventoryManagementSystem.agregarProductos(scanner, inventario, reporteInventario);
                case 3 -> inventario.modificarCantidad(scanner, reporteInventario);
                case 4 -> inventario.modificarPrecio(scanner, reporteInventario);
                case 5 -> inventario.eliminarProducto(scanner, reporteInventario);
                case 6 -> inventario.eliminarCategoria(scanner, reporteInventario);
                case 7 -> {
                    System.out.print("Ingrese el nombre del archivo CSV para exportar (por ejemplo, 'Alpha/Archivo_Proyecto/inventario.csv'): ");
                    String nombreArchivo = scanner.nextLine();
                    File archivoCSV = new File(nombreArchivo);
                    inventario.exportarInventario(archivoCSV);
                }
                case 8 -> {
                    System.out.print("Ingrese el nombre del archivo CSV para importar (por ejemplo, 'Alpha/Archivo_Proyecto/inventario.csv'): ");
                    String nombreArchivo = scanner.nextLine();
                    File ficherocsv = new File(nombreArchivo);
                    inventario.importarInventario(ficherocsv);
                }
                case 9 -> {
                    regresarAlMenu = true;
                    System.out.println("Regresando al menú principal...");
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }
}
