package org.CreadoresProgram.CreaProDroid.IA.Plugins;
import android.os.Build;
import java.util.Locale;
import android.content.Context;
public class DeviceInfo extends PluginIA{
    public DeviceInfo(Context context){
        super(context);
    }
    @Override
    public String getInfo(){
        return "[Device Info]\nMarca: " + Build.MANUFACTURER + "\nModelo: " + Build.MODEL + "\nDispositivo: " + Build.DEVICE + "\nProducto: " + Build.PRODUCT + "\nVersión Android: " + Build.VERSION.RELEASE + "\nAPI Level: " + Build.VERSION.SDK_INT + "\n\nIdioma: " + Locale.getDefault().getDisplayLanguage() + "\nPaís: " + Locale.getDefault().getCountry() + "";
    }
}