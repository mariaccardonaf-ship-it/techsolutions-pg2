package uniquindio.techsolutionspg2.model;

/**
 * Conserva los datos de identificación y contacto de un cliente.
 */
public class Cliente {
    private final String documento;
    private final String nombreCompleto;
    private final String correo;

    public Cliente(String documento, String nombreCompleto, String correo) {
        if (documento == null || documento.trim().isEmpty()
                || nombreCompleto == null || nombreCompleto.trim().isEmpty()
                || correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("Los datos del cliente son obligatorios.");
        }
        this.documento = documento;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
    }

    public String getDocumento() { return documento; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getCorreo() { return correo; }
}
