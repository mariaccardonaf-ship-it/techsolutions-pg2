package uniquindio.techsolutionspg2.model;

/**
 * Representa la instalación de redes con un costo base de $180.000.
 */
public class InstalacionRedes extends Servicio {

    private static final double COSTO_BASE = 180000;

    public InstalacionRedes(String codigo, String nombre,
                           String descripcion) {
        super(codigo, nombre, descripcion, COSTO_BASE);
    }

    /**
     * Obtiene la especialidad técnica de este servicio.
     *
     * @return especialidad de redes
     */
    @Override
    public Especialidad getEspecialidad() {
        return Especialidad.REDES;
    }
}
