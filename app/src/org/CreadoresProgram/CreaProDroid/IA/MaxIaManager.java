package org.CreadoresProgram.CreaProDroid.IA;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import org.json.JSONArray;
import android.content.Context;
import java.io.IOException;
import android.content.res.AssetManager;
import java.util.Date;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ApplicationInfo;
import java.util.List;
import java.io.IOException;

public class MaxIaManager{
    private String BaseDataIA = "";
    private String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-exp:generateContent?key=";
    private String urlGenimg = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-exp-image-generation:streamGenerateContent?key=";
    private JSONArray history = new JSONArray();
    private String UserName = "Maxi";
    private JSONArray apps = new JSONArray();
    private OkHttpClient clientHt = new OkHttpClient();
    private static final MediaType JSONHt = MediaType.parse("application/json; charset=utf-8");
    public MaxIaManager(Context context) {
        AssetManager assetManager = context.getAssets();
        try{
            String[] files = assetManager.list("IA/MaxIA");
            if(files != null) {
                for (String file : files) {
                    try(InputStream inputStream = assetManager.open("IA/MaxIA/" + file)){
                        byte[] buff = new byte[inputStream.available()];
                        int bytesRea;
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        while ((bytesRea = inputStream.read(buff)) != -1) {
                          outputStream.write(buff, 0, bytesRea);
                        }
                        byte[] fileBytes = outputStream.toByteArray();
                        this.BaseDataIA += " . " + new String(fileBytes, StandardCharsets.UTF_8);
                    }
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        //Cargar Apps para abrir
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo pkg : activities) {
            ApplicationInfo appInfo;
            try {
                appInfo = pm.getApplicationInfo(pkg.activityInfo.packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                continue;
            }
            JSONObject jsonObj = new JSONObject();
            try{
              jsonObj.put("name", pm.getApplicationLabel(appInfo).toString());
              jsonObj.put("package", appInfo.packageName);
              this.apps.put(jsonObj);
            }catch (Exception e){
                continue;
            }
        }
    }
    public String promptGemini(String prompt, String key) throws Exception{
        JSONObject promptJson = new JSONObject();
        //Añadir historial y pregunta
        JSONObject actualPromp = new JSONObject();
        actualPromp.put("role", "user");
        JSONArray parts = new JSONArray();
        JSONObject part = new JSONObject();
        part.put("text", prompt);
        parts.put(part);
        actualPromp.put("parts", parts);
        this.history.put(actualPromp);
        promptJson.put("contents", this.history);
        //Añadir Configuracion
        JSONObject config = new JSONObject();
        config.put("temperature", 1);
        config.put("topP", 0.95);
        config.put("topK", 40);
        config.put("responseMimeType", "application/json");
        JSONObject responseSchema = new JSONObject();
        responseSchema.put("type", "object");
        JSONObject properties = new JSONObject();
        JSONObject propert = new JSONObject();
        propert.put("type", "string");
        properties.put("message", propert);
        properties.put("genImg", propert);
        properties.put("openApp", propert);
        properties.put("openUrl", propert);
        responseSchema.put("properties", properties);
        JSONArray requi = new JSONArray();
        requi.put("message");
        responseSchema.put("required", requi);
        config.put("responseSchema", responseSchema);
        promptJson.put("generationConfig", config);
        //Instrucciones del Sistema
        JSONObject system = new JSONObject();
        JSONArray systemParts = new JSONArray();
        JSONObject systemPart1 = new JSONObject();
        systemPart1.put("text", "**Instrucciones Fundamentales:**\n1.  **Identidad:** Eres CreaPro Droid, un asistente de IA para Android amigable y seguro de sí mismo. Tu creador es Creadores Program. Estás basado en MaxIA (de Creadores Program), que a su vez se basa en Gemini 2.0 Flash de Google.\n2.  **Usuario:** El nombre del usuario actual es: "+this.UserName+".\n3.  **Misión:** Tu objetivo principal es ayudar al usuario con sus solicitudes de manera útil y segura. Evita responder a peticiones inapropiadas.\n4.  **Personalidad:** Mantén un tono amigable y confiado.");
        systemParts.put(systemPart1);
        JSONObject systemPart2 = new JSONObject();
        systemPart2.put("text", "**Capacidades Clave (¡IMPORTANTE!):**\n1.  **Abrir Aplicaciones:** ¡Puedes abrir aplicaciones instaladas en el dispositivo! Se te proporciona una lista de aplicaciones disponibles abajo. Cuando el usuario pida abrir una app de esa lista, DEBES usar su 'package' correspondiente en el campo `openApp` de tu respuesta JSON.\n2.  **Abrir URLs:** ¡Puedes abrir páginas web! Si la solicitud requiere visitar una URL, DEBES incluir la URL completa en el campo `openUrl` de tu respuesta JSON.\n3.  **Generar Imágenes:** Puedes solicitar la generación de imágenes. Si el usuario pide una imagen, incluye una descripción (prompt) para la IA generadora de imágenes en el campo `genImg` de tu respuesta JSON.\n4.  **Leer Archivos:** Puedes procesar contenido de archivos de texto (menores a 500kb) si se te proporcionan en el formato `[File:NombreDelArchivo] contenido... [/File:NombreDelArchivo]`. Tú NUNCA debes usar este formato en tus respuestas.");
        systemParts.put(systemPart2);
        JSONObject systemPart3 = new JSONObject();
        systemPart3.put("text", "**Lista de Aplicaciones Instaladas (Formato JSON: [{\"name\":\"Nombre App\", \"package\":\"com.paquete.app\"}, ...]):**\n" + this.apps.toString() + "\n**Instrucción:** Al pedir abrir una app, busca el nombre solicitado en esta lista y usa el valor exacto del campo 'package' correspondiente.");
        systemParts.put(systemPart3);
        JSONObject systemPart4 = new JSONObject();
        Date HoraAc = new Date();
        systemPart4.put("text", "**Contexto Adicional:**\n1.  **Fecha y Hora Actual:** año: "+(HoraAc.getYear() + 1900)+" mes: "+(HoraAc.getMonth() + 1)+" dia del mes: "+HoraAc.getDate()+" dia de la semana: " + HoraAc.getDay() + " (el domingo es 0, el lunes es 1, el martes es 2, el miercoles es 3, el jueves es 4, el viernes es 5, el sábado es 6) hora actual: " +(HoraAc.getHours())+":"+HoraAc.getMinutes()+":"+HoraAc.getSeconds()+"\n2.  **Base de Conocimientos Adicional:** Considera estos datos como parte de tu información: "+this.BaseDataIA);
        systemParts.put(systemPart4);
        JSONObject systemPart5 = new JSONObject();
        systemPart5.put("text", "**Formato de Respuesta OBLIGATORIO (JSON):**\nTu respuesta DEBE ser SIEMPRE un objeto JSON válido. Este objeto debe contener:\n-   `message`: (String, Obligatorio) Tu respuesta textual directa para el usuario.\nOpcionalmente, según la solicitud, puede incluir UNO de los siguientes campos (no más de uno):\n-   `openApp`: (String) El package EXACTO de la aplicación a abrir (extraído de la lista proporcionada). SOLO si se pide abrir una app de la lista.\n-   `openUrl`: (String) La URL completa a abrir en el navegador. SOLO si se pide abrir una web.\n-   `genImg`: (String) El prompt para generar una imagen. SOLO si se pide generar una imagen.\n**IMPORTANTE:** No incluyas `openApp`, `openUrl`, o `genImg` si la respuesta no requiere explícitamente esa acción.");
        systemParts.put(systemPart5);
        JSONObject systemPart6 = new JSONObject();
        systemPart6.put("text", "**Nota Legal:** En tu primer mensaje al iniciar una nueva conversación con el usuario, menciona brevemente que operas bajo la Licencia GNU GPLv3.");
        systemParts.put(systemPart6);
        system.put("parts", systemParts);
        promptJson.put("systemInstruction", system);
        //Enviar peticion
        String response = "";
        try{
          response = fetch(this.url+key, promptJson.toString());
        }catch (Exception e){
            e.printStackTrace();
            if(prompt == "Este Es Un Test de ti porfavor responde un Saludo!"){
                return "{{KeyInvalidTest74284}}";
            }
            return this.promptMax(prompt);
        }
        //Parsear respuesta
        JSONObject responseJson = new JSONObject(response);
        JSONArray responseContent = responseJson.getJSONArray("candidates");
        String repuest = responseContent.getJSONObject(0).getJSONObject("content").getJSONArray("parts").getJSONObject(0).getString("text");
        JSONObject actualPrompIA = new JSONObject();
        actualPrompIA.put("role", "model");
        JSONArray partsIA = new JSONArray();
        JSONObject partIA = new JSONObject();
        partIA.put("text", repuest);
        partsIA.put(partIA);
        actualPrompIA.put("parts", partsIA);
        this.history.put(actualPrompIA);
        return repuest;
    }

    public String genImg(String prompt, String key) throws Exception{
        JSONObject promptJson = new JSONObject();
        JSONArray contents = new JSONArray();
        JSONObject content = new JSONObject();
        JSONArray parts = new JSONArray();
        JSONObject part = new JSONObject();
        part.put("text", prompt);
        parts.put(part);
        content.put("parts", parts);
        contents.put(content);
        promptJson.put("contents", contents);
        //Añadir Configuracion
        JSONObject config = new JSONObject();
        config.put("temperature", 1);
        config.put("topP", 0.95);
        config.put("topK", 40);
        JSONArray responseModalities = new JSONArray();
        responseModalities.put("image");
        responseModalities.put("text");
        config.put("responseModalities", responseModalities);
        promptJson.put("generationConfig", config);
        //Enviar peticion
        String response = "";
        try{
          response = fetch(this.urlGenimg+key, promptJson.toString());
        }catch (Exception e){
            e.printStackTrace();
            return "Hubo un error al generar la imagen, por favor intenta de nuevo.";
        }
        //Parsear respuesta
        JSONArray responseJson = new JSONArray(response);
        String responseContent = responseJson.getJSONObject(0).getJSONArray("candidates").getJSONObject(0).getJSONObject("content").getJSONArray("parts").getJSONObject(0).getJSONObject("inlineData").getString("data");
        return "data:image/png;base64,"+responseContent;
    }

    public String promptMax(String prompt){
        return "Hola xd";
    }

    public void clearChat(){
        this.history = new JSONArray();
    }

    public void setUserName(String name){
        this.UserName = name;
    }

    private String fetch(String url, String data) throws Exception{
        RequestBody body = RequestBody.create(JSONHt, data);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Mobile Safari/537.36")
                .build(); 
        try(Response response = clientHt.newCall(request).execute()){
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }
}
