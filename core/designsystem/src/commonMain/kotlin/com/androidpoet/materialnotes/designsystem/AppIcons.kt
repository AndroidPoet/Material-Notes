package com.androidpoet.materialnotes.designsystem

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

/** Like [materialIcon] but with per-path colors baked in — used for brand marks (e.g. the Google G). */
private fun brandIcon(name: String, paths: List<Pair<String, Color>>): ImageVector =
    ImageVector.Builder(
        name = name,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        paths.forEach { (data, color) -> addPath(pathData = addPathNodes(data), fill = SolidColor(color)) }
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
    val CloudSync: ImageVector = materialIcon(
        "CloudSync",
        "M19.35,10.04C18.67,6.59 15.64,4 12,4 9.11,4 6.6,5.64 5.35,8.04 2.34,8.36 0,10.91 0,14c0,3.31 2.69,6 6,6h13c2.76,0 5,-2.24 5,-5 0,-2.64 -2.05,-4.78 -4.65,-4.96zM17,13l-3,3h2c0,1.66 -1.34,3 -3,3 -0.85,0 -1.61,-0.35 -2.16,-0.92l-1.42,1.42C10.32,20.41 11.6,21 13,21c2.76,0 5,-2.24 5,-5h2l-3,-3zM9,15c0,-1.66 1.34,-3 3,-3 0.85,0 1.61,0.35 2.16,0.92l1.42,-1.42C14.68,10.59 13.4,10 12,10c-2.76,0 -5,2.24 -5,5H5l3,3 3,-3H9z",
    )
    val Logout: ImageVector = materialIcon(
        "Logout",
        "M17,7l-1.41,1.41L18.17,11H8v2h10.17l-2.58,2.58L17,17l5,-5zM4,5h8V3H4c-1.1,0 -2,0.9 -2,2v14c0,1.1 0.9,2 2,2h8v-2H4V5z",
    )

    /** Apple logo, monochrome — tinted at the call site. */
    val Apple: ImageVector = materialIcon(
        "Apple",
        "M17.05,12.04c-0.03,-2.9 2.37,-4.29 2.48,-4.36 -1.35,-1.98 -3.46,-2.25 -4.21,-2.28 -1.79,-0.18 -3.5,1.05 -4.41,1.05 -0.9,0 -2.31,-1.03 -3.8,-1 -1.96,0.03 -3.76,1.14 -4.77,2.89 -2.03,3.53 -0.52,8.76 1.45,11.63 0.96,1.4 2.11,2.98 3.62,2.92 1.45,-0.06 2,-0.94 3.76,-0.94 1.75,0 2.25,0.94 3.79,0.9 1.56,-0.03 2.55,-1.43 3.51,-2.84 1.1,-1.63 1.56,-3.21 1.58,-3.29 -0.03,-0.02 -3.03,-1.16 -3.06,-4.61zM14.13,5.38c0.8,-0.97 1.34,-2.32 1.19,-3.66 -1.15,0.05 -2.54,0.77 -3.36,1.74 -0.74,0.86 -1.39,2.23 -1.21,3.55 1.28,0.1 2.59,-0.65 3.38,-1.62z",
    )

    /** The four-color Google "G" mark. */
    val Google: ImageVector = brandIcon(
        "Google",
        listOf(
            "M22.56,12.25c0,-0.78 -0.07,-1.53 -0.2,-2.25H12v4.26h5.92c-0.26,1.37 -1.04,2.53 -2.21,3.31v2.77h3.57c2.08,-1.92 3.28,-4.74 3.28,-8.09z" to Color(0xFF4285F4),
            "M12,23c2.97,0 5.46,-0.98 7.28,-2.66l-3.57,-2.77c-0.98,0.66 -2.23,1.06 -3.71,1.06 -2.86,0 -5.29,-1.93 -6.16,-4.53H2.18v2.84C3.99,20.53 7.7,23 12,23z" to Color(0xFF34A853),
            "M5.84,14.09c-0.22,-0.66 -0.35,-1.36 -0.35,-2.09s0.13,-1.43 0.35,-2.09V7.07H2.18C1.43,8.55 1,10.22 1,12s0.43,3.45 1.18,4.93l2.85,-2.22 0.81,-0.62z" to Color(0xFFFBBC05),
            "M12,5.38c1.62,0 3.06,0.56 4.21,1.64l3.15,-3.15C17.45,2.09 14.97,1 12,1 7.7,1 3.99,3.47 2.18,7.07l3.66,2.84c0.87,-2.6 3.3,-4.53 6.16,-4.53z" to Color(0xFFEA4335),
        ),
    )
}
