var apiKey;
var userName;
var filesI = "";
var chatHistoryOld = [];
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
if(localStorage.getItem("historyChats") == null){
    localStorage.setItem("historyChats", "[]");
}else{
    chatHistoryOld = JSON.parse(localStorage.getItem("historyChats"));
}
function saveChatHistory(){
    chatHistoryOld.push({
        "name": Date.now() +" "+ userName + Math.random(),
        "history": JSON.parse(Android.getChat())
    });
    if(chatHistoryOld.length > 20){
        chatHistoryOld.shift();
    }
    localStorage.setItem("historyChats", JSON.stringify(chatHistoryOld));
}
function loadChatHistory(name){
    var chatHistoryloda;
    for(var i = 0; i < chatHistoryOld.length; i++){
        if(chatHistoryOld[i].name == name){
            chatHistoryloda = chatHistoryOld[i].history;
            break;
        }
    }
    if(chatHistoryloda == null){
        alert("No se encontro el historial de chat.");
        return;
    }
    if(JSON.parse(Android.getChat()).length > 0){
        saveChatHistory();
    }
    document.getElementById("Chat").innerHTML = "";
    for(var i = 0; i < chatHistoryloda.length; i++){
        if(chatHistoryloda[i].role == "user"){
            sendToHtmlUser(chatHistoryloda[i].parts[0].text.split("[File:")[0]);
        }else{
            sendToHtml(JSON.parse(chatHistoryloda[i].parts[0].text).message);
        }
    }
    Android.setChat(JSON.stringify(chatHistoryloda));
    document.getElementById("backOlCBtn").click();
    updateHistoryChatHtml();
}
function updateHistoryChatHtml(){
    var htmlEmenHCU = document.getElementById("oldChats-container");
    htmlEmenHCU.innerHTML = "";
    for(var i = chatHistoryOld.length - 1; i >= 0; i--){
        var chatHistoryItem = document.createElement("div");
        chatHistoryItem.className += "oldChatItem";
        chatHistoryItem.innerHTML = chatHistoryOld[i].name;
        chatHistoryItem.name = chatHistoryOld[i].name;
        chatHistoryItem.onclick = function() {
            loadChatHistory(this.name);
        }.bind(chatHistoryItem);
        htmlEmenHCU.appendChild(chatHistoryItem);
        htmlEmenHCU.appendChild(document.createElement("br"));
    }
}
updateHistoryChatHtml();
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
    if(subPrompIAJson.genImg != null && subPrompIAJson.genImg.trim() != "" && subPrompIAJson.genImg.toLowerCase() != "string"){
        try{
            var genimghjkfr = Android.genImg(subPrompIAJson.genImg, apiKey);
            if(genimghjkfr == null || genimghjkfr == "Hubo un error al generar la imagen, por favor intenta de nuevo.") throw new Error("Hubo un error al generar la imagen, por favor intenta de nuevo.");
            responMSGIA += "<br/><button style='background: url(\"./resources/download.png\") 50% 50% no-repeat; background-size: contain;' onclick='Android.saveImageGen(\""+genimghjkfr+"\");'></button><img src='"+genimghjkfr+"' alt='Imagen Generada'/>";
        }catch(e){
            responMSGIA += "<br/>Hubo un error al generar la imagen, por favor intenta de nuevo.";
        }
    }
    sendToHtml(responMSGIA);
    if(isSpeak){
        Android.stopSpeak();
        Android.speak(stripHtml(responMSGIA));
    }
    if(subPrompIAJson.openApp != null && subPrompIAJson.openApp.trim() != "" && subPrompIAJson.openApp.toLowerCase() != "string"){
        try{
            Android.openApp(subPrompIAJson.openApp);
            sendToHtml("Abriendo la app...");
        }catch(e){
            sendToHtml("Error al abrir la app!");
        }
    }
    if(subPrompIAJson.openUrl != null && subPrompIAJson.openUrl.trim() != "" && subPrompIAJson.openUrl.toLowerCase() != "string"){
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
}
function handleFileChange(Str, name) {
    alert("Procesando archivo...");
    filesI += "[File:"+name + "]\n"+Str + "\n[/File:"+name+"]\n";
    alert("Archivo procesado, puedes enviar el mensaje ahora.");
}
function onSpeechResult(result) {
    sendMessage(result + filesI, true);
}
function onSpeechError(error) {
    alert("Error en el reconocimiento de voz: "+ error);
}
function copyMDcode(button) {
    var codeBlockrgfgbf = button.parentElement;
    if(codeBlockrgfgbf != null){
        Android.copyText(codeBlockrgfgbf.textContent);
        alert("Texto copiado!");
    }
}
function verifyUpdate(alertNoUp){
    if(alertNoUp){
        if(!Android.isLatestVersionByGithub() && confirm("Nueva Actualizacion Disponible!\nPesa: "+(Android.getSizeApkUpdate() / (1024 * 1024))+"mb\nPlataforma de donde se Descarga: Github.com\n¿Quieres Actualizar?")){
            Android.downloadUpdate();
            alert("Actualizando...\nNo Cierres la app!");
        }else{
            alert("No hay actualizaciones disponibles.\nO cancelaste la descarga.");
        }
    }else{
        var nowdatesdcnjd = new Date();
        if(localStorage.getItem("update") != nowdatesdcnjd.getDay()){
            localStorage.setItem("update", nowdatesdcnjd.getDay());
            if(!Android.isLatestVersionByGithub() && confirm("Nueva Actualizacion Disponible!\nPesa: "+(Android.getSizeApkUpdate() / (1024 * 1024))+"mb\nPlataforma de donde se Descarga: Github.com\n¿Quieres Actualizar?")){
                Android.downloadUpdate();
                alert("Actualizando...\nNo Cierres la app!");
            }
        }
    }
}
verifyUpdate();
