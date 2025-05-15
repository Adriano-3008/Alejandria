package Alpha.Repositories;

import java.io.*;
import java.util.*;

import Alpha.InventoryLogic.Producto;

public class InventoryRepository {
    private final File ficheroInventario;

    public InventoryRepository(File ficheroInventario) {
        this.ficheroInventario = new File("Projects_Java/Archivo_Proyecto_Alpha/.dats/Archivo_Inventario.dat");
    }

    @SuppressWarnings("unchecked")
    public Map<String, List<Producto>> cargarInventario() {
        Map<String, List<Producto>> productosPorCategoria = new HashMap<>();
        try {
            if (!ficheroInventario.exists()) {
                ficheroInventario.getParentFile().mkdirs();
                ficheroInventario.createNewFile();
                System.out.println("El archivo de inventario no existía. Se ha creado un nuevo archivo en: " + ficheroInventario.getAbsolutePath());
                return productosPorCategoria;
            }

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroInventario))) {
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
            if (!ficheroInventario.exists()) {
                ficheroInventario.getParentFile().mkdirs();
                ficheroInventario.createNewFile();
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ficheroInventario))) {
                oos.writeObject(productosPorCategoria);
            }
        } catch (IOException e) {
            System.out.println("Error al guardar el inventario: " + e.getMessage());
        }
    }
}