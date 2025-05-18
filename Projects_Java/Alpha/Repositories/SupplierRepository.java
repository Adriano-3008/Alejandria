package Alpha.Repositories;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import Alpha.SupplierLogic.Supplier;

public class SupplierRepository {
    private final File ficheroProveedores = new File("Projects_Java/Archivo_Proyecto/.dats/Archivo_Proveedores.dat");
    private List<Supplier> listSupplier = new ArrayList<>();

    public SupplierRepository() {
        // Eliminar la carga automática de proveedores
        // cargarProveedores();
    }

    @SuppressWarnings("unchecked")
    public void cargarProveedores() {
        if (!ficheroProveedores.exists()) {
            try {
                ficheroProveedores.getParentFile().mkdirs();
                ficheroProveedores.createNewFile();
            } catch (IOException e) {
                System.err.println("Error al crear el archivo de proveedores: " + e.getMessage());
            }
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroProveedores))) {
            listSupplier = (List<Supplier>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar los proveedores: " + e.getMessage());
        }
    }

    public void guardarProveedores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ficheroProveedores))) {
            oos.writeObject(listSupplier);
        } catch (IOException e) {
            System.out.println("Error al guardar los proveedores: " + e.getMessage());
        }
    }

    public List<Supplier> getListSupplier() {
        return listSupplier;
    }
}
