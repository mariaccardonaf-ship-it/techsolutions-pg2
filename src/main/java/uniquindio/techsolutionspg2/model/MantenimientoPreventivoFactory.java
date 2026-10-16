package uniquindio.techsolutionspg2.model;

/**
 * Crea servicios de mantenimiento preventivo mediante Factory Method.
 */
public class MantenimientoPreventivoFactory implements ServicioFactory {

    /**
     * Crea un servicio de mantenimiento preventivo.
     *
     * @param codigo identificador del servicio
     * @param nombre nombre del servicio
     * @param descripcion descripción del servicio
     * @return servicio de mantenimiento preventivo
     */
    @Override
    public Servicio crearServicio(String codigo, String nombre,
                                  String descripcion) {
        return new MantenimientoPreventivo(codigo, nombre, descripcion);
    }
}
