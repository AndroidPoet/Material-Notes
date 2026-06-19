package com.androidpoet.materialnotes.data.auth

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** The signed-in user, as the app needs to know them. */
data class AuthSession(
    val userId: String,
    val email: String?,
    val accessToken: String,
    val refreshToken: String,
)

/**
 * Holds the current [AuthSession] for the running app.
 *
 * This is the single source of truth for "who is signed in": the navigation gate observes
 * [session] to decide between the login screen and the notes app, and the Supabase client's
 * `accessTokenProvider` reads [accessToken] so every database request runs as that user (and
 * is therefore scoped to their rows by RLS).
 *
 * The session is kept in memory only, so a relaunch returns to the login screen. Persisting it
 * is intentionally left out: the Supabase SDK keeps session storage pluggable / bring-your-own
 * rather than bundling a Keychain / EncryptedSharedPreferences dependency.
 */
@Inject
@SingleIn(AppScope::class)
class SessionStore {
    private val _session = MutableStateFlow<AuthSession?>(null)

    /** Emits the current session, or `null` when signed out. */
    val session: StateFlow<AuthSession?> = _session.asStateFlow()

    /** The current access token, or `null` when signed out — read fresh per request by the client. */
    val accessToken: String? get() = _session.value?.accessToken

    fun set(session: AuthSession) {
        _session.value = session
    }

    fun clear() {
        _session.value = null
    }
}
