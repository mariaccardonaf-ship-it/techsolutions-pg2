package uniquindio.techsolutionspg2.viewcontroller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import uniquindio.techsolutionspg2.model.*;

/**
 * Comparte una instancia del modelo entre las cuatro vistas de la aplicación.
 */
public class PrincipalController {
    @FXML private TabPane pestanas;
    @FXML private ServiciosController serviciosController;
    @FXML private PaquetesController paquetesController;
    @FXML private SolicitudesController solicitudesController;
    @FXML private TecnicosController tecnicosController;

    private TechSolutions sistema;
    private final ObservableList<ServicioContratable> personalizados = FXCollections.observableArrayList();

    /**
     * Inyecta el modelo y conecta los controladores después de cargar el FXML.
     */
    @FXML public void initialize() {
        sistema = new TechSolutions(new LegacySupportAdapter(new LegacySupport()));
        serviciosController.configurar(sistema, personalizados);
        paquetesController.configurar(sistema, personalizados);
        solicitudesController.configurar(sistema, personalizados);
        tecnicosController.configurar(sistema);
        pestanas.getSelectionModel().selectedItemProperty().addListener((obs, anterior, actual) -> {
            serviciosController.actualizar();
            paquetesController.actualizar();
            solicitudesController.actualizar();
        });
    }
}
