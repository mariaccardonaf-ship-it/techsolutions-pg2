package uniquindio.techsolutionspg2.model;

/**
 * Crea servicios de mantenimiento correctivo mediante Factory Method.
 */
public class MantenimientoCorrectivoFactory implements ServicioFactory {

    /**
     * Crea un servicio de mantenimiento correctivo.
     *
     * @param codigo identificador del servicio
     * @param nombre nombre del servicio
     * @param descripcion descripción del servicio
     * @return servicio de mantenimiento correctivo
     */
    @Override
    public Servicio crearServicio(String codigo, String nombre,
                                  String descripcion) {
        return new MantenimientoCorrectivo(codigo, nombre, descripcion);
    }
}
