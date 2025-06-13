package org.CreadoresProgram.CreaProDroid.IA.Plugins;
import android.os.Build;
import java.util.Locale;
public class DeviceInfo extends PluginIA{
    @Override
    public String getInfo(){
        return "[Device Info]\nMarca: " + Build.MANUFACTURER + "\nModelo: " + Build.MODEL + "\nDispositivo: " + Build.DEVICE + "\nProducto: " + Build.PRODUCT + "\nVersión Android: " + Build.VERSION.RELEASE + "\nAPI Level: " + Build.VERSION.SDK_INT + "\n\nIdioma: " + Locale.getDefault().getDisplayLanguage() + "\nPaís: " + Locale.getDefault().getCountry() + "";
    }
}