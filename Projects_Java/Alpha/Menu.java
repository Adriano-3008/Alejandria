package Alpha;
// Import necesario para trabajar con archivos y flujos de entrada/salida de datos (I/O)
import java.io.*; 
// Import necesario para trabajar con colecciones y Scanner
import java.util.*; 

public class Menu { // Clase para mostrar el menú principal de opciones del sistema

    public static void showInventoryMenu (boolean esAdmin) { // Método para mostrar el menú principal de opciones
        System.out.println("\n=== MENÚ DE INVENTARIO ===");
        System.out.println("1. Mostrar Inventario");
        System.out.println("2. Modificaciones de Inventario");
        System.out.println("3. Búsqueda en Inventario");
        System.out.println("4. Reportes");
        System.out.println("5. Gestionar Proveedores");
        System.out.println("6. Cerrar sesión");
    }

    public static boolean processInventoryOption( // Método para procesar la opción seleccionada por el usuario en el menú principal
        int opcion, Scanner scanner, InventoryManagement inventoryManagement, InventoryReport reporte, File archivo, UserManagement userManagement, boolean esAdmin, InventarioRepository inventarioRepository
        ) 
    {
        SupplierManagement supplierManagement = new SupplierManagement();
        switch (opcion) {
            case 1 -> inventoryManagement.mostrarInventario();
            case 2 -> InventoryModificationMenu.showInventoryModificationSubmenu(scanner, inventoryManagement, reporte);
            case 3 -> SearchMenu.showSearchSubmenu(scanner, inventoryManagement);
            case 4 -> ReportMenu.showReportSubmenu(scanner, inventoryManagement, reporte);
            case 5 -> SupplierMenu.showSupplierSubmenu(scanner, supplierManagement);
            case 6 -> {
                inventoryManagement.guardarInventario(inventarioRepository);
                System.out.println("Sesión cerrada. ¡Hasta luego!");
                return true;
            }
            default -> System.out.println("Opción no válida.");
        }
        return false;
    }
}

/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/

class InventoryModificationMenu { // Clase para mostrar el menú de opciones relacionadas con el inventario y sus operaciones

    public static void showInventoryModificationSubmenu (Scanner scanner, InventoryManagement inventory_Management, InventoryReport inventoryReport) { // Método para mostrar el menú de opciones del inventario 
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
                case 1 -> InventoryManagementSystem.agregarCategoria(scanner, inventory_Management);
                case 2 -> InventoryManagementSystem.agregarProductos(scanner, inventory_Management, inventoryReport);
                case 3 -> inventory_Management.modificarCantidad(scanner, inventoryReport);
                case 4 -> inventory_Management.modificarPrecio(scanner, inventoryReport);
                case 5 -> inventory_Management.eliminarProducto(scanner, inventoryReport);
                case 6 -> inventory_Management.eliminarCategoria(scanner, inventoryReport);
                case 7 -> {
                    System.out.print("Ingrese el nombre del archivo CSV para exportar (por ejemplo, 'Alpha/Archivo_Proyecto/inventario.csv'): ");
                    String nombreArchivo = scanner.nextLine();
                    File archivoCSV = new File(nombreArchivo);
                    inventory_Management.exportarInventario(archivoCSV);
                }
                case 8 -> {
                    System.out.print("Ingrese el nombre del archivo CSV para importar (por ejemplo, 'Alpha/Archivo_Proyecto/inventario.csv'): ");
                    String nombreArchivo = scanner.nextLine();
                    File archivoCSV = new File(nombreArchivo);
                    inventory_Management.importarInventario(archivoCSV);
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

/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/

class SearchMenu { // Clase para mostrar el menú de opciones relacionadas con la búsqueda de productos en el inventario

    public static void showSearchSubmenu(Scanner scanner, InventoryManagement inventory_Management) { // Método para mostrar el menú de opciones de búsqueda
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
                    inventory_Management.buscarPorCategoria(scanner.nextLine());
                }
                case 2 -> {
                    System.out.print("Ingrese el nombre del producto a buscar: ");
                    String nombreProducto = Validation.leerTextoNoVacio(scanner, "El nombre del producto no puede estar vacío. Intente nuevamente.");
                    inventory_Management.buscarPorNombre(nombreProducto);
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

/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/

class ReportMenu { // Clase para mostrar el menú de opciones relacionadas con los reportes del inventario

    public static void showReportSubmenu(Scanner scanner, InventoryManagement inventory_Management, InventoryReport inventoryReport) { // Método para mostrar el menú de opciones de reportes 
        boolean regresarAlMenu = false; 
        while (!regresarAlMenu) {
            System.out.println("\n=== REPORTES ===");
            System.out.println("1. Generar reporte detallado del inventario");
            System.out.println("2. Mostrar registro de cambios");
            System.out.println("3. Regresar al menú principal");
            System.out.print("Seleccione una opción: ");
            int opcionReporte = Integer.parseInt(scanner.nextLine());
            switch (opcionReporte) {
                case 1 -> inventoryReport.generarReporte(inventory_Management);
                case 2 -> inventoryReport.mostrarRegistroDeCambios(scanner);
                case 3 -> {
                    regresarAlMenu = true;
                    System.out.println("Regresando al menú principal...");
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }
}

/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/

class SupplierMenu { // Clase para mostrar el menú de opciones relacionadas con la gestión de proveedores

    public static void showSupplierSubmenu(Scanner scanner, SupplierManagement supplierManagement) { // Método para mostrar el menú de opciones de gestión de proveedores
        boolean regresarAlMenu = false;
        while (!regresarAlMenu) {
            System.out.println("\n=== GESTIÓN DE PROVEEDORES ===");
            System.out.println("1. Mostrar Proveedores");
            System.out.println("2. Agregar Proveedor");
            System.out.println("3. Eliminar Proveedor");
            System.out.println("4. Regresar al menú principal");
            System.out.print("Seleccione una opción: ");
            int opcionProveedores = Integer.parseInt(scanner.nextLine());
            switch (opcionProveedores) {
                case 1 -> supplierManagement.mostrarProveedores();
                case 2 -> supplierManagement.agregarProveedor(scanner);
                case 3 -> supplierManagement.eliminarProveedor(scanner);
                case 4 -> {
                    regresarAlMenu = true;
                    System.out.println("Regresando al menú principal...");
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }
}