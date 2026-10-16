package uniquindio.techsolutionspg2.viewcontroller;

import java.text.NumberFormat;
import java.util.Locale;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.StringConverter;
import uniquindio.techsolutionspg2.model.*;

/**
 * Comparte únicamente operaciones de presentación y formato entre controladores.
 */
public final class UtilVista {
    private UtilVista() { }

    /**
     * Formatea una cantidad monetaria en pesos colombianos.
     * @param valor cantidad que se mostrará
     * @return texto monetario sin decimales
     */
    public static String moneda(double valor) {
        NumberFormat formato = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO"));
        formato.setMaximumFractionDigits(0);
        return formato.format(valor);
    }

    /**
     * Describe brevemente un componente en los selectores.
     * @param componente elemento del modelo
     * @return texto visible
     */
    public static String etiqueta(ComponenteTecnologico componente) {
        if (componente == null) { return ""; }
        String nombre = componente.getDescripcion();
        if (componente instanceof Servicio) {
            Servicio servicio = (Servicio) componente;
            nombre = servicio.getCodigo() + " · " + servicio.getNombre();
        } else if (componente instanceof PaqueteTecnologico) {
            PaqueteTecnologico paquete = (PaqueteTecnologico) componente;
            nombre = paquete.getCodigo() + " · " + paquete.getNombre();
        }
        return nombre + " — " + moneda(componente.getCosto());
    }

    /**
     * Muestra un mensaje de éxito o error sin interrumpir el formulario.
     * @param label lugar donde se presenta el resultado
     * @param texto mensaje de la operación
     * @param exitoso determina el estilo del mensaje
     */
    public static void mensaje(Label label, String texto, boolean exitoso) {
        label.setText(texto);
        label.getStyleClass().removeAll("mensaje-error", "mensaje-ok");
        label.getStyleClass().add(exitoso ? "mensaje-ok" : "mensaje-error");
    }

    /**
     * Configura la presentación de componentes en un selector.
     * @param combo selector de componentes
     * @param <T> tipo de componente mostrado
     */
    public static <T extends ComponenteTecnologico> void configurarCombo(ComboBox<T> combo) {
        combo.setConverter(new StringConverter<T>() {
            /** Convierte el componente seleccionado en texto. */
            @Override public String toString(T componente) { return etiqueta(componente); }
            /** El selector no permite introducir componentes como texto libre. */
            @Override public T fromString(String texto) { return null; }
        });
    }

    /**
     * Configura celdas de varias líneas para mostrar los elementos contratados.
     * @param lista listado de componentes
     * @param <T> tipo de componente
     */
    public static <T extends ComponenteTecnologico> void configurarLista(ListView<T> lista) {
        lista.setCellFactory(vista -> new ListCell<T>() {
            /** Actualiza la representación de una fila sin modificar el modelo. */
            @Override protected void updateItem(T item, boolean vacio) {
                super.updateItem(item, vacio);
                setText(vacio || item == null ? null : etiqueta(item));
                setWrapText(true);
                setPrefWidth(0);
            }
        });
    }
}
