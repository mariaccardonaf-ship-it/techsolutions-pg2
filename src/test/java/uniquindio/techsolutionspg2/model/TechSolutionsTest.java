package uniquindio.techsolutionspg2.model;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba unitaria para verificar el comportamiento de TechSolutions
 * y su integración con las clases concretas del modelo.
 */
public class TechSolutionsTest {
    @Test public void registroRechazaCodigoDuplicadoYClienteNoRegistrado() {
        TechSolutions sistema = new TechSolutions(new LegacySupportAdapter(new LegacySupport()));
        SolicitudServicio s = solicitud(sistema,correctivo());
        assertTrue(sistema.registrarSolicitud(s).isExitoso());
        assertFalse(sistema.registrarSolicitud(s).isExitoso());
        SolicitudServicio ajena = new SolicitudServicio.Builder("AJENA",cliente()).agregarElemento(correctivo()).build();
        assertFalse(sistema.registrarSolicitud(ajena).isExitoso());
    }

    @Test public void confirmarConsultaUnaVezYCobraDomicilioMasDesplazamiento() {
        AtomicInteger consultas = new AtomicInteger();
        TechSolutions sistema = new TechSolutions(e -> {consultas.incrementAndGet();return new DisponibilidadTecnica("T",e,true,30000);});
        ComponenteTecnologico personalizado = new AtencionDomicilio(new SoporteTecnico30Dias(new AtencionPrioritaria(correctivo())));
        SolicitudServicio s = solicitud(sistema,personalizado);sistema.registrarSolicitud(s);
        assertTrue(sistema.confirmarSolicitud(s.getCodigo()).isExitoso());
        assertEquals(260000,s.getCostoTotal());assertEquals(1,consultas.get());
        assertFalse(sistema.confirmarSolicitud(s.getCodigo()).isExitoso());
        assertEquals(260000,s.getCostoTotal());assertEquals(1,consultas.get());
    }

    @Test public void faltaDeCualquierTecnicoImpideConfirmar() {
        TechSolutions sistema = new TechSolutions(e -> new DisponibilidadTecnica(null,e,false,0));
        PaqueteTecnologico p = new PaqueteTecnologico("P","P");p.agregarComponente(new AtencionDomicilio(correctivo()));
        SolicitudServicio s = solicitud(sistema,p);sistema.registrarSolicitud(s);
        assertFalse(sistema.confirmarSolicitud(s.getCodigo()).isExitoso());
        assertEquals(EstadoSolicitud.REGISTRADA,s.getEstado());assertEquals(0,s.getCostoTotal());
        assertFalse(p.isBloqueado());
    }

    @Test public void confirmarBloqueaTodosLosPaquetesDescendientes() {
        TechSolutions sistema = new TechSolutions(new LegacySupportAdapter(new LegacySupport()));
        PaqueteTecnologico a = new PaqueteTecnologico("A","A");
        PaqueteTecnologico b = new PaqueteTecnologico("B","B");b.agregarComponente(correctivo());a.agregarComponente(b);
        SolicitudServicio s = solicitud(sistema,a);sistema.registrarSolicitud(s);
        assertTrue(sistema.confirmarSolicitud(s.getCodigo()).isExitoso());
        assertFalse(a.agregarComponente(correctivo()).isExitoso());
        assertFalse(b.agregarComponente(correctivo()).isExitoso());assertEquals(120000,s.getCostoTotal());
    }

    @Test public void prioridadNoCobraComplementoAutomaticamente() {
        TechSolutions sistema = new TechSolutions(new LegacySupportAdapter(new LegacySupport()));
        SolicitudServicio s = new SolicitudServicio.Builder("ALTA",sistema.consultarClientes().get(0))
                .agregarElemento(correctivo()).prioridad(Prioridad.ALTA).build();
        sistema.registrarSolicitud(s);assertTrue(sistema.confirmarSolicitud("ALTA").isExitoso());assertEquals(120000,s.getCostoTotal());
    }

    private Servicio correctivo() { return new MantenimientoCorrectivo("C1", "Correctivo", "Correctivo"); }

    private Cliente cliente() { return new Cliente("CLI", "Cliente", "cliente@example.com"); }

    private SolicitudServicio solicitud(TechSolutions sistema, ComponenteTecnologico componente) {
        return new SolicitudServicio.Builder("SOL", sistema.consultarClientes().get(0))
                .agregarElemento(componente).build();
    }
}
