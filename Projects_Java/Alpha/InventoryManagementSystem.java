package Alpha;

// Import necesario para trabajar con archivos y flujos de entrada/salida de datos (I/O)
import java.io.*; 
// Import necesario para trabajar con colecciones y Scanner
import java.util.*;

import Alpha.InteractionsMenus.MainMenu;
import Alpha.InventoryLogic.Inventory;
import Alpha.InventoryLogic.InventoryReport;
import Alpha.InventoryLogic.Producto;
import Alpha.Repositories.InventoryRepository;
import Alpha.UserLogic.UserManagement;


 public class InventoryManagementSystem { 
    public static void main(String[] args) { 
        Scanner scanner = new Scanner(System.in);
        UserManagement userManagement = new UserManagement();
        Inventory inventario = new Inventory();
        File ficheroInventario = new File("Alpha/Archivo_Proyecto/inventario.dat");
        InventoryRepository repositorioInventario = new InventoryRepository(ficheroInventario);
        InventoryReport inventoryReport = new InventoryReport();
        // --------------------------
        boolean salirSistema = false;
        while (!salirSistema) {
            String usuarioLogueado = manejarLogin(scanner, userManagement);
            inventario.cargarInventario(repositorioInventario);

            boolean cerrarSesion = false;
            boolean esAdmin = userManagement.esUsuarioAdmin(usuarioLogueado);
            while (!cerrarSesion) {
                mostrarMenu(esAdmin);
                cerrarSesion = procesarOpcion(scanner, inventario, inventoryReport, ficheroInventario, userManagement, esAdmin, repositorioInventario);
            }

            inventario.guardarInventario(repositorioInventario);
        }

        scanner.close();
    }
    // Método para manejar el inicio de sesión del usuario
    public static String manejarLogin(Scanner scanner, UserManagement userManagement) { 
        return userManagement.manejarLogin(scanner);
    }
     // Método para mostrar el menú principal del sistema
    public static void mostrarMenu(boolean esAdmin) { 
        MainMenu.showInventoryMenu(esAdmin);
    }
    //---------------------------------------------------------
    public static boolean procesarOpcion(Scanner scanner, Inventory inventario, InventoryReport inventoryReport, File archivoInventario, UserManagement userManagement, boolean esAdmin, InventoryRepository repositorioInventario) {
        try {
            System.out.print("Seleccione una opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());
            return MainMenu.processInventoryOption(opcion, scanner, inventario, inventoryReport, archivoInventario, userManagement, esAdmin, repositorioInventario);
        } catch (NumberFormatException e) {
            System.out.println("Error: Por favor, ingrese un número válido.");
            return false;
        }
    }
    // Método para agregar una nueva categoría al inventario
    public static void agregarCategoria(Scanner scanner, Inventory inventario) {
        System.out.print("Ingrese el nombre de la nueva categoría: ");
        String categoria = Validation.leerTextoNoVacio(scanner, "El nombre de la categoría no puede estar vacío. Intente nuevamente.");
        inventario.agregarCategoria(categoria);
    }
    // Método para agregar productos a una categoría del inventario
    public static void agregarProductos(Scanner scanner, Inventory inventario, InventoryReport inventoryReport) {
        System.out.print("Ingrese la categoría del producto: ");
        String categoria = Validation.leerTextoNoVacio(scanner, "La categoría no puede estar vacía. Intente nuevamente.");
    
        if (!inventario.existeCategoria(categoria)) {
            System.out.println("La categoría no existe. Cree la categoría primero.");
            return;
        }
    
        boolean agregarMas = true; // Variable para controlar si se agregan más productos a la categoría
        while (agregarMas) {
            System.out.print("Ingrese el nombre del producto: ");
            String nombre = Validation.leerTextoNoVacio(scanner, "El nombre del producto no puede estar vacío. Intente nuevamente.");
            System.out.print("Ingrese la cantidad en stock: ");
            int cantidad = Validation.leerEnteroPositivo(scanner, "Ingrese un número válido para la cantidad.");
            System.out.print("Ingrese el precio del producto: ");
            double precio = Validation.leerDoublePositivo(scanner, "Ingrese un número válido para el precio.");
    
            Producto nuevoProducto = new Producto(nombre, categoria, cantidad, precio);
            inventario.agregarProducto(nuevoProducto);
            System.out.println("Producto agregado correctamente.");
    
            inventoryReport.registrarCambio("Agregar Producto", categoria, nombre,
                    "Cantidad: " + cantidad + ", Precio: $" + precio);
    
            System.out.print("¿Desea agregar otro producto a esta categoría? (s/n): ");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("n")) {
                agregarMas = false;
            }
        }
    }
}
