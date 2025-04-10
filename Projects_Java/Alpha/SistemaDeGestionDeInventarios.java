package Alpha;

// Import necesario para trabajar con archivos y flujos de entrada/salida de datos (I/O)
import java.io.*; 
// Import necesario para trabajar con colecciones y Scanner
import java.util.*; 

// Clase para representar los productos en el inventario y sus atributos (nombre, categoría, cantidad y precio)
class Productos implements Serializable { 
    private static final long serialVersionUID = 1L;
    private String nombre; 
    private String categoria; 
    private int cantidad; 
    private double precio; 

    // Constructor de la clase Productos
    public Productos(String nombre, String categoria, int cantidad, double precio) {
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
 public class SistemaDeGestionDeInventarios { // Clase principal para ejecutar el sistema de gestión de inventarios
    public static void main(String[] args) { // Método principal para ejecutar el sistema
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();
        ControlStock controlStock = new ControlStock();
        File archivoInventario = new File("Alpha/Archivo_Proyecto/inventario.dat");
        InventarioRepository inventarioRepository = new InventarioRepository(archivoInventario);
        ReporteDeInventario reporte = new ReporteDeInventario();
        // --------------------------
        boolean salirSistema = false;
        while (!salirSistema) {
            String usuarioLogueado = manejarLogin(scanner, login);
            controlStock.cargarInventario(inventarioRepository);

            boolean cerrarSesion = false;
            boolean esAdmin = login.esUsuarioAdmin(usuarioLogueado);
            while (!cerrarSesion) {
                mostrarMenu(esAdmin);
                cerrarSesion = procesarOpcion(scanner, controlStock, reporte, archivoInventario, login, esAdmin, inventarioRepository);
            }

            controlStock.guardarInventario(inventarioRepository);
        }

        scanner.close();
    }
    // Método para manejar el inicio de sesión del usuario
    public static String manejarLogin(Scanner scanner, Login login) { 
        return login.manejarLogin(scanner);
    }
     // Método para mostrar el menú principal del sistema
    public static void mostrarMenu(boolean esAdmin) { 
        Menu.mostrarMenuInventario(esAdmin);
    }
    //---------------------------------------------------------
    public static boolean procesarOpcion(Scanner scanner, ControlStock controlStock, ReporteDeInventario reporte, File archivoInventario, Login login, boolean esAdmin, InventarioRepository inventarioRepository) {
        try {
            System.out.print("Seleccione una opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());
            return Menu.procesarOpcionInventario(opcion, scanner, controlStock, reporte, archivoInventario, login, esAdmin, inventarioRepository);
        } catch (NumberFormatException e) {
            System.out.println("Error: Por favor, ingrese un número válido.");
            return false;
        }
    }
    // Método para agregar una nueva categoría al inventario
    public static void agregarCategoria(Scanner scanner, ControlStock controlStock) {
        System.out.print("Ingrese el nombre de la nueva categoría: ");
        String categoria = Validaciones.leerTextoNoVacio(scanner, "El nombre de la categoría no puede estar vacío. Intente nuevamente.");
        controlStock.agregarCategoria(categoria);
    }
    // Método para agregar productos a una categoría del inventario
    public static void agregarProductos(Scanner scanner, ControlStock controlStock, ReporteDeInventario reporte) {
        System.out.print("Ingrese la categoría del producto: ");
        String categoria = Validaciones.leerTextoNoVacio(scanner, "La categoría no puede estar vacía. Intente nuevamente.");
    
        if (!controlStock.existeCategoria(categoria)) {
            System.out.println("La categoría no existe. Cree la categoría primero.");
            return;
        }
    
        boolean agregarMas = true; // Variable para controlar si se agregan más productos a la categoría
        while (agregarMas) {
            System.out.print("Ingrese el nombre del producto: ");
            String nombre = Validaciones.leerTextoNoVacio(scanner, "El nombre del producto no puede estar vacío. Intente nuevamente.");
            System.out.print("Ingrese la cantidad en stock: ");
            int cantidad = Validaciones.leerEnteroPositivo(scanner, "Ingrese un número válido para la cantidad.");
            System.out.print("Ingrese el precio del producto: ");
            double precio = Validaciones.leerDoublePositivo(scanner, "Ingrese un número válido para el precio.");
    
            Productos nuevoProducto = new Productos(nombre, categoria, cantidad, precio);
            controlStock.agregarProducto(nuevoProducto);
            System.out.println("Producto agregado correctamente.");
    
            reporte.registrarCambio("Agregar Producto", categoria, nombre,
                    "Cantidad: " + cantidad + ", Precio: $" + precio);
    
            System.out.print("¿Desea agregar otro producto a esta categoría? (s/n): ");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("n")) {
                agregarMas = false;
            }
        }
    }
}
