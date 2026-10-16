package uniquindio.techsolutionspg2.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba unitaria para verificar el comportamiento de InstalacionRedesFactory
 * y su integración con las clases concretas del modelo.
 */
public class InstalacionRedesFactoryTest {
    @Test public void crearServicioConservaTipoTarifaYDatos() {
        Servicio servicio = new InstalacionRedesFactory().crearServicio("COD", "Servicio", "Descripción");
        assertInstanceOf(InstalacionRedes.class, servicio);
        assertEquals(180000, servicio.getCosto());
        assertEquals(Especialidad.REDES, servicio.getEspecialidad());
        assertEquals("COD", servicio.getCodigo());
        assertEquals("Servicio", servicio.getNombre());
        assertEquals("Descripción", servicio.getDescripcion());
    }
}
