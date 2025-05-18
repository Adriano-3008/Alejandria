package Alpha.InteractionsMenus;

import java.util.Scanner;

import Alpha.SupplierLogic.SupplierManagement;

public class SupplierMenu {
    public static void mostrarMenuProovedores(Scanner scanner, SupplierManagement manejoUsuario) { // Método para mostrar el menú de opciones de gestión de proveedores
        boolean regresarAlMenu = false;
        while (!regresarAlMenu) {
            System.out.println("\n=== GESTIÓN DE PROVEEDORES ===");
            System.out.println("1. Mostrar Proveedores");
            System.out.println("2. Agregar Proveedor");
            System.out.println("3. Eliminar Proveedor");
            System.out.println("4. Regresar al menú principal");
            System.out.print("Seleccione una opción: ");
            int opcionProveedores = Integer.parseInt(scanner.nextLine());
            switch (opcionProveedores) {
                case 1 -> {
                    manejoUsuario.cargarProveedores(); // Cargar proveedores antes de mostrarlos
                    manejoUsuario.mostrarProveedores();
                }
                case 2 -> manejoUsuario.agregarProveedor(scanner);
                case 3 -> manejoUsuario.eliminarProveedor(scanner);
                case 4 -> {
                    regresarAlMenu = true;
                    System.out.println("Regresando al menú principal...");
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }
}
