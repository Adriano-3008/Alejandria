package Java;
class Empleado {
    private String nombre;
    private long ID;
    private int diasTrabajados;
    private double salarioBase;
    
    public Empleado(String nombre, long ID, int diasTrabajados, double salarioBase){
        this.nombre = nombre;
        this.ID = ID;
        this.diasTrabajados = diasTrabajados;
        this.salarioBase = salarioBase;
    }

    public Empleado(){
        this.nombre = "";
        this.ID = 0;
        this.diasTrabajados = 0;
        this.salarioBase = 0.0;
    }

    public String getNombre(){
        return nombre;
    }
    public long getID(){
        return ID;
    }
    public int getDiasTrabajados(){
        return diasTrabajados;
    }
    public double getSalarioBase(){
        return salarioBase;
    }

    public String obtenerDetalles() {
        return "Empleado: " + nombre + ", ID: " + ID + ", Días trabajados: " + diasTrabajados + ", Salario base: $" + salarioBase;
    }

    // Método base para calcular el salario
    public double calcularSalario() {
        return salarioBase * diasTrabajados;
    }
}

class EmpleadoProduccion extends Empleado {
    private String turno;
    
    public EmpleadoProduccion(String nombre, long ID, int diasTrabajados, double salarioBase, String turno){
        super(nombre, ID, diasTrabajados, salarioBase);
        this.turno = turno;
    }
    
    /**
     * Obtiene el turno del empleado de producción.
     * return El turno del empleado de producción.
     */
    public String getTurno(){
        return turno;
    }

    @Override
    public String obtenerDetalles() {
        return super.obtenerDetalles() + ", Turno: " + turno;
    }

    // Sobrescribir el método calcularSalario
    @Override
    public double calcularSalario() {
        // Por ejemplo, los empleados de producción tienen un bono del 10%
        return super.calcularSalario() * 1.10;
    }
}

class EmpleadoDistribucion extends Empleado {
    private String zona;

    public EmpleadoDistribucion(String nombre, long ID, int diasTrabajados, double salarioBase, String zona){
        super(nombre, ID, diasTrabajados, salarioBase);
        this.zona = zona;
    }
    
    public String getZona(){
        return zona;
    }

    @Override
    public String obtenerDetalles() {
        return super.obtenerDetalles() + ", Zona: " + zona;
    }

    // Sobrescribir el método calcularSalario
    @Override
    public double calcularSalario() {
        // Por ejemplo, los empleados de distribución tienen un bono fijo de $500
        return super.calcularSalario() + 500;
    }
}

public class Empresa {
    public static void main(String[] args) {
        // Polimorfismo dinámico: Usamos referencias de tipo Empleado
        Empleado empleado1 = new EmpleadoProduccion("Juan Pérez", 1001, 25, 1500.0, "Nocturno");
        Empleado empleado2 = new EmpleadoDistribucion("María López", 1002, 20, 1400.0, "Norte");

        // Llamada al método sobrescrito en tiempo de ejecución
        System.out.println("Detalles del empleado 1:");
        System.out.println(empleado1.obtenerDetalles());
        System.out.println("Salario calculado: $" + empleado1.calcularSalario());

        System.out.println("\nDetalles del empleado 2:");
        System.out.println(empleado2.obtenerDetalles());
        System.out.println("Salario calculado: $" + empleado2.calcularSalario());
    }
}