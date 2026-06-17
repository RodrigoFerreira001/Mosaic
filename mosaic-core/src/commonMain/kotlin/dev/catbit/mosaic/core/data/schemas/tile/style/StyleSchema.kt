package dev.catbit.mosaic.core.data.schemas.tile.style

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Container for all visual layout and decoration properties applied to a tile.
 *
 * The client applies these properties in the following order (via `Modifier.styledWith()`):
 * 1. [windowInsets] — padding to avoid overlapping system UI (status bar, nav bar, IME…)
 * 2. [margin] — outer spacing around the tile (applied as `Modifier.padding` outside the size)
 * 3. [size] — width and height constraints
 * 4. [clip] — shape clip applied before background, so the background respects the shape
 * 5. [background] — fill color
 * 6. onClick (injected by tile, not part of this schema)
 * 7. [border] — drawn over the background, after clip
 * 8. [padding] — inner spacing between the border and content
 *
 * **Notes:**
 * - [size] is the only mandatory field; all others are optional.
 * - `StyleSchema.default()` fills both width and height.
 * - The order of `clip` before `background` means the background is already clipped —
 *   no need to set both `clip` and a rounded `border.radius` to get rounded corners;
 *   `clip` alone is sufficient for a rounded background.
 * - `background` currently only supports solid colors (gradient/blur are planned but not implemented).
 */
@Immutable
@Serializable
data class StyleSchema(
    @SerialName("size") val size: SizeSchema,
    @SerialName("margin") val margin: MarginSchema? = null,
    @SerialName("padding") val padding: PaddingSchema? = null,
    @SerialName("background") val background: ColorSchema? = null, // Todo mudar para Solid or Gradient
    @SerialName("border") val border: BorderSchema? = null,
    @SerialName("clip") val clip: ClipSchema? = null,
    @SerialName("windowInsets") val windowInsets: WindowInsetsSchema? = null
) {
    companion object {
        fun default() = StyleSchema(
            size = SizeSchema (
                width = SizeSchema.Behavior.Horizontal.Fill(),
                height = SizeSchema.Behavior.Vertical.Fill()
            )
        )
    }
}















