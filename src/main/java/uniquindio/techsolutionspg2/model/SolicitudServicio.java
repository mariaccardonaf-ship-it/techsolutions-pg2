package uniquindio.techsolutionspg2.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa una solicitud construida con Builder y conserva su total al confirmar.
 * La confirmación la coordina TechSolutions después de validar disponibilidad.
 */
public class SolicitudServicio {
    private final String codigo;
    private final LocalDate fechaCreacion;
    private final Cliente cliente;
    private final List<ComponenteTecnologico> elementos;
    private final LocalDate fechaProgramada;
    private final String observaciones;
    private final Prioridad prioridad;
    private EstadoSolicitud estado = EstadoSolicitud.REGISTRADA;
    private double costoTotal;

    private SolicitudServicio(Builder builder) {
        codigo = builder.codigo;
        fechaCreacion = builder.fechaCreacion;
        cliente = builder.cliente;
        elementos = Collections.unmodifiableList(new ArrayList<>(builder.elementos));
        fechaProgramada = builder.fechaProgramada;
        observaciones = builder.observaciones;
        prioridad = builder.prioridad;
    }

    public String getCodigo() { return codigo; }
    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public Cliente getCliente() { return cliente; }
    public List<ComponenteTecnologico> getElementos() { return elementos; }
    public LocalDate getFechaProgramada() { return fechaProgramada; }
    public String getObservaciones() { return observaciones; }
    public Prioridad getPrioridad() { return prioridad; }
    public EstadoSolicitud getEstado() { return estado; }
    public double getCostoTotal() { return costoTotal; }

    /**
     * Guarda el importe definitivo una sola vez y confirma la solicitud.
     * @param total importe validado por la coordinación del sistema
     */
    public void confirmar(double total) {
        if (estado == EstadoSolicitud.CONFIRMADA) {
            throw new IllegalStateException("La solicitud ya está confirmada.");
        }
        costoTotal = total;
        estado = EstadoSolicitud.CONFIRMADA;
    }

    /**
     * Construye solicitudes con datos obligatorios y opcionales.
     * Al ser anidada estática puede invocar el constructor privado de la solicitud.
     */
    public static class Builder {
        private final String codigo;
        private final Cliente cliente;
        private final LocalDate fechaCreacion = LocalDate.now();
        private final List<ComponenteTecnologico> elementos = new ArrayList<>();
        private LocalDate fechaProgramada;
        private String observaciones;
        private Prioridad prioridad;

        public Builder(String codigo, Cliente cliente) {
            this.codigo = codigo;
            this.cliente = cliente;
        }

        /**
         * Incorpora un elemento a la solicitud en construcción.
         * @param componente elemento contratado
         * @return este Builder para continuar su configuración
         */
        public Builder agregarElemento(ComponenteTecnologico componente) {
            if (componente == null) {
                throw new IllegalArgumentException("El elemento no puede ser nulo.");
            }
            elementos.add(componente);
            return this;
        }

        /**
         * Define una fecha opcional de atención.
         * @param fecha fecha programada
         * @return este Builder
         */
        public Builder fechaProgramada(LocalDate fecha) {
            fechaProgramada = fecha;
            return this;
        }

        /**
         * Incorpora observaciones técnicas opcionales.
         * @param texto observaciones de la solicitud
         * @return este Builder
         */
        public Builder observaciones(String texto) {
            observaciones = texto;
            return this;
        }

        /**
         * Define la prioridad opcional sin aplicar un complemento de precio.
         * @param valor prioridad de la solicitud
         * @return este Builder
         */
        public Builder prioridad(Prioridad valor) {
            prioridad = valor;
            return this;
        }

        /**
         * Verifica los datos esenciales y la coherencia de la fecha programada.
         * @return resultado de la validación
         */
        public ResultadoOperacion validar() {
            if (codigo == null || codigo.trim().isEmpty()) {
                return new ResultadoOperacion(false, "El código es obligatorio.");
            }
            if (cliente == null || elementos.isEmpty()) {
                return new ResultadoOperacion(false, "Debe indicar cliente y al menos un elemento.");
            }
            if (fechaProgramada != null && fechaProgramada.isBefore(fechaCreacion)) {
                return new ResultadoOperacion(false, "La atención no puede preceder a la creación.");
            }
            return new ResultadoOperacion(true, "Datos válidos.");
        }

        /**
         * Construye una solicitud después de validar su información.
         * @return solicitud registrada, todavía sin confirmar
         * @throws IllegalArgumentException si falta información esencial
         */
        public SolicitudServicio build() {
            ResultadoOperacion resultado = validar();
            if (!resultado.isExitoso()) {
                throw new IllegalArgumentException(resultado.getMensaje());
            }
            return new SolicitudServicio(this);
        }
    }
}
