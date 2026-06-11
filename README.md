# Material Notes — Compose Multiplatform

A Compose Multiplatform (CMP) port of [AndroidPoet/Material-Notes](https://github.com/AndroidPoet/Material-Notes),
running on **Android, iOS, and Desktop (JVM)** from a single shared codebase.

## Why this is a rewrite, not a "port"

The original app has **no Jetpack Compose** — its UI is Fragments + XML layouts + ViewBinding/DataBinding +
RecyclerView adapters + the AndroidX Navigation Component. Compose Multiplatform shares *Compose* UI, so the
View-based UI had to be rebuilt as Composables. The data/logic layer was moved to `commonMain` largely intact.

## What changed

| Concern | Original (Android-only) | CMP version (`commonMain`) |
| --- | --- | --- |
| UI | Fragments + XML + ViewBinding + RecyclerView | Compose Multiplatform + Material 3 (redesigned: staggered grid, curated palette, color picker, empty state) |
| Motion | `metaphor` material container transform (Android-only) | Compose **shared-element transition** (`SharedTransitionLayout` + `sharedBounds`) — card morphs into detail |
| Navigation | Navigation Component (nav graph XML + Safe Args) | `org.jetbrains.androidx.navigation` (Navigation-Compose) |
| DI | Dagger **Hilt** | **Metro** (compile-time, `dev.zacsweers.metro` — a Kotlin compiler plugin) |
| Database | Room (Android) | **Room KMP 2.7** + bundled SQLite driver |
| List loading | Paging 3 (`PagingSource`) | plain `Flow<List<Note>>` (the dataset is tiny) |
| ViewModel | `androidx.lifecycle` (Android) | `org.jetbrains.androidx.lifecycle` (multiplatform) |
| Logging | Timber | Napier |
| Date / color | `java.text.SimpleDateFormat`, `android.graphics.Color` | `kotlinx-datetime`, plain ARGB `Int` packing |
| Model | Room `@Entity` + `@Parcelize` | Room `@Entity` (Parcelable dropped) |

## Project layout

```
composeApp/src/
├── commonMain/   # ALL shared code: data, DI graph, ViewModels, Compose UI, navigation
├── androidMain/  # MainActivity, Application, Android Room builder
├── iosMain/      # MainViewController (ComposeUIViewController), iOS Room builder
└── desktopMain/  # Desktop main(), desktop Room builder
iosApp/           # Xcode project (SwiftUI shell hosting the Compose UI)
```

The one per-platform seam is the Room database builder (in `di/Platform.*.kt`) — each platform supplies a
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
> validated at build time (a wiring mistake fails the build instead of crashing at runtime). This required
> bumping Kotlin 2.1.0 → 2.1.21 and KSP to `2.1.21-2.0.1` while keeping Compose Multiplatform on 1.7.3.

> **Version alignment matters.** Navigation-Compose `2.8.0-alpha13` transitively pulls Compose
> runtime `1.8.0-alpha03`, which conflicts with the CMP 1.7.3 `material3`/`foundation` and causes a
> Kotlin/Native `IrLinkageError` at runtime on iOS (JVM/desktop tolerates it). Pin navigation to
> `2.8.0-alpha10` (built against Compose 1.7.x) so the whole tree resolves to one Compose version.

### Verified
Android APK builds · iOS app builds, installs, launches & renders on the iOS 26.5 simulator ·
Desktop runs (Metro DI graph + Room + Compose UI all initialize at runtime).
iOS also requires `CADisableMinimumFrameDurationOnPhone=true` in `iosApp/iosApp/Info.plist` (CMP enforces it).
