package uniquindio.techsolutionspg2.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba unitaria para verificar el comportamiento de MantenimientoPreventivoFactory
 * y su integración con las clases concretas del modelo.
 */
public class MantenimientoPreventivoFactoryTest {
    @Test public void crearServicioConservaTipoTarifaYDatos() {
        Servicio servicio = new MantenimientoPreventivoFactory().crearServicio("COD", "Servicio", "Descripción");
        assertInstanceOf(MantenimientoPreventivo.class, servicio);
        assertEquals(80000, servicio.getCosto());
        assertEquals(Especialidad.PREVENTIVO, servicio.getEspecialidad());
        assertEquals("COD", servicio.getCodigo());
        assertEquals("Servicio", servicio.getNombre());
        assertEquals("Descripción", servicio.getDescripcion());
    }
}
