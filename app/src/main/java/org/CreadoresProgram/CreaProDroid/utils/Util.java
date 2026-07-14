package org.CreadoresProgram.CreaProDroid.utils;

import android.os.Build;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.Intent;
import android.net.Uri;
import android.graphics.Color;
import androidx.browser.customtabs.CustomTabsIntent;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;

public class Util{
    public static final Charset dataCodeStr;
    static{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            dataCodeStr = KitkatCharset.getUTF8();
        }else{
            dataCodeStr = Charset.forName("UTF-8");
        }
    }
    private static class KitkatCharset{
        static Charset getUTF8(){
            return StandardCharsets.UTF_8;
        }
    }
    public static String readAssetAsString(AssetManager assetManager, String filePath) {
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            inputStream = assetManager.open(filePath);
            outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return new String(outputStream.toByteArray(), dataCodeStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (outputStream != null) {
                try { outputStream.close(); } catch (IOException ignored) {}
            }
            if (inputStream != null) {
                try { inputStream.close(); } catch (IOException ignored) {}
            }
        }
    }
    public static void openUrl(String url, Context context){
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setShowTitle(true);
        builder.setToolbarColor(Color.parseColor("#FF6200EE"));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}
