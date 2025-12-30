package org.CreadoresProgram.CreaProDroid.update;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.json.JSONArray;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.webkit.WebView;
import android.content.Intent;
import android.net.Uri;
import android.graphics.Color;
import android.support.customtabs.CustomTabsIntent;
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

public class GithubUpdate{
    private String repoUrl = "https://api.github.com/repos/Creadores-Program/CreaProDroid/releases/latest";
    private String currentVersion;
    private String urlDownload;
    private long sizeApkDown;
    private String descriptionVer;
    private OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(180, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build();
    public GithubUpdate(Context context){
        currentVersion = getCurrentVersionName(context);
        File outputFile = new File(context.getExternalFilesDir(null), "CreaProDroid.apk");
        if(outputFile.exists()){
            outputFile.delete();
        }
    }
    public void downloadUpdate(Context context){
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setShowTitle(true);
        builder.setToolbarColor(Color.parseColor("#FF6200EE"));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(urlDownload));
    }
    public boolean isLatestVersionByGithub(WebView view){
        Request request = new Request.Builder()
                .url(repoUrl)
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Mobile Safari/537.36")
                .addHeader("Accept", "application/vnd.github+json")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray assetsGithub = jsonObject.getJSONArray("assets");
                for(int i = 0; i < assetsGithub.length(); i++){
                    JSONObject asset = assetsGithub.getJSONObject(i);
                    if(asset.getString("name").endsWith(".apk")){
                        urlDownload = jsonObject.getString("html_url");
                        sizeApkDown = asset.getLong("size");
                        descriptionVer = jsonObject.getString("name")+"\n"+jsonObject.getString("body");
                    }
                }
                String latestVersion = jsonObject.getString("tag_name");
                view.post(new Runnable(){
                    @Override
                    public void run(){
                        view.loadUrl("javascript:window.errrorVerifyVersion = false;");
                    }
                });
                return latestVersion.equals(currentVersion);
            }else{
                throw new RuntimeException("Error al verificar versión");
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.post(new Runnable(){
                @Override
                public void run(){
                    view.loadUrl("javascript:window.errrorVerifyVersion = true;");
                }
            });
            return true;
        }finally{
            try{
                if(response != null){
                    response.close();
                }
            }catch(Exception err){
                err.printStackTrace();
            }
        }
    }
    public long getSizeApk(){
        return sizeApkDown;
    }
    public String getDescriptionVer(){
        return descriptionVer;
    }
    private String getCurrentVersionName(Context context){
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}


