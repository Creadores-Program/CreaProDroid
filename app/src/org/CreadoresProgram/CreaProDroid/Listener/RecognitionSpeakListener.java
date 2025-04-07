package org.CreadoresProgram.CreaProDroid.Listener;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.webkit.WebView;
import android.os.Bundle;
import java.util.ArrayList;
public class RecognitionSpeakListener implements RecognitionListener {
    private WebView mWebView;
    public RecognitionSpeakListener(WebView mWebView) {
        this.mWebView = mWebView;
    }
    @Override
    public void onReadyForSpeech(Bundle params) {
        mWebView.evaluateJavascript("window.speechSynthesisAndroid.onListeningReady();", null);
    }
    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (matches != null && !matches.isEmpty()) {
            String result = matches.get(0);
            mWebView.evaluateJavascript("window.speechSynthesisAndroid.onSpeechResult("+org.json.JSONObject.quote(result)+");", null);
        }
    }
    @Override
    public void onError(int error) {
        mWebView.evaluateJavascript("window.speechSynthesisAndroid.onSpeechError("+error+");", null);
    }
    @Override
    public void onBeginningOfSpeech() {}
    @Override
    public void onEndOfSpeech() {}
    @Override
    public void onBufferReceived(byte[] buffer) {}
    @Override
    public void onPartialResults(Bundle partialResults) {}
    @Override
    public void onEvent(int eventType, Bundle params) {}
    @Override
    public void onRmsChanged(float rmsdB) {}
}