var apiKey;
var userName;
var filesI = "";
var chatHistoryOld = [];
window.langPage = JSON.parse(Android.getLangJson());
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
    apiKey = prompt(window.langPage.escribirApikey);
    if(apiKey == null || apiKey.trim() == ""){
        alert(window.langPage.noContinuarSinApiKey);
        Android.finish();
        throw new Error(window.langPage.noContinuarSinApiKey);
    }
    if(localStorage.getItem("model") == null){
        localStorage.setItem("model", "1");
    }
    Android.setModel(parseInt(localStorage.getItem("model")));
    try{
        if(Android.promptGemini("Este Es Un Test de ti porfavor responde un Saludo!", apiKey) == "{{KeyInvalidTest74284}}") throw new Error(window.langPage.keyInvalida);
    }catch(e){
        alert(window.langPage.keyInvalida);
        Android.finish();
        throw e;
    }
    Android.clearChat();
    localStorage.setItem("apiKey", apiKey);
}else{
    apiKey = localStorage.getItem("apiKey");
}
if(localStorage.getItem("userName") == null){
    userName = prompt(window.langPage.escribeNombre);
    if(userName == null || userName.trim() == ""){
        alert(window.langPage.noContinuarSinNombre);
        Android.finish();
        throw new Error(window.langPage.noContinuarSinNombre);
    }else if(userName.length < 5 || userName.length > 20){
        alert(window.langPage.nombreInvalido);
        Android.finish();
        throw new Error(window.langPage.nombreInvalido);
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
    var timeCreatedCekj = new Date();
    chatHistoryOld.push({
        "name": timeCreatedCekj.getTimezoneOffset() + " "+ timeCreatedCekj.getFullYear() + "/" + timeCreatedCekj.getMonth() + "/" + timeCreatedCekj.getDay()+" "+timeCreatedCekj.getHours()+":"+timeCreatedCekj.getMinutes()+" " + JSON.parse(Android.getChat())[0].parts[0].text.split("[File:")[0] + " " +  Math.random(),
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
            chatHistoryOld.splice(i, 1);
            break;
        }
    }
    if(chatHistoryloda == null){
        alert(window.langPage.chatNoEncontrado);
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
            var subPrompIAJson = JSON.parse(chatHistoryloda[i].parts[0].text);
            var responMSGIA = MarkdownToHtml.parse(subPrompIAJson.message);
            if(subPrompIAJson.genImg != null && subPrompIAJson.genImg.trim() != "" && subPrompIAJson.genImg.toLowerCase() != "string"){
                try{
                    var genimghjkfr = "https://image.pollinations.ai/prompt/"+encodeURIComponent(subPrompIAJson.genImg);
                    responMSGIA += "<br/><button style='background: url(\"./resources/download.png\") 50% 50% no-repeat; background-size: contain;' onclick='var validimgD = this.parentNode.getElementsByTagName(\"img\")[0]; if(!validimgD || validimgD.naturalWidth === 0){ return; } Android.saveImageGen(\""+genimghjkfr+"\");'></button><img src='"+genimghjkfr+"' alt='Imagen Generada'/>";
                }catch(e){
                    responMSGIA += "<br/>"+window.langPage.errorGenImg;
                }
            }
            sendToHtml(responMSGIA);
        }
    }
    Android.setChat(JSON.stringify(chatHistoryloda));
    document.getElementById("backOlCBtn").click();
    updateHistoryChatHtml();
}
function updateHistoryChatHtml(){
    var htmlEmenHCU = document.getElementById("oldChats-container");
    htmlEmenHCU.innerHTML = "";
    for(var i = chatHistoryOld.length - 1; i > -1; i--){
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
function setCustomPrompt(){
    var customPrompt = document.getElementById("customPrompt").value;
    if((customPrompt == null || customPrompt.trim() == "") && localStorage.getItem("customPrompt")){
        if(confirm(window.langPage.eliminarInstrucPersonalizada)){
            localStorage.removeItem("customPrompt");
            Android.setCustomSistemPrompt("");
            alert(window.langPage.instrucPersonalizadaEliminada);
        }
        return;
    }
    if(customPrompt == null || customPrompt.trim() == ""){
        alert(window.langPage.noGuardarInstrucVacia);
        return;
    }
    localStorage.setItem("customPrompt", customPrompt);
    Android.setCustomSistemPrompt(customPrompt);
}
function sendToHtml(msg){
    var chatfj = document.getElementById("Chat");
    var chatIAd = document.createElement("div");
    chatIAd.className += "message bot clearfix";
    var IAavatar = document.createElement("img");
    IAavatar.src = "file:///android_res/drawable/ic_launcher";
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
        alert(window.langPage.textoCopiado);
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
function stripHtml(html) {
    var tempDiv = document.createElement("div");
    tempDiv.innerHTML = html;
    var codeElements = tempDiv.querySelectorAll("code");
    for(var i = 0; i < codeElements.length; i++){
        codeElements[i].remove();
    }
    return tempDiv.textContent;
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
            sendToHtml(window.langPage.errorIA);
        }else{
            sendToHtml(prompIAJson);
        }
        return;
    }
    var responMSGIA = MarkdownToHtml.parse(subPrompIAJson.message);
    if(subPrompIAJson.genImg != null && subPrompIAJson.genImg.trim() != "" && subPrompIAJson.genImg.toLowerCase() != "string"){
        try{
            var genimghjkfr = "https://image.pollinations.ai/prompt/"+encodeURIComponent(subPrompIAJson.genImg);
            responMSGIA += "<br/><button style='background: url(\"./resources/download.png\") 50% 50% no-repeat; background-size: contain;' onclick='var validimgD = this.parentNode.getElementsByTagName(\"img\")[0]; if(!validimgD || validimgD.naturalWidth === 0){ return; } Android.saveImageGen(\""+genimghjkfr+"\");'></button><img src='"+genimghjkfr+"' alt='Imagen Generada'/>";
        }catch(e){
            responMSGIA += "<br/>"+window.langPage.errorGenImg;
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
        if(!subPrompIAJson.openUrl.startsWith("https://") && !subPrompIAJson.openUrl.startsWith("http://")){
            if(confirm("Quieres abrir la accion que va a hacer CreaProDroid?")){
                Android.openUrl(subPrompIAJson.openUrl);
                sendToHtml("Iniciando Accion...");
            }
        }else{
            if(confirm("Quieres abrir la url "+subPrompIAJson.openUrl+"?")){
                Android.openUrl(subPrompIAJson.openUrl);
                sendToHtml("Abriendo la url...");
            }
        }
    }
    if(subPrompIAJson.personality && subPrompIAJson.personality.trim() != "" && subPrompIAJson.personality.toLowerCase() != "string"){
        Android.setPersonalityPrompt(subPrompIAJson.personality);
        localStorage.setItem("personalityPrompt", subPrompIAJson.personality);
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
var pluginsIA;
window.onload = function() {
    if(localStorage.getItem("model") != null && parseInt(localStorage.getItem("model")) < 3){
        Android.setModel(parseInt(localStorage.getItem("model")));
        document.getElementById("modelIA").value = localStorage.getItem("model");
    }else{
        localStorage.setItem("model", "1");
        Android.setModel(parseInt(localStorage.getItem("model")));
    }
    document.getElementById("modelIA").onchange = function() {
        var selectedModel = parseInt(this.value);
        Android.setModel(selectedModel);
        localStorage.setItem("model", selectedModel);
    };
    if(localStorage.getItem("customPrompt")){
        document.getElementById("customPrompt").value = localStorage.getItem("customPrompt");
        Android.setCustomSistemPrompt(localStorage.getItem("customPrompt"));
    }
    if(pluginsIA != null){
        if(pluginsIA.indexOf(0) != -1){
            document.getElementById("pluginDeviceInfo").checked = true;
        }
        if(pluginsIA.indexOf(1) != -1){
            document.getElementById("pluginContacts").checked = true;
        }
        if(pluginsIA.indexOf(2) != -1){
            document.getElementById("pluginAvancedActions").checked = true;
        }
        if(pluginsIA.indexOf(3) != -1){
            document.getElementById("pluginPersonality").checked = true;
            if(localStorage.getItem("personalityPrompt")){
                Android.setPersonalityPrompt(localStorage.getItem("personalityPrompt"));
            }
        }
    }
    document.getElementById("pluginDeviceInfo").onchange = function(){
        if(this.checked){
            pluginsIA.push(0);
            Android.setPlugins(JSON.stringify(pluginsIA));
            localStorage.setItem("pluginsIA", JSON.stringify(pluginsIA));
        }else{
            pluginsIA.splice(pluginsIA.indexOf(0), 1);
            Android.setPlugins(JSON.stringify(pluginsIA));
            localStorage.setItem("pluginsIA", JSON.stringify(pluginsIA));
        }
    };
    document.getElementById("pluginContacts").onchange = function(){
        if(this.checked){
            pluginsIA.push(1);
            Android.setPlugins(JSON.stringify(pluginsIA));
            localStorage.setItem("pluginsIA", JSON.stringify(pluginsIA));
        }else{
            pluginsIA.splice(pluginsIA.indexOf(1), 1);
            Android.setPlugins(JSON.stringify(pluginsIA));
            localStorage.setItem("pluginsIA", JSON.stringify(pluginsIA));
        }
    };
    document.getElementById("pluginAvancedActions").onchange = function(){
        if(this.checked){
            pluginsIA.push(2);
            Android.setPlugins(JSON.stringify(pluginsIA));
            localStorage.setItem("pluginsIA", JSON.stringify(pluginsIA));
        }else{
            pluginsIA.splice(pluginsIA.indexOf(2), 1);
            Android.setPlugins(JSON.stringify(pluginsIA));
            localStorage.setItem("pluginsIA", JSON.stringify(pluginsIA));
        }
    };
    document.getElementById("pluginPersonality").onchange = function(){
        if(this.checked){
            pluginsIA.push(3);
            Android.setPlugins(JSON.stringify(pluginsIA));
            localStorage.setItem("pluginsIA", JSON.stringify(pluginsIA));
        }else{
            pluginsIA.splice(pluginsIA.indexOf(3), 1);
            Android.setPlugins(JSON.stringify(pluginsIA));
            localStorage.setItem("pluginsIA", JSON.stringify(pluginsIA));
        }
    };
    if(!Android.hasPerms()){
        document.getElementById('Home').style.display = 'none'; 
        document.getElementById('ReqPerms').style.display = 'block';
    }
    document.getElementById("inputChat").placeholder = window.langPage.inputChatPlaceholder;
    var elementsQlang = document.querySelectorAll("[langId]");
    for(var idod = 0; idod < elementsQlang.length; idod++){
        var elementQlang = elementsQlang[idod];
        var attrLang = elementQlang.getAttribute("langId");
        if(window.langPage[attrLang]){
            elementQlang.textContent = window.langPage[attrLang];
        }else{
            console.warn("Invalid key " + attrLang);
        }
    }
    document.body.style.opacity = "1";
};

//setPlugins
if(localStorage.getItem("pluginsIA") != null){
    Android.setPlugins(localStorage.getItem("pluginsIA"));
    pluginsIA = JSON.parse(localStorage.getItem("pluginsIA"));
}else{
    pluginsIA = [];
}

//update
function verifyUpdate(alertNoUp){
    if(alertNoUp){
        if(!Android.isLatestVersionByGithub() && confirm("Nueva Actualizacion Disponible!\nPesa: "+(Android.getSizeApkUpdate() / (1024 * 1024))+"mb\nPlataforma de donde se Descarga: Github.com\n"+Android.getDescriptionVer()+"\n¿Quieres Actualizar?")){
            Android.downloadUpdate();
        }else{
            if(window.errrorVerifyVersion){
                alert("Ocurrio un error Desconocido al verificar actualizaciones!");
                return;
            }
            alert("No hay actualizaciones disponibles.\nO cancelaste la descarga.");
        }
    }else{
        var nowdatesdcnjd = new Date();
        if(localStorage.getItem("update") != nowdatesdcnjd.getDay()){
            localStorage.setItem("update", nowdatesdcnjd.getDay());
            if(!Android.isLatestVersionByGithub() && confirm("Nueva Actualizacion Disponible!\nPesa: "+(Android.getSizeApkUpdate() / (1024 * 1024))+"mb\nPlataforma de donde se Descarga: Github.com\n"+Android.getDescriptionVer()+"\n¿Quieres Actualizar?")){
                Android.downloadUpdate();
            }
        }
    }
}
verifyUpdate();

