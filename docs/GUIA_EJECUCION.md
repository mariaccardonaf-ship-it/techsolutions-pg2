# TechSolutions

Aplicación JavaFX con modelo orientado a objetos y organización MVC.

## Ejecutar desde IntelliJ

1. Abrir `pom.xml` como proyecto Maven y recargar las dependencias.
2. Seleccionar JDK 25, que es la versión configurada en el proyecto.
3. En la ventana Maven ejecutar `Plugins > javafx > javafx:run`.

La clase de inicio es `uniquindio.techsolutionspg2.viewcontroller.App`.
No se necesita crear otro proyecto ni copiar las clases a otra carpeta.

Desde PowerShell, en la raíz del proyecto:

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd javafx:run
```

## Organización

- `model`: entidades, interfaces, enumeraciones, patrones y reglas del negocio.
- `viewcontroller`: `App`, controlador principal, cuatro controladores funcionales y utilidades de presentación.
- `resources/uniquindio/techsolutionspg2/view`: las cinco vistas FXML y el estilo CSS.
- `test/java`: 22 pruebas del modelo y 2 de integración de la interfaz.
- `docs`: diagramas editables y esta guía.

## Recorrido de demostración

1. Servicios: seleccionar Correctivo y activar prioridad, soporte y domicilio.
   El desglose muestra $120.000 + $35.000 + $50.000 + $25.000 = $230.000.
   Guardar la personalización; la tarifa original sigue en $120.000.
2. Paquetes: crear un paquete y agregar la personalización. Crear otro paquete
   y agregar el primero para mostrar la agrupación. Un ciclo directo o indirecto se rechaza.
3. Solicitudes: seleccionar el cliente precargado, agregar el paquete y los datos
   opcionales. Registrar, seleccionar la solicitud y confirmar. Para Correctivo
   a domicilio, el desplazamiento simulado es $30.000 y el total es $260.000.
4. Técnicos: consultar cada especialidad. Recuperación no tiene técnico en la
   simulación; una solicitud con Recuperación a domicilio no se confirma.
5. Volver al paquete confirmado: no debe permitir añadir componentes.

## Reglas y alcance

Los datos viven en memoria y se reinician al cerrar la aplicación. No se incluye
autenticación, persistencia ni CRUD completo de clientes.

`LegacySupport` es una simulación de una API externa. Los códigos 1 a 4, los
identificadores y los desplazamientos son datos del ejercicio, no datos de un
proveedor real. La integración utiliza `ProveedorTecnicos` y `LegacySupportAdapter`.

La atención presencial se solicita mediante el complemento de domicilio.
Se consulta una vez y se cobra como máximo un desplazamiento por especialidad
presencial. Los $25.000 de domicilio no incluyen ese desplazamiento.

`Prioridad.ALTA` es un dato opcional de la solicitud y no añade automáticamente
el complemento de atención prioritaria.

Tras confirmar se bloquean los paquetes anidados y se conserva el costo total.
Las listas de especialidades son `List`, sin repetidos mediante `contains()`.

## Pruebas

`ModeloTest`: fábricas y tarifas, decoradores, paquetes, ciclos, Builder, fechas,
listas protegidas, adaptación de respuestas, costos, registro, disponibilidad,
bloqueo de paquetes y Singleton.

`InterfazTest`: carga real de los FXML y flujos de creación, personalización,
paquetes, solicitudes, confirmación y rechazo por falta de técnico. Abre brevemente
una ventana JavaFX y genera capturas en `target/capturas`, carpeta excluida de Git.
Necesita una sesión gráfica disponible.

## Entrega

Falta grabar y publicar la sustentación, completar el enlace del video en el Word
y verificar la fecha límite y las condiciones de entrega establecidas por el docente.
