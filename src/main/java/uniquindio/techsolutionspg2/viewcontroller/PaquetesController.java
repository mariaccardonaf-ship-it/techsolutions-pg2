package uniquindio.techsolutionspg2.viewcontroller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import uniquindio.techsolutionspg2.model.*;

/**
 * Permite configurar paquetes anidados y consultar su estructura y costo.
 */
public class PaquetesController {
    @FXML private TextField nombrePaquete;
    @FXML private ComboBox<PaqueteTecnologico> paqueteSeleccionado;
    @FXML private ComboBox<ComponenteTecnologico> componentePaquete;
    @FXML private TreeView<String> arbolPaquete;
    @FXML private Label costoPaquete;
    @FXML private Label mensajePaquetes;
    private TechSolutions sistema;
    private ObservableList<ServicioContratable> personalizados;

    /** Prepara los selectores y el árbol de componentes. */
    @FXML public void initialize() {
        UtilVista.configurarCombo(paqueteSeleccionado);
        UtilVista.configurarCombo(componentePaquete);
        paqueteSeleccionado.valueProperty().addListener((obs, antes, ahora) -> mostrarPaquete());
    }

    /**
     * Conecta el controlador con el modelo compartido.
     * @param sistema modelo de negocio
     * @param personalizados servicios personalizados disponibles
     */
    public void configurar(TechSolutions sistema, ObservableList<ServicioContratable> personalizados) {
        this.sistema = sistema;
        this.personalizados = personalizados;
        actualizar();
    }

    /** Actualiza los paquetes y elementos disponibles sin perder la selección. */
    public void actualizar() {
        if (sistema == null) { return; }
        PaqueteTecnologico seleccionado = paqueteSeleccionado.getValue();
        paqueteSeleccionado.setItems(FXCollections.observableArrayList(sistema.consultarPaquetes()));
        if (seleccionado != null) { paqueteSeleccionado.setValue(seleccionado); }
        else { paqueteSeleccionado.getSelectionModel().selectFirst(); }
        ObservableList<ComponenteTecnologico> elementos = FXCollections.observableArrayList();
        elementos.addAll(sistema.consultarServicios());
        elementos.addAll(personalizados);
        elementos.addAll(sistema.consultarPaquetes());
        componentePaquete.setItems(elementos);
        componentePaquete.getSelectionModel().selectFirst();
        mostrarPaquete();
    }

    /** Crea un paquete vacío que puede configurarse antes de su contratación. */
    @FXML public void crearPaquete() {
        try {
            PaqueteTecnologico paquete = sistema.crearPaquete(nombrePaquete.getText());
            actualizar();
            paqueteSeleccionado.setValue(paquete);
            nombrePaquete.clear();
            UtilVista.mensaje(mensajePaquetes, "Paquete creado. Agrega sus componentes.", true);
        } catch (IllegalArgumentException excepcion) {
            UtilVista.mensaje(mensajePaquetes, excepcion.getMessage(), false);
        }
    }

    /** Delega al paquete la validación de ciclos y del bloqueo de sus componentes. */
    @FXML public void agregarComponente() {
        PaqueteTecnologico paquete = paqueteSeleccionado.getValue();
        if (paquete == null || componentePaquete.getValue() == null) {
            UtilVista.mensaje(mensajePaquetes, "Selecciona un paquete y un componente.", false);
            return;
        }
        ResultadoOperacion resultado = paquete.agregarComponente(componentePaquete.getValue());
        UtilVista.mensaje(mensajePaquetes, resultado.getMensaje(), resultado.isExitoso());
        mostrarPaquete();
    }

    /** Presenta los niveles de agrupación y el precio calculado por el modelo. */
    public void mostrarPaquete() {
        PaqueteTecnologico paquete = paqueteSeleccionado.getValue();
        if (paquete == null) {
            arbolPaquete.setRoot(null);
            costoPaquete.setText("Crea o selecciona un paquete.");
            return;
        }
        arbolPaquete.setRoot(crearNodo(paquete));
        costoPaquete.setText("Total: " + UtilVista.moneda(paquete.getCosto())
                + (paquete.isBloqueado() ? " · Contratado: no admite cambios" : " · En configuración"));
    }

    /**
     * Construye una representación visual de los componentes recursivos.
     * @param componente elemento del árbol de negocio
     * @return nodo visual con sus descendientes
     */
    private TreeItem<String> crearNodo(ComponenteTecnologico componente) {
        TreeItem<String> nodo = new TreeItem<>(UtilVista.etiqueta(componente));
        nodo.setExpanded(true);
        if (componente instanceof PaqueteTecnologico) {
            for (ComponenteTecnologico hijo : ((PaqueteTecnologico) componente).getComponentes()) {
                nodo.getChildren().add(crearNodo(hijo));
            }
        }
        return nodo;
    }
}
