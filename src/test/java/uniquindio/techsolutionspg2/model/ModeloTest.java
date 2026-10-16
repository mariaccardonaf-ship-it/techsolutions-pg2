package uniquindio.techsolutionspg2.model;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifica las reglas del modelo y la integración entre sus patrones de diseño.
 */
public class ModeloTest {
    private Servicio correctivo() { return new MantenimientoCorrectivo("C1", "Correctivo", "Correctivo"); }
    private Cliente cliente() { return new Cliente("CLI", "Cliente", "cliente@example.com"); }
    private SolicitudServicio solicitud(TechSolutions sistema, ComponenteTecnologico componente) {
        return new SolicitudServicio.Builder("SOL", sistema.consultarClientes().get(0))
                .agregarElemento(componente).build();
    }

    @Test public void fabricasCreanTiposYTarifasDelEnunciado() {
        ServicioFactory[] fabricas = {new MantenimientoPreventivoFactory(), new MantenimientoCorrectivoFactory(),
                new InstalacionRedesFactory(), new RecuperacionInformacionFactory()};
        double[] costos = {80000, 120000, 180000, 150000};
        Especialidad[] especialidades = Especialidad.values();
        for (int i = 0; i < fabricas.length; i++) {
            Servicio servicio = fabricas[i].crearServicio("COD" + i, "Servicio", "Descripción");
            assertEquals(costos[i], servicio.getCosto());
            assertEquals(especialidades[i], servicio.getEspecialidad());
            assertEquals("COD" + i, servicio.getCodigo());
        }
    }

    @Test public void decoradoresAcumulanSinModificarCatalogo() {
        Servicio base = correctivo();
        ServicioContratable personalizado = new AtencionDomicilio(new SoporteTecnico30Dias(new AtencionPrioritaria(base)));
        assertEquals(230000, personalizado.getCosto());
        assertEquals(120000, base.getCosto());
        assertTrue(personalizado.getDescripcion().contains("domicilio"));
        assertEquals(List.of(Especialidad.CORRECTIVO), personalizado.getEspecialidadesPresenciales());
    }

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

    @Test public void listasNoPermitenModificarLaSolicitudOPaquete() {
        SolicitudServicio s = new SolicitudServicio.Builder("S", cliente()).agregarElemento(correctivo()).build();
        assertThrows(UnsupportedOperationException.class, () -> s.getElementos().clear());
        PaqueteTecnologico p = new PaqueteTecnologico("P","P");
        assertThrows(UnsupportedOperationException.class, () -> p.getComponentes().add(correctivo()));
    }

    @Test public void adapterTraduceEspecialidadYRespuestaDisponible() {
        LegacySupport proveedor = new LegacySupport() {
            /** Comprueba el código numérico enviado al proveedor simulado. */
            @Override public String consultar(int codigo) { assertEquals(3,codigo);return "TEC-77|DISPONIBLE|45000"; }
        };
        DisponibilidadTecnica d = new LegacySupportAdapter(proveedor).consultarTecnico(Especialidad.REDES);
        assertEquals("TEC-77",d.getIdTecnico());assertTrue(d.isDisponible());assertEquals(45000,d.getCostoDesplazamiento());
    }

    @Test public void adapterRepresentaLaAusenciaDeTecnico() {
        DisponibilidadTecnica d = new LegacySupportAdapter(new LegacySupport()).consultarTecnico(Especialidad.RECUPERACION);
        assertFalse(d.isDisponible());assertNull(d.getIdTecnico());assertEquals(0,d.getCostoDesplazamiento());
    }

    @Test public void adapterRechazaRespuestasInvalidas() {
        for (String texto : new String[]{"malformada", "TEC|DISPONIBLE|NaN", "TEC|DISPONIBLE|-1", "TEC|DISPONIBLE|Infinity", "TEC|OTRO|0"}) {
            LegacySupport proveedor = new LegacySupport() {
                /** Simula una respuesta inválida del proveedor externo. */
                @Override public String consultar(int codigo) { return texto; }
            };
            assertThrows(IllegalArgumentException.class, () -> new LegacySupportAdapter(proveedor).consultarTecnico(Especialidad.REDES));
        }
    }

    @Test public void calculadoraNoAcumulaResultadosNiRepiteDesplazamiento() {
        CalculadoraCosto c = new CalculadoraCosto();
        DisponibilidadTecnica d = new DisponibilidadTecnica("T",Especialidad.CORRECTIVO,true,30000);
        assertEquals(150000,c.calcularTotal(List.of(correctivo()),List.of(d,d)));
        assertEquals(150000,c.calcularTotal(List.of(correctivo()),List.of(d)));
    }

    @Test public void calculadoraConservaDesplazamientosDeEspecialidadesDiferentes() {
        CalculadoraCosto c = new CalculadoraCosto();
        assertEquals(190000,c.calcularTotal(List.of(correctivo()),List.of(
                new DisponibilidadTecnica("T1",Especialidad.CORRECTIVO,true,30000),
                new DisponibilidadTecnica("T2",Especialidad.REDES,true,40000))));
    }

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

    @Test public void singletonGeneraCodigosDistintosEnUnaEjecucion() {
        assertSame(GeneradorCodigos.getInstancia(),GeneradorCodigos.getInstancia());
        String a = GeneradorCodigos.getInstancia().siguienteCodigo("SOL");
        assertNotEquals(a,GeneradorCodigos.getInstancia().siguienteCodigo("SOL"));
    }
}
