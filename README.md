# Explora2 — Sistema de Apoyo Didáctico Digital

**Caso institucional:** Escuela Escritora Marcela Paz (MPAZ PRO), segundo básico.  
**Asignatura:** Desarrollo de Aplicaciones Móviles (DSY1105), Duoc UC.  
**Entrega:** Evaluación Parcial 4.

## Integrantes

* Javier Cruz
* Kevis Gonzalez

## Descripción

Explora2 es un MVP educativo nativo para Android que apoya la creación, autorización y asignación de actividades pedagógicas, así como la participación de estudiantes de segundo básico. Utiliza datos ficticios para las demostraciones académicas.

## Funcionalidades

|Perfil|Funciones|
|-|-|
|Docente|Crear, consultar, modificar y eliminar planificaciones según su estado; enviar solicitudes a UTP; asignar actividades autorizadas; revisar respuestas; configurar intentos, fechas y versión de la evaluación.|
|UTP|Consultar solicitudes y autorizar planificaciones pedagógicas.|
|Alumno|Consultar actividades asignadas, entregar respuestas y rendir una evaluación con control de disponibilidad e intentos.|

La aplicación incorpora navegación por perfiles, síntesis de voz nativa, consumo visual de PokeAPI y persistencia local de preferencias, intentos y último puntaje mediante DataStore. Las planificaciones y la configuración del examen se almacenan en el servidor.

## Arquitectura y tecnologías

* Android: Kotlin, Jetpack Compose, Material 3, MVVM, StateFlow y corrutinas.
* Comunicación: Retrofit y Gson; Coil para imágenes.
* Backend: Java 21, Spring Boot, Spring Data JPA, Hibernate, MySQL/MariaDB mediante XAMPP.
* Pruebas: JUnit, Kotest, MockK, kotlinx-coroutines-test y JaCoCo.
* Control de versiones: Git y GitHub; planificación colaborativa: Trello.

```text
Pantallas Compose -> ViewModel -> Repository -> Retrofit
                                              |-> Spring Boot -> MySQL/MariaDB
                                              |-> PokeAPI (HTTPS)
                               -> DataStore (persistencia local)
```

Paquetes principales bajo `app/src/main/java/com/example/explora2/`:

|Paquete|Responsabilidad|
|-|-|
|`ui/`|Pantallas, componentes y recursos de interfaz.|
|`navigation/`|Rutas y navegación entre pantallas.|
|`viewmodel/`, `viewModel/`|Estado y comportamiento de las vistas.|
|`repository/`|Acceso a servicios remotos y persistencia local.|
|`model/`|Estados de interfaz y modelos locales.|
|`data/model/`|DTO y modelos de respuestas REST.|
|`data/remote/`|Interfaces y clientes Retrofit.|

## Repositorios

* [Aplicación Android](https://github.com/arturojavier04/AppsMoviles)
* [Backend Spring Boot](https://github.com/kevishowardgonzalez/Explora2-backend)

## API propia

Base desde el computador: `http://localhost:8089/`.
Base desde el emulador Android: `http://10.0.2.2:8089/`.
Swagger: `http://localhost:8089/swagger-ui/index.html`.

|Método|Endpoint|Operación|Respuesta esperada|
|-|-|-|-|
|GET|`/api/planificaciones`|Listar planificaciones|200|
|GET|`/api/planificaciones/{id}`|Consultar una planificación|200|
|POST|`/api/planificaciones`|Crear planificación|201|
|PUT|`/api/planificaciones/{id}`|Actualizar planificación, autorización, asignación o respuesta|200|
|DELETE|`/api/planificaciones/{id}`|Eliminar planificación|204|
|GET|`/api/configuracion-examen`|Consultar configuración global|200|
|PUT|`/api/configuracion-examen`|Crear o actualizar configuración global|200|

Las operaciones POST y PUT utilizan JSON. Los PUT de planificación envían el objeto completo. La configuración contiene los campos `intentosPermitidos`, `fechaInicio`, `fechaFin` y `version`; las fechas se expresan como `dd/MM/yyyy`.

## API externa: PokeAPI

* Base: `https://pokeapi.co/api/v2/`.
* Endpoint: `GET /pokemon/{id}` (relativo a la base).
* Ejemplo: `https://pokeapi.co/api/v2/pokemon/25`.
* La aplicación consulta mediante Retrofit datos de Pokémon y muestra imágenes y nombres en su interfaz, sin modificar los registros pedagógicos del backend.

## Pasos para ejecutar (Windows)

### 1. Backend

Requisitos: JDK 21, XAMPP con MySQL/MariaDB activo en el puerto 3306, conexión a internet para descargar dependencias cuando corresponda.

```powershell
git clone https://github.com/kevishowardgonzalez/Explora2-backend.git
cd Explora2-backend
java -version
.\gradlew.bat bootRun
```

La base `explora2_db` se crea si no existe y las tablas se actualizan mediante Hibernate según la configuración del servicio. Revisar `application.properties` y usar credenciales locales adecuadas; pueden suministrarse mediante las variables `SPRING_DATASOURCE_USERNAME` y `SPRING_DATASOURCE_PASSWORD`.

Mantener el backend ejecutándose y, en otra terminal, comprobar:

```powershell
curl.exe -i http://localhost:8089/api/planificaciones
curl.exe -i http://localhost:8089/api/configuracion-examen
```

Ambas consultas deben devolver HTTP 200. Una colección vacía puede devolver `[]`.

### 2. Aplicación Android

Requisitos: Android Studio, JDK 17 para Gradle de Android, SDK 36 y emulador Android (API 24 o superior).

```powershell
git clone https://github.com/arturojavier04/AppsMoviles.git
cd AppsMoviles
.\gradlew.bat :app:assembleDebug
```

Abrir el proyecto en Android Studio, sincronizar Gradle, iniciar un emulador y ejecutar la configuración `app`. Mantener XAMPP y Spring Boot activos. La dirección `10.0.2.2` corresponde al equipo anfitrión del emulador; no es una dirección de producción ni sirve directamente para un teléfono físico.

### 3. APK firmado

Instalar el APK de entrega en el emulador con Android Studio o ADB:

```powershell
$env:Path += ";$env:LOCALAPPDATA\Android\Sdk\platform-tools"
adb devices
adb install -r app\release\app-release.apk
adb shell am start -n com.example.appmpazpro2/com.example.explora2.MainActivity
```

Ajustar la ruta al APK si se descargó en otra carpeta. Si existe una instalación firmada con otra clave, utilizar un emulador de prueba distinto o desinstalar la versión anterior, teniendo presente que esto borra los datos locales.

## Pruebas unitarias y cobertura

```powershell
.\gradlew.bat :app:testDebugUnitTest
.\gradlew.bat :app:createDebugUnitTestCoverageReport
Start-Process app\build\reports\tests\testDebugUnitTest\index.html
Start-Process app\build\reports\coverage\test\debug\index.html
```

Se probaron validaciones, ViewModels, acceso a repositorios, respuestas y errores REST simulados, PokeAPI y persistencia temporal DataStore (preferencias, intentos, notas, límites y migración de progreso).

Última medición de JaCoCo:

|Paquete|Instrucciones cubiertas|Líneas cubiertas / totales|
|-|-:|-:|
|`viewmodel`|83%|574/700|
|`viewModel`|0%|0/25|
|`repository`|80%|125/137|
|`model`|97%|81/81|
|`data.model`|78%|31/38|

**Cobertura de líneas combinada de `viewmodel`, `viewModel` y `repository`: 699/862 = 81,1%.** El cálculo pondera las líneas de los tres paquetes, no promedia los porcentajes. Este alcance representa los módulos seleccionados para la medición lógica; no acredita la totalidad de la lógica fuera de ellos. La cobertura global de instrucciones informada por JaCoCo es 6%, e incluye las pantallas Compose y otros componentes no cubiertos por estas pruebas unitarias.

## Firma y validación de release

El APK release fue generado con una llave académica `.jks` y verificado con `apksigner` mediante APK Signature Scheme v2, un firmante y RSA de 2048 bits. Se comprobó su instalación en el emulador y el funcionamiento de las consultas, configuración remota, restricción de fechas, PokeAPI y audio.

Para generar otra versión firmada desde Android Studio: `Build > Generate Signed App Bundle / APK > APK`, seleccionar la llave académica, su alias y la variante `release`.

La llave privada y sus contraseñas no deben publicarse en un repositorio público. La llave académica se entrega al docente por el canal privado autorizado; no se utiliza para producción.

## Evidencias de entrega

Agregar las capturas reales a la carpeta `evidencias/` con estos nombres antes de publicar el README definitivo:

* [APK firmado y archivo JKS](evidencias/apk-jks.png)
* [Verificación de firma](evidencias/firma-apk.png)
* [Resultados de pruebas unitarias](evidencias/pruebas-unitarias.png)
* [Cobertura JaCoCo](evidencias/cobertura-jacoco.png)
* [Planificación en Trello](evidencias/trello.png)

## Alcance y limitaciones

MVP académico para ejecución local con datos ficticios. Los perfiles y las reglas de transición están implementados en el flujo de la aplicación; el backend no constituye un sistema de autorización de producción. La configuración de examen es global y el progreso de intentos y último puntaje se guarda localmente en el dispositivo. La conexión HTTP con el backend está prevista para el emulador; un despliegue real requiere HTTPS y controles de seguridad adicionales.







## Generación del APK firmado mediante Gradle



Crear un archivo local `keystore.properties` en la raíz del proyecto con las siguientes propiedades:



- storeFile: ruta de la llave JKS.

- storePassword: contraseña del almacén.

- keyAlias: alias de la llave.

- keyPassword: contraseña de la llave.



Ejecutar `.\gradlew.bat :app:assembleRelease`.



El APK se genera en `app/build/outputs/apk/release/`.



Las credenciales y la llave privada están excluidas del control de versiones.



## Resultados finales de pruebas



- Pruebas ejecutadas: 89.

- Pruebas aprobadas: 89.

- Fallos: 0.

- Tasa de éxito: 100%.

- Cobertura de líneas en ViewModels y repositorios: 81,1%.



