package uniquindio.techsolutionspg2.model;

/**
 * Genera códigos consecutivos durante una ejecución mediante Singleton.
 * No garantiza unicidad entre ejecuciones si se incorpora persistencia.
 */
public class GeneradorCodigos {
    private static final GeneradorCodigos instancia = new GeneradorCodigos();
    private long contador;

    private GeneradorCodigos() { }

    public static GeneradorCodigos getInstancia() {
        return instancia;
    }

    /**
     * Genera un código con prefijo y consecutivo compartido.
     * @param prefijo identifica el tipo de elemento
     * @return código generado
     */
    public synchronized String siguienteCodigo(String prefijo) {
        if (prefijo == null || prefijo.trim().isEmpty()) {
            throw new IllegalArgumentException("El prefijo es obligatorio.");
        }
        contador++;
        return prefijo + "-" + contador;
    }
}
