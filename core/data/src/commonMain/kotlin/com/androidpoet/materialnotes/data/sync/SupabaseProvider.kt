package com.androidpoet.materialnotes.data.sync

import com.androidpoet.materialnotes.data.auth.SessionStore
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.androidpoet.supabase.auth.AuthClient
import io.github.androidpoet.supabase.auth.createAuthClient
import io.github.androidpoet.supabase.client.Supabase
import io.github.androidpoet.supabase.client.SupabaseClient
import io.github.androidpoet.supabase.database.DatabaseClient
import io.github.androidpoet.supabase.database.createDatabaseClient

/**
 * Builds and shares the one Supabase client the app uses for both auth and the database, on
 * AndroidPoet's own Supabase KMP SDK.
 *
 * The client is wired with an `accessTokenProvider` that reads [SessionStore.accessToken] fresh
 * on every request, so as soon as a user signs in, database calls carry their JWT and Row Level
 * Security scopes every row to them. When signed out the provider yields `null` and the client
 * falls back to the anon key (which the per-user RLS policies match nothing for).
 *
 * Everything is created lazily and only when [SupabaseConfig.isConfigured], so an un-configured
 * build never constructs a client (which would throw on a blank URL / key) and the app keeps
 * working fully offline via SQLDelight.
 */
@Inject
@SingleIn(AppScope::class)
class SupabaseProvider(
    private val sessionStore: SessionStore,
) {
    val client: SupabaseClient? by lazy {
        if (!SupabaseConfig.isConfigured) {
            null
        } else {
            Supabase.create(SupabaseConfig.PROJECT_URL, SupabaseConfig.ANON_KEY) {
                accessTokenProvider = { sessionStore.accessToken }
                logging = true
            }
        }
    }

    val auth: AuthClient? by lazy { client?.let { createAuthClient(it) } }

    val database: DatabaseClient? by lazy { client?.let { createDatabaseClient(it) } }
}
