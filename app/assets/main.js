var apiKey;
var userName;
var filesI = "";
if (!String.prototype.startsWith) {
    String.prototype.startsWith = function(search, pos) {
        pos = pos || 0;
        return this.substring(pos, pos + search.length) === search;
    };
}
if(localStorage.getItem("apiKey") == null){
    apiKey = prompt("Escribe tu API key:");
    if(apiKey == null || apiKey.trim() == ""){
        alert("No se puede continuar sin la API key.");
        Android.finish();
    }
    try{
        Android.promptGemini("Este Es Un Test de ti porfavor responde un Saludo!", apiKey);
    }catch(e){
        alert("Error Key Invalida!");
        Android.finish();
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
    }
    Android.setUserName(userName);
    localStorage.setItem("userName", userName);
}else{
    userName = localStorage.getItem("userName");
    Android.setUserName(userName);
}
function sendMessage(msg, isSpeak) {
    sendToHtmlUser(msg);
    var prompIAJson = Android.promptGemini(msg, apiKey);
    var subPrompIAJson;
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
    var responMSGIA = Android.mdToHtml(subPrompIAJson.message);
    if(subPrompIAJson.genImg != null && subPrompIAJson.genImg.trim() != ""){
        try{
            responMSGIA += "<br/><img src='"+Android.genImg(subPrompIAJson.genImg)+"' alt='Imagen Generada'/>";
        }catch(e){
            responMSGIA += "<br/>Error al generar la imagen.";
        }
    }
    sendToHtml(responMSGIA);
    if(isSpeak){
        Android.speak(responMSGIA);
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
    function sendToHtml(msg){
        var chatfj = document.getElementById("Chat");
        var chatIAd = document.createElement("div");
        chatIAd.classList.add("message", "bot", "clearfix");
        var IAavatar = document.createElement("img");
        IAavatar.src = "./resources/AvatarIA.jpeg";
        IAavatar.classList.add("avatar");
        chatIAd.appendChild(IAavatar);
        var chatIAdText = document.createElement("div");
        chatIAdText.classList.add("bubble");
        chatIAdText.innerHTML = msg;
        chatIAd.appendChild(chatIAdText);
        chatfj.appendChild(chatIAd);
    }
    function sendToHtmlUser(msg){
        var chatfj = document.getElementById("Chat");
        var chatUserd = document.createElement("div");
        chatUserd.classList.add("message", "user", "clearfix");
        var chatUserdText = document.createElement("div");
        chatUserdText.classList.add("bubble");
        chatUserdText.innerHTML = msg;
        chatUserd.appendChild(chatUserdText);
        chatfj.appendChild(chatUserd);
    }
}
function handleFileChange(event) {
    alert("Procesando archivos...");
    for(var i = 0; i < event.target.files.length; i++){
        var file = event.target.files[i];
        if(!file) continue;
        filesI += "[File:"+file.name + "]\n";
        var reader = new FileReader();
        reader.onload = function(e) {
            var fileContent = e.target.result;
            filesI += fileContent + "\n[/File:"+file.name+"]\n";
        }
        reader.onerror = function(e) {
            alert("Error leyendo el archivo"+file.name, e);
        }
        reader.readAsText(file);
    }
    alert("Archivos procesados, puedes enviar el mensaje ahora.");
}