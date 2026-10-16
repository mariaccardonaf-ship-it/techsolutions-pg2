package uniquindio.techsolutionspg2.model;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba unitaria para verificar el comportamiento de ServicioDecorator
 * y su integración con las clases concretas del modelo.
 */
public class ServicioDecoratorTest {
    @Test public void decoradoresAcumulanSinModificarCatalogo() {
        Servicio base = correctivo();
        ServicioContratable personalizado = new AtencionDomicilio(new SoporteTecnico30Dias(new AtencionPrioritaria(base)));
        assertEquals(230000, personalizado.getCosto());
        assertEquals(120000, base.getCosto());
        assertTrue(personalizado.getDescripcion().contains("domicilio"));
        assertEquals(List.of(Especialidad.CORRECTIVO), personalizado.getEspecialidadesPresenciales());
    }

    private Servicio correctivo() { return new MantenimientoCorrectivo("C1", "Correctivo", "Correctivo"); }
}
