var apiKey;
var userName;
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
    var userName = localStorage.getItem("userName");
}
function sendMessage() {
}