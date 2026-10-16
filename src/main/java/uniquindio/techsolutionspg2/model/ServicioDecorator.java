package uniquindio.techsolutionspg2.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Añade complementos a un servicio sin modificar el objeto del catálogo.
 * Solo envuelve ServicioContratable; no admite paquetes directamente.
 */
public abstract class ServicioDecorator
        implements ServicioContratable {

    private final ServicioContratable componente;
    private final double incremento;

    protected ServicioDecorator(ServicioContratable componente,
                                double incremento) {

        if (componente == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un servicio."
            );
        }

        this.componente = componente;
        this.incremento = incremento;
    }

    public ServicioContratable getComponente() {
        return componente;
    }

    public double getIncremento() {
        return incremento;
    }

    /**
     * Suma el complemento al costo del servicio envuelto.
     *
     * @return costo personalizado sin desplazamiento externo
     */
    @Override
    public double getCosto() {
        return componente.getCosto() + incremento;
    }

    /**
     * Conserva la descripción del elemento envuelto.
     *
     * @return descripción acumulada
     */
    @Override
    public String getDescripcion() {
        return componente.getDescripcion();
    }

    /**
     * Propaga los requisitos presenciales sin repetir especialidades.
     *
     * @return lista de solo lectura con las especialidades requeridas
     */
    @Override
    public List<Especialidad> getEspecialidadesPresenciales() {

        List<Especialidad> especialidades = new ArrayList<>();

        for (Especialidad especialidad
                : componente.getEspecialidadesPresenciales()) {

            if (!especialidades.contains(especialidad)) {
                especialidades.add(especialidad);
            }
        }

        return Collections.unmodifiableList(especialidades);
    }
}