# Evidencias de Explora2 — DSY1105

Capturas reales del desarrollo y cierre del 27 de noviembre de 2026. Se recortaron únicamente bordes, espacio sobrante, controles ajenos y secciones no necesarias. No se modificaron los datos ni resultados.

## Funcionamiento e integración

| Archivo | Evidencia y alcance |
| --- | --- |
| [asignacion-actividad.png](asignacion-actividad.png) | Panel docente: planificación #3 asignada y otra disponible para asignar (captura del desarrollo). |
| [configuracion-examen.png](configuracion-examen.png) | Guardado de parámetros del examen: tres intentos, fechas y versión v1.2 (captura anterior a la configuración final). |
| [evaluacion-alumno.png](evaluacion-alumno.png) | Pregunta de Lenguaje, opciones, contador de intentos y controles de audio. |
| [pokeapi.png](pokeapi.png) | Pantalla Mis Logros, con Pokémon y sus nombres. Evidencia visual de la interfaz; para acreditar el consumo remoto se complementa con el código Retrofit y una demostración en ejecución. |
| [swagger.png](swagger.png) | Swagger UI: siete endpoints de planificaciones y configuración de examen. |
| [swagger-planificaciones.png](swagger-planificaciones.png) | GET /api/planificaciones: HTTP 200, JSON de la planificación #3 autorizada, asignada y respondida. |
| [swagger-configuracion.png](swagger-configuracion.png) | GET /api/configuracion-examen: HTTP 200, cuatro intentos, vigencia 01/11/2026 a 31/12/2026, versión v1.2. |

## Pruebas y cobertura

| Archivo | Evidencia y alcance |
| --- | --- |
| [pruebas-unitarias.png](pruebas-unitarias.png) | Gradle: 89 pruebas, 0 fallos, 0 ignoradas, 100% de éxito, duración 6,358 s. |
| [cobertura-jacoco.png](cobertura-jacoco.png) | 83% de instrucciones en `viewmodel` y 80% en `repository`. Incluyendo también `viewModel`, 699 de 862 líneas cubiertas (81,1%). Cobertura global de instrucciones: 6%. La cifra de líneas se refiere a los paquetes indicados, no a toda la lógica de la aplicación. |

## APK firmado

| Archivo | Evidencia y alcance |
| --- | --- |
| [generacion-apk.png](generacion-apk.png) | Android Studio confirma la generación exitosa del APK firmado en variante release. |
| [firma-apk.png](firma-apk.png) | `apksigner` confirma firma v2 válida, un firmante y RSA de 2048 bits. |
| [apk-jks.png](apk-jks.png) | Explorador de archivos: `app-release.apk` y `explora2.jks` generados. La captura no incluye la clave privada ni sus contraseñas. |

## Colaboración

| Archivo | Evidencia y alcance |
| --- | --- |
| [trello.png](trello.png) | Tablero Explora2 con dos miembros visibles, dos tareas pendientes, tres en proceso y 19 en Hecho. La captura general no muestra los responsables individuales de cada tarjeta. Complementar con capturas de tarjetas asignadas y el historial de commits de ambos integrantes. |

## Notas

- Las capturas de errores y mediciones antiguas de pruebas/cobertura no se incluyen.
- Los archivos binarios APK y JKS no forman parte de este ZIP de evidencias. Deben entregarse aparte; conservar la llave de firma y sus credenciales de forma segura.
- Swagger acredita las consultas GET y el inventario de endpoints, no la ejecución individual de todos los métodos CRUD.
