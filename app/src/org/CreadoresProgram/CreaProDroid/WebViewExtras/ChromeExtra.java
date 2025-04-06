package org.CreadoresProgram.CreaProDroid.WebViewExtras;
import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebChromeClient;
import android.webkit.JsResult;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.webkit.JsPromptResult;
import android.graphics.Color;
public class ChromeExtra extends WebChromeClient {
    private Context context;
    public ChromeExtra(Context context) {
        this.context = context;
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
            }).create().show();
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
            }).create().show();
        return true;
    }
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        final EditText input = new EditText(context);
        input.setText(defaultValue);
        input.setTextColor(Color.BLACK);
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
            }).create().show();
        return true;
    }
}