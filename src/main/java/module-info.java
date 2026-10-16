module uniquindio.techsolutionspg2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens uniquindio.techsolutionspg2 to javafx.fxml;
    exports uniquindio.techsolutionspg2;
    exports uniquindio.techsolutionspg2.model;
    opens uniquindio.techsolutionspg2.model to javafx.fxml;
}