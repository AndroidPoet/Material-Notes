# Material Notes — Compose Multiplatform

A clean, Material 3 notes app built with **Compose Multiplatform**, running on **Android, iOS, and Desktop (JVM)**
from a single shared codebase. Write a note, pick a color, and watch the card morph into the full note with a
shared-element transition.

<p align="center">
  <img src="art/demo.gif" alt="Material Notes demo on iOS" width="300" />
</p>

## Features

- 📝 Create, read, and delete notes — persisted locally with **Room**
- 🎨 Curated pastel palette + color picker per note
- 🧱 Staggered (masonry) grid home screen with an empty state
- ✨ Shared-element transition — the note card morphs into the detail screen
- 📱💻 One UI, three platforms: Android, iOS, and Desktop

## Tech stack

| Layer | Choice |
| --- | --- |
| UI | Compose Multiplatform + Material 3 |
| Motion | Shared-element transition (`SharedTransitionLayout` + `sharedBounds`) |
| Navigation | `org.jetbrains.androidx.navigation` (Navigation-Compose) |
| DI | **Metro** — compile-time dependency graph (`dev.zacsweers.metro`) |
| Database | **Room KMP** + bundled SQLite driver |
| ViewModel / Lifecycle | `org.jetbrains.androidx.lifecycle` (multiplatform) |
| Async | Kotlin Coroutines / `Flow` |
| Logging | Napier |
| Date / color | `kotlinx-datetime`, plain ARGB `Int` packing |

## Project layout

```
composeApp/src/
├── commonMain/   # ALL shared code: data, DI graph, ViewModels, Compose UI, navigation
├── androidMain/  # MainActivity, Application, Android Room builder
├── iosMain/      # MainViewController (ComposeUIViewController), iOS Room builder
└── desktopMain/  # Desktop main(), desktop Room builder
iosApp/           # Xcode project (SwiftUI shell hosting the Compose UI)
```

The one per-platform seam is the Room database builder (`di/Platform.*.kt`) — each platform supplies a
`RoomDatabase.Builder<AppDatabase>` (Android needs a `Context`; iOS/Desktop use a file path). Each entry point
builds the Metro graph via `buildAppGraph(builder, ioDispatcher)` and hands it to `App()`.

## Running

- **Android:** `./gradlew :composeApp:assembleDebug` (or run the `composeApp` config in Android Studio).
- **Desktop:** `./gradlew :composeApp:run`
- **iOS:** open `iosApp/iosApp.xcodeproj` in Xcode, set your signing **TEAM_ID** in
  `iosApp/Configuration/Config.xcconfig`, pick a simulator, and run. The "Compile Kotlin Framework" build
  phase invokes Gradle to build the shared framework.

## Version matrix

Kotlin 2.1.21 · Compose Multiplatform 1.7.3 · AGP 8.7.3 · Room 2.7.1 · Metro 0.3.8 ·
Navigation-Compose 2.8.0-alpha10 · Lifecycle (JB) 2.8.4 · Gradle 8.11.1 (see `gradle/libs.versions.toml`).

> **Metro needs Kotlin ≥ 2.1.20.** Metro is a compile-time DI compiler plugin, so the whole graph is
> validated at build time — a wiring mistake fails the build instead of crashing at runtime.

> **Version alignment matters.** Navigation-Compose `2.8.0-alpha13` transitively pulls Compose
> runtime `1.8.0-alpha03`, which conflicts with CMP 1.7.3 `material3`/`foundation` and causes a
> Kotlin/Native `IrLinkageError` at runtime on iOS (JVM/desktop tolerates it). Pin navigation to
> `2.8.0-alpha10` (built against Compose 1.7.x) so the whole tree resolves to one Compose version.

> **iOS** requires `CADisableMinimumFrameDurationOnPhone=true` in `iosApp/iosApp/Info.plist` (CMP enforces it).

### Verified
Android APK builds · iOS app builds, installs, launches & renders on the iOS 26.5 simulator ·
Desktop runs (Metro DI graph + Room + Compose UI all initialize at runtime).
