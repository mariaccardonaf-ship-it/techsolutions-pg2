package uniquindio.techsolutionspg2.model;

/**
 * Adapta las especialidades y respuestas de LegacySupport al contrato del modelo.
 * La tabla 1 a 4 es una decisión para la simulación del ejercicio.
 */
public class LegacySupportAdapter implements ProveedorTecnicos {
    private final LegacySupport legacy;

    public LegacySupportAdapter(LegacySupport legacy) {
        if (legacy == null) {
            throw new IllegalArgumentException("El proveedor externo es obligatorio.");
        }
        this.legacy = legacy;
    }

    /**
     * Consulta al proveedor y transforma su respuesta en información estructurada.
     * @param especialidad especialidad requerida
     * @return disponibilidad y desplazamiento del técnico
     */
    @Override
    public DisponibilidadTecnica consultarTecnico(Especialidad especialidad) {
        return interpretar(legacy.consultar(traducirEspecialidad(especialidad)), especialidad);
    }

    /**
     * Convierte el enum en el código aceptado por la simulación externa.
     * @param especialidad especialidad del modelo
     * @return código numérico externo
     */
    private int traducirEspecialidad(Especialidad especialidad) {
        if (especialidad == null) {
            throw new IllegalArgumentException("La especialidad es obligatoria.");
        }
        switch (especialidad) {
            case PREVENTIVO: return 1;
            case CORRECTIVO: return 2;
            case REDES: return 3;
            case RECUPERACION: return 4;
            default: throw new IllegalArgumentException("Especialidad no soportada.");
        }
    }

    /**
     * Valida y convierte la cadena del proveedor externo.
     * @param respuesta cadena con tres campos separados por barras
     * @param especialidad especialidad consultada
     * @return resultado estructurado
     */
    private DisponibilidadTecnica interpretar(String respuesta, Especialidad especialidad) {
        if (respuesta == null) {
            throw new IllegalArgumentException("El proveedor no devolvió una respuesta.");
        }
        String[] campos = respuesta.split("\\|", -1);
        if (campos.length != 3) {
            throw new IllegalArgumentException("Formato de respuesta inválido.");
        }
        double desplazamiento;
        try {
            desplazamiento = Double.parseDouble(campos[2]);
        } catch (NumberFormatException excepcion) {
            throw new IllegalArgumentException("Desplazamiento inválido.");
        }
        if (!(desplazamiento >= 0 && desplazamiento <= Double.MAX_VALUE)) {
            throw new IllegalArgumentException("Desplazamiento fuera de rango.");
        }
        if ("DISPONIBLE".equals(campos[1]) && !campos[0].trim().isEmpty()
                && !"SIN_TECNICO".equals(campos[0])) {
            return new DisponibilidadTecnica(campos[0], especialidad, true, desplazamiento);
        }
        if ("NO_DISPONIBLE".equals(campos[1]) && "SIN_TECNICO".equals(campos[0])
                && desplazamiento == 0) {
            return new DisponibilidadTecnica(null, especialidad, false, 0);
        }
        throw new IllegalArgumentException("Respuesta de disponibilidad inconsistente.");
    }
}
