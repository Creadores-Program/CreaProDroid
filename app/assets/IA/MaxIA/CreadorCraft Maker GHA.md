# CreadorCraft-Maker-GHA

![Github](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
![Github Actions](https://img.shields.io/badge/Github%20Actions-282a2e?style=for-the-badge&logo=githubactions&logoColor=367cfe)
![NodeJS](https://img.shields.io/badge/Node%20js-339933?style=for-the-badge&logo=nodedotjs&logoColor=white)
![NPM](https://img.shields.io/badge/npm-CB3837?style=for-the-badge&logo=npm&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-323330?style=for-the-badge&logo=javascript&logoColor=F7DF1E)
![Json](https://img.shields.io/badge/json-5E5C5C?style=for-the-badge&logo=json&logoColor=white)

Github Action to package your CreatorCraft game!

You can now package your CreatorCraft game on Github!

If you want to know how to create a game in CreadorCraft I recommend you check out the [CreadorCraft Maker wiki](https://creadorcraft-maker.blogspot.com/p/documentacionwiki.html) you can also run many programming languages for your game!

## Example Work
```yml
name: CI

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    if: ${{ !contains(github.event.head_commit.message, '[ci skip]') }}
    steps:
      - uses: actions/checkout@v4
      - name: CreadorCraft-Maker
        uses: Creadores-Program/CreadorCraft-Maker-GHA@v1
        with:
          path: "./src"
      - name: Upload Artifact
        uses: actions/upload-artifact@v4
        with:
          name: My Game Example
          path: gameBuildCCM/TestName 1.0.0.creadorcraftgame.zip
```

### Structure Repo:
myName/RepoGameName/src

   - manifest.json:
   ```json
   {
    "name": "TestName",
    "description": "Test game",
    "version": "1.0.0",
    "mainHtml": "index.html",//dir main html file
    "mainCSS": "index.css",
    "mainJS": "index.js"
   }
   ```
   - index.html
   - index.css
   - index.js

more info in Wiki!
Github Market Place de CreadorCraft Maker Github Action: https://github.com/marketplace/actions/creadorcraft-maker