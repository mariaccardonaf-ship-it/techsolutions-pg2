package uniquindio.techsolutionspg2.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Añade atención a domicilio por $25.000 e identifica su especialidad presencial.
 * El incremento no incluye el desplazamiento informado por LegacySupport.
 */
public class AtencionDomicilio extends ServicioDecorator {
    private static final double INCREMENTO = 25000;

    public AtencionDomicilio(ServicioContratable componente) {
        super(componente, INCREMENTO);
    }

    /**
     * Añade la atención a domicilio a la descripción.
     * @return descripción del servicio personalizado
     */
    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + atención a domicilio";
    }

    /**
     * Añade una vez la especialidad del servicio individual envuelto.
     * @return especialidades que requieren técnicos presenciales
     */
    @Override
    public List<Especialidad> getEspecialidadesPresenciales() {
        List<Especialidad> especialidades = new ArrayList<>(
                super.getEspecialidadesPresenciales());
        ServicioContratable actual = getComponente();
        while (actual instanceof ServicioDecorator) {
            actual = ((ServicioDecorator) actual).getComponente();
        }
        if (!(actual instanceof Servicio)) {
            throw new IllegalStateException("No se puede identificar la especialidad del servicio.");
        }
        Especialidad especialidad = ((Servicio) actual).getEspecialidad();
        if (!especialidades.contains(especialidad)) {
            especialidades.add(especialidad);
        }
        return Collections.unmodifiableList(especialidades);
    }
}
