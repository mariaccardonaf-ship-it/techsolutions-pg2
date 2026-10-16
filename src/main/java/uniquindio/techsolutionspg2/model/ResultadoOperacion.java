package uniquindio.techsolutionspg2.model;

/**
 * Comunica el resultado de una operación y un mensaje para el usuario.
 */
public class ResultadoOperacion {

    private final boolean exitoso;
    private final String mensaje;

    public ResultadoOperacion(boolean exitoso, String mensaje) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public String getMensaje() {
        return mensaje;
    }
}
