package Alpha.Repositories;
//-----------------------------//
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import Alpha.InventoryLogic.Inventory;
import Alpha.InventoryLogic.Producto;

public class FileCSV {
    
    public void exportar(File ficherocsv, Map<String, List<Producto>> productosPorCategoria) {
        try (BufferedWriter writer = Files.newBufferedWriter(ficherocsv.toPath())) {
            writer.write("Categoría,Producto,Cantidad,Precio\n"); // Encabezado del archivo CSV
            for (var entry : productosPorCategoria.entrySet()) {
                String categoria = entry.getKey();
                for (Producto producto : entry.getValue()) {
                    writer.write(String.format("%s,%s,%d,%.2f\n",
                            categoria, producto.getNombre(), producto.getCantidad(), producto.getPrecio()));
                }
            }
            System.out.println("Inventario exportado correctamente a " + ficherocsv.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error al exportar el inventario a CSV: " + e.getMessage());
        }
    }

    public void importar(File ficherocsv, Map<String, List<Producto>> productosPorCategoria, Inventory inventario) {
        if (!ficherocsv.exists()) {
            System.out.println("El archivo CSV especificado no existe en: " + ficherocsv.getAbsolutePath());
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(ficherocsv.toPath())) {
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
                        Producto productoExistente = inventario.buscarProducto(categoria, nombre);
                        if (productoExistente != null) {
                            // Actualizar cantidad y precio del producto existente
                            productoExistente.setCantidad(productoExistente.getCantidad() + cantidad);
                            productoExistente.setPrecio(precio);
                            System.out.printf("Producto actualizado: Categoría='%s', Nombre='%s', Nueva Cantidad=%d, Nuevo Precio=%.2f\n",
                                    categoria, nombre, productoExistente.getCantidad(), precio);
                        } else {
                            // Agregar nuevo producto
                            Producto nuevoProducto = new Producto(nombre, categoria, cantidad, precio);
                            inventario.agregarProducto(nuevoProducto);
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
            System.out.println("Inventario importado correctamente desde " + ficherocsv.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

}
