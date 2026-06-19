package com.androidpoet.materialnotes.data.sync

/**
 * Where the app's cloud points.
 *
 *  - [PROJECT_URL] — the project URL.
 *  - [ANON_KEY]    — the project's **anon / publishable** key (Project Settings → API).
 *
 * Both values come from [SupabaseSecrets], which is **generated at build time** from
 * `local.properties` (`supabase.url` / `supabase.anonKey`) or the `SUPABASE_URL` /
 * `SUPABASE_ANON_KEY` env vars — so no credentials ever live in committed source (see
 * `core/data/build.gradle.kts`). Until they're provided, [isConfigured] is `false` and the app
 * runs fully offline via SQLDelight.
 *
 * The anon key is designed to ship in clients: it is public and constrained entirely by Row Level
 * Security. Every note row is scoped to its owner via `auth.uid()` (see `supabase/schema.sql`), so
 * it can only ever read or write the signed-in user's own notes. NEVER use the `service_role` key
 * in a client — it bypasses RLS.
 */
object SupabaseConfig {
    val PROJECT_URL: String = SupabaseSecrets.PROJECT_URL

    val ANON_KEY: String = SupabaseSecrets.ANON_KEY

    /** Table that mirrors the local `Note` store. */
    const val NOTES_TABLE: String = "notes"

    val isConfigured: Boolean
        get() = PROJECT_URL.isNotBlank() && ANON_KEY.isNotBlank()
}
