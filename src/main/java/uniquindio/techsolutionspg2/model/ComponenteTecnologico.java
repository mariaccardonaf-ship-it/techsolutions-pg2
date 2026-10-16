package uniquindio.techsolutionspg2.model;
import java.util.List;

public interface ComponenteTecnologico {
    public double getCosto();
    public String getDescripcion();
    public List<Especialidad> getEspecialidadesPresenciales();
}
