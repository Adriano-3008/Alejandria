package Alpha;
// Import necesario para trabajar con archivos y flujos de entrada/salida de datos (I/O)
import java.io.*; 
// Import necesario para trabajar con colecciones y Scanner
import java.util.*; 

public class ReporteDeInventario { // Clase para generar reportes detallados del inventario y mostrar el historial de cambios
   
    private final File archivoHistorial = new File("Alpha/Archivo_Proyecto/historial_cambios.dat"); // Ruta del archivo para almacenar el historial de cambios
    private List<String> registroDeCambios = new ArrayList<>(); // Lista para almacenar los registros de cambios en el inventario

    public void registrarCambio(String accion, String categoria, String producto, String detalle) { // Método para registrar un cambio en el inventario
        String timestamp = new Date().toString();
        String tipoMovimiento;
    
        // Determinar si la acción es un ingreso o un retiro
        if (accion.equalsIgnoreCase("Agregar Producto") || accion.equalsIgnoreCase("Modificar Cantidad") && detalle.contains("Nueva cantidad mayor")) {
            tipoMovimiento = "Ingreso";
        } else if (accion.equalsIgnoreCase("Eliminar Producto") || accion.equalsIgnoreCase("Modificar Cantidad") && detalle.contains("Nueva cantidad menor")) {
            tipoMovimiento = "Retiro";
        } else {
            tipoMovimiento = "Otro"; // Para acciones que no sean ingresos o retiros
        }
    
        // Formatear el registro
        String registro = String.format("[%s] Movimiento: %s | Acción: %s | Categoría: %s | Producto: %s | Detalle: %s",
                timestamp, tipoMovimiento, accion, categoria, producto, detalle);
        registroDeCambios.add(registro);
        guardarHistorial(); 
    }

    
    public void mostrarRegistroDeCambios(Scanner scanner) { // Método para mostrar el historial de cambios en el inventario
        cargarHistorial(); 
        if (registroDeCambios.isEmpty()) {
            System.out.println("No hay cambios registrados en el inventario.");
            return;
        }

        System.out.println("\n=== Registro de Cambios en el Inventario ===");
        for (String registro : registroDeCambios) {
            System.out.println(registro);
        }

        System.out.print("\n¿Desea borrar el historial de cambios? (s/n): ");
        String respuesta = scanner.nextLine();
        if (respuesta.equalsIgnoreCase("s")) {
            borrarHistorial();
        }
    }

    
    public void borrarHistorial() { // Método para borrar el historial de cambios
        registroDeCambios.clear();
        if (archivoHistorial.exists()) {
            archivoHistorial.delete(); 
        }
        System.out.println("El historial de cambios ha sido borrado.");
    }

    
    public void guardarHistorial() { // Método para guardar el historial de cambios en un archivo
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoHistorial))) {
            oos.writeObject(registroDeCambios);
        } catch (IOException e) {
            System.out.println("Error al guardar el historial de cambios: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked") //Para evitar advertencias de compilación de tipos en la conversión de objetos a List<String>
    public void cargarHistorial() { // Método para cargar el historial de cambios desde un archivo
        if (!archivoHistorial.exists()) {
            try {
                archivoHistorial.getParentFile().mkdirs(); 
                archivoHistorial.createNewFile(); 
                System.out.println("El archivo de historial de cambios no existía. Se ha creado un nuevo archivo en: " + archivoHistorial.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Error al crear el archivo de historial de cambios: " + e.getMessage());
            }
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoHistorial))) {
            registroDeCambios = (List<String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar el historial de cambios: " + e.getMessage());
        }
    }

    public void generarReporte(ControlStock controlStock) { // Método para generar un reporte detallado del inventario
        Map<String, List<Producto>> productosPorCategoria = controlStock.getProductosPorCategoria(); // Obtener los productos por categoría del inventario actual
    
        if (productosPorCategoria.isEmpty()) {
            System.out.println("El inventario está vacío. No hay datos para generar el reporte.");
            return;
        }
    
        double valorTotalInventario = 0.0;
    
        System.out.println("\n=== Reporte Detallado del Inventario ===");
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
            double valorTotalCategoria = 0.0;
            for (Producto producto : productos) {
                String precioFormateado = String.format("$%,.2f", producto.getPrecio());
                String valorTotalFormateado = String.format("$%,.2f", producto.getCantidad() * producto.getPrecio());
                System.out.printf(formatoFila, producto.getNombre(), producto.getCantidad(), precioFormateado, valorTotalFormateado);
    
                valorTotalCategoria += producto.getCantidad() * producto.getPrecio();
            }
    
            System.out.println(separador);
            System.out.printf("Valor total de la categoría '%s': $%,.2f\n", categoria, valorTotalCategoria);
            valorTotalInventario += valorTotalCategoria;
        }
    
        System.out.printf("\n=== Valor total del inventario: $%,.2f ===\n", valorTotalInventario);
    }
}