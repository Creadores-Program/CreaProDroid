# Scratch to CreadorCraft Maker GHA

![Github](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
![Github Actions](https://img.shields.io/badge/Github%20Actions-282a2e?style=for-the-badge&logo=githubactions&logoColor=367cfe)
![NodeJS](https://img.shields.io/badge/Node%20js-339933?style=for-the-badge&logo=nodedotjs&logoColor=white)
![NPM](https://img.shields.io/badge/npm-CB3837?style=for-the-badge&logo=npm&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-323330?style=for-the-badge&logo=javascript&logoColor=F7DF1E)
![Json](https://img.shields.io/badge/json-5E5C5C?style=for-the-badge&logo=json&logoColor=white)

Github Action to transpile your Scratch game to CreatorCraft game on Github!

If you want to know how to create a game in CreadorCraft I recommend you check out the [CreadorCraft Maker wiki](https://creadorcraft-maker.blogspot.com/p/documentacionwiki.html) you can also run many programming languages for your game!

## Example Work
You need a Scratch game file .sb, .sb2 or .sb3
### Action options
- path: destination of the game files CreatorCraft (the game's manifest.json should be there when creating) required
- id: Scratch Game ID if you want to get by Project ID
- url: Scratch game file URL if you want to get it by url
- pathGame: Scratch game directory if it is in your Github repo
- pathCustomJs: additional JavaScript file directory for the Scratch game (must be in a different directory than where the CraftCreator game will be generated, same for the css)
- pathCustomCSS: additional css to the Scratch game
- enableGamepad: should accept gamepads?
- isBakeExtensions: what the variable says (boolean)
- specialCloudBehaviors: specialCloudBehaviors? same as Htmlifier
- loadingText: text in the loading bar
- pathExtensions: a Scratch extensions Json directory example:
```json
{
  "extensions": [
    //extensions...
  ]
}
```

### Example of task:

```yml
name: CI

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    if: ${{ !contains(github.event.head_commit.message, '[ci skip]') }}
    steps:
      - uses: actions/checkout@v4
      - name: Scratch-CreadorCraft-Maker
        uses: Creadores-Program/Scratch-to-CreadorCraft-Maker-GHA@v1.0.0
        with:
          path: "./src" # Destinity CreadorCraft Game (this not genere manifest.json)
          # id: "id" Id by game Scratch
          # url: "url" Url by game Scratch
          pathGame: "./Game.sb3" # Only uses 1 of the above
      # Pack..
      - name: CreadorCraft-Maker
        uses: Creadores-Program/CreadorCraft-Maker-GHA@v1.1.0
        with:
          path: "./src"
      - name: Upload Artifact
        uses: actions/upload-artifact@v4
        with:
          name: My Game Example
          path: gameBuildCCM/TestName 1.0.0.creadorcraftgame.zip
```

### Structure Repo:
myName/RepoGameName/
- Game.sb3

- src

   - manifest.json:
   ```json
   {
    "name": "TestName",
    "description": "Test game",
    "version": "1.0.0",
    "mainHtml": "index.html",
    "mainCSS": "index.css",
    "mainJS": "main.js"
   }
   ```

this use TurboWarp!

Made in Mexico.

Creadores Program © 2024

accion link: https://github.com/marketplace/actions/scratch-creadorcraft-maker
