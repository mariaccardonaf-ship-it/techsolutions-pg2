package uniquindio.techsolutionspg2.viewcontroller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Inicia la aplicación JavaFX y carga la vista principal de TechSolutions.
 */
public class App extends Application {

    /**
     * Construye la ventana principal a partir del archivo FXML.
     * @param stage ventana proporcionada por JavaFX
     * @throws Exception si no puede cargarse la vista
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(
                "/uniquindio/techsolutionspg2/view/principal.fxml"));
        Scene scene = new Scene(loader.load(), 1180, 780);
        scene.getStylesheets().add(App.class.getResource(
                "/uniquindio/techsolutionspg2/view/estilos.css").toExternalForm());
        stage.setTitle("TechSolutions · Gestión de servicios");
        stage.setMinWidth(1000);
        stage.setMinHeight(680);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Ejecuta el ciclo de vida de JavaFX.
     * @param args argumentos de ejecución
     */
    public static void main(String[] args) {
        launch(args);
    }
}
