package uniquindio.techsolutionspg2.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Coordina catálogo, paquetes, clientes y solicitudes del modelo.
 * Confirma únicamente después de validar técnicos y calcular el costo definitivo.
 */
public class TechSolutions {
    private final List<Servicio> catalogo = new ArrayList<>();
    private final List<PaqueteTecnologico> paquetes = new ArrayList<>();
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<SolicitudServicio> solicitudes = new ArrayList<>();
    private final ProveedorTecnicos proveedor;
    private final CalculadoraCosto calculadora = new CalculadoraCosto();

    public TechSolutions(ProveedorTecnicos proveedor) {
        if (proveedor == null) { throw new IllegalArgumentException("Indique un proveedor."); }
        this.proveedor = proveedor;
        // Datos ficticios precargados; no se implementa un CRUD de clientes.
        clientes.add(new Cliente("CLI-001", "Cliente de ejemplo", "cliente@example.com"));
        registrarServicio(new MantenimientoPreventivoFactory(), "Preventivo", "Mantenimiento preventivo");
        registrarServicio(new MantenimientoCorrectivoFactory(), "Correctivo", "Mantenimiento correctivo");
        registrarServicio(new InstalacionRedesFactory(), "Redes", "Instalación de redes");
        registrarServicio(new RecuperacionInformacionFactory(), "Recuperación", "Recuperación de información");
    }

    /**
     * Crea y registra un servicio mediante la fábrica seleccionada.
     * @param fabrica fábrica del tipo de servicio
     * @param nombre nombre del servicio
     * @param descripcion descripción del servicio
     * @return servicio añadido al catálogo
     */
    public Servicio registrarServicio(ServicioFactory fabrica, String nombre, String descripcion) {
        if (fabrica == null || nombre == null || nombre.trim().isEmpty()
                || descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("Indique fábrica, nombre y descripción.");
        }
        String codigo = GeneradorCodigos.getInstancia().siguienteCodigo("SER");
        Servicio servicio = fabrica.crearServicio(codigo, nombre, descripcion);
        catalogo.add(servicio);
        return servicio;
    }

    /**
     * Registra un paquete vacío para configurarlo mediante agregarComponente().
     * @param nombre nombre del paquete
     * @return paquete registrado
     */
    public PaqueteTecnologico crearPaquete(String nombre) {
        PaqueteTecnologico paquete = new PaqueteTecnologico(
                GeneradorCodigos.getInstancia().siguienteCodigo("PAQ"), nombre);
        paquetes.add(paquete);
        return paquete;
    }

    /**
     * Registra una solicitud de un cliente precargado, verificando su código único.
     * @param solicitud solicitud construida mediante Builder
     * @return resultado del registro
     */
    public ResultadoOperacion registrarSolicitud(SolicitudServicio solicitud) {
        if (solicitud == null || solicitud.getEstado() != EstadoSolicitud.REGISTRADA) {
            return new ResultadoOperacion(false, "Solicitud inválida para registrar.");
        }
        boolean clienteRegistrado = false;
        for (Cliente cliente : clientes) {
            if (cliente.getDocumento().equals(solicitud.getCliente().getDocumento())) {
                clienteRegistrado = true;
            }
        }
        if (!clienteRegistrado) { return new ResultadoOperacion(false, "El cliente no está registrado."); }
        for (SolicitudServicio existente : solicitudes) {
            if (existente.getCodigo().equals(solicitud.getCodigo())) {
                return new ResultadoOperacion(false, "El código de solicitud ya existe.");
            }
        }
        solicitudes.add(solicitud);
        return new ResultadoOperacion(true, "Solicitud registrada.");
    }

    /**
     * Valida todas las especialidades y fija el costo sin repetir cargos.
     * @param codigo identificador de una solicitud registrada
     * @return resultado de la confirmación
     */
    public ResultadoOperacion confirmarSolicitud(String codigo) {
        SolicitudServicio solicitud = null;
        for (SolicitudServicio existente : solicitudes) {
            if (existente.getCodigo().equals(codigo)) { solicitud = existente; break; }
        }
        if (solicitud == null) { return new ResultadoOperacion(false, "No existe la solicitud."); }
        if (solicitud.getEstado() == EstadoSolicitud.CONFIRMADA) {
            return new ResultadoOperacion(false, "La solicitud ya está confirmada.");
        }
        try {
            List<DisponibilidadTecnica> desplazamientos = obtenerDesplazamientos(solicitud);
            double total = calculadora.calcularTotal(solicitud.getElementos(), desplazamientos);
            bloquearPaquetes(solicitud);
            solicitud.confirmar(total);
            return new ResultadoOperacion(true, "Solicitud confirmada. Total: $" + total);
        } catch (IllegalArgumentException | IllegalStateException excepcion) {
            return new ResultadoOperacion(false, excepcion.getMessage());
        }
    }

    /**
     * Consulta al proveedor utilizando el contrato independiente del adaptador.
     * @param especialidad especialidad requerida
     * @return disponibilidad del técnico
     */
    public DisponibilidadTecnica consultarTecnico(Especialidad especialidad) {
        return proveedor.consultarTecnico(especialidad);
    }

    /**
     * Devuelve una vista de solo lectura del catálogo.
     * @return servicios disponibles
     */
    public List<Servicio> consultarServicios() { return Collections.unmodifiableList(catalogo); }

    /**
     * Devuelve los paquetes configurados sin permitir modificar el listado.
     * @return paquetes registrados
     */
    public List<PaqueteTecnologico> consultarPaquetes() { return Collections.unmodifiableList(paquetes); }

    /**
     * Consulta los clientes precargados.
     * @return lista de clientes de solo lectura
     */
    public List<Cliente> consultarClientes() { return Collections.unmodifiableList(clientes); }

    /**
     * Consulta las solicitudes y sus estados.
     * @return solicitudes registradas en una lista de solo lectura
     */
    public List<SolicitudServicio> consultarSolicitudes() { return Collections.unmodifiableList(solicitudes); }

    /**
     * Reúne especialidades sin repetidos y consulta una vez cada una.
     * @param solicitud solicitud que se confirmará
     * @return disponibilidades válidas para calcular los desplazamientos
     */
    private List<DisponibilidadTecnica> obtenerDesplazamientos(SolicitudServicio solicitud) {
        List<Especialidad> especialidades = new ArrayList<>();
        for (ComponenteTecnologico elemento : solicitud.getElementos()) {
            for (Especialidad especialidad : elemento.getEspecialidadesPresenciales()) {
                if (!especialidades.contains(especialidad)) { especialidades.add(especialidad); }
            }
        }
        List<DisponibilidadTecnica> resultados = new ArrayList<>();
        for (Especialidad especialidad : especialidades) {
            DisponibilidadTecnica resultado = consultarTecnico(especialidad);
            if (resultado == null || !resultado.isDisponible()
                    || resultado.getEspecialidad() != especialidad) {
                throw new IllegalStateException("No hay técnico disponible para " + especialidad);
            }
            resultados.add(resultado);
        }
        return resultados;
    }

    /**
     * Protege todos los paquetes contratados frente a cambios posteriores.
     * @param solicitud solicitud validada antes de confirmar
     */
    private void bloquearPaquetes(SolicitudServicio solicitud) {
        for (ComponenteTecnologico elemento : solicitud.getElementos()) {
            if (elemento instanceof PaqueteTecnologico) {
                ((PaqueteTecnologico) elemento).bloquearRecursivamente();
            }
        }
    }
}
