package uniquindio.techsolutionspg2.model;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba unitaria para verificar el comportamiento de PaqueteTecnologico
 * y su integración con las clases concretas del modelo.
 */
public class PaqueteTecnologicoTest {
    @Test public void paquetesCalculanTodosLosNiveles() {
        PaqueteTecnologico mantenimiento = new PaqueteTecnologico("M", "Mantenimiento");
        mantenimiento.agregarComponente(new MantenimientoPreventivo("P", "P", "P"));
        mantenimiento.agregarComponente(correctivo());
        PaqueteTecnologico empresarial = new PaqueteTecnologico("E", "Empresarial");
        empresarial.agregarComponente(mantenimiento);
        empresarial.agregarComponente(new InstalacionRedes("R", "R", "R"));
        empresarial.agregarComponente(new RecuperacionInformacion("I", "I", "I"));
        assertEquals(200000, mantenimiento.getCosto());
        assertEquals(530000, empresarial.getCosto());
    }

    @Test public void paqueteRechazaCicloDirecto() {
        PaqueteTecnologico a = new PaqueteTecnologico("A", "A");
        assertFalse(a.agregarComponente(a).isExitoso());
        assertTrue(a.getComponentes().isEmpty());
    }

    @Test public void paqueteRechazaCicloIndirectoProfundo() {
        PaqueteTecnologico a = new PaqueteTecnologico("A", "A");
        PaqueteTecnologico b = new PaqueteTecnologico("B", "B");
        PaqueteTecnologico c = new PaqueteTecnologico("C", "C");
        a.agregarComponente(b);b.agregarComponente(c);
        assertFalse(c.agregarComponente(a).isExitoso());
        assertTrue(a.contieneComponente(c));
    }

    @Test public void paqueteNoRepiteEspecialidadesPresenciales() {
        PaqueteTecnologico p = new PaqueteTecnologico("P", "P");
        p.agregarComponente(new AtencionDomicilio(correctivo()));
        p.agregarComponente(new AtencionDomicilio(correctivo()));
        assertEquals(List.of(Especialidad.CORRECTIVO), p.getEspecialidadesPresenciales());
    }

    @Test public void componentesNoPermitenCambiosExternos() {
        PaqueteTecnologico paquete = new PaqueteTecnologico("P", "Paquete");
        assertThrows(UnsupportedOperationException.class, () -> paquete.getComponentes().add(correctivo()));
    }

    private Servicio correctivo() { return new MantenimientoCorrectivo("C1", "Correctivo", "Correctivo"); }
}
