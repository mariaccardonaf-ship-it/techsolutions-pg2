package uniquindio.techsolutionspg2.model;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba unitaria para verificar el comportamiento de AtencionDomicilio
 * y su integración con las clases concretas del modelo.
 */
public class AtencionDomicilioTest {
    @Test public void complementoConservaBaseYAplicaSuComportamiento() {
        Servicio base = correctivo();
        ServicioContratable complemento = new AtencionDomicilio(base);
        assertEquals(145000, complemento.getCosto());
        assertEquals(120000, base.getCosto());
        assertEquals(List.of(Especialidad.CORRECTIVO), complemento.getEspecialidadesPresenciales());
        assertTrue(complemento.getDescripcion().toLowerCase().contains("domicilio"));
    }

    private Servicio correctivo() { return new MantenimientoCorrectivo("C1", "Correctivo", "Correctivo"); }
}
