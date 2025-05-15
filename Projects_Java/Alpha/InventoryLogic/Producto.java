package Alpha.InventoryLogic;
import java.io.Serializable; 

public class Producto implements Serializable { 
    private static final long serialVersionUID = 1L;
    private String nombre; 
    private String categoria; 
    private int cantidad; 
    private double precio; 

    // Constructor de la clase Productos
    public Producto(String nombre, String categoria, int cantidad, double precio) {
        this.nombre = nombre; 
        this.categoria = categoria; 
        this.cantidad = cantidad; 
        this.precio = precio; 
    }
    // Métodos getter y setter para los atributos de la clase Productos
    public String getNombre() { return nombre; } 
    public String getCategoria() { return categoria; } 
    public int getCantidad() { return cantidad; } 
    public void setCantidad(int cantidad) { this.cantidad = cantidad; } 
    public double getPrecio() { return precio; } 
    public void setPrecio(double precio) { this.precio = precio; } 
 }
