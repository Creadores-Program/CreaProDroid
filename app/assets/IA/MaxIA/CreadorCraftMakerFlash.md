# CreadorCraft-Maker-Flash

![Github](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
![Github Actions](https://img.shields.io/badge/Github%20Actions-282a2e?style=for-the-badge&logo=githubactions&logoColor=367cfe)
![NodeJS](https://img.shields.io/badge/Node%20js-339933?style=for-the-badge&logo=nodedotjs&logoColor=white)
![NPM](https://img.shields.io/badge/npm-CB3837?style=for-the-badge&logo=npm&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-323330?style=for-the-badge&logo=javascript&logoColor=F7DF1E)
![Json](https://img.shields.io/badge/json-5E5C5C?style=for-the-badge&logo=json&logoColor=white)

Github Action to convert your flash game to CreatorCraft game!

If you want to know how to create a game in CreadorCraft I recommend you check out the [CreadorCraft Maker wiki](https://creadorcraft-maker.blogspot.com/p/documentacionwiki.html) you can also run many programming languages for your game!

## Example Work
You need a Flash game file .swf
```yml
name: CI

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    if: ${{ !contains(github.event.head_commit.message, '[ci skip]') }}
    steps:
      - uses: actions/checkout@v4
      - name: CreadorCraft-Maker-Flash
        uses: Creadores-Program/CreadorCraft-Maker-Flash@v1.0.0
        with:
          path: "./src" # Dir by game: This does not generate CSS or HTML or the manifest, the manifest in JS must be main.js
          pathSWF: "game.swf" #Game Path from Previous Path arg
          elementID: 'Test' #Html element ID to put the flash game
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

Made in Mexico.

Creadores Program © 2024

Link de la Accion: https://github.com/marketplace/actions/creadorcraft-maker-flash