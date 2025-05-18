package Alpha.InteractionsMenus;

import java.util.Scanner;

import Alpha.InventoryLogic.Inventory;
import Alpha.InventoryLogic.InventoryReport;

public class ReportMenu {
     public static void mostrarMenuReportes(Scanner scanner, Inventory inventario, InventoryReport reporteInventario) { // Método para mostrar el menú de opciones de reportes 
        boolean regresarAlMenu = false; 
        while (!regresarAlMenu) {
            System.out.println("\n=== REPORTES ===");
            System.out.println("1. Generar reporte detallado del inventario");
            System.out.println("2. Mostrar registro de cambios");
            System.out.println("3. Regresar al menú principal");
            System.out.print("Seleccione una opción: ");
            int opcionReporte = Integer.parseInt(scanner.nextLine());
            switch (opcionReporte) {
                case 1 -> reporteInventario.generarReporte(inventario);
                case 2 -> reporteInventario.mostrarRegistroDeCambios(scanner);
                case 3 -> {
                    regresarAlMenu = true;
                    System.out.println("Regresando al menú principal...");
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }
}
