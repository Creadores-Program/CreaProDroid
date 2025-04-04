var apiKey;
var userName;
var filesI = "";
if(localStorage.getItem("apiKey") == null){
    apiKey = prompt("Escribe tu API key:");
    if(apiKey == null || apiKey == ""){
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
    if(userName == null || userName == ""){
        alert("No se puede continuar sin el nombre.");
        Android.finish();
    }
    Android.setUserName(userName);
    localStorage.setItem("userName", userName);
}else{
    userName = localStorage.getItem("userName");
    Android.setUserName(userName);
}
function sendMessage(msg) {
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