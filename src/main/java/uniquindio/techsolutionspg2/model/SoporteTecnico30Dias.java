package uniquindio.techsolutionspg2.model;

/**
 * Añade soporte técnico durante 30 días a un servicio por $50,000.
 */
public class SoporteTecnico30Dias extends ServicioDecorator {
    private static final double INCREMENTO = 50000;

    public SoporteTecnico30Dias(ServicioContratable componente) {
        super(componente, INCREMENTO);
    }

    /**
     * Incluye el complemento en la descripción del servicio.
     * @return descripción del servicio con soporte técnico durante 30 días
     */
    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + soporte técnico durante 30 días";
    }
}
