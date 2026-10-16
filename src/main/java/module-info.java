/**
 * Declara las dependencias JavaFX y los paquetes del modelo y la presentación.
 */
module uniquindio.techsolutionspg2 {
    requires javafx.controls;
    requires javafx.fxml;

    exports uniquindio.techsolutionspg2.model;
    exports uniquindio.techsolutionspg2.viewcontroller;
    opens uniquindio.techsolutionspg2.viewcontroller to javafx.fxml;
}
