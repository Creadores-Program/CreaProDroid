# ServerWebGamePost
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![JSON](https://img.shields.io/badge/json-5E5C5C?style=for-the-badge&logo=json&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-323330?style=for-the-badge&logo=javascript&logoColor=F7DF1E)
![NPM](https://img.shields.io/badge/npm-CB3837?style=for-the-badge&logo=npm&logoColor=white)
![NodeJS](https://img.shields.io/badge/Node%20js-339933?style=for-the-badge&logo=nodedotjs&logoColor=white)
![Maven](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Python](https://img.shields.io/badge/python-3670A0?style=for-the-badge&logo=python&logoColor=ffdd54)

[![](https://jitpack.io/v/Creadores-Program/ServerWebGamePost.svg)](https://jitpack.io/#Creadores-Program/ServerWebGamePost)

A multiplayer server system based on web servers with POST method and datapacks in Json

With this library you can make your own multiplayer server software for your game!

The usage is simple and easy to use! You can see the available programming languages and how to use them in the [Wiki of this project!](https://github.com/Creadores-Program/ServerWebGamePost/wiki)
by Creadores Program ©2025
Wiki de ServerWebGamePost:
Java Edition:
# Maven
In maven you must add the following repository:
```xml
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>
```

I in dependencies:
```xml
<dependency>
 <groupId>com.github.Creadores-Program</groupId>
 <artifactId>ServerWebGamePost</artifactId>
 <version>1.2.0</version>
</dependency>
```
## Or by the releases jar file
```xml
<dependency>
 <groupId>org.creadoresprogram</groupId>
 <artifactId>ServerWebGamePost</artifactId>
 <version>1.2.0</version>
</dependency>
```
# Create Server
## Processing Datapackets
First you need to extend the org.CreadoresProgram.ServerWebGamePost.server.ProcessDatapackServer class

And to process the datapackets that the client sends you must implement Override to the processDatapack function

Something like that:
```java
@Override
public void processDatapack(@NonNull JSONObject datapack){
      //code...
      //JSONObject= com.alibaba.fastjson2.JSONObject
      //NonNull = lombok.NonNull
      //this.server = org.CreadoresProgram.ServerWebGamePost.server.ServerWebGamePostServer instance
}
```

## Send Datapackets
Simply in the org.CreadoresProgram.ServerWebGamePost.server.ServerWebGamePostServer class Execute the sendDataPacket function

Something like that:
```java
//ServerWebGamePostServer = server
server.sendDataPacket(identifier, json); //identifier you must obtain from the datapacket that the client gives in its datapacket (it is a String) json Is com.alibaba.fastjson2.JSONObject
```

## Ban IP
You can ban IPs with the banIp and unbanIp functions in ServerWebGamePostServer class (The IP is String and is only that argument)

## Filter Origins
If you never run filter origins the origin header will be "*" if you run it it will only accept the given origins (Origin Header) The functions addFilterOrigin and removeFilterOrigin in ServerWebGamePostServer class only accept one string argument.

## Create instance
You must create a new org.CreadoresProgram.ServerWebGamePost.server.ServerWebGamePostServer instance
```java
ServerWebGamePostServer server = new ServerWebGamePostServer(port, imgSrc, procecerDatapacks);
// port = int(Here you must specify the port where the server opens), imgSrc = String (Directory in string of the server icon type image can be null), procecerDatapacks = ProcessDatapackServer (Datapacket handling instance class)
```
## Implement HTTPS encryption
If you want to implement HTTPS just use the Spark java API

Get the spark service:
```java
//ServerWebGamePostServer = server
server.getSparkServer();
```

## Stop server
just run the stop function in the Server class

# Create Client
## Processing Datapackets
First you need to extend the org.CreadoresProgram.ServerWebGamePost.client.ProcessDatapackClient class

And to process the datapackets that the server sends you must implement Override to the processDatapack function

Something like that:
```java
@Override
public void processDatapack(@NonNull JSONObject datapack){
      //code...
      //JSONObject= com.alibaba.fastjson2.JSONObject
      //NonNull = lombok.NonNull
      //this.server = org.CreadoresProgram.ServerWebGamePost.client.ServerWebGamePostClient instance
}
```

## Send Datapackets
Simply in the org.CreadoresProgram.ServerWebGamePost.client.ServerWebGamePostClient class Execute the sendDataPacket function

Something like that:
```java
//ServerWebGamePostClient = client
client.sendDataPacket(json); //Remember to add the identifier key to the json (it is a String) json Is com.alibaba.fastjson2.JSONObject
```

## Create Instance
You must create a new org.CreadoresProgram.ServerWebGamePost.client.ServerWebGamePostClient instance
```java
ServerWebGamePostClient client = new ServerWebGamePostClient(domain, port, isHttps);
// domain = String(server domain or ip), port = int(Here you must specify the port where the server), isHttps = boolean(Find out if the server has HTTPS encryption or not)
ProcessDatapackClient procecerDatapacks = new ProcessDatapackClient(client);//your class extends
client.setProcessDatapacks(procecerDatapacks);
//procecerDatapacks = ProcessDatapackClient (Datapacket handling instance class)
```

Node.JS & Deno Edition:
# NPM
In your script add this with NPM installed

npm Command:
```shell
npm install https://github.com/Creadores-Program/ServerWebGamePost/releases/download/1.0.0/ServerWebGamePost-1.2.0.Nodejs.Edition.tgz
```

package.json:
```json
"dependencies":{
  "ServerWebGamePost": "1.2.0"
}
```

Script:
```js
const ServerWebGamePost = requiere("ServerWebGamePost");
```

# Create Server

## Processing Datapackets
You need to create a callback function. 
Something like that:
```js
function processDatapacks(datapack){
//datapack = json
}
```

## Send Datapackets
You can use the sendDataPacket function of the Server class:
```js
//server = ServerWebGamePost.Server Instance
server.sendDataPacket(datapack.identifier, {
  key: "data..."
});
//first argument = String(client identifier), Second argument = json(Datapacket)
```

## Ban IP
You can ban IPs with the banIp and unbanIp functions in ServerWebGamePost.Server class (The IP is String and is only that argument)

## Filter Origins
If you never run filter origins the origin header will be "*" if you run it it will only accept the given origins (Origin Header) The functions addFilterOrigin and removeFilterOrigin in ServerWebGamePost.Server class only accept one string argument.

## Create Instance 
You must create a new ServerWebGamePost.Server instance
```js
var Server = new ServerWebGamePost.Server(port, imgSrc, procecerDatapacks);
// port = Number(port where the server will open), imgSrc String (Server icon directory can be null), procecerDatapacks function(Callback for handling data packets)
```

## Implement HTTPS encryption
If you want to implement HTTPS just use the http API

Get the http server:
```js
//server = ServerWebGamePost.Server instance
server.getHttpServer();
```
## Stop server
just run the stop function in the Server class

# Create Client
## Processing Datapackets
Same as the server
```js
function processDatapacks(datapack){
//datapack = json
}
```
## Send Datapackets
Simply in the ServerWebGamePost.Client class Execute the sendDatapacket function

Something like that:
```js
//client = ServerWebGamePost.Client
client.sendDatapacket(packet);
//packet = json
// Remember to also send the identifier key
```
## Create Instance
You must create a new ServerWebGamePost.Client instance
```js
var client = new ServerWebGamePost.Client(domain, port, isHttps);
// domain = String (server domain or IP)
// port = Number (Server port)
// isHttps= boolean (whether the server has HTTPS encryption or not)
client.processDatapacks = callback;
//callback = function(Callback in charge of handling data packets )
```

Browser JS Edition:
# Installation
In your Html add this:
```html
<script src="https://cdn.jsdelivr.net/gh/Creadores-Program/ServerWebGamePost@1.2.0/browser/org/CreadoresProgram/ServerWebGamePost/client/ServerWebGamePostClient.js"></script>
```
> [!NOTE]
> If the server you want to query does not support HTTPS, you probably need to add the following tag to the header of your Html:
> ```html
> <meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests" />
> <!-- If it doesn't work you can also try: -->
> <meta http-equiv="Content-Security-Policy" content="default-src https: http:; script-src https: http:;" />
> ```

# Create Server
Not supported!

# Create Client
## Processing Datapackets
You need to create a callback function. Something like that:
```js
function processDatapacks(datapack){
//datapack = json
}
```
## Send Datapackets
Simply in the ServerWebGamePostClient class Execute the sendDatapacket function

Something like that:
```js
//client = ServerWebGamePostClient
client.sendDataPacket(packet);
//packet = json
// Remember to also send the identifier key
```

## Create Instance
You must create a new ServerWebGamePostClient instance
```js
var client = new ServerWebGamePostClient(domain, port, isHttps);
// domain = String (server domain or IP)
// port = Number (Server port)
// isHttps= boolean (whether the server has HTTPS encryption or not)
client.processDatapacks = callback;
//callback = function(Callback in charge of handling data packets )
```
Python Edition:
# Pip
PIP must be installed in your project

pip Command:
```shell
pip install https://github.com/Creadores-Program/ServerWebGamePost/releases/download/1.2.0/ServerWebGamePost-1.2.0.Python.Edition.tar.gz
```
And:
```shell
pip install request
# or require in your script
```

in your File.py:
```py
import ServerWebGamePost
```
# Create Server
> [!CAUTION]
> THE SERVER MUST BE OPENED IN A THREAD SEPARATE FROM THE MAIN ONE SINCE THE MODULE USED ENTERS AN INFINITE LOOP TO RECEIVE REQUESTS

## Processing Datapackets
First you need to extend the ServerWebGamePost.Server.ProcessDatapackServer class

And to process the datapackets that the client sends you must implement processDatapack function
Something like that:
```py
class MyProcessorDatapacks(ServerWebGamePost.Server.ProcessDatapackServer):
    def processDatapack(self, datapack): # datapack = json object
        #code...
```
## Send Datapackets
Simply in the ServerWebGamePost instance Execute the sendDataPacket function

Something like that:
```py
# ServerWebGamePost.Server = server
server.sendDataPacket(identifier, json) #identifier you must obtain from the datapacket that the client gives in its datapacket (it is a String) json is datapacket in json object
# in ServerWebGamePost.Server.ProcessDatapackServer = self.server.serverFat
```

## Ban IP
You can ban IPs with the banIp and unbanIp functions in ServerWebGamePost.Server class (The IP is String and is only that argument)

## Filter Origins
If you never run filter origins the origin header will be "*" if you run it it will only accept the given origins (Origin Header) The functions addFilterOrigin and removeFilterOrigin in ServerWebGamePost.Server class only accept one string argument.

## Create Instance
You must create a new ServerWebGamePost.Server instance
```py
server = ServerWebGamePost.Server(port, imgSrc, processDatapacks)
# port = int(Here you must specify the port where the server opens), imgSrc = String (Directory in string of the server icon type image can be None), processDatapacks = ServerWebGamePost.Server.ProcessDatapackServer extended class(It should NOT be an instance)
```
## Modify server
Get http.server.HTTPServer:
```py
server.getHttpServer()
```
## Stop Server
just run the stop function in the Server Class
# Create Client
## Processing Datapackets
You need to create a callback function.

Something like that:
```py
def processDatapacks(datapack):
    # datapack = json
```
## Send Datapackets
Simply in ServerWebGamePost.Client class Execute the sendDataPacket function

Something like that:
```py
# client = ServerWebGamePost.Client
client.sendDataPacket(packet)
# packet = json
# Remember to also send the identifier key
```
## Create Instance
You must create a new ServerWebGamePost.Client instance
```py
client = ServerWebGamePost.Client(domain, port, isHttps)
# domain = String (server domain or IP)
# port = String (Server port)
# isHttps= boolean (whether the server has HTTPS encryption or not)
client.setProcessDatapacks(callback)
# callback = function(Callback in charge of handling data packets )
```