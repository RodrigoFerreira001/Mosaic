package dev.catbit.mosaic.core.data.schemas.event.events.image

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Mirrors `io.github.aryapreetam.cmpimgcompress.ResizeOptions` 1:1 so it can be used as an
 * [dev.catbit.mosaic.core.data.schemas.event.EventSchema] field. Only takes effect alongside a
 * non-null [CompressionScheme].
 *
 * @property maxLongEdgePx Caps the image's longest edge, preserving aspect ratio. Null leaves it unconstrained.
 * @property downscaleOnly If true, never upscales images smaller than [maxLongEdgePx].
 * @property maintainAspectRatio If true, resizing preserves the original aspect ratio.
 */
@Immutable
@Serializable
data class ImageResizeOptions(
    @SerialName("maxLongEdgePx") val maxLongEdgePx: Int? = 2560,
    @SerialName("downscaleOnly") val downscaleOnly: Boolean = true,
    @SerialName("maintainAspectRatio") val maintainAspectRatio: Boolean = true
)
