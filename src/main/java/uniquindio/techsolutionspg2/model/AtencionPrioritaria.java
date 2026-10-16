package uniquindio.techsolutionspg2.model;

public class AtencionPrioritaria extends ServicioDecorator {

    private static final double INCREMENTO = 35000;

    public AtencionPrioritaria(ServicioContratable componente) {
        super(componente, INCREMENTO);
    }

    /**
     * Incluye la atención prioritaria en la descripción.
     *
     * @return descripción del servicio personalizado
     */
    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + atención prioritaria";
    }
}