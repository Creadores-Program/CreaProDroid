package org.CreadoresProgram.CreaProDroid.update;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class GithubUpdate{
    private String repoUrl = "https://api.github.com/repos/CreadoresProgram/CreaProDroid/releases/latest";
    private String currentVersion;
    public GithubUpdate(Context context){
        currentVersion = getCurrentVersionName(context);
    }
    public boolean isLatestVersionByGithub(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(repoUrl)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                String latestVersion = jsonObject.getString("tag_name");
                return !latestVersion.equals(currentVersion);
        }catch (Exception e) {
            e.printStackTrace();
        }
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