package uniquindio.techsolutionspg2.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba unitaria para verificar el comportamiento de GeneradorCodigos
 * y su integración con las clases concretas del modelo.
 */
public class GeneradorCodigosTest {
    @Test public void singletonGeneraCodigosDistintosEnUnaEjecucion() {
        assertSame(GeneradorCodigos.getInstancia(),GeneradorCodigos.getInstancia());
        String a = GeneradorCodigos.getInstancia().siguienteCodigo("SOL");
        assertNotEquals(a,GeneradorCodigos.getInstancia().siguienteCodigo("SOL"));
    }
}
