package Alpha;
// Import necesario para trabajar con archivos y flujos de entrada/salida de datos (I/O)
import java.io.*; 
// Import necesario para trabajar con colecciones y Scanner
import java.util.*; 

public class Proveedor implements Serializable{ // Clase para representar un proveedor
    private static final long serialVersionUID = 1L;
    private String nombre;
    private String contacto;
    private String productosSuministrados;

    public Proveedor(String nombre, String contacto, String productosSuministrados) { // Constructor de la clase Proveedor
        this.nombre = nombre;
        this.contacto = contacto;
        this.productosSuministrados = productosSuministrados;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }

    public String getProductosSuministrados() { return productosSuministrados; }
    public void setProductosSuministrados(String productosSuministrados) { this.productosSuministrados = productosSuministrados; }

    @Override
    public String toString() {
        return String.format("Proveedor: %s | Contacto: %s | Productos: %s", nombre, contacto, productosSuministrados);
    }
}

class ControlProveedor{ // Clase para controlar los proveedores y sus operaciones (agregar, modificar, eliminar, listar, etc.)
    private final File archivoProveedores = new File("Alpha/Archivo_Proyecto/proveedores.dat"); // Ruta del archivo para almacenar los proveedores
    private List<Proveedor> listaProveedores = new ArrayList<>(); // Lista para almacenar los proveedores

    public ControlProveedor() { // Constructor de la clase ControlProveedores
        cargarProveedores();
    }

    public void agregarProveedor(Scanner scanner) { // Método para agregar un nuevo proveedor
        System.out.println("\n=== REGISTRAR NUEVO PROVEEDOR ===");
        System.out.print("Ingrese el nombre del proveedor: ");
        String nombre = Validaciones.leerTextoNoVacio(scanner, "El nombre no puede estar vacío.");
        System.out.print("Ingrese el contacto del proveedor: ");
        String contacto = Validaciones.leerTextoNoVacio(scanner, "El contacto no puede estar vacío.");
        System.out.print("Ingrese los productos suministrados por el proveedor: ");
        String productos = Validaciones.leerTextoNoVacio(scanner, "Los productos no pueden estar vacíos.");

        Proveedor nuevoProveedor = new Proveedor(nombre, contacto, productos);
        listaProveedores.add(nuevoProveedor);
        guardarProveedores();
        System.out.println("Proveedor registrado exitosamente.");
    }

    public void listarProveedores() { // Método para listar los proveedores registrados
        System.out.println("\n=== LISTA DE PROVEEDORES ===");
        if (listaProveedores.isEmpty()) {
            System.out.println("No hay proveedores registrados.");
            return;
        }
        for (Proveedor proveedor : listaProveedores) {
            System.out.println(proveedor);
        }
    }

    public void mostrarProveedores() {
        listarProveedores();
    }

    public void modificarProveedor(Scanner scanner) { // Método para modificar un proveedor existente
        System.out.println("\n=== MODIFICAR PROVEEDOR ===");
        listarProveedores();
        System.out.print("Ingrese el nombre del proveedor que desea modificar: ");
        String nombre = scanner.nextLine();

        Proveedor proveedor = buscarProveedor(nombre);
        if (proveedor == null) {
            System.out.println("Proveedor no encontrado.");
            return;
        }

        System.out.print("Ingrese el nuevo contacto (deje vacío para no cambiar): ");
        String nuevoContacto = scanner.nextLine();
        if (!nuevoContacto.isEmpty()) {
            proveedor.setContacto(nuevoContacto);
        }

        System.out.print("Ingrese los nuevos productos suministrados (deje vacío para no cambiar): ");
        String nuevosProductos = scanner.nextLine();
        if (!nuevosProductos.isEmpty()) {
            proveedor.setProductosSuministrados(nuevosProductos);
        }

        guardarProveedores();
        System.out.println("Proveedor modificado exitosamente.");
    }

    public void eliminarProveedor(Scanner scanner) { // Método para eliminar un proveedor existente
        System.out.println("\n=== ELIMINAR PROVEEDOR ===");
        listarProveedores();
        System.out.print("Ingrese el nombre del proveedor que desea eliminar: ");
        String nombre = scanner.nextLine();

        Proveedor proveedor = buscarProveedor(nombre);
        if (proveedor != null) {
            listaProveedores.remove(proveedor);
            guardarProveedores();
            System.out.println("Proveedor eliminado exitosamente.");
        } else {
            System.out.println("Proveedor no encontrado.");
        }
    }

    public Proveedor buscarProveedor(String nombre) { // Método para buscar un proveedor por su nombre en la lista de proveedores registrados y devolverlo si se encuentra
        return listaProveedores.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }
    @SuppressWarnings("unchecked") //Para evitar advertencias de compilación de tipos en la conversión de objetos a Map<String, List<Productos>>
    public void cargarProveedores() { // Método para cargar los proveedores desde el archivo de proveedores
        if (!archivoProveedores.exists()) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoProveedores))) { 
            listaProveedores = (List<Proveedor>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar los proveedores: " + e.getMessage());
        }
    }

    public void guardarProveedores() { // Método para guardar los proveedores en el archivo de proveedores
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoProveedores))) {
            oos.writeObject(listaProveedores);
        } catch (IOException e) {
            System.out.println("Error al guardar los proveedores: " + e.getMessage());
        }
    }
}