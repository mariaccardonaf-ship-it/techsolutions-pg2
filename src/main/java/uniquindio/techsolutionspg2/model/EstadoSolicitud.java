package uniquindio.techsolutionspg2.model;

/**
 * Representa el estado de una solicitud durante su registro y confirmación.
 */
public enum EstadoSolicitud{
    /**
     * Solicitud registrada que todavía no ha sido confirmada.
     */
    REGISTRADA,
    /**
     * Solicitud confirmada después de validar disponibilidad y calcular su total.
     */
    CONFIRMADA
}
