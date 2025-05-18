package Alpha.InteractionsMenus;
// Import necesario para trabajar con archivos y flujos de entrada/salida de datos (I/O)
import java.io.*; 
// Import necesario para trabajar con colecciones y Scanner
import java.util.*;

import Alpha.InventoryLogic.Inventory;
import Alpha.InventoryLogic.InventoryReport;
import Alpha.Repositories.InventoryRepository;
import Alpha.SupplierLogic.SupplierManagement;
import Alpha.UserLogic.UserManagement; 

public class MainMenu { // Clase para mostrar el menú principal de opciones del sistema

    public static void showInventoryMenu (boolean esAdmin) { // Método para mostrar el menú principal de opciones
        System.out.println("\n=== MENÚ DE INVENTARIO ===");
        System.out.println("1. Mostrar Inventario");
        System.out.println("2. Modificaciones de Inventario");
        System.out.println("3. Búsqueda en Inventario");
        System.out.println("4. Reportes");
        System.out.println("5. Gestionar Proveedores");
        System.out.println("6. Cerrar sesión");
    }

    public static boolean processInventoryOption(int opcion, Scanner scanner, Inventory inventario, InventoryReport reporte, File archivo, UserManagement userManagement, boolean esAdmin, InventoryRepository repositorioInventario) 
    {
        SupplierManagement supplierManagement = new SupplierManagement();
        switch (opcion) {
            case 1 -> inventario.mostrarInventario();
            case 2 -> InventoryModificationMenu.showInventoryModificationSubmenu(scanner, inventario, reporte);
            case 3 -> SearchMenu.showSearchSubmenu(scanner, inventario);
            case 4 -> ReportMenu.showReportSubmenu(scanner, inventario, reporte);
            case 5 -> SupplierMenu.showSupplierSubmenu(scanner, supplierManagement);
            case 6 -> {
                inventario.guardarInventario(repositorioInventario);
                System.out.println("Sesión cerrada. ¡Hasta luego!");
                return true;
            }
            default -> System.out.println("Opción no válida.");
        }
        return false;
    }
}
