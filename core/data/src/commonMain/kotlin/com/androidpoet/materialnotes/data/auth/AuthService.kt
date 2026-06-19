package com.androidpoet.materialnotes.data.auth

import com.androidpoet.materialnotes.data.sync.SupabaseProvider
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.androidpoet.supabase.auth.models.Session
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/** Outcome of a sign-in / sign-up attempt, surfaced to the UI. */
sealed interface AuthOutcome {
    data class Success(val session: AuthSession) : AuthOutcome
    data class Failure(val message: String) : AuthOutcome
    /** Cloud isn't configured (blank URL / key) — auth is unavailable. */
    data object NotConfigured : AuthOutcome
}

/**
 * Email + password authentication against Supabase, built on AndroidPoet's Supabase KMP SDK.
 *
 * On a successful sign-in or sign-up the resulting [AuthSession] is stored in [SessionStore],
 * which both unlocks the notes UI and hands the access token to every subsequent database
 * request so it runs as this user. Sign-out revokes the session server-side and clears it locally.
 */
@Inject
@SingleIn(AppScope::class)
class AuthService(
    private val supabase: SupabaseProvider,
    private val sessionStore: SessionStore,
    private val ioContext: CoroutineContext,
) {

    suspend fun signUp(email: String, password: String): AuthOutcome =
        authenticate { it.signUpWithEmail(email = email.trim(), password = password) }

    suspend fun signIn(email: String, password: String): AuthOutcome =
        authenticate { it.signInWithEmail(email = email.trim(), password = password) }

    /** Revokes the current session on the server (best-effort) and clears it locally. */
    suspend fun signOut() {
        withContext(ioContext) {
            val auth = supabase.auth
            val token = sessionStore.accessToken
            if (auth != null && token != null) {
                runCatching { auth.signOut(token) }
            }
            sessionStore.clear()
        }
    }

    private suspend inline fun authenticate(
        crossinline call: suspend (io.github.androidpoet.supabase.auth.AuthClient) -> io.github.androidpoet.supabase.core.result.SupabaseResult<Session>,
    ): AuthOutcome = withContext(ioContext) {
        val auth = supabase.auth ?: return@withContext AuthOutcome.NotConfigured
        val result = call(auth)
        val session = result.getOrNull()
            ?: return@withContext AuthOutcome.Failure(
                result.errorOrNull()?.message ?: "Authentication failed",
            )
        if (session.accessToken.isBlank()) {
            return@withContext AuthOutcome.Failure(
                "Check your email to confirm your account, then sign in.",
            )
        }
        val authSession = session.toAuthSession()
        sessionStore.set(authSession)
        AuthOutcome.Success(authSession)
    }
}

private fun Session.toAuthSession(): AuthSession = AuthSession(
    userId = user.id,
    email = user.email,
    accessToken = accessToken,
    refreshToken = refreshToken,
)
