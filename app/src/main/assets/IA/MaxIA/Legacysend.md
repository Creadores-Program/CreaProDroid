# LegacySend (旧版互传)

Una aplicación nativa para Android en Java desarrollada de forma independiente para la transferencia de archivos en red local (LAN), cuyo objetivo es ofrecer compatibilidad con la detección de dispositivos y la API de carga de LocalSend Protocol v2.2 en sistemas antiguos (desde Android 2.3 hasta Android 6.0 / API 9 a API 23). El proyecto no referencia, importa ni compila directorios de código fuente de LocalSend.

Versión actual: `1.3` (versionCode 5).

## Descarga e Instalación

Los archivos APK precompilados están disponibles para su descarga e instalación directa desde la página de ****[GitHub Releases](https://github.com/Creadores-Program/legacysend/releases)**** de este repositorio, por lo que no es necesario compilar manualmente. Las entregas incluyen firma v1 lista para su instalación directa en dispositivos legados (Android 2.3 a 6.0).

## Compilación desde el Código Fuente (Opcional)

Si deseas compilar el proyecto manualmente, se requiere el siguiente entorno:

* JDK 17
* Gradle Wrapper 8.9
* Android Gradle Plugin 8.7.3
* Android SDK Platform 23 (Android 6.0 Marshmallow)
* Android SDK Build Tools 23.0.3 (o superior)

Comando de compilación y verificación:

```bash
./gradlew testDebugUnitTest lintDebug assembleDebug

```

Ruta de salida del APK compilado:

```text
app/build/outputs/apk/debug/app-debug.apk

```

El proyecto establece un `minSdkVersion` de 9, un `compileSdkVersion` de 23 y un `targetSdkVersion` de 23 (Android 6.0 Marshmallow). Esto garantiza un entorno de compilación liviano y unificado, totalmente alineado con los permisos de almacenamiento y servicios de la era clásica de Android. Este proyecto está diseñado exclusivamente para hardware legado y no está destinado a publicarse en Google Play.

## Características

* Descubrimiento de dispositivos, anuncios y respuestas mediante multicast UDP.
* Interfaz de registro HTTP/HTTPS compatible con LocalSend v2 (v2.2).
* Soporte para codificación de transferencia por bloques (`Chunked Transfer Encoding`).
* Selección, envío y recepción de archivos individuales y múltiples.
* Confirmación (Aceptar / Rechazar) previa a la recepción.
* Verificación de tokens por archivo, ID de sesión, IP de origen y huella digital de certificado.
* Carga y guardado en flujo de datos (*streaming*) sin cargar archivos completos en memoria.
* Progreso general, notificaciones de fallo y cancelación por cualquiera de las partes.
* Renombrado automático con sufijos `(1)`, `(2)` para evitar sobrescrituras.
* Compatibilidad con nombres de archivo con caracteres chinos, espacios y caracteres especiales.
* Servicio de recepción en primer plano; la recreación de la Activity no interrumpe la transferencia en curso.
* Interfaz de usuario traducida a chino simplificado.

## Estructura Principal del Proyecto

```text
app/src/main/java/com/blithe/legacysend/
├── LegacySendApp.java       Estado a nivel de aplicación, tareas en segundo plano y eventos de UI
├── ReceiveService.java      Servicio de recepción en primer plano
├── discovery/               Descubrimiento por multicast UDP
├── model/                   Modelos de datos para dispositivos y archivos
├── protocol/                Formatos JSON de LocalSend (v2.2)
├── security/                Identidad autofirmada, BKS KeyStore, mTLS, fijado de certificados, SimpleX509Generator
├── server/                  Endpoints HTTP/HTTPS (registro, preparación, carga, cancelación)
├── storage/                 Manejo de almacenamiento con SAF, sistema de archivos legado y renombrado
├── transfer/                Cliente de envío HTTPS/HTTP, control de progreso y cancelación
├── ui/                      Interfaz de usuario nativa en Android Views
└── util/                    Utilidades para copiado en flujo y control de velocidad

```

El código fuente está escrito 100% en Java, sin dependencias de Kotlin, Jetpack Compose, Flutter, Dart o React Native. Se utiliza Groovy DSL para Gradle.

## Compatibilidad y Adaptaciones para Android 2.3–6.0 (API 9–23)

* **Soporte para minSdk 9 (Android 2.3) hasta Android 6.0 (API 23):** Cobertura completa enfocada en hardware y firmware antiguo.
* **Generación de certificados en API 9–17:** Implementación de `SimpleX509Generator` para resolver la ausencia de `AndroidKeyStore` y la estricta validación ASN.1/DER de OpenSSL / Conscrypt en Android 2.3–4.2.
* **Formato BKS KeyStore:** Uso de `BouncyCastle` (`BKS`) en API 9–17 para almacenar claves y certificados privados, evitando fallos de serialización `PKCS12` en Android 2.3.
* **Corrección de Estructura ASN.1 / DER:** Envoltura `SEQUENCE` agregada al par `AttributeTypeAndValue` dentro de la jerarquía de certificados y cambio explícito del tipo de cadena de `commonName` (2.5.4.3) a `PrintableString` (0x13), solucionando definitivamente la excepción `ASN.1 encoding routines:OPENSSL_internal:WRONG_TAG`.
* **Mitigación del efecto Y2K38:** Los certificados autofirmados en API 9–17 utilizan firmas `SHA1withRSA` y un periodo de validez limitado a 10 años para evitar desbordamientos de tiempo de 32 bits.
* **Acceso a Archivos Legado y SAF:** Se utiliza `ACTION_OPEN_DOCUMENT` con SAF en API 19–23, mientras que API 9–18 recurre a un explorador de archivos interno para leer el almacenamiento externo directamente.
* **Compatibilidad TLS en Recepción para Android 4.4.2 (API 19–20):** El servidor TLS 1.2 de Kindle Android 4.4.2 solo soporta cifrados CBC, incompatibles con el cliente TLS en Rust de LocalSend 1.17.0. En API 19–20 se utiliza la modalidad oficial `protocol: "http"`, mientras que API 9–18 y API 21–23 mantienen HTTPS para recepción y envíos salientes.
* **Rutas de Guardado:** Almacenamiento en el directorio público `Download/LegacySend` en todo el rango soportado (API 9–23).
* **MulticastLock y Subprocesos:** Escucha de multicast asegurada tras adquirir `MulticastLock`; todas las operaciones de red e E/S de archivos se ejecutan en hilos secundarios.
* **Procesamiento de Flujo:** Transferencia con búfer de 32 KiB compatible con *Chunked Streaming*, validando la coherencia entre el tamaño real recibido y los metadatos.
* **Notificaciones:** Servicio en primer plano con notificaciones tradicionales para todo el rango de API 9 a 23.

## Dependencias

Sin dependencias de terceros en tiempo de ejecución. Únicamente utiliza el SDK de Android, la librería estándar de Java y `org.json` (incluido en el sistema operativo).

Dependencias de prueba:

* JUnit 4.13.2: Ejecutado únicamente en la JVM del host (no empaquetado en el APK).
* `org.json:json:20240303`: Implementación para pruebas unitarias en la JVM (no empaquetado en el APK).

## Estado de Verificación Actual

### Funcionalidades Probadas y Verificadas

* 12 pruebas unitarias en host: serialización del protocolo, metadatos multiarchivo, caracteres especiales/chinos, confirmaciones/cancelaciones/tiempos de espera, renombrado, cálculo de hash SHA-256 y copiado en flujo.
* Compilación Gradle, inspección Lint y empaquetado de APK debug.
* Verificación del manifiesto APK validando `minSdkVersion=9`, `compileSdkVersion=23` y `targetSdkVersion=23`.
* Validación de firmas APK v1/v2 (firma v1 lista para instalación en Android 2.3, 4.4.2 y hasta 6.0).
* **Prueba Real en Android 2.3.6 (API 9):** Generación exitosa de certificados (sin excepción `WRONG_TAG`), inicio del servicio HTTPS en el puerto 53317, descubrimiento correcto del cliente oficial de LocalSend, y recepción/escritura en disco por *Chunked Upload* con verificación SHA-256 idéntica.
* **Prueba Real en Kindle Android 4.4.2 (API 19):** Inicio, selección interna de archivos, envío exitoso hacia Android 11 y recepción exitosa desde LocalSend 1.17.0 oficial con validación de hash SHA-256.

## Solución para la Selección de Archivos en Kindle 4.4.2 y Versiones Antiguas

La aplicación DocumentsUI de la GPU de Kindle conserva registros de descargas eliminadas o movidas, arrojando excepciones `FileNotFoundException` al intentar abrir sus URIs `content://`. En API 9–20 se utiliza un explorador interno que lista directamente los archivos reales del almacenamiento; API 21–23 continúa usando el SAF del sistema.

### Implementado pero Pendiente de Pruebas Extensivas

* Cambios frecuentes de red Wi-Fi en hardware real, restricciones agresivas de batería de fabricantes y transferencias de archivos de gran tamaño en Android 5.0–6.0.

### Funcionalidades No Implementadas

* LocalSend Reverse Download API (descargas web desde navegador); la API Upload principal de Android a LocalSend no depende de esto.
* Funciones secundarias como PIN, historial, portapapeles, temas, actualizaciones automáticas o cuentas.
* Escaneo alternativo por subred IP; actualmente se utiliza descubrimiento por multicast predeterminado y registro bidireccional mediante `/register`.

### Limitaciones del Entorno de Desarrollo

* Android Emulator 36 en arquitectura Apple Silicon no soporta imágenes ARMv7 QEMU2 para API 9 o API 19.

## Límites de Seguridad en el Modo de Recepción API 19–20

Debido a la falta de cifrados en común entre el servidor TLS de Android 4.4 y el cliente Rust de LocalSend 1.17.0, la recepción en API 19–20 opera bajo el modo HTTP oficial del protocolo. En esta dirección, los archivos y metadatos no viajan cifrados por TLS, aunque se mantienen las verificaciones de IP de origen, ID de sesión y tokens únicos por archivo. Se recomienda su uso exclusivo en redes locales de confianza. Todos los envíos salientes desde LegacySend y la recepción en API 9–18 y API 21–23 mantienen cifrado HTTPS y fijado de certificados.

Para detalles adicionales sobre la especificación y los endpoints, consulta [docs/protocol.md](https://github.com/Blithe-Chiang/legacysend/blob/main/docs/protocol.md).

## Contribuciones

Los Issues y Pull Requests son bienvenidos. El objetivo principal de LegacySend es ofrecer una solución ligera, estable e interoperable con LocalSend (v2.2) en dispositivos legados (Android 2.3 a Android 6.0 / API 9 a API 23). Se priorizará la compatibilidad con sistemas antiguos sobre la adición de nuevas características.

### Directrices de Desarrollo

* Mantener la implementación independiente sin copiar ni incluir código fuente de LocalSend.
* Utilizar exclusivamente Java y vistas nativas de Android (evitar Kotlin, Jetpack Compose, Flutter o Google Play Services).
* Preservar la compatibilidad enfocada estrictamente en el rango `minSdkVersion 9` a `targetSdkVersion 23`.
* Ejecutar todas las operaciones de red e E/S de archivos en hilos secundarios utilizando flujos de datos (*streaming*).
* Verificar el comportamiento en el rango objetivo antes de enviar cambios significativos.