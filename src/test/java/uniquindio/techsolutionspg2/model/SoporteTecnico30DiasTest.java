package uniquindio.techsolutionspg2.model;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba unitaria para verificar el comportamiento de SoporteTecnico30Dias
 * y su integración con las clases concretas del modelo.
 */
public class SoporteTecnico30DiasTest {
    @Test public void complementoConservaBaseYAplicaSuComportamiento() {
        Servicio base = correctivo();
        ServicioContratable complemento = new SoporteTecnico30Dias(base);
        assertEquals(170000, complemento.getCosto());
        assertEquals(120000, base.getCosto());
        assertEquals(List.of(), complemento.getEspecialidadesPresenciales());
        assertTrue(complemento.getDescripcion().toLowerCase().contains("30"));
    }

    private Servicio correctivo() { return new MantenimientoCorrectivo("C1", "Correctivo", "Correctivo"); }
}
