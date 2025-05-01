package Alpha;
// Import necesario para trabajar con archivos CSV y Paths
import java.nio.file.*; 
// Import necesario para trabajar con archivos y flujos de entrada/salida de datos (I/O)
import java.io.*; 
// Import necesario para trabajar con colecciones y Scanner
import java.util.*; 

// Clase para controlar el inventario de productos y sus operaciones (agregar, modificar, eliminar, buscar, etc.)
public class InventoryManagement {
    private Map<String, List<Producto>> productosPorCategoria = new HashMap<>();
    private alertaStock alerta = new alertaStock();
    private ArchivoCSV archivoCSV = new ArchivoCSV();

    public void agregarProducto(Producto producto) {
        productosPorCategoria.computeIfAbsent(producto.getCategoria(), k -> new ArrayList<>()).add(producto);
        // Registrar el ingreso
        String detalle = "Ingreso inicial: " + producto.getCantidad() + " unidades.";
        InventoryReport reporte = new InventoryReport();
        reporte.registrarCambio("Agregar Producto", producto.getCategoria(), producto.getNombre(), detalle);
    
        // Verificar stock bajo solo si es necesario
        if (producto.getCantidad() < alertaStock.LIMITE_STOCK_BAJO) {
            alerta.verificarStockBajo(productosPorCategoria);
        }
    }

    // Método para verificar si una categoría ya existe en el inventario
    public boolean existeCategoria(String categoria) { 
        return productosPorCategoria.containsKey(categoria); 
    }

    // Método para agregar una nueva categoría al inventario
    public void agregarCategoria(String categoria) { 
        if (existeCategoria(categoria)) { 
            System.out.println("La categoría '" + categoria + "' ya existe."); 
        } 
        else {
            productosPorCategoria.put(categoria, new ArrayList<>());
            System.out.println("Categoría '" + categoria + "' creada correctamente.");
        }
    }

     // Método para mostrar el inventario actualizado
    public void mostrarInventario() { 
        if (productosPorCategoria.isEmpty()) {
            System.out.println("El inventario está vacío.");
            return;
        }
    
        System.out.println("\n=== Inventario Actual ===");
        for (var entry : productosPorCategoria.entrySet()) {
            String categoria = entry.getKey();
            List<Producto> productos = entry.getValue();
    
            System.out.println("\nCategoría: " + categoria);
    
            int anchoProducto = Math.max("Producto".length(), productos.stream().mapToInt(p -> p.getNombre().length()).max().orElse(0));
            int anchoCantidad = Math.max("Cantidad".length(), productos.stream().mapToInt(p -> String.valueOf(p.getCantidad()).length()).max().orElse(0));
            int anchoPrecio = Math.max("Precio".length(), productos.stream().mapToInt(p -> String.format("$%,.2f", p.getPrecio()).length()).max().orElse(0));
            int anchoValorTotal = Math.max("Valor Total".length(), productos.stream().mapToInt(p -> String.format("$%,.2f", p.getCantidad() * p.getPrecio()).length()).max().orElse(0));

            String formatoEncabezado = "| %-" + anchoProducto + "s | %-" + anchoCantidad + "s | %-" + anchoPrecio + "s | %-" + anchoValorTotal + "s |\n";
            String separador = "+-" + "-".repeat(anchoProducto) + "-+-" + "-".repeat(anchoCantidad) + "-+-" + "-".repeat(anchoPrecio) + "-+-" + "-".repeat(anchoValorTotal) + "-+";
    
            System.out.println(separador);
            System.out.printf(formatoEncabezado, "Producto", "Cantidad", "Precio", "Valor Total");
            System.out.println(separador);
    
            String formatoFila = "| %-" + anchoProducto + "s | %-" + anchoCantidad + "d | %-" + anchoPrecio + "s | %-" + anchoValorTotal + "s |\n";
            for (Producto p : productos) {
                String precioFormateado = String.format("$%,.2f", p.getPrecio());
                String valorTotalFormateado = String.format("$%,.2f", p.getCantidad() * p.getPrecio());
                System.out.printf(formatoFila, p.getNombre(), p.getCantidad(), precioFormateado, valorTotalFormateado);
            }
            System.out.println(separador);
        }
    }

     // Método para buscar un producto por su nombre y categoría en el inventario
    public Producto buscarProducto(String categoria, String nombreProducto) { 
        return productosPorCategoria.getOrDefault(categoria, new ArrayList<>())
                .stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombreProducto))
                .findFirst()
                .orElse(null);
    }

     // Método para modificar la cantidad de un producto en el inventario por su nombre y categoría
    public void modificarCantidad(Scanner scanner, InventoryReport reporte) {
        System.out.print("\nIngrese la categoría del producto: ");
        String categoria = Validation.leerTextoNoVacio(scanner, "La categoría no puede estar vacía. Intente nuevamente.");
        System.out.print("Ingrese el nombre del producto: ");
        String nombreProducto = Validation.leerTextoNoVacio(scanner, "El nombre del producto no puede estar vacío. Intente nuevamente.");
    
        Producto producto = buscarProducto(categoria, nombreProducto);
        if (producto != null) {
            System.out.print("Ingrese la nueva cantidad: ");
            int nuevaCantidad = Validation.leerEnteroPositivo(scanner, "Ingrese un número válido para la cantidad.");
            int cantidadAnterior = producto.getCantidad();
            producto.setCantidad(nuevaCantidad);
    
            String detalle = nuevaCantidad > cantidadAnterior
                    ? "Nueva cantidad mayor: Ingreso de " + (nuevaCantidad - cantidadAnterior) + " unidades."
                    : "Nueva cantidad menor: Retiro de " + (cantidadAnterior - nuevaCantidad) + " unidades.";
    
            reporte.registrarCambio("Modificar Cantidad", categoria, nombreProducto, detalle);
            System.out.println("Cantidad actualizada correctamente.");
            alerta.verificarStockBajo(productosPorCategoria);
        } else {
            System.out.println("Producto o categoría no encontrado.");
        }
    }

     // Método para modificar el precio de un producto en el inventario por su nombre y categoría
    public void modificarPrecio(Scanner scanner, InventoryReport reporte) {
        System.out.print("\nIngrese la categoría del producto: ");
        String categoria = Validation.leerTextoNoVacio(scanner, "La categoría no puede estar vacía. Intente nuevamente.");
        System.out.print("Ingrese el nombre del producto: ");
        String nombreProducto = Validation.leerTextoNoVacio(scanner, "El nombre del producto no puede estar vacío. Intente nuevamente.");

        Producto producto = buscarProducto(categoria, nombreProducto);
        if (producto != null) {
            System.out.print("Ingrese el nuevo precio: ");
            double nuevoPrecio = Validation.leerDoublePositivo(scanner, "Ingrese un número válido para el precio.");
            double precioAnterior = producto.getPrecio();
            producto.setPrecio(nuevoPrecio);
            System.out.println("Precio actualizado correctamente.");
            reporte.registrarCambio("Modificar Precio", categoria, nombreProducto,
                    "Precio anterior: $" + precioAnterior + ", Nuevo precio: $" + nuevoPrecio);
            alerta.verificarStockBajo(productosPorCategoria);
        } else {
            System.out.println("Producto o categoría no encontrado.");
        }
    }

    // Método para eliminar un producto del inventario por su nombre y categoría
    public void eliminarProducto(Scanner scanner, InventoryReport reporte) { 
        System.out.print("\nIngrese la categoría del producto: ");
        String categoria = scanner.nextLine();
        System.out.print("Ingrese el nombre del producto: ");
        String nombreProducto = scanner.nextLine();
    
        List<Producto> productos = productosPorCategoria.get(categoria);
        if (productos != null && productos.removeIf(p -> p.getNombre().equalsIgnoreCase(nombreProducto))) {
            String detalle = "Retiro total: Producto eliminado del inventario.";
            reporte.registrarCambio("Eliminar Producto", categoria, nombreProducto, detalle);
            System.out.println("Producto eliminado correctamente.");
            alerta.verificarStockBajo(productosPorCategoria);
        } else {
            System.out.println("Producto o categoría no encontrado.");
        }
    }

     // Método para eliminar una categoría y sus productos del inventario
    public void eliminarCategoria(Scanner scanner, InventoryReport reporte) { 
        System.out.print("\nIngrese el nombre de la categoría que desea eliminar: ");
        String categoria = scanner.nextLine();

        if (productosPorCategoria.containsKey(categoria)) {
            productosPorCategoria.remove(categoria);
            System.out.println("Categoría '" + categoria + "' eliminada correctamente junto con sus productos.");
        } else {
            System.out.println("La categoría no existe.");
        }
    }

    // Método para buscar un producto por su nombre en el inventario
    public void buscarPorNombre(String nombreProducto) {
        boolean encontrado = false;
        System.out.println("\n=== Resultados de la búsqueda por nombre ===");
        for (var entry : productosPorCategoria.entrySet()) {
            for (Producto p : entry.getValue()) {
                if (p.getNombre().equalsIgnoreCase(nombreProducto)) {
                    System.out.printf("Categoría: %s | Producto: %s | Cantidad: %d | Precio: $%.2f\n",
                            entry.getKey(), p.getNombre(), p.getCantidad(), p.getPrecio());
                    encontrado = true;
                }
            }
        }
        if (!encontrado) {
            System.out.println("No se encontró ningún producto con el nombre especificado.");
        }
    }

    // Método para buscar productos por categoría  en el inventario
    public void buscarPorCategoria(String categoria) { 
        List<Producto> productos = productosPorCategoria.get(categoria);
        if (productos != null && !productos.isEmpty()) {
            System.out.println("\n=== Productos en la categoría: " + categoria + " ===");
            for (Producto p : productos) {
                System.out.printf("Producto: %s | Cantidad: %d | Precio: $%.2f\n",
                        p.getNombre(), p.getCantidad(), p.getPrecio());
            }
        } else {
            System.out.println("No se encontraron productos en la categoría especificada.");
        }
    }

    public void cargarInventario(InventarioRepository repository) {
        productosPorCategoria = repository.cargarInventario();
    }

    public void guardarInventario(InventarioRepository repository) {
        repository.guardarInventario(productosPorCategoria);
    }

    // Método para obtener los productos por categoría en el inventario
    public Map<String, List<Producto>> getProductosPorCategoria() { 
        return productosPorCategoria;
    }

    public void exportarInventario(File archivoCSV) {
        this.archivoCSV.exportar(archivoCSV, productosPorCategoria);
    }

    public void importarInventario(File archivoCSV) {
        this.archivoCSV.importar(archivoCSV, productosPorCategoria, this);
    }
}

/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/

class InventorySearch{

}

/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/

class InventarioRepository {
    private final File archivoInventario;

    public InventarioRepository(File archivo) {
        this.archivoInventario = new File("Projects_Java/Archivo_Proyecto_Alpha/.dats/Archivo_Inventario.dat");
    }

    @SuppressWarnings("unchecked")
    public Map<String, List<Producto>> cargarInventario() {
        Map<String, List<Producto>> productosPorCategoria = new HashMap<>();
        try {
            if (!archivoInventario.exists()) {
                archivoInventario.getParentFile().mkdirs();
                archivoInventario.createNewFile();
                System.out.println("El archivo de inventario no existía. Se ha creado un nuevo archivo en: " + archivoInventario.getAbsolutePath());
                return productosPorCategoria;
            }

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoInventario))) {
                Object obj = ois.readObject();
                if (obj instanceof Map<?, ?>) {
                    productosPorCategoria = (Map<String, List<Producto>>) obj;
                } else {
                    System.out.println("El archivo no contiene un formato válido de inventario.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar el inventario: " + e.getMessage());
        }
        return productosPorCategoria;
    }

    public void guardarInventario(Map<String, List<Producto>> productosPorCategoria) {
        try {
            if (!archivoInventario.exists()) {
                archivoInventario.getParentFile().mkdirs();
                archivoInventario.createNewFile();
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoInventario))) {
                oos.writeObject(productosPorCategoria);
            }
        } catch (IOException e) {
            System.out.println("Error al guardar el inventario: " + e.getMessage());
        }
    }
}

/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/

class ArchivoCSV {

    public void exportar(File archivoCSV, Map<String, List<Producto>> productosPorCategoria) {
        try (BufferedWriter writer = Files.newBufferedWriter(archivoCSV.toPath())) {
            writer.write("Categoría,Producto,Cantidad,Precio\n"); // Encabezado del archivo CSV
            for (var entry : productosPorCategoria.entrySet()) {
                String categoria = entry.getKey();
                for (Producto producto : entry.getValue()) {
                    writer.write(String.format("%s,%s,%d,%.2f\n",
                            categoria, producto.getNombre(), producto.getCantidad(), producto.getPrecio()));
                }
            }
            System.out.println("Inventario exportado correctamente a " + archivoCSV.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error al exportar el inventario a CSV: " + e.getMessage());
        }
    }

    public void importar(File archivoCSV, Map<String, List<Producto>> productosPorCategoria, InventoryManagement controlStock) {
        if (!archivoCSV.exists()) {
            System.out.println("El archivo CSV especificado no existe en: " + archivoCSV.getAbsolutePath());
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(archivoCSV.toPath())) {
            String linea = reader.readLine(); // Leer encabezado
            if (linea == null || !linea.equals("Categoría,Producto,Cantidad,Precio")) {
                System.out.println("El archivo CSV no tiene el formato esperado. Asegúrate de que el encabezado sea 'Categoría,Producto,Cantidad,Precio'.");
                return;
            }

            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 4) {
                    try {
                        String categoria = datos[0].trim();
                        String nombre = datos[1].trim();
                        int cantidad = Integer.parseInt(datos[2].trim());
                        double precio = Double.parseDouble(datos[3].trim());

                        // Verificar si el producto ya existe
                        Producto productoExistente = controlStock.buscarProducto(categoria, nombre);
                        if (productoExistente != null) {
                            // Actualizar cantidad y precio del producto existente
                            productoExistente.setCantidad(productoExistente.getCantidad() + cantidad);
                            productoExistente.setPrecio(precio);
                            System.out.printf("Producto actualizado: Categoría='%s', Nombre='%s', Nueva Cantidad=%d, Nuevo Precio=%.2f\n",
                                    categoria, nombre, productoExistente.getCantidad(), precio);
                        } else {
                            // Agregar nuevo producto
                            Producto nuevoProducto = new Producto(nombre, categoria, cantidad, precio);
                            controlStock.agregarProducto(nuevoProducto);
                            System.out.printf("Producto importado: Categoría='%s', Nombre='%s', Cantidad=%d, Precio=%.2f\n",
                                    categoria, nombre, cantidad, precio);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error al procesar una línea del archivo CSV. Asegúrate de que los valores de cantidad y precio sean numéricos.");
                    }
                } else {
                    System.out.println("Línea ignorada por formato incorrecto: " + linea);
                }
            }
            System.out.println("Inventario importado correctamente desde " + archivoCSV.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }
}

/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/

// Clase para verificar y mostrar alertas de productos con stock bajo
class alertaStock { 
    public static final int LIMITE_STOCK_BAJO = 20; // Límite de stock bajo para mostrar la alerta

    // Método para verificar si hay productos con stock bajo
    public void verificarStockBajo(Map<String, List<Producto>> productosPorCategoria) { 
        boolean hayStockBajo = false;
        StringBuilder alerta = new StringBuilder("\n=== ALERTA: Productos con stock bajo ===\n");

        for (var entry : productosPorCategoria.entrySet()) {
            String categoria = entry.getKey();
            for (Producto producto : entry.getValue()) {
                if (producto.getCantidad() < LIMITE_STOCK_BAJO) {
                    hayStockBajo = true;
                    alerta.append(String.format("Categoría: %s | Producto: %s | Stock: %d | Precio: $%.2f\n",
                            categoria, producto.getNombre(), producto.getCantidad(), producto.getPrecio()));
                }
            }
        }

        if (hayStockBajo) {
            System.out.println(alerta);
        }
    }
}