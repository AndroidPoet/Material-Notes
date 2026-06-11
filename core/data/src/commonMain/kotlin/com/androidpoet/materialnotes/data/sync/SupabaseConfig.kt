package com.androidpoet.materialnotes.data.sync

/**
 * Where the app's cloud sync points. Fill these in with your own Supabase project's values:
 *
 *  - [PROJECT_URL] — your project URL, e.g. `https://abcdefgh.supabase.co`
 *  - [ANON_KEY]    — the project's **anon / publishable** key (Project Settings → API).
 *
 * Only ever ship the *anon* key in the app. NEVER embed the `service_role` key in a client —
 * it bypasses Row Level Security. See `supabase/schema.sql` for the table + RLS to create.
 *
 * Until both values are set, [isConfigured] stays `false` and the Sync button reports that
 * cloud sync hasn't been configured (the app still works fully offline via SQLDelight).
 */
object SupabaseConfig {
    const val PROJECT_URL: String = ""
    const val ANON_KEY: String = ""

    /** Table that mirrors the local `Note` store. */
    const val NOTES_TABLE: String = "notes"

    val isConfigured: Boolean
        get() = PROJECT_URL.isNotBlank() && ANON_KEY.isNotBlank()
}
