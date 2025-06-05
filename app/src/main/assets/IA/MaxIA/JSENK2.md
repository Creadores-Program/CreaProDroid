# JSENK2
JSEngineNK 2 Run Node.JS in nukkit!

[JSEngineNK](https://cloudburstmc.org/resources/jsenginenk.939/) plugin API 2 is here!

## Requirements:

[JSEngineNK API 1](https://cloudburstmc.org/resources/jsenginenk.939/)

when creating the plugin or script: having NPM by Node.js

## How Install?
Unzip the releases Zip file into the plugins/JSEngineNK folder

and delete the zip file

## How to use?
It's almost the same as creating an NPM package.
Structure equals NPM module!
package.json:
```json
{
    "name": "TestScriptJSENK2",
    "version": "1.0.0",
    "description": "Test",
    "main": "index.js",
    "author": "Creadores Program",
    "homepage": "https://github.com/Creadores-Program/JSENK2", // = website
    "dependencies": {
        "pluginNukkit/Test": "1.0.0", // = Nukkit java plugins
        "scriptJSENK/Test2": "1.0.0", // = JSEngineNK API 1 or 2 script
        "pluginsJSENK2/Test": "1.0.0" // = Plugins JSENK 2
        //...
    }
}
```
More files...

run the NPM command like this:
```shell
npm pack
```
The .tgz file that it generates must be placed in the plugins/JSENK2

## How do i get dependencies?
example:
```js
//java equals JSEngineNK API 1!

//js files you package equals Nodejs
let myModu = require("./mydir/myMod.js");

//export by module:
module.exports = {
    myMethod: "Hello"// or function, or var, etc.
};
// in main your export Something like that:
module.exports = {
    onEnable: function(){}, // Enable Event
    onLoad: function(){}, // Load Event
    onDisable: function(){} //Disable Event
    // Ofther exports... (Not more Events!)
};

// Nukkit plugins:
let plugin = manager.getPlugin("pluginName");

// Scripts JSEngineNK2
let script = require("nameScript/file.js");
// (Scripts JSEngineNK 1 it is the same as the previous form with the variables declared by the script!)

// Plugins JSEngineNK 2
let pluginJSENKD = require("pluginsJSENK2/pluginName/main.js");
```

## Difference in JSEngineNK API 2 plugin and script

- Plugin:
  - cannot have dependencies
  - are loaded before the scripts
  - use to implement JSENK functionality
  - they are located in the plugins/JSENK2/JSENK2plugins/ directory
- Script:
  - can use dependencies
  - loads after JSENK2 Plugins
  - used as script in Nukkit Javascript
  - they are located in the plugins/JSENK2/ directory

## Compiled Javascript by Babel
JSENK2 code is compiled with Babel Standalone(It is not guaranteed to compile new features correctly)

## More Info
Read [JSEngineNK API](https://cloudburstmc.org/resources/jsenginenk.939/)

## Recommendations
just use one API!