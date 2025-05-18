package Alpha.UserLogic;

import java.util.*; 
import Alpha.Repositories.UserRepository;
import Alpha.Validation;

public class UserManagement { // Clase para manejar el registro y autenticación de usuarios en el sistema

    private final UserRepository repositorioUsuario; 
    private Map<String, String> usuarios = new HashMap<>(); 
    private Map<String, Boolean> jerarquiaUsuarios = new HashMap<>();  
    private final UserAuthentication userAuthentication; 

    public UserManagement() { // Constructor de la clase UserManagement
        this.repositorioUsuario = new UserRepository(); 
        this.userAuthentication = new UserAuthentication(this); 
        cargarUsuarios(); // Sincronizar los mapas al inicializar
    }

    private void cargarUsuarios() { // Método para cargar los usuarios desde el repositorio
        this.usuarios = repositorioUsuario.getUsuarios(); 
        this.jerarquiaUsuarios = repositorioUsuario.getJerarquiaUsuarios(); 
    }
    
    public Map<String, String> getUsuarios() { // Método para obtener el mapa de usuarios
        return usuarios;
    }
    
    public void registrarUsuario(Scanner scanner) { // Método para registrar un nuevo usuario en el sistema
        System.out.println("\n=== REGISTRO DE NUEVO USUARIO ===");
        String usuario = Validation.leerTextoNoVacio(scanner, "El nombre de usuario no puede estar vacío.");
        if (usuarios.containsKey(usuario)) {
            System.out.println("El usuario ya existe. Intente con otro nombre.");
            return;
        }

        String contrasena = Validation.leerTextoNoVacio(scanner, "La contraseña no puede estar vacía.");
        System.out.print("¿Desea establecer esta cuenta como administrador? (s/n): ");
        String respuesta = scanner.nextLine();

        boolean esAdmin = false;
        if (respuesta.equalsIgnoreCase("s")) {
            String credencialesAdmin = Validation.leerTextoNoVacio(scanner, "La contraseña de administrador no puede estar vacía.");
            if ("admin2025".equals(credencialesAdmin)) {
                esAdmin = true;
                System.out.println("Cuenta registrada como administrador.");
            } else {
                System.out.println("Contraseña de administrador incorrecta. La cuenta será registrada como usuario normal.");
            }
        }

        usuarios.put(usuario, contrasena);
        jerarquiaUsuarios.put(usuario, esAdmin);
        repositorioUsuario.guardarUsuarios();
        System.out.println("Usuario registrado exitosamente.");
    }


    public void borrarUsuario(Scanner scanner) { // Método para borrar un usuario existente del sistema
        System.out.print("Ingrese el nombre del usuario que desea borrar: ");
        String usuarioABorrar = scanner.nextLine();

        if (usuarios.containsKey(usuarioABorrar)) {
            usuarios.remove(usuarioABorrar);
            jerarquiaUsuarios.remove(usuarioABorrar);
            repositorioUsuario.guardarUsuarios(); // Guardar los cambios en el archivo
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
                    return userAuthentication.iniciarSesion(scanner); 
                case "2":
                    registrarUsuario(scanner);
                    break;
                case "3":
                    manejarUsuariosRegistrados(scanner); 
                    break;
                case "4":
                    System.out.println("Saliendo del programa... ¡Hasta luego!");
                    repositorioUsuario.guardarUsuarios();
                    System.exit(0); 
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

    public void manejarUsuariosRegistrados(Scanner scanner) { // Método para manejar la visualización y eliminación de usuarios registrados
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
