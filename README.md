<h1 align="center">Material Notes</h1>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=24"><img alt="API" src="https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat"/></a>
  <img alt="Platform" src="https://img.shields.io/badge/Platform-Android%20%7C%20iOS%20%7C%20Desktop-brightgreen.svg"/> <br>
  <img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-2.1.21-blueviolet.svg?logo=kotlin"/>
  <img alt="Compose Multiplatform" src="https://img.shields.io/badge/Compose%20Multiplatform-1.7.3-blue.svg"/>
  <img alt="DI" src="https://img.shields.io/badge/DI-Metro-orange.svg"/>
</p>

<p align="center">
📝 Material Notes is a clean, Material 3 note-taking app built with Compose Multiplatform — running on Android, iOS, and Desktop from a single shared codebase, with Room, Metro DI, Coroutines, Flow, and a card-to-detail shared-element transition based on MVVM architecture.
</p>

> [!TIP]
> The entire UI and logic live in `commonMain` — the same Compose code runs natively on Android, iOS, and Desktop (JVM).

<img src="art/demo.gif" align="right" width="300"/>

## Running

- **Android** — `./gradlew :composeApp:assembleDebug`, or run the `composeApp` configuration in Android Studio.
- **Desktop** — `./gradlew :composeApp:run`
- **iOS** — open `iosApp/iosApp.xcodeproj` in Xcode, set your signing **TEAM_ID** in
  `iosApp/Configuration/Config.xcconfig`, pick a simulator, and run. The "Compile Kotlin Framework"
  build phase invokes Gradle to build the shared framework.

## Tech stack & Open-source libraries

- Minimum SDK level 24 · targets **Android, iOS, and Desktop (JVM)**.
- [Kotlin](https://kotlinlang.org/) based, with [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) and [Flow](https://kotlinlang.org/docs/flow.html) for async streams.
- Compose Multiplatform Libraries:
  - [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) — declarative UI + Material 3.
  - Lifecycle & ViewModel — `org.jetbrains.androidx.lifecycle` (multiplatform).
  - Navigation — `org.jetbrains.androidx.navigation` (Navigation-Compose).
  - [Room KMP](https://developer.android.com/kotlin/multiplatform/room) — local persistence with the bundled SQLite driver.
- [Metro](https://github.com/ZacSweers/metro) — compile-time dependency injection (a Kotlin compiler plugin).
- Architecture:
  - MVVM Architecture (View → ViewModel → Repository → DAO).
  - Repository Pattern.
- [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime) — multiplatform date/time formatting.
- [Napier](https://github.com/AAkira/Napier) — multiplatform logging.
- [KSP](https://github.com/google/ksp) — Room code generation per target.
- **Shared-element transition** — `SharedTransitionLayout` + `sharedBounds` morph the note card into the detail screen.

## Architecture

Material Notes follows MVVM and a single-module, source-set-based structure. All shared code lives in
`commonMain`; each platform only supplies a thin entry point and a Room database builder.

```
composeApp/src/
├── commonMain/   # ALL shared code: data, DI graph, ViewModels, Compose UI, navigation
├── androidMain/  # MainActivity, Application, Android Room builder
├── iosMain/      # MainViewController (ComposeUIViewController), iOS Room builder
└── desktopMain/  # Desktop main(), desktop Room builder
iosApp/           # Xcode project (SwiftUI shell hosting the Compose UI)
```

The one per-platform seam is the Room database builder (`di/Platform.*.kt`): each platform supplies a
`RoomDatabase.Builder<AppDatabase>` (Android needs a `Context`; iOS/Desktop use a file path). Each entry
point builds the Metro dependency graph via `buildAppGraph(builder, ioDispatcher)` and hands it to `App()`.
Because Metro validates the graph at compile time, a wiring mistake fails the build instead of crashing at runtime.

### Version matrix

Kotlin 2.1.21 · Compose Multiplatform 1.7.3 · AGP 8.7.3 · Room 2.7.1 · Metro 0.3.8 ·
Navigation-Compose 2.8.0-alpha10 · Lifecycle (JB) 2.8.4 · Gradle 8.11.1 (see `gradle/libs.versions.toml`).

> [!NOTE]
> **Version alignment matters.** Navigation-Compose `2.8.0-alpha13` transitively pulls Compose runtime
> `1.8.0-alpha03`, which conflicts with CMP 1.7.3 `material3`/`foundation` and causes a Kotlin/Native
> `IrLinkageError` at runtime on iOS (JVM/desktop tolerates it). Pin navigation to `2.8.0-alpha10` so the
> whole tree resolves to one Compose version. iOS also requires
> `CADisableMinimumFrameDurationOnPhone=true` in `iosApp/iosApp/Info.plist`.

## Find this repository useful? :heart:
Support it by joining __[stargazers](https://github.com/AndroidPoet/Material-Notes/stargazers)__ for this repository. :star: <br>
Also, __[follow me](https://github.com/AndroidPoet)__ on GitHub for my next creations! 🤩

# License
```xml
Designed and developed by 2026 AndroidPoet (Ranbir Singh)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
