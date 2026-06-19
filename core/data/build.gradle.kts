import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.metro)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            // JVM 17 because the Supabase SDK's Android artifact ships JVM 17 inline functions
            // (selectTyped / upsertTyped) that NoteSyncService inlines.
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm("desktop")

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)

            // AndroidPoet's own Supabase KMP SDK — auth + cloud note sync.
            implementation(libs.supabase.client)
            implementation(libs.supabase.database)
            implementation(libs.supabase.auth)
        }
        androidMain.dependencies {
            implementation(libs.sqldelight.android.driver)
        }
        iosMain.dependencies {
            implementation(libs.sqldelight.native.driver)
        }
        val desktopMain by getting
        desktopMain.dependencies {
            implementation(libs.sqldelight.sqlite.driver)
        }
    }
}

android {
    namespace = "com.androidpoet.materialnotes.core.data"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

sqldelight {
    databases {
        create("NotesDatabase") {
            packageName.set("com.androidpoet.materialnotes.db")
        }
    }
}

// ── Supabase credentials (kept OUT of source control) ────────────────────────────────────────────
// The project URL + public anon key are read at build time from `local.properties`
// (`supabase.url` / `supabase.anonKey`) or the `SUPABASE_URL` / `SUPABASE_ANON_KEY` env vars, and
// baked into a generated `SupabaseSecrets`. Nothing secret lives in committed source. Leave them
// unset and the app just runs offline (`SupabaseConfig.isConfigured == false`).
val supabaseLocalProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}
val supabaseUrl: String = supabaseLocalProps.getProperty("supabase.url") ?: System.getenv("SUPABASE_URL") ?: ""
val supabaseAnonKey: String = supabaseLocalProps.getProperty("supabase.anonKey") ?: System.getenv("SUPABASE_ANON_KEY") ?: ""
val supabaseSecretsDir = layout.buildDirectory.dir("generated/supabaseSecrets/kotlin")

val generateSupabaseSecrets by tasks.registering {
    inputs.property("url", supabaseUrl)
    inputs.property("key", supabaseAnonKey)
    outputs.dir(supabaseSecretsDir)
    doLast {
        val pkg = supabaseSecretsDir.get().asFile.resolve("com/androidpoet/materialnotes/data/sync")
        pkg.mkdirs()
        pkg.resolve("SupabaseSecrets.kt").writeText(
            "package com.androidpoet.materialnotes.data.sync\n\n" +
                "// GENERATED at build time from local.properties / env — do not edit, do not commit.\n" +
                "internal object SupabaseSecrets {\n" +
                "    const val PROJECT_URL: String = \"$supabaseUrl\"\n" +
                "    const val ANON_KEY: String = \"$supabaseAnonKey\"\n" +
                "}\n",
        )
    }
}

kotlin.sourceSets.named("commonMain") {
    kotlin.srcDir(generateSupabaseSecrets)
}
