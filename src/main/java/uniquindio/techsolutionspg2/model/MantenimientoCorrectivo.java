package uniquindio.techsolutionspg2.model;

/**
 * Representa el mantenimiento correctivo con un costo base de $120.000.
 */
public class MantenimientoCorrectivo extends Servicio {

    private static final double COSTO_BASE = 120000;

    public MantenimientoCorrectivo(String codigo, String nombre,
                           String descripcion) {
        super(codigo, nombre, descripcion, COSTO_BASE);
    }

    /**
     * Obtiene la especialidad técnica de este servicio.
     *
     * @return especialidad de mantenimiento correctivo
     */
    @Override
    public Especialidad getEspecialidad() {
        return Especialidad.CORRECTIVO;
    }
}
