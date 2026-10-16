package uniquindio.techsolutionspg2.viewcontroller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import uniquindio.techsolutionspg2.model.*;

/**
 * Consulta la disponibilidad técnica mediante el contrato del proveedor del modelo.
 */
public class TecnicosController {
    @FXML private ComboBox<Especialidad> especialidadTecnico;
    @FXML private Label resultadoTecnico;
    @FXML private Label mensajeTecnicos;
    private TechSolutions sistema;

    /** Carga las especialidades de los servicios disponibles. */
    @FXML public void initialize() {
        especialidadTecnico.setItems(FXCollections.observableArrayList(Especialidad.values()));
        especialidadTecnico.getSelectionModel().selectFirst();
    }

    /**
     * Asigna el modelo compartido.
     * @param sistema coordinador del negocio
     */
    public void configurar(TechSolutions sistema) { this.sistema = sistema; }

    /** Consulta el adaptador y presenta identificador, disponibilidad y desplazamiento. */
    @FXML public void consultarTecnico() {
        try {
            DisponibilidadTecnica resultado = sistema.consultarTecnico(especialidadTecnico.getValue());
            resultadoTecnico.setText("Especialidad: " + resultado.getEspecialidad()
                    + "\nDisponibilidad: " + (resultado.isDisponible() ? "Disponible" : "Sin técnico disponible")
                    + "\nTécnico: " + (resultado.getIdTecnico() == null ? "No asignado" : resultado.getIdTecnico())
                    + "\nDesplazamiento: " + UtilVista.moneda(resultado.getCostoDesplazamiento()));
            UtilVista.mensaje(mensajeTecnicos, resultado.isDisponible()
                    ? "Consulta realizada." : "No se podrá confirmar la atención presencial para esta especialidad.", resultado.isDisponible());
        } catch (IllegalArgumentException excepcion) {
            UtilVista.mensaje(mensajeTecnicos, excepcion.getMessage(), false);
        }
    }
}
