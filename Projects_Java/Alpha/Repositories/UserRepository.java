package Alpha.Repositories;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private final File archivoUsuarios = new File("Projects_Java/Archivo_Proyecto_Alpha/.dats/Archivo_Usuarios.dat"); // Ruta del archivo para almacenar los usuarios
    private Map<String, String> usuarios = new HashMap<>();
    private Map<String, Boolean> jerarquiaUsuarios = new HashMap<>();

    public UserRepository() {
        cargarUsuarios();
    }

    @SuppressWarnings("unchecked")
    public void cargarUsuarios() {
        if (!archivoUsuarios.exists()) {
            try {
                archivoUsuarios.getParentFile().mkdirs();
                archivoUsuarios.createNewFile();
                System.out.println("El archivo de usuarios no existía. Se ha creado un nuevo archivo.");
            } catch (IOException e) {
                System.out.println("Error al crear el archivo de usuarios: " + e.getMessage());
            }
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoUsuarios))) {
            usuarios = (Map<String, String>) ois.readObject();
            jerarquiaUsuarios = (Map<String, Boolean>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar los usuarios: " + e.getMessage());
        }
    }

    public void guardarUsuarios() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoUsuarios))) {
            oos.writeObject(usuarios);
            oos.writeObject(jerarquiaUsuarios);
        } catch (IOException e) {
            System.out.println("Error al guardar los usuarios: " + e.getMessage());
        }
    }

    public Map<String, String> getUsuarios() {
        return usuarios;
    }

    public Map<String, Boolean> getJerarquiaUsuarios() {
        return jerarquiaUsuarios;
    }
}
