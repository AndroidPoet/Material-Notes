@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.androidpoet.materialnotes.designsystem

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.LocalNavAnimatedContentScope

/** Set by [SharedTransitionLayout] in the nav host; null when shared transitions aren't active. */
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

/**
 * Tags an element as a shared "container" across destinations. When the home card and the detail
 * screen use the same [key], Compose morphs one into the other (Material container transform).
 *
 * The [AnimatedVisibilityScope][androidx.compose.animation.AnimatedVisibilityScope] is provided by
 * Navigation 3's `NavDisplay` via [LocalNavAnimatedContentScope].
 */
@Composable
fun Modifier.sharedNoteBounds(key: Any): Modifier {
    val sharedScope = LocalSharedTransitionScope.current ?: return this
    val animatedScope = LocalNavAnimatedContentScope.current
    return with(sharedScope) {
        this@sharedNoteBounds.sharedBounds(
            sharedContentState = rememberSharedContentState(key = key),
            animatedVisibilityScope = animatedScope,
        )
    }
}
