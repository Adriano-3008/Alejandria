package Alpha;

// Import necesario para trabajar con archivos y flujos de entrada/salida de datos (I/O)
import java.io.*; 
// Import necesario para trabajar con colecciones y Scanner
import java.util.*; 

// Clase para representar los productos en el inventario y sus atributos (nombre, categoría, cantidad y precio)
class Producto implements Serializable { 
    private static final long serialVersionUID = 1L;
    private String nombre; 
    private String categoria; 
    private int cantidad; 
    private double precio; 

    // Constructor de la clase Productos
    public Producto(String nombre, String categoria, int cantidad, double precio) {
        this.nombre = nombre; 
        this.categoria = categoria; 
        this.cantidad = cantidad; 
        this.precio = precio; 
    }
    // Métodos getter y setter para los atributos de la clase Productos
    public String getNombre() { return nombre; } 
    public String getCategoria() { return categoria; } 
    public int getCantidad() { return cantidad; } 
    public void setCantidad(int cantidad) { this.cantidad = cantidad; } 
    public double getPrecio() { return precio; } 
    public void setPrecio(double precio) { this.precio = precio; } 
 }

 // Clase principal para ejecutar el sistema de gestión de inventarios
 public class InventoryManagementSystem { // Clase principal para ejecutar el sistema de gestión de inventarios
    public static void main(String[] args) { // Método principal para ejecutar el sistema
        Scanner scanner = new Scanner(System.in);
        UserManagement userManagement = new UserManagement();
        InventoryManagement inventoryManagement = new InventoryManagement();
        File archivoInventario = new File("Alpha/Archivo_Proyecto/inventario.dat");
        InventarioRepository inventarioRepository = new InventarioRepository(archivoInventario);
        InventoryReport inventoryReport = new InventoryReport();
        // --------------------------
        boolean salirSistema = false;
        while (!salirSistema) {
            String usuarioLogueado = manejarLogin(scanner, userManagement);
            inventoryManagement.cargarInventario(inventarioRepository);

            boolean cerrarSesion = false;
            boolean esAdmin = userManagement.esUsuarioAdmin(usuarioLogueado);
            while (!cerrarSesion) {
                mostrarMenu(esAdmin);
                cerrarSesion = procesarOpcion(scanner, inventoryManagement, inventoryReport, archivoInventario, userManagement, esAdmin, inventarioRepository);
            }

            inventoryManagement.guardarInventario(inventarioRepository);
        }

        scanner.close();
    }
    // Método para manejar el inicio de sesión del usuario
    public static String manejarLogin(Scanner scanner, UserManagement userManagement) { 
        return userManagement.manejarLogin(scanner);
    }
     // Método para mostrar el menú principal del sistema
    public static void mostrarMenu(boolean esAdmin) { 
        Menu.mostrarMenuInventario(esAdmin);
    }
    //---------------------------------------------------------
    public static boolean procesarOpcion(Scanner scanner, InventoryManagement inventoryManagement, InventoryReport inventoryReport, File archivoInventario, UserManagement userManagement, boolean esAdmin, InventarioRepository inventarioRepository) {
        try {
            System.out.print("Seleccione una opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());
            return Menu.procesarOpcionInventario(opcion, scanner, inventoryManagement, inventoryReport, archivoInventario, userManagement, esAdmin, inventarioRepository);
        } catch (NumberFormatException e) {
            System.out.println("Error: Por favor, ingrese un número válido.");
            return false;
        }
    }
    // Método para agregar una nueva categoría al inventario
    public static void agregarCategoria(Scanner scanner, InventoryManagement inventoryManagement) {
        System.out.print("Ingrese el nombre de la nueva categoría: ");
        String categoria = Validation.leerTextoNoVacio(scanner, "El nombre de la categoría no puede estar vacío. Intente nuevamente.");
        inventoryManagement.agregarCategoria(categoria);
    }
    // Método para agregar productos a una categoría del inventario
    public static void agregarProductos(Scanner scanner, InventoryManagement inventoryManagement, InventoryReport inventoryReport) {
        System.out.print("Ingrese la categoría del producto: ");
        String categoria = Validation.leerTextoNoVacio(scanner, "La categoría no puede estar vacía. Intente nuevamente.");
    
        if (!inventoryManagement.existeCategoria(categoria)) {
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
            inventoryManagement.agregarProducto(nuevoProducto);
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
