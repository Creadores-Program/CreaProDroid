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
import org.CreadoresProgram.CreaProDroid.update.GithubUpdate;

public class JSInterface{
    private MainActivity mContext;
    private MaxIaManager mMaxIaManager;
    private TextToSpeech tts;
    private WebView mWebView;
    private GithubUpdate mGithubUpdate;
    private OkHttpClient clientHt = new OkHttpClient();
    private static final MediaType JSONHt = MediaType.parse("application/json; charset=utf-8");
    public JSInterface(MainActivity c, WebView webView) {
        mContext = c;
        mWebView = webView;
        mMaxIaManager = new MaxIaManager((Context) c);
        mGithubUpdate = new GithubUpdate((Context) c);
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
        mWebView.destroy();
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
            Response response = null;
            try{
                response = clientHt.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                return response.body().string();
            }finally {
                if(response != null) response.close();
            }
        }else{
            Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
            Response response = null;
            try{
                response = clientHt.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                return response.body().string();
            }finally {
                if(response != null) response.close();
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
    public void setChat(String chat){
        mMaxIaManager.setHistory(chat);
    }
    @JavascriptInterface
    public String getChat(){
        return mMaxIaManager.getHistory();
    }
    @JavascriptInterface
    public void setModel(int model){
        mMaxIaManager.setModel(model);
    }
    @JavascriptInterface
    public void setPlugins(String arrayjs){
        try{
            org.json.JSONArray jsonArrJS = new org.json.JSONArray(arrayjs);
            int[] arr = new int[jsonArrJS.length()];
            for(int i = 0; i < jsonArrJS.length(); i++){
                arr[i] = jsonArrJS.optInt(i);
            }
            mMaxIaManager.setPlugins(arr);
        }catch(Exception e){
            e.printStackTrace();
        }
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
    public void setPersonalityPrompt(String prompt){
        mMaxIaManager.setPersonalityPrompt(prompt);
    }
    @JavascriptInterface
    public void setCustomSistemPrompt(String prompt){
        mMaxIaManager.setCustomSistemPrompt(prompt);
    }
    @JavascriptInterface
    public void openUrl(String url){
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setShowTitle(true);
        builder.setToolbarColor(Color.parseColor("#FF6200EE"));
        CustomTabsIntent customTabsIntent = builder.build();
        if(url.startsWith("data:") || url.startsWith("intent:") || url.startsWith("package:")){
            url = "javascript:location.href=" + org.json.JSONObject.quote(url) + ";";
        }
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
    public void startSpeechRecognition(){
        mContext.startSpeechRecognition();
    }
    @JavascriptInterface
    public void clearCache(){
        mWebView.clearCache(true);
    }
    @JavascriptInterface
    public boolean isLatestVersionByGithub(){
        return mGithubUpdate.isLatestVersionByGithub(mWebView);
    }
    @JavascriptInterface
    public void downloadUpdate(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mGithubUpdate.downloadUpdate(mContext);
            }
        }).start();
    }
    @JavascriptInterface
    public long getSizeApkUpdate(){
        return mGithubUpdate.getSizeApk();
    }
    @JavascriptInterface
    public String getDescriptionVer(){
        return mGithubUpdate.getDescriptionVer();
    }
    @JavascriptInterface
    public void saveImageGen(String base64data){
        try{
            byte[] decodedBytes = android.util.Base64.decode(base64data.split(",")[1], android.util.Base64.DEFAULT);
            File file = new File(android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS), "GenImg_"+System.currentTimeMillis()+".png");
            java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
            fos.write(decodedBytes);
            fos.close();
            mWebView.post(new Runnable(){
                @Override
                public void run(){
                    mWebView.loadUrl("javascript:alert('Imagen guardada en: '+" + org.json.JSONObject.quote(file.getAbsolutePath()) + ");");
                }
            });
        }catch(Exception e){
            e.printStackTrace();
            mWebView.post(new Runnable(){
                @Override
                public void run(){
                    mWebView.loadUrl("javascript:alert('Error al guardar la imagen: '+" + org.json.JSONObject.quote(e.getMessage()) + ");");
                }
            });
        }
    }
}

