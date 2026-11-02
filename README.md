# Explora2 — Sistema de Apoyo Didáctico Digital
**Caso Institucional:** Escuela Escritora Marcela Paz (MPAZ PRO — 2.° Básico)  
**Asignatura:** Desarrollo de Aplicaciones Móviles

---

## 👥 Integrantes del Equipo
* **Javier Cruz**
* **Kevis Gonzalez**

---

## 📖 Descripción del Proyecto
**Explora2** es un Producto Mínimo Viable (MVP) desarrollado de forma nativa para Android. Está diseñado para apoyar los procesos de aprendizaje y evaluación de estudiantes de 2.° básico (7 a 8 años) mediante una interfaz lúdica, accesible y de baja carga cognitiva, integrando módulos específicos para docentes y la jefatura técnica pedagógica (UTP).

---

## 🎯 Perfiles y Roles de Usuario

1. **Jefatura UTP:**
    * Supervisión y administración de las 4 asignaturas curriculares del nivel: *Lenguaje y Comunicación, Matemática, Ciencias Naturales e Historia, Geografía y Ciencias Sociales*.
    * Aprobación y trazabilidad de unidades y Objetivos de Aprendizaje (OA).
    * Visualización de informes de rendimiento curricular consolidados.

2. **Docente de 2.° Básico:**
    * Creación y gestión de contenidos pedagógicos asociados a los OA.
    * Configuración de evaluaciones: parametrización de rangos de disponibilidad y número máximo de intentos permitidos.
    * Seguimiento individual y grupal de calificaciones y retroalimentación.

3. **Estudiante de 2.° Básico:**
    * Interfaz adaptada con tipografía legible, botones grandes y apoyos visuales.
    * Soporte multimedia auditivo (*TextToSpeech* nativo) para instrucciones de lectura guiada.
    * Rendición de desafíos y pruebas con control reactivo de intentos y bloqueo tras agotar oportunidades.
    * Privacidad estricta de datos (información ficticia y anónima según la normativa escolar).

---

## 🏗️ Arquitectura y Tecnologías

* **Lenguaje:** Kotlin 2.0+
* **Framework UI:** Jetpack Compose con Material Design 3
* **Patrón de Diseño:** MVVM (Model - View - ViewModel)
* **Gestión de Estado Reactivo:** `StateFlow` y `rememberSaveable`
* **Navegación:** `NavHost` centralizado con rutas tipadas en `sealed class Screen`
* **Diseño Adaptativo (Guía 9):** Implementación de *Window Size Classes* para resoluciones `Compact` (móvil vertical), `Medium` (tablet vertical/plegable) y `Expanded` (tablet horizontal)
* **Persistencia Local:** `DataStore Preferences` para almacenamiento de configuraciones de sesión
* **Configuración Gradle:** Modo `android.nonTransitiveRClass=true` para optimización modular de recursos gráficos (`R.drawable.logo`)

---

## 📂 Estructura de Paquetes (`app/src/main/java/com/example/explora2/`)

```text
├── model/           # Data classes y estados de UI (UsuarioUiState, GestionUiState, etc.)
├── navigation/      # Grafo central de rutas (Screen.kt, AppNavigation.kt)
├── ui/
│   ├── screens/
│   │   ├── inicio/   # Vistas adaptativas HomeScreen (Compact, Medium, Expanded)
│   │   ├── perfiles/ # Selector de roles institucionales y acceso
│   │   ├── alumno/   # Asignaturas, Objetivos, Actividades, Prueba con audio y Progreso
│   │   ├── docentes/ # Gestión pedagógica, configuración de intentos y revisión
│   │   └── utp/      # Panel UTP, autorización de unidades y reportes OA
│   └── theme/        # Paleta de colores institucionales y tipografías
└── viewmodel/       # Lógica de negocio y validación de formularios