package uniquindio.techsolutionspg2.viewcontroller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import uniquindio.techsolutionspg2.model.*;

/**
 * Gestiona las acciones de catálogo y personalización sin alterar el servicio base.
 */
public class ServiciosController {
    @FXML private TableView<Servicio> tablaServicios;
    @FXML private TableColumn<Servicio, String> codigoCol;
    @FXML private TableColumn<Servicio, String> nombreCol;
    @FXML private TableColumn<Servicio, String> costoCol;
    @FXML private ComboBox<String> tipoServicio;
    @FXML private TextField nombreServicio;
    @FXML private TextField descripcionServicio;
    @FXML private CheckBox prioridadComplemento;
    @FXML private CheckBox soporteComplemento;
    @FXML private CheckBox domicilioComplemento;
    @FXML private Label desgloseServicio;
    @FXML private Label mensajeServicios;
    @FXML private ListView<ServicioContratable> listaPersonalizados;

    private TechSolutions sistema;
    private ObservableList<ServicioContratable> personalizados;

    /** Prepara las columnas y las opciones de la vista del catálogo. */
    @FXML public void initialize() {
        codigoCol.setCellValueFactory(celda -> new ReadOnlyStringWrapper(celda.getValue().getCodigo()));
        nombreCol.setCellValueFactory(celda -> new ReadOnlyStringWrapper(celda.getValue().getNombre()));
        costoCol.setCellValueFactory(celda -> new ReadOnlyStringWrapper(UtilVista.moneda(celda.getValue().getCosto())));
        tipoServicio.setItems(FXCollections.observableArrayList("Preventivo", "Correctivo", "Redes", "Recuperación"));
        tipoServicio.getSelectionModel().selectFirst();
        tablaServicios.getSelectionModel().selectedItemProperty().addListener((obs, antes, ahora) -> mostrarDesglose());
        UtilVista.configurarLista(listaPersonalizados);
    }

    /**
     * Comparte el modelo y las personalizaciones con las otras vistas.
     * @param sistema modelo de la aplicación
     * @param personalizados servicios personalizados durante la ejecución
     */
    public void configurar(TechSolutions sistema, ObservableList<ServicioContratable> personalizados) {
        this.sistema = sistema;
        this.personalizados = personalizados;
        listaPersonalizados.setItems(personalizados);
        actualizar();
    }

    /** Refresca el catálogo conservando la selección cuando sea posible. */
    public void actualizar() {
        if (sistema == null) { return; }
        Servicio seleccionado = tablaServicios.getSelectionModel().getSelectedItem();
        tablaServicios.setItems(FXCollections.observableArrayList(sistema.consultarServicios()));
        if (seleccionado == null) { tablaServicios.getSelectionModel().selectFirst(); }
        else { tablaServicios.getSelectionModel().select(seleccionado); }
        mostrarDesglose();
    }

    /** Crea un servicio delegando su construcción a la fábrica correspondiente. */
    @FXML public void crearServicio() {
        try {
            ServicioFactory fabrica;
            switch (tipoServicio.getValue()) {
                case "Preventivo": fabrica = new MantenimientoPreventivoFactory(); break;
                case "Correctivo": fabrica = new MantenimientoCorrectivoFactory(); break;
                case "Redes": fabrica = new InstalacionRedesFactory(); break;
                default: fabrica = new RecuperacionInformacionFactory(); break;
            }
            Servicio servicio = sistema.registrarServicio(fabrica, nombreServicio.getText(), descripcionServicio.getText());
            actualizar();
            tablaServicios.getSelectionModel().select(servicio);
            nombreServicio.clear();
            descripcionServicio.clear();
            UtilVista.mensaje(mensajeServicios, "Servicio creado: " + servicio.getCodigo(), true);
        } catch (IllegalArgumentException excepcion) {
            UtilVista.mensaje(mensajeServicios, excepcion.getMessage(), false);
        }
    }

    /** Construye los decoradores seleccionados y conserva la configuración para contratarla. */
    @FXML public void personalizarServicio() {
        Servicio base = tablaServicios.getSelectionModel().getSelectedItem();
        if (base == null || (!prioridadComplemento.isSelected() && !soporteComplemento.isSelected()
                && !domicilioComplemento.isSelected())) {
            UtilVista.mensaje(mensajeServicios, "Selecciona un servicio y al menos un complemento.", false);
            return;
        }
        ServicioContratable configurado = base;
        if (prioridadComplemento.isSelected()) { configurado = new AtencionPrioritaria(configurado); }
        if (soporteComplemento.isSelected()) { configurado = new SoporteTecnico30Dias(configurado); }
        if (domicilioComplemento.isSelected()) { configurado = new AtencionDomicilio(configurado); }
        personalizados.add(configurado);
        listaPersonalizados.getSelectionModel().select(configurado);
        mostrarDesglose();
        UtilVista.mensaje(mensajeServicios, "Personalización guardada. El servicio del catálogo conserva su tarifa.", true);
    }

    /** Muestra el precio base y cada incremento antes de guardar la personalización. */
    @FXML public void mostrarDesglose() {
        Servicio base = tablaServicios.getSelectionModel().getSelectedItem();
        if (base == null) { desgloseServicio.setText("Selecciona un servicio del catálogo."); return; }
        String detalle = "Base: " + UtilVista.moneda(base.getCosto());
        ServicioContratable configurado = base;
        if (prioridadComplemento.isSelected()) {
            configurado = new AtencionPrioritaria(configurado); detalle += "\nAtención prioritaria: +$35.000";
        }
        if (soporteComplemento.isSelected()) {
            configurado = new SoporteTecnico30Dias(configurado); detalle += "\nSoporte 30 días: +$50.000";
        }
        if (domicilioComplemento.isSelected()) {
            configurado = new AtencionDomicilio(configurado); detalle += "\nAtención a domicilio: +$25.000";
        }
        desgloseServicio.setText(detalle + "\nTotal del servicio: " + UtilVista.moneda(configurado.getCosto())
                + "\nEl desplazamiento se consulta al confirmar.");
    }
}
