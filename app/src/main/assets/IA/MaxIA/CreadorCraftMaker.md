CreadorCraft Maker Wiki de API:
#### 📚

### API

El juego puede usar todos los scripts que tiene CreadorCraft (ver en créditos de CreadorCraft).

## GameProps API (JavaScript, TypeScript y LatinoScript):

**GameProps.status**: ver el estado de la conexión con el juego CreadorCraft o CreadorCraft API retorna `null` si no está conectado o un string `"ready"` si se ha conectado correctamente!

**GameProps.getFileGame()**: retorna el directorio de tu juego con la API de JSZip.

**GameProps.isDiscordPlayer()**: retorna si el jugador tiene cuenta de discord o no.

**GameProps.getPlayerId()**: retorna el ID del Jugador.

**GameProps.getPlayerName()**: retorna el nombre del Jugador.

**GameProps.getGame()**: retorna un Json con información del juego:
- **GameProps.getGame().getVersion()**: retorna la versión del Juego.
- **GameProps.getGame().isBeta()**: retorna si el jugador está usando una versión beta.
- **GameProps.getGame().getBetaVersion()**: retorna la versión beta.

**GameProps.addEventListener(event, callback)**: agrega eventos al juego con un callback.
- **Eventos disponibles**:
  - **resumeEvent**: se ejecuta cuando un jugador resume el juego después de estar en pausa.
  - **playerMessageEvent**: se ejecuta cuando un jugador envía un mensaje en el chat también dando el mensaje al callback como argumento 1 el segundo argumento es un CustomEvent que puede ser cancelado!.
  - **pauseEvent**: se ejecuta cuando el jugador entra a la pausa del juego.

**GameProps.sendPlayerMessage(message)**: enviar un mensaje al jugador!

**GameProps.exitGame()**: saca al jugador del juego!

**GameProps.getClickSound()**: retorna un audio de click.

**GameProps.getGameMusic()**: retorna un audio de juego de fondo.

**GameProps.getWarnSound()**: retorna un sonido de advertencia.

**GameProps.getWinSound()**: retorna un sonido de ganar.

**GameProps.getClickStartSound()**: retorna el sonido del click al entrar al juego.

**GameProps.getStorage()**: retorna una clase de almacenamiento del juego:
```javascript
{
  GameProps.getStorage().set(ID, object): guardar un objeto en el almacenamiento de CreadorCraft.
  GameProps.getStorage().get(ID): obtener un objeto previamente guardado en el almacenamiento de CreadorCraft.
  GameProps.getStorage().delete(ID): eliminar un objeto previamente guardado en el almacenamiento de CreadorCraft.
}
```

## LoadUrlCC API (JavaScript, TypeScript, EsJS, etc):

**LoadUrlCC.fileToUrl(path, mimetype)**: convierte un archivo de tu juego a url util para src de imagen, uso en DOM API, etc. esta es una función asincronoma(async)!
**LoadUrlCC.setUrlToImg(img, path)**: establece el src a una imagen de Html con una imagen de tu juego
 
Para ejecutar en otros lenguajes puedes ver cómo usar variables javascript en ese lenguaje!.

## Ejecución de Código:

**RECUERDA QUE NO PUEDES USAR ETIQUETAS script ni style EN TU CODIGO HTML**
CreadorCraft ejecutará en este orden el código de su juego:
- CSS
- Html y Htmx
- SCSS
- JS
- JS tipo module
- TypeScript
- LiveScript
- CoffeeScript
- PHP
- Python
- Ruby
- Lua
- Perl
- EJS
- WebAssembly
- EsJS
- EsHtml
- Markdown (Depende de cómo lo uses en JavaScript)

## Manifest.json:

Este es el archivo que contiene información importante de su juego aquí les dejo los parámetros del Json que debe tener su juego:
• name (obligatorio): Nombre de su juego.
• icon (opcional): Icono o baner de su juego la imagen debe ser tipo URL.
• description (obligatorio): Descripción de su juego.
• version (recomendado): Version de tu juego util para Juegos CreadorCraft API 1.0.0+ para manejar actualizaciones
• mainHtml (obligatorio): El directorio del archivo main del código Html.
• mainJS (obligatorio): El directorio del archivo main del código JavaScript.
• mainJSmodule (opcional): El directorio del archivo main del código JavaScript tipo module.
• mainCSS (opcional): El directorio del archivo main del código CSS.
• uuid (obligatorio): Esta opción es obligatoria para la versión 1.0.0 ESTABLE si no esta definida se manejara como un juego de CreadorCraft ALFA x (actualmente no es necesario hasta que salga CreadorCraft 1.0.0 ESTABLE)
• mainPython (opcional): El directorio del archivo main del código Python.
• configPy (opcional): Configuración  de tu modulo python Más Información en PyScript en la opción config de html
• mainWebAssembly (opcional): El directorio del archivo main del Código compilado Web Assembly.
• mainCoffeeScript (opcional): El directorio del archivo main del código CoffeeScript.
• mainLS (opcional): El directorio del archivo main del código LiveScript.
• mainTS (opcional): El directorio del archivo main del código TypeScript.
• mainEsJS (opcional): El directorio del archivo main del código EsJS.
• mainEsJSmodulo (opcional): El directorio del archivo main del código EsJS tipo modulo.
• mainEsHtml (opcional): El directorio del archivo main del código EsHtml.
• mainSCSS (opcional): El directorio del archivo main del código SCSS.
• onMarkdown (opcional): Añadir API de Markdown (Marked). (Este es una opción boleana poner true o false o simplemente no poner si no lo utiliza)
• on3Dengine (opcional): Añadir motor 3D de three.js (Este es una opción boleana poner true o false o simplemente no poner si no lo utiliza)(Solo funciona para scripts tipo module como el js de la opción mainJSmodule)
• onServerWebGamePost (opcional): Añadir el sistema Multijugador ServerWebGamePost de Creadores Program (Este es una opción boleana poner true o false o simplemente no poner si no lo utiliza)
• onCommonJS (opcional): Añadir CommonJS de navegador (funcion require) (Este es una opción boleana poner true o false o simplemente no poner si no lo utiliza)
   Ahora usa CommonJS Nativo fork de simple-browser-require Funciona igual excepto que tiene una función extra de promesa para leer todos los archivos de tu juego ejemplo de uso:
   ```js
    window.addEventListener("load", async function(){
     await require.CreadorCraftInit();
     //require.TSInit(); //para TypeScript(debe tener mainTS)
     //require.EsJSInit(); //para EsJS(debe tener mainEsJS)
     //require("rootDir");
     //require.EJSInit(); //para EJS (debe tener mainEJS y solo el require retorna un string parchado de EJS)
   });
```
   si existe un main de js tipo modulo también añade la función requireModule solo usable en scripts tipo modulo y su directorio siempre es raiz ejemplo de uso:
   ```js
   const ModuTest = (await requireModule("rootDir"));
   //const TSMod = (await requireTSModule("rootDir"));//para TypeScript(debe tener mainTS)
   //const EsJSMod = (await requireEsJSModule ("rootDir")); //para EsJS(debe tener main EsJS tipo Modulo)
   ```
• importJSmap (opcional): Añadir un mapa de import de Scripts externos JavaScript.
• importCSSMap (opcional):  Añadir un mapa de importa de CSS externos en array ejemplo:
```json
"importCSSMap": [
    {
         //Igual que en link pero en Json
         "href": "página..",
         "rel": "....."
    }
]
```
• mainPerl (opcional): El directorio del archivo main del código Perl.
• mainEJS(opcional): El directorio del archivo main del código EJS.
• mainRuby (opcional): El directorio del archivo main del código Ruby.
• mainLua (opcional): El directorio del archivo main del código Lua.
• mainPHP (opcional): El directorio del archivo main del código PHP Es obligatorio especificar configPHP.
• configPHP (opcional): (Está opción es obligatorio si usas PHP en tu juego!) Configuración del lenguaje PHP en un subjson(el texto después de // son comentarios no escribas eso en tu manifest!)
```json
{
   "input": "#idDelElemento", //ID del elemento Html para enviar comandos al intérprete PHP
   "output": "#idDelElemento", //ID del elemento Html para añadir los mensajes de la consola
  "error":"#idDelElemento" //ID del elemento Html para añadir los mensajes de error de PHP
}
```
• javaLibs (opcional): Usa librerías de Java o usa tu app de Java en CreadorCraft! suber el jar y está opción es un array de directorios de tus jars a cargar ejemplo:
```json
javaLibs: [
   "myfirjars/mylib.jar"
]
```
Puedes cargar librerías o apps Java ejemplo de uso en JS:
  Uso para Libs en JS:
  ```js
   await cheerpjInit();
   const cj = await cheerpjRunLibrary(javaLibs[0]); //pasa la variable del manifest a JavaScript
  const MyClass = await cj.com.library.MyClass;
   const obj = await new MyClass();
   await obj.myMethod();
```
  Uso para Apps Java:
  ```js
  await cheerpjInit(); 
  cheerpjCreateDisplay(800, 600);
  await cheerpjRunJar(javaLibs[0]);
  ```
  
  Tambien se puede generar juegos por IA en CreadorCraft Maker!
  
  Como usar Editor Three.js para tu Juego CreadorCraft
 Puedes usar el [Editor 3D de Three.js](https://threejs.org/editor/) para crear tu juego en CreadorCraft solo tienes que obtener el Zip que da Three.js al terminar de crear tu juego en el Editor 3D de Three.js despues de descargarlo [Abre Esta Página](https://creadorcraft-maker.blogspot.com/p/convertir-zip-de-threejs-creadorcraft.html) para convertir a un juego de CreadorCraft!

Como Obtener el Zip del Editor 3D de Three.js?




Presiona en PROJECT



Escribe el nombre de tu juego y lo que quieras editar después
Presiona PUBLISH

Descarga el Zip.
Después Súbelo a la Pagina de Conversión espera a que lo Convierta y Listo!

Creadores Program © 2025 no está afiliado de ninguna manera a Three.js

CreadorCraft Maker & WingNodes: ¡Potencia tu juego Multijugador!
¿Desarrollas juegos en CreadorCraft Maker y sueñas con crear experiencias multijugador épicas? ¡WingNodes es tu solución!  Gracias a nuestra nueva alianza, WingNodes es ahora el hosting oficial de CreadorCraft, lo que te da acceso a servidores de alta performance, optimizados para juegos, con soporte técnico 24/7 y precios competitivos.  Deja de preocuparte por la infraestructura y enfócate en lo que realmente importa: ¡la creación de juegos increíbles!  Con la integración de ServerWebGamePost, podrás gestionar fácilmente tus juegos multijugador, enviando y recibiendo datos de manera eficiente y segura.  Descubre cómo WingNodes y ServerWebGamePost pueden simplificar tu desarrollo y llevar tus juegos al éxito.
Puedes pedirlo en un ticket de WingNodes en Discord!
mas info aqui: https://creadorcraft-maker.blogspot.com/p/creadorcraft-maker-wingnodes-potencia.html