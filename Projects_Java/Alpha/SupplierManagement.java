package Alpha;
// Import necesario para trabajar con archivos y flujos de entrada/salida de datos (I/O)
import java.io.*; 
// Import necesario para trabajar con colecciones y Scanner
import java.util.*;

class Supplier implements Serializable{ // Clase para representar un proveedor
    private static final long serialVersionUID = 1L;
    private String nombre;
    private String contacto;
    private String productosSuministrados;

    public Supplier(String nombre, String contacto, String productosSuministrados) { // Constructor de la clase Proveedor
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

/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/


public class SupplierManagement{ // Clase para controlar los proveedores y sus operaciones (agregar, modificar, eliminar, listar, etc.)
    private List<Supplier> listSupplier = new ArrayList<>(); // Lista para almacenar los proveedores
    private SupplierRepository supplierRepository;

    public SupplierManagement() { // Constructor de la clase ControlProveedores
        this.supplierRepository = new SupplierRepository(); // Inicializa el repositorio de proveedores
        supplierRepository.cargarProveedores();
        this.listSupplier = supplierRepository.getListSupplier(); // Carga la lista de proveedores desde el archivo
    }

    public void agregarProveedor(Scanner scanner) { // Método para agregar un nuevo proveedor
        System.out.println("\n=== REGISTRAR NUEVO PROVEEDOR ===");
        System.out.print("Ingrese el nombre del proveedor: ");
        String nombre = Validation.leerTextoNoVacio(scanner, "El nombre no puede estar vacío.");
        System.out.print("Ingrese el contacto del proveedor: ");
        String contacto = Validation.leerTextoNoVacio(scanner, "El contacto no puede estar vacío.");
        System.out.print("Ingrese los productos suministrados por el proveedor: ");
        String productos = Validation.leerTextoNoVacio(scanner, "Los productos no pueden estar vacíos.");

        Supplier supplier = new Supplier(nombre, contacto, productos);
        listSupplier.add(supplier);
        supplierRepository.guardarProveedores(); // Guarda la lista de proveedores en el archivo
        System.out.println("Proveedor registrado exitosamente.");
    }

    public void listarProveedores() { // Método para listar los proveedores registrados
        System.out.println("\n=== LISTA DE PROVEEDORES ===");
        if (listSupplier.isEmpty()) {
            System.out.println("No hay proveedores registrados.");
            return;
        }
        for (Supplier proveedor : listSupplier) {
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

        Supplier supplier = buscarProveedor(nombre);
        if (supplier == null) {
            System.out.println("Proveedor no encontrado.");
            return;
        }

        System.out.print("Ingrese el nuevo contacto (deje vacío para no cambiar): ");
        String nuevoContacto = scanner.nextLine();
        if (!nuevoContacto.isEmpty()) {
            supplier.setContacto(nuevoContacto);
        }

        System.out.print("Ingrese los nuevos productos suministrados (deje vacío para no cambiar): ");
        String nuevosProductos = scanner.nextLine();
        if (!nuevosProductos.isEmpty()) {
            supplier.setProductosSuministrados(nuevosProductos);
        }

        supplierRepository.guardarProveedores(); // Guarda la lista de proveedores en el archivo
        System.out.println("Proveedor modificado exitosamente.");
    }

    public void eliminarProveedor(Scanner scanner) { // Método para eliminar un proveedor existente
        System.out.println("\n=== ELIMINAR PROVEEDOR ===");
        listarProveedores();
        System.out.print("Ingrese el nombre del proveedor que desea eliminar: ");
        String nombre = scanner.nextLine();

        Supplier supplier = buscarProveedor(nombre);
        if (supplier != null) {
            listSupplier.remove(supplier);
            supplierRepository.guardarProveedores(); // Guarda la lista de proveedores en el archivo
            System.out.println("Proveedor eliminado exitosamente.");
        } else {
            System.out.println("Proveedor no encontrado.");
        }
    }

    public Supplier buscarProveedor(String nombre) { // Método para buscar un proveedor por su nombre en la lista de proveedores registrados y devolverlo si se encuentra
        return listSupplier.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }
   
}

/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/

class SupplierRepository {
    private final File archivoProveedores = new File("Projects_Java/Archivo_Proyecto_Alpha/.dats/Archivo_Proveedores.dat");
    private List<Supplier> listSupplier = new ArrayList<>();

    public SupplierRepository() {
        cargarProveedores();
    }
  
    
    @SuppressWarnings("unchecked")
    public void cargarProveedores() {
        if (!archivoProveedores.exists()) {
            try {
                archivoProveedores.getParentFile().mkdirs();
                archivoProveedores.createNewFile();
                System.out.println("El archivo de proveedores no existía. Se ha creado un nuevo archivo.");
            } catch (IOException e) {
                System.out.println("Error al crear el archivo de proveedores: " + e.getMessage());
            }
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoProveedores))) {
            listSupplier = (List<Supplier>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar los proveedores: " + e.getMessage());
        }
    }

    public void guardarProveedores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoProveedores))) {
            oos.writeObject(listSupplier);
        } catch (IOException e) {
            System.out.println("Error al guardar los proveedores: " + e.getMessage());
        }
    }

    public List<Supplier> getListSupplier() {
        return listSupplier;
    }
}