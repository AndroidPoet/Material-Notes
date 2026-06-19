package com.androidpoet.materialnotes.data.auth

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

/** A third-party identity provider the login screen offers a button for. */
enum class SocialProvider(val label: String) {
    Google("Google"),
    Apple("Apple"),
}

/**
 * Native social sign-in (Google / Apple) for the app, on AndroidPoet's Supabase KMP SDK.
 *
 * The login screen always shows both buttons; this service decides what happens when one is
 * tapped. Native sign-in is **platform-specific** in the SDK — Google ships an Android provider
 * (`supabase-auth-google`), Apple an iOS/macOS provider (`supabase-auth-apple`) — and each needs
 * its OAuth provider enabled on the Supabase project with a real client id / secret. Until that
 * configuration exists, a tap returns [AuthOutcome.NotConfigured] and the UI nudges the user to
 * email sign-in, which works everywhere today.
 *
 * ### Activating a provider (one place to wire it)
 * 1. Enable the provider in the Supabase dashboard (Authentication → Providers) with its client
 *    id / secret — Google needs a Web OAuth client id; Apple a Services id + key.
 * 2. Add the provider artifact to the platform that owns it (`supabase-auth-google` to androidApp,
 *    `supabase-auth-apple` to iosApp) and run its native ceremony to obtain an id token:
 *
 *    ```kotlin
 *    // Android (Google):
 *    val credential = googleAuthProvider(GoogleSignInConfig(serverClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID))
 *        .signIn().getOrThrow()
 *    val session = supabase.auth!!.signInWithIdToken(
 *        provider = "google", idToken = credential.idToken, nonce = credential.nonce,
 *    ).getOrThrow()
 *    sessionStore.set(session.toAuthSession())
 *    ```
 * 3. Hand that flow to this service per platform (e.g. via an `expect`/`actual` launcher that
 *    needs the Android `Activity` / iOS presentation anchor) and return [AuthOutcome.Success].
 */
@Inject
@SingleIn(AppScope::class)
class SocialAuthService {

    /**
     * Starts native sign-in for [provider]. Currently returns [AuthOutcome.NotConfigured] for both
     * providers because no OAuth provider is enabled on the demo project yet — see the class doc
     * for how to light each one up. Email sign-in via [AuthService] is the working path today.
     */
    @Suppress("UNUSED_PARAMETER")
    suspend fun signIn(provider: SocialProvider): AuthOutcome = AuthOutcome.NotConfigured
}
