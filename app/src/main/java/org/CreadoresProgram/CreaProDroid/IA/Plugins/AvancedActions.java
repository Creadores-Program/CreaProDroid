package org.CreadoresProgram.CreaProDroid.IA.Plugins;
import android.content.Context;
public class AvancedActions extends PluginIA {
    public AvancedActions(Context context){
        super(context);
    }
    @Override
    public String getInfo(){
        return "[AvancedActions]\nEn la opcion url de tu json de respuesta puedes usar URL schemes como:\n tel: para llamar ejemplo: tel:1412948282\nsms: para enviar mensajes ejemplo: sms:1412948282 o con contenido sms:1412948282?body=Hola\nmailto: para enviar correos ejemplo: mailto:creaprodroid@algo.com o con contenido mailto:creaprodroid@algo.com?subject=Hola&body=Hola\ngeo: para abrir una direccion en maps o el GPS ejemplo: geo:40.4167,-3.70325 o geo:0,0?q=Nombre+del+Lugar\nmarket: para abrir la tienda de apps ejemplo: market://details?id=org.CreadoresProgram.CreaProDroid\nintent: para lanzar un intent perzonalizado ejemplo: intent://scan/#Intent;scheme=zxing;package=com.google.zxing.client.android;end\ncontent: para acceder a contenido interno desde el navegador ejemplo: content://people/1\nfile: acceso a archivos locales desde el navegador ejemplo: file:///sdcard/ejemplo.txt\ndata: para incrustar datos en el navegador ejemplo: data:text/html,<!DOCTYPE html><html><body><h1>Hola</h1></body></html>\njavascript: para ejecutar codigo javascript en el navegador ejemplo: javascript:alert('Hola')\ncallto: para realizar llamadas usando apps VoIP ejemplo: callto:usuario\nsip: para realizar llamadas SIP ejemplo: sip:creaprodroid@algo.com\npackage: para abrir detalles de una app ejemplo: package:org.CreadoresProgram.CreaProDroid\nwhatsapp: para abrir WhatsApp ejemplo: whatsapp://send?phone=1412948282\nspotify: abre contenido en spotify ejemplo: spotify:track:ID\nEn fin cualquier URL scheme.";
    }
}