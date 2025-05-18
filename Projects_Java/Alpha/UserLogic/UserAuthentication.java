package Alpha.UserLogic;

import java.util.Scanner;

public class UserAuthentication {
    private final UserManagement manejoUsuario;

    public UserAuthentication(UserManagement manejoUsuario) {
        this.manejoUsuario = manejoUsuario;
    }

    public String iniciarSesion(Scanner scanner) { // Método para iniciar sesión
        while (true) {
            System.out.println("\n=== LOGIN ===");
            System.out.print("Ingrese su nombre de usuario: ");
            String usuario = scanner.nextLine();
            System.out.print("Ingrese su contraseña: ");
            String contrasena = scanner.nextLine();

            if (manejoUsuario.getUsuarios().containsKey(usuario) &&
                manejoUsuario.getUsuarios().get(usuario).equals(contrasena)) {
                if (manejoUsuario.esUsuarioAdmin(usuario)) {
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
}
