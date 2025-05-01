package Alpha;

import java.io.*; // Import necesario para trabajar con archivos y flujos de entrada/salida de datos (I/O)

import java.util.*; // Import necesario para trabajar con colecciones y Scanner

public class UserManagement { // Clase para manejar el registro y autenticación de usuarios en el sistema

    private final UserRepository userRepository; // Repositorio para manejar la persistencia de usuarios
    private Map<String, String> usuarios = new HashMap<>(); // Mapa para almacenar los usuarios y sus contraseñas
    private Map<String, Boolean> jerarquiaUsuarios = new HashMap<>();  // Mapa para almacenar los usuarios y sus roles (administrador o normal)

    public UserManagement() { // Constructor de la clase UserManagement
        this.userRepository = new UserRepository(); 
        cargarUsuarios(); // Sincronizar los mapas al inicializar
    }

    private void cargarUsuarios() { // Método para cargar los usuarios desde el repositorio
        this.usuarios = userRepository.getUsuarios(); // Obtener los usuarios del repositorio
        this.jerarquiaUsuarios = userRepository.getJerarquiaUsuarios(); // Obtener la jerarquía de usuarios del repositorio
    }
    
    public String iniciarSesion(Scanner scanner) { // Método para iniciar sesión en el sistema
        while (true) {
            userRepository.cargarUsuarios(); // Cargar los usuarios desde el archivo al iniciar sesión
            System.out.println("\n=== LOGIN ===");
            System.out.print("Ingrese su nombre de usuario: ");
            String usuario = scanner.nextLine();
            System.out.print("Ingrese su contraseña: ");
            String contrasena = scanner.nextLine();

            if (usuarios.containsKey(usuario) && usuarios.get(usuario).equals(contrasena)) { 
                if (jerarquiaUsuarios.getOrDefault(usuario, false)) {
                    System.out.println("Has iniciado sesión como administrador.");
                } else {
                    System.out.println("¡Bienvenido, " + usuario + "!");
                }
                return usuario; 
            } else {
                System.out.println("Usuario o contraseña incorrectos. Intente nuevamente.");
            }
        }
    }

    
    public void registrarUsuario(Scanner scanner) { // Método para registrar un nuevo usuario en el sistema
        System.out.println("\n=== REGISTRO DE NUEVO USUARIO ===");
        System.out.print("Ingrese un nombre de usuario: ");
        String usuario = scanner.nextLine();

        if (usuarios.containsKey(usuario)) {
            System.out.println("El usuario ya existe. Intente con otro nombre.");
            return;
        }

        System.out.print("Ingrese una contraseña: ");
        String contrasena = scanner.nextLine();

        System.out.print("¿Desea establecer esta cuenta como administrador? (s/n): ");
        String respuesta = scanner.nextLine();

        boolean esAdmin = false;
        if (respuesta.equalsIgnoreCase("s")) {
            System.out.print("Ingrese la contraseña de administrador: ");
            String credencialesAdmin = scanner.nextLine();
            if ("admin2025".equals(credencialesAdmin)) {
                esAdmin = true;
                System.out.println("Cuenta registrada como administrador.");
            } else {
                System.out.println("Contraseña de administrador incorrecta. La cuenta será registrada como usuario normal.");
            }
        }

        usuarios.put(usuario, contrasena);
        jerarquiaUsuarios.put(usuario, esAdmin);
        userRepository.guardarUsuarios(); // Guardar los cambios en el archivo
        System.out.println("Usuario registrado exitosamente.");
    }


    public void borrarUsuario(Scanner scanner) { // Método para borrar un usuario existente del sistema
        System.out.print("Ingrese el nombre del usuario que desea borrar: ");
        String usuarioABorrar = scanner.nextLine();

        if (usuarios.containsKey(usuarioABorrar)) {
            usuarios.remove(usuarioABorrar);
            jerarquiaUsuarios.remove(usuarioABorrar);
            userRepository.guardarUsuarios(); // Guardar los cambios en el archivo
            System.out.println("Usuario '" + usuarioABorrar + "' borrado correctamente.");
        } else {
            System.out.println("El usuario especificado no existe.");
        }
    }

    
    public String manejarLogin(Scanner scanner) { // Método para manejar el proceso de inicio de sesión y registro de usuarios
        while (true) {
            System.out.println("\n=== SISTEMA DE LOGIN ===");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Registrar nuevo usuario");
            System.out.println("3. Ver usuarios registrados"); // Nueva ubicación de esta opción
            System.out.println("4. Salir del programa");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    return iniciarSesion(scanner); 
                case "2":
                    registrarUsuario(scanner);
                    break;
                case "3":
                    manejarUsuariosRegistrados(scanner); // Nueva opción para manejar usuarios registrados
                    break;
                case "4":
                    System.out.println("Saliendo del programa... ¡Hasta luego!");
                    userRepository.guardarUsuarios();
                    System.exit(0); 
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

    public void manejarUsuariosRegistrados(Scanner scanner) { // Método para manejar las opciones relacionadas con usuarios registrados
        System.out.println("\n=== USUARIOS REGISTRADOS ===");
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }

        for (var entry : usuarios.entrySet()) {
            String tipo = jerarquiaUsuarios.getOrDefault(entry.getKey(), false) ? "Administrador" : "Normal";
            System.out.printf("Usuario: %s | Tipo: %s\n", entry.getKey(), tipo);
        }

        System.out.println("\n1. Borrar usuario");
        System.out.println("2. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
        String opcion = scanner.nextLine();

        switch (opcion) {
            case "1":
                borrarUsuario(scanner); // Llamar al método para borrar usuario
                break;
            case "2":
                System.out.println("Volviendo al menú principal...");
                break;
            default:
                System.out.println("Opción no válida. Intente nuevamente.");
        }
    }

    public void verUsuariosRegistrados() { // Método para mostrar los usuarios registrados en el sistema
        System.out.println("\n=== USUARIOS REGISTRADOS ===");
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }

        for (var entry : usuarios.entrySet()) {
            String tipo = jerarquiaUsuarios.getOrDefault(entry.getKey(), false) ? "Administrador" : "Normal";
            System.out.printf("Usuario: %s | Contraseña: %s | Tipo: %s\n", entry.getKey(), entry.getValue(), tipo);
        }
    }

    public boolean esUsuarioAdmin(String usuario) { // Método para verificar si un usuario es administrador
        return jerarquiaUsuarios.getOrDefault(usuario, false);
    }
}

/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
 /*--------------------------------------------------------------------------------------------------------------------------------------------------------*/
 /*--------------------------------------------------------------------------------------------------------------------------------------------------------*/

class UserRepository {
    private final File archivoUsuarios = new File("Alpha/Archivo_Proyecto/usuarios.dat");
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