package uniquindio.techsolutionspg2.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un servicio tecnológico individual de TechSolutions.
 * Reúne los datos comunes de los servicios y conserva sus atributos inmutables
 * para evitar que una personalización altere el servicio original del catálogo.
 */
public abstract class Servicio implements ServicioContratable {

    private final String codigo;
    private final String nombre;
    private final String descripcion;
    private final double costoBase;

    // Constructor
    protected Servicio(String codigo, String nombre,
                       String descripcion, double costoBase) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costoBase = costoBase;
    }

    // Getters
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la descripción original del servicio del catálogo.
     *
     * @return descripción del servicio
     */
    @Override
    public String getDescripcion() {
        return descripcion;
    }

    public double getCostoBase() {
        return costoBase;
    }

    // Métodos de negocio
    /**
     * Obtiene el costo del servicio individual sin personalizaciones.
     *
     * @return costo base del servicio en pesos
     */
    @Override
    public double getCosto() {
        return costoBase;
    }

    public abstract Especialidad getEspecialidad();

    /**
     * Obtiene los requisitos presenciales del servicio base.
     * En esta implementación son vacíos; el complemento de atención a domicilio
     * incorporará la especialidad correspondiente al personalizar el servicio.
     *
     * @return una nueva lista vacía de especialidades presenciales
     */
    @Override
    public List<Especialidad> getEspecialidadesPresenciales() {
        return new ArrayList<>();
    }
}
