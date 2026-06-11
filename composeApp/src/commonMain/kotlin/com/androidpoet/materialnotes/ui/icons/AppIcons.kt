package com.androidpoet.materialnotes.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

/**
 * A tiny, self-contained icon set drawn from standard Material path data.
 *
 * `material-icons-extended` is no longer published for Compose Multiplatform 1.8+, and `material3`
 * doesn't bundle `material-icons-core`, so we vector these few icons ourselves. They are filled black
 * and recolored at the call site via `Icon(tint = …)`.
 */
private fun materialIcon(name: String, pathData: String): ImageVector =
    ImageVector.Builder(
        name = name,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        addPath(pathData = addPathNodes(pathData), fill = SolidColor(Color.Black))
    }.build()

object AppIcons {
    val Add: ImageVector = materialIcon(
        "Add",
        "M19,13h-6v6h-2v-6H5v-2h6V5h2v6h6V13z",
    )
    val Close: ImageVector = materialIcon(
        "Close",
        "M19,6.41L17.59,5 12,10.59 6.41,5 5,6.41 10.59,12 5,17.59 6.41,19 12,13.41 17.59,19 19,17.59 13.41,12z",
    )
    val Check: ImageVector = materialIcon(
        "Check",
        "M9,16.17L4.83,12l-1.42,1.41L9,19 21,7l-1.41,-1.41z",
    )
    val ArrowBack: ImageVector = materialIcon(
        "ArrowBack",
        "M20,11H7.83l5.59,-5.59L12,4l-8,8 8,8 1.41,-1.41L7.83,13H20z",
    )
    val Delete: ImageVector = materialIcon(
        "Delete",
        "M6,19c0,1.1 0.9,2 2,2h8c1.1,0 2,-0.9 2,-2V7H6v12zM19,4h-3.5l-1,-1h-5l-1,1H5v2h14V4z",
    )
    val Edit: ImageVector = materialIcon(
        "Edit",
        "M3,17.25V21h3.75L17.81,9.94l-3.75,-3.75L3,17.25zM20.71,7.04c0.39,-0.39 0.39,-1.02 0,-1.41l-2.34,-2.34c-0.39,-0.39 -1.02,-0.39 -1.41,0l-1.83,1.83 3.75,3.75 1.83,-1.83z",
    )
}
