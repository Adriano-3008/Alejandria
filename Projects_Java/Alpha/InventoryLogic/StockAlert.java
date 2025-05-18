package Alpha.InventoryLogic;

import java.util.List;
import java.util.Map;

public class StockAlert {
     public static final int LIMITE_STOCK_BAJO = 20; // Límite de stock bajo para mostrar la alerta

    // Método para verificar si hay productos con stock bajo
    public void verificarStockBajo(Map<String, List<Producto>> productosPorCategoria) { 
        boolean hayStockBajo = false;
        StringBuilder alerta = new StringBuilder("\n=== ALERTA: Productos con stock bajo ===\n");

        for (var entry : productosPorCategoria.entrySet()) {
            String categoria = entry.getKey();
            for (Producto producto : entry.getValue()) {
                if (producto.getCantidad() < LIMITE_STOCK_BAJO) {
                    hayStockBajo = true;
                    alerta.append(String.format("Categoría: %s | Producto: %s | Stock: %d | Precio: $%.2f\n",
                            categoria, producto.getNombre(), producto.getCantidad(), producto.getPrecio()));
                }
            }
        }

        if (hayStockBajo) {
            System.out.println(alerta);
        }
    }
}
