package uniquindio.techsolutionspg2.model;

/**
 * Define la creación de servicios mediante Factory Method, permitiendo que cada
 * fábrica concreta determine el tipo de servicio que construye.
 */
public interface ServicioFactory {

    /**
     * Crea un servicio del tipo correspondiente a la fábrica concreta.
     * El costo base lo establece el tipo de servicio.
     *
     * @param codigo identificador del servicio
     * @param nombre nombre del servicio
     * @param descripcion descripción del servicio
     * @return servicio creado por la fábrica
     */
    public Servicio crearServicio(String codigo, String nombre, String descripcion);
}
