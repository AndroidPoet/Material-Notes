@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.androidpoet.materialnotes.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier

/** Set by [SharedTransitionLayout] in the nav host; null when shared transitions aren't active. */
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

/** Set per navigation destination (each `composable {}` is an [AnimatedVisibilityScope]). */
val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

/**
 * Tags an element as a shared "container" across destinations. When the home card and the detail
 * screen use the same [key], Compose morphs one into the other (Material container transform).
 */
@Composable
fun Modifier.sharedNoteBounds(key: Any): Modifier {
    val sharedScope = LocalSharedTransitionScope.current ?: return this
    val animatedScope = LocalNavAnimatedVisibilityScope.current ?: return this
    return with(sharedScope) {
        this@sharedNoteBounds.sharedBounds(
            sharedContentState = rememberSharedContentState(key = key),
            animatedVisibilityScope = animatedScope,
        )
    }
}
