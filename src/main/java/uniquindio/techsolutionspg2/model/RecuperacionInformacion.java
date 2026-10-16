package uniquindio.techsolutionspg2.model;

/**
 * Representa la recuperación de información con un costo base de $150.000.
 */
public class RecuperacionInformacion extends Servicio {

    private static final double COSTO_BASE = 150000;

    public RecuperacionInformacion(String codigo, String nombre,
                           String descripcion) {
        super(codigo, nombre, descripcion, COSTO_BASE);
    }

    /**
     * Obtiene la especialidad técnica de este servicio.
     *
     * @return especialidad de recuperación de información
     */
    @Override
    public Especialidad getEspecialidad() {
        return Especialidad.RECUPERACION;
    }
}
