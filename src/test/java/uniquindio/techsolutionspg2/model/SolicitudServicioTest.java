package uniquindio.techsolutionspg2.model;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba unitaria para verificar el comportamiento de SolicitudServicio
 * y su clase interna Builder, integrando las clases concretas del modelo.
 */
public class SolicitudServicioTest {
    @Test public void builderAceptaSolicitudSinOpcionales() {
        SolicitudServicio s = new SolicitudServicio.Builder("S", cliente()).agregarElemento(correctivo()).build();
        assertNull(s.getFechaProgramada());assertNull(s.getPrioridad());
        assertEquals(EstadoSolicitud.REGISTRADA, s.getEstado());
        assertEquals(LocalDate.now(), s.getFechaCreacion());
    }

    @Test public void builderConservaLosOpcionales() {
        SolicitudServicio s = new SolicitudServicio.Builder("S", cliente()).agregarElemento(correctivo())
                .fechaProgramada(LocalDate.now().plusDays(1)).observaciones("Revisar disco").prioridad(Prioridad.ALTA).build();
        assertEquals(Prioridad.ALTA,s.getPrioridad());assertEquals("Revisar disco",s.getObservaciones());
        assertEquals(LocalDate.now().plusDays(1),s.getFechaProgramada());
    }

    @Test public void builderRechazaDatosEsencialesAusentes() {
        assertThrows(IllegalArgumentException.class, () -> new SolicitudServicio.Builder("S", null).agregarElemento(correctivo()).build());
        assertThrows(IllegalArgumentException.class, () -> new SolicitudServicio.Builder("S", cliente()).build());
        assertThrows(IllegalArgumentException.class, () -> new SolicitudServicio.Builder(" ", cliente()).agregarElemento(correctivo()).build());
        assertThrows(IllegalArgumentException.class, () -> new SolicitudServicio.Builder("S", cliente()).agregarElemento(null));
    }

    @Test public void builderRechazaFechaAnteriorALaCreacion() {
        assertThrows(IllegalArgumentException.class, () -> new SolicitudServicio.Builder("S", cliente())
                .agregarElemento(correctivo()).fechaProgramada(LocalDate.now().minusDays(1)).build());
    }

    @Test public void elementosNoPermitenCambiosExternos() {
        SolicitudServicio solicitud = new SolicitudServicio.Builder("S", cliente()).agregarElemento(correctivo()).build();
        assertThrows(UnsupportedOperationException.class, () -> solicitud.getElementos().clear());
    }

    private Servicio correctivo() { return new MantenimientoCorrectivo("C1", "Correctivo", "Correctivo"); }

    private Cliente cliente() { return new Cliente("CLI", "Cliente", "cliente@example.com"); }
}
