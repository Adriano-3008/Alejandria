package Alpha.SupplierLogic;
import java.io.Serializable;

public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombre;
    private String contacto;
    private String productosSuministrados;

    public Supplier(String nombre, String contacto, String productosSuministrados) { // Constructor de la clase Proveedor
        this.nombre = nombre;
        this.contacto = contacto;
        this.productosSuministrados = productosSuministrados;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }

    public String getProductosSuministrados() { return productosSuministrados; }
    public void setProductosSuministrados(String productosSuministrados) { this.productosSuministrados = productosSuministrados; }

    @Override
    public String toString() {
        return String.format("Proveedor: %s | Contacto: %s | Productos: %s", nombre, contacto, productosSuministrados);
    }
}
