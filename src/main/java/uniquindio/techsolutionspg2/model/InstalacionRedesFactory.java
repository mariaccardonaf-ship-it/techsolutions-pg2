package uniquindio.techsolutionspg2.model;

/**
 * Crea servicios de instalación de redes mediante Factory Method.
 */
public class InstalacionRedesFactory implements ServicioFactory {

    /**
     * Crea un servicio de instalación de redes.
     *
     * @param codigo identificador del servicio
     * @param nombre nombre del servicio
     * @param descripcion descripción del servicio
     * @return servicio de instalación de redes
     */
    @Override
    public Servicio crearServicio(String codigo, String nombre,
                                  String descripcion) {
        return new InstalacionRedes(codigo, nombre, descripcion);
    }
}
