# AudioGame.JS
Easily play optimized audio for your game!
## Installation

Add the following script tag to your HTML:

```html
<script src="https://cdn.jsdelivr.net/gh/Creadores-Program/AudioGame.JS@1.0.0-beta/src/org/CreadoresProgram/AudioGameJS/AudioGame.js"></script>
```

## Basic Usage

```js
// Create an AudioGame instance
const sound = new AudioGame('path/to/file.mp3', {
  loop: false,      // Repeat audio (default: false)
  volume: 1,        // Volume (0 to 1, default: 1)
  is3D: false,      // 3D audio (default: false)
  pan: 0,           // Stereo pan (-1 to 1, only if is3D is false)
  autoplay: false   // Autoplay on load (default: false)
});

// Play
sound.play();

// Pause
sound.pause();

// Change volume
sound.volume = 0.5;

// Change 3D position (if is3D is true)
sound.setPosition3D(1, 0, 0);
```

## API

- **play()**: Play the audio.
- **pause()**: Pause the audio.
- **dispose()**: Release audio resources.
- **volume**: Volume (0 to 1).
- **loop**: Whether the audio repeats.
- **pan**: Stereo pan (-1 to 1, only if is3D is false).
- **is3D**: Enable 3D audio.
- **setPosition3D(x, y, z)**: Set the 3D position of the audio.
- **autoplay**: Whether the audio plays automatically on load.
- **currentTime**: Current playback time.
- **duration**: Audio duration.
- **paused**: Whether the audio is paused.
- **readyState**: 4 if loaded, 0 if not.

## License
MIT