package uniquindio.techsolutionspg2.model;

/**
 * Crea servicios de recuperación de información mediante Factory Method.
 */
public class RecuperacionInformacionFactory implements ServicioFactory {

    /**
     * Crea un servicio de recuperación de información.
     *
     * @param codigo identificador del servicio
     * @param nombre nombre del servicio
     * @param descripcion descripción del servicio
     * @return servicio de recuperación de información
     */
    @Override
    public Servicio crearServicio(String codigo, String nombre,
                                  String descripcion) {
        return new RecuperacionInformacion(codigo, nombre, descripcion);
    }
}
