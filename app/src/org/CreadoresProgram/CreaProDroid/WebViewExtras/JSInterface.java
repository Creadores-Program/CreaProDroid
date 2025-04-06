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
import org.CreadoresProgram.CreaProDroid.IA.MaxIaManager;
import android.speech.tts.TextToSpeech;
import java.util.Locale;
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
                    tts.setLanguage(Locale.getDefault());
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
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
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
}