package dev.catbit.mosaic.server.builder.event.builders.image

import dev.catbit.mosaic.core.data.schemas.event.events.image.CompressionScheme
import dev.catbit.mosaic.core.data.schemas.event.events.image.ImageResizeOptions

/** Re-encodes the image to WebP at [qualityPercent] (0-100). */
fun byQuality(qualityPercent: Float): CompressionScheme = CompressionScheme.ByQuality(qualityPercent)

/** Re-encodes the image to WebP, iterating quality to approximate [targetSizeKb]. */
fun byTargetSize(targetSizeKb: Int): CompressionScheme = CompressionScheme.ByTargetSize(targetSizeKb)

/**
 * Resize applied in the same pass as the compression. Only takes effect alongside a non-null
 * `compression`.
 *
 * @param maxLongEdgePx Caps the image's longest edge, preserving aspect ratio. `null` leaves it unconstrained. Defaults to 2560.
 * @param downscaleOnly Whether images smaller than [maxLongEdgePx] are never upscaled. Defaults to true.
 * @param maintainAspectRatio Whether resizing preserves the original aspect ratio. Defaults to true.
 */
fun imageResizeOptions(
    maxLongEdgePx: Int? = 2560,
    downscaleOnly: Boolean = true,
    maintainAspectRatio: Boolean = true,
): ImageResizeOptions = ImageResizeOptions(
    maxLongEdgePx = maxLongEdgePx,
    downscaleOnly = downscaleOnly,
    maintainAspectRatio = maintainAspectRatio,
)
