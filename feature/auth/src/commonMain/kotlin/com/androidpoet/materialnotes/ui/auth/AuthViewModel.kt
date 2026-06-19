package com.androidpoet.materialnotes.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidpoet.materialnotes.data.auth.AuthOutcome
import com.androidpoet.materialnotes.data.auth.AuthService
import com.androidpoet.materialnotes.data.auth.SocialAuthService
import com.androidpoet.materialnotes.data.auth.SocialProvider
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Whether the form is creating a new account or signing into an existing one. */
enum class AuthMode { SignIn, SignUp }

data class AuthUiState(
    val mode: AuthMode = AuthMode.SignIn,
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
) {
    val canSubmit: Boolean
        get() = !loading && email.isNotBlank() && password.isNotBlank()
}

/**
 * Drives the login / sign-up screen. On success the [AuthService] writes the session into the
 * shared SessionStore, which the navigation gate observes to swap to the notes app — so this view
 * model never navigates itself; it only owns the form and its loading / error state.
 */
@Inject
class AuthViewModel(
    private val authService: AuthService,
    private val socialAuthService: SocialAuthService,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun onEmailChange(value: String) = _state.update { it.copy(email = value, error = null) }

    fun onPasswordChange(value: String) = _state.update { it.copy(password = value, error = null) }

    fun toggleMode() = _state.update {
        val next = if (it.mode == AuthMode.SignIn) AuthMode.SignUp else AuthMode.SignIn
        it.copy(mode = next, error = null)
    }

    fun submit() {
        val current = _state.value
        if (current.loading) return
        if (current.password.length < 6) {
            _state.update { it.copy(error = "Password must be at least 6 characters.") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            val outcome = when (current.mode) {
                AuthMode.SignIn -> authService.signIn(current.email, current.password)
                AuthMode.SignUp -> authService.signUp(current.email, current.password)
            }
            handleOutcome(outcome)
        }
    }

    fun signInWith(provider: SocialProvider) {
        if (_state.value.loading) return
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            handleOutcome(socialAuthService.signIn(provider), providerLabel = provider.label)
        }
    }

    private fun handleOutcome(outcome: AuthOutcome, providerLabel: String? = null) {
        _state.update {
            when (outcome) {
                // Success: the SessionStore flips and the gate moves us on; just stop the spinner.
                is AuthOutcome.Success -> it.copy(loading = false, error = null)
                is AuthOutcome.Failure -> it.copy(loading = false, error = outcome.message)
                AuthOutcome.NotConfigured -> it.copy(
                    loading = false,
                    error = providerLabel?.let { p -> "$p sign-in isn't enabled yet — use your email for now." }
                        ?: "Cloud sign-in isn't configured.",
                )
            }
        }
    }
}
