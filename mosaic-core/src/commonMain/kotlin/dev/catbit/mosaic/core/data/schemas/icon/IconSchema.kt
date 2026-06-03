package dev.catbit.mosaic.core.data.schemas.icon

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a Material Symbol icon rendered via the custom `MaterialSymbol` composable.
 *
 * **Fields:**
 * - [name] — Material Symbol identifier string (e.g. `"home"`, `"visibility"`, `"add"`).
 *   Must match a symbol available in the bundled font.
 * - [color] — Optional tint applied to the icon. When `null`, inherits the local content color.
 * - [size] — Icon size in dp. Defaults to `24` when created via the `icon()` DSL helper.
 *   When `null` in JSON, the composable receives `null` and renders at its intrinsic size.
 * - [style] — Glyph style variant: [Style.OUTLINED] (default), [Style.ROUNDED], or [Style.SHARP].
 *
 * **DSL helpers (mosaic-server):**
 * ```kotlin
 * icon("home")                                                      // 24dp, outlined, no tint
 * icon("visibility", color = color(themeColorPrimary()), size = 20) // with overrides
 * icon("star", style = outlinedIcon())                              // explicit style
 * icon("star", style = roundedIcon())
 * icon("star", style = sharpIcon())
 * ```
 *
 * **Notes:** The `filled` parameter (bold/filled variant) is not part of this schema —
 * it is controlled per-call site in the client composable.
 */
@Serializable
data class IconSchema(
    @SerialName("name") val name: String,
    @SerialName("color") val color: ColorSchema ? = null,
    @SerialName("size") val size: Int ? = null,
    @SerialName("style") val style: Style = Style.OUTLINED
) {
    enum class Style {
        OUTLINED, ROUNDED, SHARP
    }
}