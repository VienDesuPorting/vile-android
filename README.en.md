# VILE for Android

A historical archive of **VILE for Android** — an Android port of the **ViLE (Visual Library Engine)** visual novel engine, created in 2015–2016 by a developer known as "Ivan".

The finished app was distributed through the «Визуальные новеллы для Android» (Visual Novels for Android) VK community — the port's changelog survives in the [archived VK thread](https://vk.ru/topic-69564393_33447386). The sources were posted too — on Yandex Disk — but the link eventually went dead: the author apparently cleaned the disk. The copy that survived in a private archive is published here for the historical record. The code is imported as-is, without cleanup or edits, in the state it was left in by early 2016.

> **Status:** archive. Development of this version is finished and will not resume; the code is not maintained. The actively developed Android port of the engine is [IkuraDroid](https://github.com/VienDesuPorting/ikuradroid).

Русская версия: [README.md](README.md)

## History

ViLE is an open-source cross-platform engine for running Japanese visual novels, supporting several proprietary scripting formats.

In 2015–2016 an Android port was built on top of it: the ViLE 0.4.13 core, a native layer on SDL 2.0.3 and a Java wrapper around SDLActivity. The project was built in Eclipse/ADT with Apache Ant and targeted the devices of its era.

After work on the port stopped, its sources survived in a private archive — and it was exactly those sources that later became the foundation of the new Android visual novel player, IkuraDroid. This repository publishes the original code it all started from.

## Technical information

| Parameter | Value |
|---|---|
| Core version | ViLE 0.4.13 |
| App version | 1.0 (the menu string reads "Vile 0.54") |
| Minimum Android version | API 8 |
| Target platform | Android API 23 |
| Architecture | ARMv7 (`armeabi-v7a`) |
| Graphics and multimedia | SDL 2.0.3 |
| Build tooling | Eclipse ADT / Apache Ant |

## Project contents

### Game engines

The ViLE 0.4.13 core with the engines of the original families:

- Crowd
- C-Ware
- Ikura GDL
- JAST
- T-Love
- Will
- Windy

On top of them, the port's own modules: **Ikura Heartdr** and **Ikura Virgin** (Ikura family), **Will Princess** and **Will Starry** (Will family).

### Native side

- `jni/vile/` — the ViLE core and the game engines;
- `jni/Vile.cpp` — the JNI bridge between the core and Java;
- `jni/vile/media/hh/` — the port's audio and video backends;
- `jni/vile/common/edl_texture.*` — texture handling;
- `jni/vile/external/hq2x`, `hq3x`, `hq4x` — image scaling algorithms.

### Third-party libraries

`jni/` holds the vendored sources of everything needed for the native build:

- SDL 2.0.3
- SDL_image 2.0.0
- SDL_mixer 2.0.1
- SDL_ttf 2.0.14
- SDL_gfx
- libmikmod 3.1.12
- SMPEG2 2.0.0
- libwebp

## Repository structure

```text
vile-android/
├── assets/               # App resources (default.ttf, vilevn.pck)
├── bin/
│   └── Vile.apk          # The original 2016 build
├── jni/                  # Native side (C/C++)
│   ├── vile/             # The ViLE core and the game engines
│   ├── Vile.cpp          # JNI bridge
│   ├── Android.mk
│   ├── Application.mk
│   └── sdl/, sdl_image/, sdl_mixer/, sdl_ttf/, sdl_gfx/,
│       sdl_main/, libmikmod-3.1.12/, smpeg2-2.0.0/, webp/
├── libs/                 # Prebuilt .so for armeabi-v7a and android-support-v4.jar
├── res/                  # Android resources
├── src/                  # Java side: ru.visualandroid.vile and SDLActivity
├── AndroidManifest.xml
├── build.xml             # Apache Ant build
├── project.properties    # target=android-23, references to external libraries
├── lint.xml
└── local.properties      # An empty ADT leftover
```

## Build and compatibility

The project is preserved in its historical state and was not adapted to modern development tools. Building it in its own era required:

- Eclipse with ADT and Apache Ant;
- the Android SDK (target android-23);
- the Android NDK with the `gnustl_static` static library;
- external libraries `android-support-v7-appcompat`, `cardview` and `recyclerview`, referenced by `project.properties` — the libraries themselves are not in the repository.

Building from a clean checkout with modern tooling is not guaranteed. The presence of the sources and a ready APK does not imply compatibility with current Android versions: the minimum supported API is 8, the target is 23, and the native side is `armeabi-v7a` only.

The compiled historical build sits in `bin/Vile.apk`.

## Project continuation

Development of the Android port of the engine continued in [IkuraDroid](https://github.com/VienDesuPorting/ikuradroid) — that is exactly the code it grew out of. IkuraDroid moved to modern tooling (Gradle, current SDK/NDK), its Java side is fully rewritten, and support for visual novel families keeps expanding.

This repository is kept separately so that the original port remains available for study and comparison.

## Acknowledgements

- **ViLE Team** — the original ViLE engine.
- **Ivan** — the author of the VILE Android port (2015–2016).
- **VienDesu! Porting Team** — preserving the sources and carrying the Android version on as IkuraDroid.

## License

The ViLE core is licensed under the **GNU General Public License v3**; the text is in [COPYING](COPYING). The vendored third-party libraries in `jni/` are distributed under the licenses of their projects.
