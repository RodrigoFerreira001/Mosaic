package dev.catbit.mosaic.client.extensions

import dev.catbit.mosaic.core.data.schemas.event.events.image.CompressionScheme
import dev.catbit.mosaic.core.data.schemas.event.events.image.ImageResizeOptions
import io.github.aryapreetam.cmpimgcompress.CompressionConfig
import io.github.aryapreetam.cmpimgcompress.ResizeOptions

fun CompressionScheme.toCompressionConfig(): CompressionConfig = when (this) {
    is CompressionScheme.ByQuality -> CompressionConfig.ByQuality(qualityPercent)
    is CompressionScheme.ByTargetSize -> CompressionConfig.ByTargetSize(targetSizeKb)
}

fun ImageResizeOptions.toResizeOptions(): ResizeOptions = ResizeOptions(
    maxLongEdgePx = maxLongEdgePx,
    downscaleOnly = downscaleOnly,
    maintainAspectRatio = maintainAspectRatio
)

/** Sniffs the mime type of raw image bytes from their magic-byte signature. */
fun sniffImageMimeType(bytes: ByteArray): String = when {
    bytes.size >= 3 &&
        bytes[0] == 0xFF.toByte() && bytes[1] == 0xD8.toByte() && bytes[2] == 0xFF.toByte() -> "image/jpeg"

    bytes.size >= 8 &&
        bytes[0] == 0x89.toByte() && bytes[1] == 0x50.toByte() -> "image/png"

    bytes.size >= 12 &&
        bytes[8] == 0x57.toByte() && bytes[9] == 0x45.toByte() &&
        bytes[10] == 0x42.toByte() && bytes[11] == 0x50.toByte() -> "image/webp"

    else -> "image/jpeg"
}
