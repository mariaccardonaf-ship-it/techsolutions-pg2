package uniquindio.techsolutionspg2.model;
import java.util.List;

/**
 * Define el contrato común de los servicios, servicios personalizados y paquetes
 * tecnológicos que pueden incluirse en una solicitud.
 */
public interface ComponenteTecnologico {
    /**
     * Calcula el costo del componente, incluyendo sus complementos o componentes
     * internos cuando corresponda. No incluye el desplazamiento del proveedor.
     *
     * @return costo del componente en pesos
     */
    public double getCosto();
    /**
     * Obtiene la descripción del componente contratado.
     *
     * @return descripción del servicio, personalización o paquete
     */
    public String getDescripcion();
    /**
     * Obtiene las especialidades que requieren atención presencial.
     * Las implementaciones deben evitar especialidades repetidas en el resultado.
     *
     * @return lista de especialidades presenciales; vacía si no se requieren
     */
    public List<Especialidad> getEspecialidadesPresenciales();
}
