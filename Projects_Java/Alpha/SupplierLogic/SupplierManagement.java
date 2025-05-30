package Alpha.SupplierLogic;
import java.util.*;
import Alpha.Validation;
import Alpha.Repositories.SupplierRepository;

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
