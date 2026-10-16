/**
 * Declara el módulo de TechSolutions y sus dependencias de JavaFX.
 * Configura los paquetes exportados y el acceso necesario para la carga FXML.
 */
module uniquindio.techsolutionspg2 {
    requires javafx.controls;
    requires javafx.fxml;




    exports uniquindio.techsolutionspg2.model;
    opens uniquindio.techsolutionspg2.model to javafx.fxml;
}