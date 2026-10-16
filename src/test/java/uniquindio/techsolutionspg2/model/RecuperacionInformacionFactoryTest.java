package uniquindio.techsolutionspg2.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba unitaria para verificar el comportamiento de RecuperacionInformacionFactory
 * y su integración con las clases concretas del modelo.
 */
public class RecuperacionInformacionFactoryTest {
    @Test public void crearServicioConservaTipoTarifaYDatos() {
        Servicio servicio = new RecuperacionInformacionFactory().crearServicio("COD", "Servicio", "Descripción");
        assertInstanceOf(RecuperacionInformacion.class, servicio);
        assertEquals(150000, servicio.getCosto());
        assertEquals(Especialidad.RECUPERACION, servicio.getEspecialidad());
        assertEquals("COD", servicio.getCodigo());
        assertEquals("Servicio", servicio.getNombre());
        assertEquals("Descripción", servicio.getDescripcion());
    }
}
