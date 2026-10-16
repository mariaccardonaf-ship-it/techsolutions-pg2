package uniquindio.techsolutionspg2.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Agrupa servicios y paquetes mediante Composite y previene ciclos recursivos.
 * Un paquete contratado queda bloqueado al confirmar una solicitud.
 */
public class PaqueteTecnologico implements ComponenteTecnologico {
    private final String codigo;
    private final String nombre;
    private final List<ComponenteTecnologico> componentes = new ArrayList<>();
    private boolean bloqueado;

    public PaqueteTecnologico(String codigo, String nombre) {
        if (codigo == null || codigo.trim().isEmpty()
                || nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El código y el nombre son obligatorios.");
        }
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public boolean isBloqueado() { return bloqueado; }
    public List<ComponenteTecnologico> getComponentes() {
        return Collections.unmodifiableList(componentes);
    }

    /**
     * Agrega un componente si el paquete admite cambios y no se forma un ciclo.
     * @param componente servicio o paquete que se incorpora
     * @return resultado de la operación
     */
    public ResultadoOperacion agregarComponente(ComponenteTecnologico componente) {
        if (bloqueado) {
            return new ResultadoOperacion(false, "El paquete ya está contratado y no puede modificarse.");
        }
        if (componente == null || componente == this) {
            return new ResultadoOperacion(false, "El componente es inválido o genera un ciclo.");
        }
        if (componente instanceof PaqueteTecnologico
                && ((PaqueteTecnologico) componente).contieneComponente(this)) {
            return new ResultadoOperacion(false, "La agrupación genera un ciclo indirecto.");
        }
        componentes.add(componente);
        return new ResultadoOperacion(true, "Componente agregado.");
    }

    /**
     * Busca un componente por identidad en todos los niveles del paquete.
     * @param buscado componente que se desea localizar
     * @return true si aparece en el árbol de componentes
     */
    public boolean contieneComponente(ComponenteTecnologico buscado) {
        for (ComponenteTecnologico componente : componentes) {
            if (componente == buscado) { return true; }
            if (componente instanceof PaqueteTecnologico
                    && ((PaqueteTecnologico) componente).contieneComponente(buscado)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Suma los costos completos de los componentes una vez por contratación.
     * @return costo total del paquete
     */
    @Override
    public double getCosto() {
        double total = 0;
        for (ComponenteTecnologico componente : componentes) {
            total += componente.getCosto();
        }
        return total;
    }

    /**
     * Describe el paquete y sus componentes anidados.
     * @return descripción de la agrupación
     */
    @Override
    public String getDescripcion() {
        String descripcion = nombre + " [";
        for (int i = 0; i < componentes.size(); i++) {
            if (i > 0) { descripcion += "; "; }
            descripcion += componentes.get(i).getDescripcion();
        }
        return descripcion + "]";
    }

    /**
     * Reúne las especialidades presenciales de todos los niveles sin duplicados.
     * @return lista de solo lectura de especialidades requeridas
     */
    @Override
    public List<Especialidad> getEspecialidadesPresenciales() {
        List<Especialidad> especialidades = new ArrayList<>();
        for (ComponenteTecnologico componente : componentes) {
            for (Especialidad especialidad : componente.getEspecialidadesPresenciales()) {
                if (!especialidades.contains(especialidad)) {
                    especialidades.add(especialidad);
                }
            }
        }
        return Collections.unmodifiableList(especialidades);
    }

    /**
     * Impide cambios en este paquete y en todos sus paquetes descendientes.
     */
    public void bloquearRecursivamente() {
        bloqueado = true;
        for (ComponenteTecnologico componente : componentes) {
            if (componente instanceof PaqueteTecnologico) {
                ((PaqueteTecnologico) componente).bloquearRecursivamente();
            }
        }
    }
}
