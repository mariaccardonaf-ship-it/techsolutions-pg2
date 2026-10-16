package uniquindio.techsolutionspg2.model;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba unitaria para verificar el comportamiento de CalculadoraCosto
 * y su integración con las clases concretas del modelo.
 */
public class CalculadoraCostoTest {
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

    private Servicio correctivo() { return new MantenimientoCorrectivo("C1", "Correctivo", "Correctivo"); }
}
