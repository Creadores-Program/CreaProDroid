package org.CreadoresProgram.CreaProDroid.WebViewExtras;
import android.webkit.JavascriptInterface;
import android.content.Context;
import android.webkit.WebView;
import android.net.Uri;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileInputStream;
import org.CreadoresProgram.CreaProDroid.IA.MaxIaManager;
import android.speech.tts.TextToSpeech;
import java.util.Locale;
import android.os.Bundle;
import java.util.ArrayList;
import android.content.Intent;
import android.graphics.Color;
import android.support.customtabs.CustomTabsIntent;
import org.CreadoresProgram.CreaProDroid.MainActivity;

public class JSInterface{
    private MainActivity mContext;
    private MaxIaManager mMaxIaManager;
    private TextToSpeech tts;
    private WebView mWebView;
    private OkHttpClient clientHt = new OkHttpClient();
    private static final MediaType JSONHt = MediaType.parse("application/json; charset=utf-8");
    public JSInterface(MainActivity c, WebView webView) {
        mContext = c;
        mWebView = webView;
        mMaxIaManager = new MaxIaManager((Context) c);
        tts = new TextToSpeech(c, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        tts.setLanguage(new Locale("es"));
                    }
                }
            }
        });
    }
    @JavascriptInterface
    public void finish() {
        if(tts != null) {
            tts.stop();
            tts.shutdown();
        }
        mContext.finish();
    }
    @JavascriptInterface
    public String fetch(String url, String method, String data) {
        try{
        if(method == "POST"){
            RequestBody body = RequestBody.create(JSONHt, data);
            Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build(); 
            try(Response response = clientHt.newCall(request).execute()){
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                return response.body().string();
            }
        }else{
            Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
            try(Response response = clientHt.newCall(request).execute()){
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                return response.body().string();
            }
        }
        }catch (IOException e){
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
    @JavascriptInterface
    public String promptGemini(String prompt, String key){
        try{
          return mMaxIaManager.promptGemini(prompt, key);
        }catch (Exception e){
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
    @JavascriptInterface
    public void clearChat(){
        mMaxIaManager.clearChat();
    }
    @JavascriptInterface
    public String genImg(String prompt, String key){
        try{
          return mMaxIaManager.genImg(prompt, key);
        }catch (Exception e){
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
    @JavascriptInterface
    public void setUserName(String name){
        mMaxIaManager.setUserName(name);
    }
    @JavascriptInterface
    public void openUrl(String url){
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setShowTitle(true);
        builder.setToolbarColor(Color.parseColor("#FF6200EE"));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(mContext, Uri.parse(url));
    }
    @JavascriptInterface
    public void openApp(String packageApp){
        Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(packageApp);
        if (launchIntent != null) {
            mContext.startActivity(launchIntent);
        }
    }
    @JavascriptInterface
    public void speak(String text){
        if(tts != null) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    @JavascriptInterface
    public void copyText(String text){
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Texto Copiado", text);
        clipboard.setPrimaryClip(clip);
    }
    @JavascriptInterface
    public void stopSpeak(){
        if(tts != null) {
            tts.stop();
        }
    }
    @JavascriptInterface
    public void openFileUploader(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        mContext.startActivityForResult(Intent.createChooser(intent, "Selecciona un archivo"), MainActivity.FILE_UPLOAD_REQUEST_CODE); 
    }
    @JavascriptInterface
    public String readFile(String filePath) {
        try {
            InputStream fis;
            if(filePath.startsWith("content://")){
                Uri uri = Uri.parse(filePath);
                fis = mContext.getContentResolver().openInputStream(uri);
            }else{
                File file = new File(filePath);
                fis = new FileInputStream(file);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            reader.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al leer el archivo.";
        }
    }
    @JavascriptInterface
    public void startSpeechRecognition(){
        mContext.startSpeechRecognition();
    }
    @JavascriptInterface
    public void clearCache(){
        mWebView.clearCache(true);
    }
}