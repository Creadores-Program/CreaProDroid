package org.CreadoresProgram.CreaProDroid.WebViewExtras;
import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebChromeClient;
import android.webkit.JsResult;
import android.webkit.WebStorage;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.webkit.JsPromptResult;
import android.graphics.Color;
import android.view.ContextThemeWrapper;
import android.os.Build;

public class ChromeExtra extends WebChromeClient {
    private Context context;
    private ContextThemeWrapper themeInput;
    public ChromeExtra(Context context) {
        this.context = context;
        this.themeInput = new ContextThemeWrapper(this.context, android.R.style.Theme_Holo_Light_Dialog);
    }
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        new AlertDialog.Builder(context, android.R.style.Theme_Holo_Light_Dialog)
            .setTitle("CreaProDroid")
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            })
            .setCancelable(false)
            .create().show();
        return true;
    }
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        new AlertDialog.Builder(context, android.R.style.Theme_Holo_Light_Dialog)
            .setTitle("CreaProDroid")
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            })
            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            })
            .setCancelable(false)
            .create().show();
        return true;
    }
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        final EditText input = new EditText(themeInput);
        input.setText(defaultValue);
        new AlertDialog.Builder(context, android.R.style.Theme_Holo_Light_Dialog)
            .setTitle("CreaProDroid")
            .setMessage(message)
            .setView(input)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm(input.getText().toString());
                }
            })
            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            })
            .setCancelable(false)
            .create().show();
        return true;
    }
    @SuppressWarnings("deprecation")
    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            super.onExceededDatabaseQuota(url, databaseIdentifier, currentQuota, estimatedSize, totalUsesQuota, quotaUpdater);
            return;
        }
        quotaUpdater.updateQuota(estimatedSize * 2); 
    }
}
