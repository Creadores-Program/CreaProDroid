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
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;

public class GithubUpdate{
    private String repoUrl = "https://api.github.com/repos/CreadoresProgram/CreaProDroid/releases/latest";
    private String currentVersion;
    private String urlDownload;
    private long sizeApkDown;
    private OkHttpClient client = new OkHttpClient();
    public GithubUpdate(Context context){
        currentVersion = getCurrentVersionName(context);
        File outputFile = new File(context.getExternalFilesDir(null), "CreaProDroid.apk");
        if(outputFile.exists()){
            outputFile.delete();
        }
    }
    public void downloadUpdate(Context context, WebView view){
        Request request = new Request.Builder()
            .url(urlDownload)
            .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Mobile Safari/537.36")
            .build();
        try(Response response = client.newCall(request).execute()){
            if (response.isSuccessful() && response.body() != null) {
                InputStream inputStream = response.body().byteStream();
                File outputFile = new File(context.getExternalFilesDir(null), "CreaProDroid.apk");
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                byte[] buffer = new byte[2048];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
                fileOutputStream.close();
                inputStream.close();
                installApk(context, outputFile);
            }else{
                throw new RuntimeException("Ocurrió un error desconocido al Descargar el apk!");
            }
        }catch(Exception err){
            err.printStackTrace();
            view.post(new Runnable(){
                    @Override
                    public void run(){
                        view.loadUrl("javascript:alert('Ocurrio un error Desconocido al Descargar la actualización!');");
                    }
            });
        }
    }
    public boolean isLatestVersionByGithub(){
        Request request = new Request.Builder()
                .url(repoUrl)
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Mobile Safari/537.36")
                .addHeader("Accept", "application/vnd.github+json")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray assetsGithub = jsonObject.getJSONArray("assets");
                for(int i = 0; i < assetsGithub.length(); i++){
                    JSONObject asset = assetsGithub.getJSONObject(i);
                    if(asset.getString("name").endsWith(".apk")){
                        urlDownload = asset.getString("browser_download_url");
                        sizeApkDown = asset.getLong("size");
                    }
                }
                String latestVersion = jsonObject.getString("tag_name");
                return latestVersion.equals(currentVersion);
            }else{
                throw new RuntimeException("Error al verificar versión");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
    private void installApk(Context context, File apkFile){
        if (apkFile.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
    public long getSizeApk(){
        return sizeApkDown;
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
