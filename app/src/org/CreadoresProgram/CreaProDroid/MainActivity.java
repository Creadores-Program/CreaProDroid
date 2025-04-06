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

public class MainActivity extends Activity {
    public static final int FILE_UPLOAD_REQUEST_CODE = 1;
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
        webView.addJavascriptInterface(new JSInterface(this, webView), "Android");
        webView.loadUrl("file:///android_asset/index.html");
        this.webview = webView;
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
}