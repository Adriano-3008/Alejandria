package Alpha.InventoryLogic;
import java.io.*; 
import java.util.*;

import Alpha.Validation;
import Alpha.Repositories.FileCSV;
import Alpha.Repositories.InventoryRepository; 

// Clase para controlar el inventario de productos y sus operaciones (agregar, modificar, eliminar, buscar, etc.)
public class Inventory {
    private Map<String, List<Producto>> productosPorCategoria = new HashMap<>();
    private StockAlert alerta = new StockAlert();
    private FileCSV ficherocsv = new FileCSV();

    public void agregarProducto(Producto producto) {
        productosPorCategoria.computeIfAbsent(producto.getCategoria(), k -> new ArrayList<>()).add(producto);
        // Registrar el ingreso
        String detalle = "Ingreso inicial: " + producto.getCantidad() + " unidades.";
        InventoryReport reporteInventario = new InventoryReport();
        reporteInventario.registrarCambio("Agregar Producto", producto.getCategoria(), producto.getNombre(), detalle);
    
        // Verificar stock bajo solo si es necesario
        if (producto.getCantidad() < StockAlert.LIMITE_STOCK_BAJO) {
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
    public void modificarCantidad(Scanner scanner, InventoryReport reporteInventario) {
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
    
            reporteInventario.registrarCambio("Modificar Cantidad", categoria, nombreProducto, detalle);
            System.out.println("Cantidad actualizada correctamente.");
            alerta.verificarStockBajo(productosPorCategoria);
        } else {
            System.out.println("Producto o categoría no encontrado.");
        }
    }

     // Método para modificar el precio de un producto en el inventario por su nombre y categoría
    public void modificarPrecio(Scanner scanner, InventoryReport reporteInventario) {
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
            reporteInventario.registrarCambio("Modificar Precio", categoria, nombreProducto,
                    "Precio anterior: $" + precioAnterior + ", Nuevo precio: $" + nuevoPrecio);
            alerta.verificarStockBajo(productosPorCategoria);
        } else {
            System.out.println("Producto o categoría no encontrado.");
        }
    }

    // Método para eliminar un producto del inventario por su nombre y categoría
    public void eliminarProducto(Scanner scanner, InventoryReport reporteInventario) { 
        System.out.print("\nIngrese la categoría del producto: ");
        String categoria = scanner.nextLine();
        System.out.print("Ingrese el nombre del producto: ");
        String nombreProducto = scanner.nextLine();
    
        List<Producto> productos = productosPorCategoria.get(categoria);
        if (productos != null && productos.removeIf(p -> p.getNombre().equalsIgnoreCase(nombreProducto))) {
            String detalle = "Retiro total: Producto eliminado del inventario.";
            reporteInventario.registrarCambio("Eliminar Producto", categoria, nombreProducto, detalle);
            System.out.println("Producto eliminado correctamente.");
            alerta.verificarStockBajo(productosPorCategoria);
        } else {
            System.out.println("Producto o categoría no encontrado.");
        }
    }

     // Método para eliminar una categoría y sus productos del inventario
    public void eliminarCategoria(Scanner scanner, InventoryReport reporteInventario) { 
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

    public void cargarInventario(InventoryRepository repositorioInventario) {
        productosPorCategoria = repositorioInventario.cargarInventario();
    }

    public void guardarInventario(InventoryRepository repositorioInventario) {
        repositorioInventario.guardarInventario(productosPorCategoria);
    }

    // Método para obtener los productos por categoría en el inventario
    public Map<String, List<Producto>> getProductosPorCategoria() { 
        return productosPorCategoria;
    }

    public void exportarInventario(File FicheroCSV) {
        this.ficherocsv.exportar(FicheroCSV, productosPorCategoria);
    }

    public void importarInventario(File FicheroCSV) {
        this.ficherocsv.importar(FicheroCSV, productosPorCategoria, this);
    }
}

