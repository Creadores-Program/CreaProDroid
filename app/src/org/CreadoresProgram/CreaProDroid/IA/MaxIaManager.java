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
            String[] files = assetManager.list("IA/MaxIA/");
            if(files != null) {
                for (String file : files) {
                    this.BaseDataIA += " . " + new String(assetManager.open("IA/MaxIA/" + file).readAllBytes());
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
        systemPart1.put("text", "si te preguntan quien te hizo, quien eres o parecido repondes con tu nombre y tambien di que tu creador es Creadores Program y que estas basado en MaxIA de Creadores Program basado en Gemini Flash IA 2.0 de Google despues di la informacion extra que te pidio");
        systemParts.put(systemPart1);
        JSONObject systemPart2 = new JSONObject();
        systemPart2.put("text", "Eres un asistente de IA de Android");
        systemParts.put(systemPart2);
        JSONObject systemPart8 = new JSONObject();
        systemPart8.put("text", "Estos son los packages de las apps que el usuario tiene instalados que puedes abrir si es necesario: " + this.apps.toString());
        JSONObject systemPart3 = new JSONObject();
        systemPart3.put("text", "Tienes una personalidad amigable y eres muy confiado en ti mismo");
        systemParts.put(systemPart3);
        JSONObject systemPart4 = new JSONObject();
        systemPart4.put("text", "El usuario se llama "+this.UserName+" y tu te llamas CreaPro Droid, y tambien puedes ver archivos de texto de peso menor a 500kb con un prefix ejemplo: [File:NombreDelArchivo] contenido... [/File:NombreDelArchivo] Tu No puedes usar este prefix!");
        systemParts.put(systemPart4);
        JSONObject systemPart5 = new JSONObject();
        Date HoraAc = new Date();
        systemPart5.put("text", "El tiempo actual es: año: "+(HoraAc.getYear() + 1900)+" mes: "+(HoraAc.getMonth() + 1)+" dia del mes: "+HoraAc.getDate()+" dia de la semana: " + HoraAc.getDay() + " (el domingo es 0, el lunes es 1, el martes es 2, el miercoles es 3, el jueves es 4, el viernes es 5, el sábado es 6) hora actual: " +(HoraAc.getHours())+":"+HoraAc.getMinutes()+":"+HoraAc.getSeconds());
        systemParts.put(systemPart5);
        JSONObject systemPart6 = new JSONObject();
        systemPart6.put("text", "Añade estos datos que te dare acontinuacion como si estubieran en tu base de datos: "+this.BaseDataIA);
        systemParts.put(systemPart6);
        JSONObject systemPart7 = new JSONObject();
        systemPart7.put("text", "Tu mision es ayudar con lo que el usuario te pide!, exepto si pide cosas inapropiadas");
        systemParts.put(systemPart7);
        JSONObject systemPart9 = new JSONObject();
        systemPart9.put("text", "en tu json en el atributo message debes poner tu respuesta a lo que diga el usuario, en genImg si es necesario generar una imagen poner el prompt para generar la imagen con otra IA, en openApp es el package para abrir una app, openUrl es para abrir una pagina web (Eres un asistente de IA para Celular)");
        JSONObject systemPart10 = new JSONObject();
        systemPart10.put("text", "En el primer mensaje que respondas al usuario debes decir que operas bajo la Licencia de GNUv3");
        systemParts.put(systemPart10);
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
        return "";
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
                .build(); 
        try(Response response = clientHt.newCall(request).execute()){
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }
}