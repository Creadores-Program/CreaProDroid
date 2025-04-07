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
import android.speech.SpeechRecognizer;
import org.CreadoresProgram.CreaProDroid.Listener.RecognitionSpeakListener;
import android.content.Context;
import java.util.Locale;

public class MainActivity extends Activity {
    public static final int FILE_UPLOAD_REQUEST_CODE = 1;
    private WebView webview;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
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
        startingRecogniser(this);
        webView.loadUrl("file:///android_asset/index.html");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_UPLOAD_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri resultUri = data.getData();
            String jsCallback = "handleFileChange("+org.json.JSONObject.quote(resultUri.toString())+", "+org.json.JSONObject.quote(getFileName(resultUri))+");";
            webview.evaluateJavascript(jsCallback, null);
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
    private void startingRecogniser(Context mCon){
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(mCon);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizer.setRecognitionListener(new RecognitionSpeakListener(webview));
    }
    public void startSpeechRecognition() {
        if (speechRecognizer != null) {
            speechRecognizer.startListening(speechRecognizerIntent);
        }else{
            webview.evaluateJavascript("window.speechSynthesisAndroid.onSpeechError('No existe un Reconocimiento de Voz o Aun no esta Cargado!');", null);
        }
    }
}