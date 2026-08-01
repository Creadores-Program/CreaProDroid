package org.CreadoresProgram.CreaProDroid.IA.Plugins;
import android.content.Context;
public class CreaLink extends PluginIA {
    public CreaLink(Context context){
        super(context);
    }
    @Override
    public String getInfo(){
        return "[CreaLink]\n**si el plugin AvancedActions esta desactivado ignora este plugin**\nEn la opcion url de tu json de respuesta puedes usar estos intents para conectar con apps de Creadores Program (si estan instaladas)\n- CreaTV\n  - package: org.CreadoresProgram.CreaTv\n  - formato de intent: para abrir un live o stream de Twitch, Kick, YouTube o TikTok puedes abrir CreaTV con la clase org.CreadoresProgram.CreaTv.StreamActivity, con data del url y algunos extras!, extras que existen:\n    - org.CreadoresProgram.CreaTv.QUALITY: calidad del stream (solamente hay 'link_worst' y 'link_best')\n    - org.CreadoresProgram.CreaTv.ONCHAT: para abrir el chat (solo disponible para streams de Twitch)\n  - Ejemplo de intent: intent://twitch.tv/trollhunters501#Intent;scheme=https;package=org.CreadoresProgram.CreaTv;component=org.CreadoresProgram.CreaTv/.StreamActivity;end\nUsa los intents si el usuario tiene las apps anteriores y te pide algo incluido en los intents.";
    }
}
