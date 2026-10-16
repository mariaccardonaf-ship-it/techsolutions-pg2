package uniquindio.techsolutionspg2.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba unitaria para verificar el comportamiento de LegacySupportAdapter
 * y su integración con las clases concretas del modelo.
 */
public class LegacySupportAdapterTest {
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
}
