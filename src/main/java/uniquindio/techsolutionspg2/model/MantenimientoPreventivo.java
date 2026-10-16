package uniquindio.techsolutionspg2.model;

/**
 * Representa el mantenimiento preventivo con un costo base de $80.000.
 */
public class MantenimientoPreventivo extends Servicio {

    private static final double COSTO_BASE = 80000;

    public MantenimientoPreventivo(String codigo, String nombre,
                           String descripcion) {
        super(codigo, nombre, descripcion, COSTO_BASE);
    }

    /**
     * Obtiene la especialidad técnica de este servicio.
     *
     * @return especialidad de mantenimiento preventivo
     */
    @Override
    public Especialidad getEspecialidad() {
        return Especialidad.PREVENTIVO;
    }
}
