package uniquindio.techsolutionspg2.model;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba unitaria para verificar el comportamiento de AtencionPrioritaria
 * y su integración con las clases concretas del modelo.
 */
public class AtencionPrioritariaTest {
    @Test public void complementoConservaBaseYAplicaSuComportamiento() {
        Servicio base = correctivo();
        ServicioContratable complemento = new AtencionPrioritaria(base);
        assertEquals(155000, complemento.getCosto());
        assertEquals(120000, base.getCosto());
        assertEquals(List.of(), complemento.getEspecialidadesPresenciales());
        assertTrue(complemento.getDescripcion().toLowerCase().contains("prioritaria"));
    }

    private Servicio correctivo() { return new MantenimientoCorrectivo("C1", "Correctivo", "Correctivo"); }
}
