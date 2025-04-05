package org.CreadoresProgram.CreaProDroid.WebViewExtras;
import android.webkit.JavascriptInterface;
import android.content.Context;
import android.webkit.WebView;
import android.net.Uri;
import org.jsoup.Jsoup;
import org.jsoup.Connection;
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
            return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0 Mobile Safari/537.36")
                .header("Content-Type", "application/json")
                .requestBody(data)
                .method(Connection.Method.POST)
                .ignoreContentType(true)
                .execute().body();
        }else{
            return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0 Mobile Safari/537.36")
                .method(Connection.Method.GET)
                .ignoreContentType(true)
                .execute().body();
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
        customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        customTabsIntent.intent.setData(Uri.parse(url));
        mContext.startActivity(customTabsIntent.intent);
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
    public String mdToHtml(String md){
        return mMaxIaManager.procesorMD.markdownToHtml(md);
    }
}