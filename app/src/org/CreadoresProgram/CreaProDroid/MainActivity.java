package org.CreadoresProgram.CreaProDroid;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import org.CreadoresProgram.CreaProDroid.WebViewExtras.ChromeExtra;
import org.CreadoresProgram.CreaProDroid.WebViewExtras.JSInterface;
import android.database.Cursor;
import android.provider.OpenableColumns;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import java.util.Locale;
import java.util.ArrayList;

public class MainActivity extends Activity {
    public static final int FILE_UPLOAD_REQUEST_CODE = 1;
    public static final int RECOGNIZE_SPEECH_ACTIVITY = 282;
    private WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        WebView webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new ChromeExtra(this));
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webView.setBackgroundColor(Color.BLACK);
        webView.addJavascriptInterface(new JSInterface(this, webView), "Android");
        this.webview = webView;
        webView.loadUrl("file:///android_asset/index.html");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_UPLOAD_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri resultUri = data.getData();
            String jsCallback = "handleFileChange("+org.json.JSONObject.quote(resultUri.toString())+", "+org.json.JSONObject.quote(getFileName(resultUri))+");";
            webview.evaluateJavascript(jsCallback, null);
        }else if(requestCode == RECOGNIZE_SPEECH_ACTIVITY && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if(result != null && !result.isEmpty()) {
                webview.evaluateJavascript("onSpeechResult("+org.json.JSONObject.quote(result.get(0))+");", null);
        }
    }
    @Override
    protected void onDestroy() {
        webview.evaluateJavascript("Android.finish();", null);
        webview.destroy();
        if(speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }
    private String getFileName(Uri uri) {
        String result = null;
        if ("content".equals(uri.getScheme())) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    public void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        try{
            startActivityForResult(intent, RECOGNIZE_SPEECH_ACTIVITY);
        }catch(ActivityNotFoundException e) {
            e.printStackTrace();
            webview.evaluateJavascript("onSpeechError('Tu dispositivo no soporta el reconocimiento por voz!');", null);
        }
    }
}