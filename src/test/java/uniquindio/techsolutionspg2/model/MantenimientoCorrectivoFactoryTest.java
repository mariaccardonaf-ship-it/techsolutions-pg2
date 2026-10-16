package uniquindio.techsolutionspg2.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba unitaria para verificar el comportamiento de MantenimientoCorrectivoFactory
 * y su integración con las clases concretas del modelo.
 */
public class MantenimientoCorrectivoFactoryTest {
    @Test public void crearServicioConservaTipoTarifaYDatos() {
        Servicio servicio = new MantenimientoCorrectivoFactory().crearServicio("COD", "Servicio", "Descripción");
        assertInstanceOf(MantenimientoCorrectivo.class, servicio);
        assertEquals(120000, servicio.getCosto());
        assertEquals(Especialidad.CORRECTIVO, servicio.getEspecialidad());
        assertEquals("COD", servicio.getCodigo());
        assertEquals("Servicio", servicio.getNombre());
        assertEquals("Descripción", servicio.getDescripcion());
    }
}
