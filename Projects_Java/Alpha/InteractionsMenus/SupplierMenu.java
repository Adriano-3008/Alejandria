package Alpha.InteractionsMenus;

import java.util.Scanner;
import Alpha.Validation;


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
            int opcionProveedores = Validation.leerEnteroPositivo(scanner, "Seleccione una opción válida.");
            switch (opcionProveedores) {
                case 1 -> manejoUsuario.mostrarProveedores();
                case 2 -> manejoUsuario.agregarProveedor(scanner);
                case 3 -> manejoUsuario.eliminarProveedor(scanner);
                case 4 -> regresarAlMenu = true;
                default -> System.out.println("Opción no válida.");
            }
        }
    }
}
