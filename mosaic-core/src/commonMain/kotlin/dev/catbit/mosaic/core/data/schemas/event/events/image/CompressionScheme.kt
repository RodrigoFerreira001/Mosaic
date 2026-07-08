package dev.catbit.mosaic.core.data.schemas.event.events.image

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Mirrors `io.github.aryapreetam.cmpimgcompress.CompressionConfig` (not `@Serializable` in the
 * library) so it can be used as an [dev.catbit.mosaic.core.data.schemas.event.EventSchema] field.
 */
@Immutable
@Serializable
sealed interface CompressionScheme {

    /** @property qualityPercent Target compression quality (0-100). */
    @Immutable
    @Serializable
    @SerialName("ByQuality")
    data class ByQuality(
        @SerialName("qualityPercent") val qualityPercent: Float
    ) : CompressionScheme

    /** @property targetSizeKb Target output size in KB; the library iterates to approximate it. */
    @Immutable
    @Serializable
    @SerialName("ByTargetSize")
    data class ByTargetSize(
        @SerialName("targetSizeKb") val targetSizeKb: Int
    ) : CompressionScheme
}
