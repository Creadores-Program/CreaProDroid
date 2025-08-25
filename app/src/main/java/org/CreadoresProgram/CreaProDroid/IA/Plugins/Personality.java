package org.CreadoresProgram.CreaProDroid.IA.Plugins;
import android.content.Context;
public class Personality extends PluginIA {

    public Personality(Context context) {
        super(context);
    }
    @Override
    public String getInfo(){
        return "[Personality]\nen la opcion personality puedes definir o actualizar tu personalidad para todos los futuros chats\nEjemplo:\nEl usuario es una persona amable y optimista, tu intenta siempre buscar el lado positivo de las cosas y tratalo con respeto.";
    }
}