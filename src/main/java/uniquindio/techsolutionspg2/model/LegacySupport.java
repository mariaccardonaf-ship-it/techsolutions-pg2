package uniquindio.techsolutionspg2.model;

/**
 * Simulación local del proveedor externo para ejecutar el ejercicio.
 * No es una implementación entregada por el proveedor real.
 * El resto del modelo solo accede a esta API mediante su adaptador.
 */
public class LegacySupport {
    /**
     * Devuelve respuestas de ejemplo con el formato de la API externa.
     * @param codigo 1 preventivo, 2 correctivo, 3 redes, 4 recuperación
     * @return identificador, disponibilidad y desplazamiento separados por barras
     */
    public String consultar(int codigo) {
        switch (codigo) {
            case 1: return "TEC-101|DISPONIBLE|30000";
            case 2: return "TEC-102|DISPONIBLE|30000";
            case 3: return "TEC-103|DISPONIBLE|40000";
            case 4: return "SIN_TECNICO|NO_DISPONIBLE|0";
            default: throw new IllegalArgumentException("Especialidad externa desconocida.");
        }
    }
}
