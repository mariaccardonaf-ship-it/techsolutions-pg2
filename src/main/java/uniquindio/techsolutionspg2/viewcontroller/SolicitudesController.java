package uniquindio.techsolutionspg2.viewcontroller;

import java.util.Collections;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import uniquindio.techsolutionspg2.model.*;

/**
 * Registra solicitudes mediante Builder y solicita su confirmación al modelo.
 */
public class SolicitudesController {
    @FXML private ComboBox<Cliente> clienteSolicitud;
    @FXML private ComboBox<ComponenteTecnologico> elementoSolicitud;
    @FXML private ComboBox<Prioridad> prioridadSolicitud;
    @FXML private DatePicker fechaSolicitud;
    @FXML private TextArea observacionesSolicitud;
    @FXML private ListView<ComponenteTecnologico> carritoSolicitud;
    @FXML private Label subtotalSolicitud;
    @FXML private Label mensajeSolicitudes;
    @FXML private Label detalleSolicitud;
    @FXML private TableView<SolicitudServicio> tablaSolicitudes;
    @FXML private TableColumn<SolicitudServicio, String> codigoSolicitudCol;
    @FXML private TableColumn<SolicitudServicio, String> clienteSolicitudCol;
    @FXML private TableColumn<SolicitudServicio, String> estadoSolicitudCol;
    @FXML private TableColumn<SolicitudServicio, String> totalSolicitudCol;

    private TechSolutions sistema;
    private ObservableList<ServicioContratable> personalizados;
    private final ObservableList<ComponenteTecnologico> carrito = FXCollections.observableArrayList();

    /** Configura las columnas, los selectores y el detalle de solicitudes. */
    @FXML public void initialize() {
        UtilVista.configurarCombo(elementoSolicitud);
        UtilVista.configurarLista(carritoSolicitud);
        carritoSolicitud.setItems(carrito);
        prioridadSolicitud.setItems(FXCollections.observableArrayList(Prioridad.values()));
        clienteSolicitud.setConverter(new StringConverter<Cliente>() {
            /** Presenta la identificación del cliente precargado. */
            @Override public String toString(Cliente c) {
                return c == null ? "" : c.getDocumento() + " · " + c.getNombreCompleto();
            }
            /** El selector no acepta edición de clientes como texto. */
            @Override public Cliente fromString(String texto) { return null; }
        });
        codigoSolicitudCol.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getCodigo()));
        clienteSolicitudCol.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getCliente().getNombreCompleto()));
        estadoSolicitudCol.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getEstado().name()));
        totalSolicitudCol.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                c.getValue().getEstado() == EstadoSolicitud.CONFIRMADA
                ? UtilVista.moneda(c.getValue().getCostoTotal()) : "Por confirmar"));
        tablaSolicitudes.getSelectionModel().selectedItemProperty().addListener((obs, antes, ahora) -> mostrarDetalle());
    }

    /**
     * Recibe las dependencias compartidas de las cuatro vistas.
     * @param sistema modelo de negocio
     * @param personalizados personalizaciones disponibles para contratar
     */
    public void configurar(TechSolutions sistema, ObservableList<ServicioContratable> personalizados) {
        this.sistema = sistema;
        this.personalizados = personalizados;
        actualizar();
        actualizarSubtotal();
    }

    /** Refresca clientes, elementos y solicitudes del modelo. */
    public void actualizar() {
        if (sistema == null) { return; }
        SolicitudServicio seleccionada = tablaSolicitudes.getSelectionModel().getSelectedItem();
        clienteSolicitud.setItems(FXCollections.observableArrayList(sistema.consultarClientes()));
        clienteSolicitud.getSelectionModel().selectFirst();
        ObservableList<ComponenteTecnologico> elementos = FXCollections.observableArrayList();
        elementos.addAll(sistema.consultarServicios());
        elementos.addAll(personalizados);
        elementos.addAll(sistema.consultarPaquetes());
        elementoSolicitud.setItems(elementos);
        elementoSolicitud.getSelectionModel().selectFirst();
        tablaSolicitudes.setItems(FXCollections.observableArrayList(sistema.consultarSolicitudes()));
        if (seleccionada != null) { tablaSolicitudes.getSelectionModel().select(seleccionada); }
        tablaSolicitudes.refresh();
        mostrarDetalle();
    }

    /** Incorpora un elemento al formulario de la nueva solicitud. */
    @FXML public void agregarElemento() {
        if (elementoSolicitud.getValue() == null) {
            UtilVista.mensaje(mensajeSolicitudes, "Selecciona un elemento.", false); return;
        }
        carrito.add(elementoSolicitud.getValue());
        actualizarSubtotal();
    }

    /** Retira un elemento del formulario antes de registrar la solicitud. */
    @FXML public void quitarElemento() {
        int indice = carritoSolicitud.getSelectionModel().getSelectedIndex();
        if (indice >= 0) { carrito.remove(indice); actualizarSubtotal(); }
    }

    /** Construye y registra la solicitud usando los datos obligatorios y opcionales. */
    @FXML public void registrarSolicitud() {
        try {
            SolicitudServicio.Builder builder = new SolicitudServicio.Builder(
                    GeneradorCodigos.getInstancia().siguienteCodigo("SOL"), clienteSolicitud.getValue());
            for (ComponenteTecnologico elemento : carrito) { builder.agregarElemento(elemento); }
            builder.fechaProgramada(fechaSolicitud.getValue())
                    .observaciones(observacionesSolicitud.getText())
                    .prioridad(prioridadSolicitud.getValue());
            SolicitudServicio solicitud = builder.build();
            ResultadoOperacion resultado = sistema.registrarSolicitud(solicitud);
            UtilVista.mensaje(mensajeSolicitudes, resultado.getMensaje(), resultado.isExitoso());
            if (resultado.isExitoso()) {
                carrito.clear(); fechaSolicitud.setValue(null); prioridadSolicitud.setValue(null);
                observacionesSolicitud.clear(); actualizarSubtotal(); actualizar();
                tablaSolicitudes.getSelectionModel().select(solicitud);
            }
        } catch (IllegalArgumentException excepcion) {
            UtilVista.mensaje(mensajeSolicitudes, excepcion.getMessage(), false);
        }
    }

    /** Confirma mediante TechSolutions, que valida técnicos y bloquea los paquetes. */
    @FXML public void confirmarSolicitud() {
        SolicitudServicio solicitud = tablaSolicitudes.getSelectionModel().getSelectedItem();
        if (solicitud == null) {
            UtilVista.mensaje(mensajeSolicitudes, "Selecciona una solicitud registrada.", false); return;
        }
        ResultadoOperacion resultado = sistema.confirmarSolicitud(solicitud.getCodigo());
        UtilVista.mensaje(mensajeSolicitudes, resultado.isExitoso()
                ? "Solicitud confirmada. Total: " + UtilVista.moneda(solicitud.getCostoTotal())
                : resultado.getMensaje(), resultado.isExitoso());
        tablaSolicitudes.refresh();
        mostrarDetalle();
    }

    /** Calcula solo el subtotal de elementos; el desplazamiento se valida al confirmar. */
    private void actualizarSubtotal() {
        double subtotal = new CalculadoraCosto().calcularTotal(carrito, Collections.emptyList());
        subtotalSolicitud.setText("Subtotal sin desplazamiento: " + UtilVista.moneda(subtotal));
    }

    /** Presenta información, opcionales y costos de la solicitud seleccionada. */
    private void mostrarDetalle() {
        SolicitudServicio solicitud = tablaSolicitudes.getSelectionModel().getSelectedItem();
        if (solicitud == null) { detalleSolicitud.setText("Selecciona una solicitud para consultar su detalle."); return; }
        String detalle = "Creación: " + solicitud.getFechaCreacion()
                + " · Atención: " + (solicitud.getFechaProgramada() == null ? "Sin programar" : solicitud.getFechaProgramada())
                + " · Prioridad: " + (solicitud.getPrioridad() == null ? "Sin especificar" : solicitud.getPrioridad())
                + "\nObservaciones: " + (solicitud.getObservaciones() == null ? "" : solicitud.getObservaciones());
        for (ComponenteTecnologico elemento : solicitud.getElementos()) { detalle += "\n• " + UtilVista.etiqueta(elemento); }
        double subtotal = new CalculadoraCosto().calcularTotal(solicitud.getElementos(), Collections.emptyList());
        detalle += "\nSubtotal de elementos: " + UtilVista.moneda(subtotal);
        if (solicitud.getEstado() == EstadoSolicitud.CONFIRMADA) {
            detalle += " · Desplazamiento: " + UtilVista.moneda(solicitud.getCostoTotal() - subtotal)
                    + " · Total: " + UtilVista.moneda(solicitud.getCostoTotal());
        } else { detalle += " · Total definitivo pendiente de confirmación."; }
        detalleSolicitud.setText(detalle);
    }
}
