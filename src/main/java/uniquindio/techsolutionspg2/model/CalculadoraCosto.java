package uniquindio.techsolutionspg2.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Calcula importes desde cero, sin duplicar desplazamientos por especialidad.
 * El complemento de domicilio se incluye en el costo del componente.
 */
public class CalculadoraCosto {
    /**
     * Suma los elementos y como máximo un desplazamiento por especialidad.
     * @param elementos elementos contratados, cuyos costos ya incluyen sus hijos
     * @param desplazamientos resultados de las consultas presenciales
     * @return costo total de la solicitud
     */
    public double calcularTotal(List<ComponenteTecnologico> elementos,
                                List<DisponibilidadTecnica> desplazamientos) {
        if (elementos == null || desplazamientos == null) {
            throw new IllegalArgumentException("Las listas son obligatorias.");
        }
        double total = 0;
        for (ComponenteTecnologico elemento : elementos) {
            if (elemento == null) { throw new IllegalArgumentException("Elemento inválido."); }
            total += elemento.getCosto();
        }
        List<Especialidad> cobradas = new ArrayList<>();
        for (DisponibilidadTecnica disponibilidad : desplazamientos) {
            if (disponibilidad == null || !disponibilidad.isDisponible()
                    || disponibilidad.getEspecialidad() == null) {
                throw new IllegalArgumentException("No hay disponibilidad válida.");
            }
            double costo = disponibilidad.getCostoDesplazamiento();
            if (!(costo >= 0 && costo <= Double.MAX_VALUE)) {
                throw new IllegalArgumentException("Desplazamiento inválido.");
            }
            if (!cobradas.contains(disponibilidad.getEspecialidad())) {
                total += costo;
                cobradas.add(disponibilidad.getEspecialidad());
            }
        }
        return total;
    }
}
