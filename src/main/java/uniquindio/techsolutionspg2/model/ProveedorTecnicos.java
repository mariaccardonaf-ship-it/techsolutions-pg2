package uniquindio.techsolutionspg2.model;

/**
 * Define la consulta de disponibilidad técnica utilizada por el modelo,
 * sin exponer el formato ni los códigos particulares de un proveedor externo.
 */
public interface ProveedorTecnicos {

    /**
     * Consulta la disponibilidad de atención para una especialidad.
     *
     * @param especialidad especialidad técnica requerida
     * @return resultado de disponibilidad con los datos del técnico y el
     *         costo de desplazamiento cuando correspondan
     */
    public DisponibilidadTecnica consultarTecnico(Especialidad especialidad);
}
