package uniquindio.techsolutionspg2.model;

/**
 * Representa el resultado de consultar la disponibilidad de un técnico.
 * Contiene su identificador, especialidad y costo de desplazamiento.
 * Puede representar una consulta sin técnicos disponibles.
 */
public class DisponibilidadTecnica {

    private final String idTecnico;
    private final Especialidad especialidad;
    private final boolean disponible;
    private final double costoDesplazamiento;

    public DisponibilidadTecnica(String idTecnico,
                                 Especialidad especialidad,
                                 boolean disponible,
                                 double costoDesplazamiento) {
        this.idTecnico = idTecnico;
        this.especialidad = especialidad;
        this.disponible = disponible;
        this.costoDesplazamiento = costoDesplazamiento;
    }

    public String getIdTecnico() {
        return idTecnico;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    /**
     * Indica si existe un técnico disponible para la especialidad consultada.
     *
     * @return true si hay un técnico disponible; false en caso contrario
     */
    public boolean isDisponible() {
        return disponible;
    }

    public double getCostoDesplazamiento() {
        return costoDesplazamiento;
    }
}
