package uniquindio.techsolutionspg2.viewcontroller;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.zip.CRC32;
import java.util.zip.DeflaterOutputStream;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import uniquindio.techsolutionspg2.model.*;

/**
 * Carga las vistas FXML y verifica sus flujos reales sobre el hilo de JavaFX.
 * Las capturas de verificación se guardan en target/capturas durante las pruebas.
 */
public class InterfazTest {
    @BeforeAll public static void iniciarJavaFX() throws Exception {
        CountDownLatch listo = new CountDownLatch(1);
        Platform.startup(() -> {Platform.setImplicitExit(false);listo.countDown();});
        assertTrue(listo.await(15,TimeUnit.SECONDS));
    }

    @AfterAll public static void terminarJavaFX() { Platform.exit(); }

    @Test public void flujoServiciosPaquetesYSolicitudConfirmada() throws Exception {
        enJavaFX(() -> {
            try {
                Parent root = new FXMLLoader(App.class.getResource("/uniquindio/techsolutionspg2/view/principal.fxml")).load();
                Stage stage = new Stage();stage.setScene(new Scene(root,1180,780));
                stage.getScene().getStylesheets().add(App.class.getResource("/uniquindio/techsolutionspg2/view/estilos.css").toExternalForm());
                stage.show();root.applyCss();root.layout();
                try {
                    TableView<Servicio> catalogo = nodo(root,"tablaServicios");
                    assertEquals(4,catalogo.getItems().size());
                    ((TextField)nodo(root,"nombreServicio")).setText("Servicio de prueba");
                    ((TextField)nodo(root,"descripcionServicio")).setText("Creado desde JavaFX");
                    ((Button)nodo(root,"crearServicio")).fire();
                    assertEquals(5,catalogo.getItems().size());
                    catalogo.getSelectionModel().select(1);
                    ((CheckBox)nodo(root,"prioridadComplemento")).setSelected(true);
                    ((CheckBox)nodo(root,"soporteComplemento")).setSelected(true);
                    ((CheckBox)nodo(root,"domicilioComplemento")).setSelected(true);
                    ((Button)nodo(root,"personalizarServicio")).fire();
                    ListView<ServicioContratable> personalizados = nodo(root,"listaPersonalizados");
                    assertEquals(1,personalizados.getItems().size());
                    assertEquals(230000,personalizados.getItems().get(0).getCosto());
                    assertTrue(((Label)nodo(root,"desgloseServicio")).getText().contains("230.000"));
                    capturar(root,"01-servicios.png");
                    TabPane tabs = nodo(root,"pestanas");tabs.getSelectionModel().select(1);root.applyCss();root.layout();
                    ((TextField)nodo(root,"nombrePaquete")).setText("Paquete de prueba");
                    ((Button)nodo(root,"crearPaquete")).fire();
                    ComboBox<ComponenteTecnologico> componentes = nodo(root,"componentePaquete");
                    componentes.getSelectionModel().select(5);
                    ((Button)nodo(root,"agregarComponente")).fire();
                    ComboBox<PaqueteTecnologico> paquetes = nodo(root,"paqueteSeleccionado");
                    PaqueteTecnologico paquete = paquetes.getValue();assertEquals(230000,paquete.getCosto());
                    componentes.setValue(paquete);((Button)nodo(root,"agregarComponente")).fire();
                    assertTrue(((Label)nodo(root,"mensajePaquetes")).getText().contains("ciclo"));
                    capturar(root,"02-paquetes.png");
                    tabs.getSelectionModel().select(2);root.applyCss();root.layout();
                    ((Button)nodo(root,"registrarSolicitud")).fire();
                    assertFalse(((Label)nodo(root,"mensajeSolicitudes")).getText().isEmpty());
                    ComboBox<ComponenteTecnologico> elementos = nodo(root,"elementoSolicitud");elementos.setValue(paquete);
                    ((Button)nodo(root,"agregarElemento")).fire();
                    ((Button)nodo(root,"registrarSolicitud")).fire();
                    TableView<SolicitudServicio> solicitudes = nodo(root,"tablaSolicitudes");assertEquals(1,solicitudes.getItems().size());
                    ((Button)nodo(root,"confirmarSolicitud")).fire();
                    assertEquals(EstadoSolicitud.CONFIRMADA,solicitudes.getItems().get(0).getEstado());
                    assertEquals(260000,solicitudes.getItems().get(0).getCostoTotal());
                    assertTrue(paquete.isBloqueado());
                    capturar(root,"03-solicitudes.png");
                    tabs.getSelectionModel().select(3);root.applyCss();root.layout();
                    ComboBox<Especialidad> especialidad = nodo(root,"especialidadTecnico");especialidad.setValue(Especialidad.CORRECTIVO);
                    ((Button)nodo(root,"consultarTecnico")).fire();
                    assertTrue(((Label)nodo(root,"resultadoTecnico")).getText().contains("TEC-102"));
                    capturar(root,"04-tecnicos.png");
                } finally { stage.close(); }
            } catch (Exception e) { throw new RuntimeException(e); }
        });
    }

    @Test public void interfazRechazaConfirmacionSinTecnico() throws Exception {
        enJavaFX(() -> {
            try {
                Parent root = new FXMLLoader(App.class.getResource("/uniquindio/techsolutionspg2/view/principal.fxml")).load();
                new Scene(root,1180,780);root.applyCss();root.layout();
                TableView<Servicio> catalogo = nodo(root,"tablaServicios");catalogo.getSelectionModel().select(3);
                ((CheckBox)nodo(root,"domicilioComplemento")).setSelected(true);
                ((Button)nodo(root,"personalizarServicio")).fire();
                TabPane tabs = nodo(root,"pestanas");tabs.getSelectionModel().select(2);root.applyCss();root.layout();
                ComboBox<ComponenteTecnologico> elementos = nodo(root,"elementoSolicitud");elementos.getSelectionModel().select(4);
                ((Button)nodo(root,"agregarElemento")).fire();((Button)nodo(root,"registrarSolicitud")).fire();
                ((Button)nodo(root,"confirmarSolicitud")).fire();
                TableView<SolicitudServicio> tabla = nodo(root,"tablaSolicitudes");
                assertEquals(EstadoSolicitud.REGISTRADA,tabla.getItems().get(0).getEstado());
                assertTrue(((Label)nodo(root,"mensajeSolicitudes")).getText().contains("No hay técnico"));
            } catch (Exception e) { throw new RuntimeException(e); }
        });
    }

    private static void enJavaFX(Runnable action) throws Exception {
        FutureTask<Void> task = new FutureTask<>(() -> {action.run();return null;});
        Platform.runLater(task);task.get(40,TimeUnit.SECONDS);
    }

    @SuppressWarnings("unchecked")
    private static <T> T nodo(Parent root,String id) {
        Object node = root.lookup("#"+id);assertNotNull(node,"No se encontró #"+id);return (T)node;
    }

    private static void capturar(Parent root,String nombre) throws Exception {
        root.applyCss();root.layout();WritableImage image = root.snapshot(null,null);
        int width=(int)image.getWidth(),height=(int)image.getHeight();
        ByteArrayOutputStream raw = new ByteArrayOutputStream();
        for(int y=0;y<height;y++) {
            raw.write(0);
            for(int x=0;x<width;x++) {
                int argb=image.getPixelReader().getArgb(x,y);
                raw.write((argb>>>16)&255);raw.write((argb>>>8)&255);raw.write(argb&255);raw.write((argb>>>24)&255);
            }
        }
        ByteArrayOutputStream compressed=new ByteArrayOutputStream();
        try(DeflaterOutputStream def=new DeflaterOutputStream(compressed)){def.write(raw.toByteArray());}
        Path target=Path.of("target","capturas",nombre);Files.createDirectories(target.getParent());
        try(DataOutputStream out=new DataOutputStream(Files.newOutputStream(target))) {
            out.write(new byte[]{(byte)137,80,78,71,13,10,26,10});
            ByteArrayOutputStream hdr=new ByteArrayOutputStream();
            try(DataOutputStream h=new DataOutputStream(hdr)){h.writeInt(width);h.writeInt(height);h.write(new byte[]{8,6,0,0,0});}
            chunk(out,"IHDR",hdr.toByteArray());chunk(out,"IDAT",compressed.toByteArray());chunk(out,"IEND",new byte[0]);
        }
    }

    private static void chunk(DataOutputStream out,String name,byte[] data) throws Exception {
        byte[] type=name.getBytes(java.nio.charset.StandardCharsets.US_ASCII);
        CRC32 crc=new CRC32();crc.update(type);crc.update(data);
        out.writeInt(data.length);out.write(type);out.write(data);out.writeInt((int)crc.getValue());
    }
}
