package com.androidpoet.materialnotes.designsystem

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

/** Creates/retains a [ViewModel] built from the Metro graph, scoped to the current Compose owner. */
@Composable
inline fun <reified VM : ViewModel> rememberViewModel(
    key: String? = null,
    crossinline create: () -> VM,
): VM = viewModel(key = key, factory = viewModelFactory { initializer { create() } })
