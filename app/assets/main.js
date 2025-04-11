var apiKey;
var userName;
var filesI = "";
if (!String.prototype.startsWith) {
    String.prototype.startsWith = function(search, pos) {
        pos = pos || 0;
        return this.substring(pos, pos + search.length) === search;
    };
}
if (!String.prototype.trim) {
    String.prototype.trim = function() {
        return this.replace(/^\s+|\s+$/g, '');
    };
}
if(localStorage.getItem("apiKey") == null){
    apiKey = prompt("Escribe tu API key:");
    if(apiKey == null || apiKey.trim() == ""){
        alert("No se puede continuar sin la API key.");
        Android.finish();
        throw new Error("No se puede continuar sin la API key.");
    }
    try{
        if(Android.promptGemini("Este Es Un Test de ti porfavor responde un Saludo!", apiKey) == "{{KeyInvalidTest74284}}") throw new Error("Error Key Invalida!");
    }catch(e){
        alert("Error Key Invalida!");
        Android.finish();
        throw new Error("Error Key Invalida!");
    }
    Android.clearChat();
    localStorage.setItem("apiKey", apiKey);
}else{
    apiKey = localStorage.getItem("apiKey");
}
if(localStorage.getItem("userName") == null){
    userName = prompt("Escribe tu nombre:");
    if(userName == null || userName.trim() == ""){
        alert("No se puede continuar sin el nombre.");
        Android.finish();
        throw new Error("No se puede continuar sin el nombre.");
    }else if(userName.length < 5 || userName.length > 20){
        alert("El nombre debe tener entre 5 y 20 caracteres.");
        Android.finish();
        throw new Error("El nombre debe tener entre 5 y 20 caracteres.");
    }
    Android.setUserName(userName);
    localStorage.setItem("userName", userName);
}else{
    userName = localStorage.getItem("userName");
    Android.setUserName(userName);
}
function sendMessage(msg, isSpeak) {
    Android.stopSpeak();
    sendToHtmlUser(msg.split("[File:")[0]);
    var prompIAJson = Android.promptGemini(msg, apiKey);
    var subPrompIAJson = "";
    try{
        subPrompIAJson = JSON.parse(prompIAJson);
    }catch(e){
        if(prompIAJson.startsWith("{")){
            sendToHtml("Hubo un error al procesar la respuesta de la IA, porfavor intenta de nuevo.");
        }else{
            sendToHtml(prompIAJson);
        }
        return;
    }
    var responMSGIA = MarkdownToHtml.parse(subPrompIAJson.message);
    if(subPrompIAJson.genImg != null && subPrompIAJson.genImg.trim() != ""){
        try{
            var genimghjkfr = Android.genImg(subPrompIAJson.genImg, apiKey);
            if(genimghjkfr == null || genimghjkfr == "Hubo un error al generar la imagen, por favor intenta de nuevo.") throw new Error("Hubo un error al generar la imagen, por favor intenta de nuevo.");
            responMSGIA += "<br/><button style='background: url(\"./resources/download.png\") 50% 50% no-repeat; background-size: contain;' onclick='Android.openUrl(\""+genimghjkfr+"\");'></button><img src='"+genimghjkfr+"' alt='Imagen Generada'/>";
        }catch(e){
            responMSGIA += "<br/>Hubo un error al generar la imagen, por favor intenta de nuevo.";
        }
    }
    sendToHtml(responMSGIA);
    if(isSpeak){
        Android.stopSpeak();
        Android.speak(stripHtml(responMSGIA));
    }
    if(subPrompIAJson.openApp != null && subPrompIAJson.openApp.trim() != ""){
        try{
            Android.openApp(subPrompIAJson.openApp);
            sendToHtml("Abriendo la app...");
        }catch(e){
            sendToHtml("Error al abrir la app!");
        }
    }
    if(subPrompIAJson.openUrl != null && subPrompIAJson.openUrl.trim() != ""){
        if(confirm("Quieres abrir la url "+subPrompIAJson.openUrl+"?")){
            Android.openUrl(subPrompIAJson.openUrl);
            sendToHtml("Abriendo la url...");
        }
    }
    function stripHtml(html) {
        var tempDiv = document.createElement("div");
        tempDiv.innerHTML = html;
        var codeElements = tempDiv.querySelectorAll("code");
        for(var i = 0; i < codeElements.length; i++){
            codeElements[i].remove();
        }
        return tempDiv.textContent;
    }
    function sendToHtml(msg){
        var chatfj = document.getElementById("Chat");
        var chatIAd = document.createElement("div");
        chatIAd.className += "message bot clearfix";
        var IAavatar = document.createElement("img");
        IAavatar.src = "./resources/AvatarIA.jpeg";
        IAavatar.className += "avatar";
        chatIAd.appendChild(IAavatar);
        var djdfiimtemBtn = document.createElement("button");
        djdfiimtemBtn.style.background = 'url("./resources/volume.png") 50% 50% no-repeat';
        djdfiimtemBtn.style.backgroundSize = 'contain';
        djdfiimtemBtn.textChat = stripHtml(msg);
        djdfiimtemBtn.onclick = function() {
            Android.stopSpeak();
            Android.speak(this.textChat);
        }.bind(djdfiimtemBtn);
        chatIAd.appendChild(djdfiimtemBtn);
        var copymsghkv = document.createElement("button");
        copymsghkv.style.background = 'url("./resources/copy.png") 50% 50% no-repeat';
        copymsghkv.style.backgroundSize = 'contain';
        copymsghkv.textChat = stripHtml(msg);
        copymsghkv.onclick = function() {
            Android.copyText(this.textChat);
            alert("Texto copiado!");
        }.bind(copymsghkv);
        chatIAd.appendChild(copymsghkv);
        var chatIAdText = document.createElement("div");
        chatIAdText.className += "bubble";
        chatIAdText.innerHTML = msg;
        chatIAd.appendChild(chatIAdText);
        chatfj.appendChild(chatIAd);
    }
    function sendToHtmlUser(msg){
        var chatfj = document.getElementById("Chat");
        var chatUserd = document.createElement("div");
        chatUserd.className += "message user clearfix";
        var chatUserdText = document.createElement("div");
        chatUserdText.className += "bubble";
        chatUserdText.textContent = msg;
        chatUserd.appendChild(chatUserdText);
        chatfj.appendChild(chatUserd);
    }
}
function handleFileChange(Str, name) {
    alert("Procesando archivo...");
    filesI += "[File:"+name + "]\n"+Android.readFile(Str) + "\n[/File:"+name+"]\n";
    alert("Archivo procesado, puedes enviar el mensaje ahora.");
}
function onSpeechResult(result) {
    sendMessage(result + filesI, true);
}
function onSpeechError(error) {
    alert("Error en el reconocimiento de voz: "+ error);
}
