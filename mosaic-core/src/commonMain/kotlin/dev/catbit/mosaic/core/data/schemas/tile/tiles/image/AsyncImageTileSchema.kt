package dev.catbit.mosaic.core.data.schemas.tile.tiles.image

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnAsyncImageLoadFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnAsyncImageLoadStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnAsyncImageLoadSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders an image using Coil 3's [AsyncImage] composable. The image is loaded from [model]
 * (a remote URL, raw bytes, or a base64-encoded string) and displayed with the given
 * [contentScale], [alignment], and [alpha]. Optionally clips the image to its layout bounds
 * when [clipToBounds] is `true`.
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `model: Model`, `contentDescription: String?`,
 * `contentScale: ContentScale`, `alpha: Float`, `clipToBounds: Boolean`,
 * `alignment: AlignmentSchema.TwoDimensional`
 *
 * **Triggers dispatched:**
 * - `OnAsyncImageLoadStartEventTrigger` — fired when Coil begins loading the image
 *   (`onLoading` callback).
 * - `OnAsyncImageLoadSuccessEventTrigger` — fired when the image is successfully decoded and
 *   ready to display (`onSuccess` callback).
 * - `OnAsyncImageLoadFailureEventTrigger` — fired if the image fails to load for any reason
 *   (`onError` callback).
 *
 * **Notes:** All three load-state triggers are always wired regardless of whether events are
 * registered, meaning the server can react to load outcomes by attaching event runners to any
 * of these triggers. Content scale options are CROP, FIT, FILL_HEIGHT, FILL_WIDTH, INSIDE,
 * and FILL_BOUNDS.
 */
@Immutable
@Triggers(
    [
        OnAsyncImageLoadFailureEventTrigger::class,
        OnAsyncImageLoadStartEventTrigger::class,
        OnAsyncImageLoadSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("AsyncImage")
data class AsyncImageTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("model") val model: Model,
    @SerialName("contentDescription") val contentDescription: String?,
    @SerialName("contentScale") val contentScale: ContentScale,
    @SerialName("alpha") val alpha: Float,
    @SerialName("clipToBounds") val clipToBounds: Boolean,
    @SerialName("alignment") val alignment: AlignmentSchema.TwoDimensional
) : TileSchema {

    enum class ContentScale {
        CROP,
        FIT,
        FILL_HEIGHT,
        FILL_WIDTH,
        INSIDE,
        FILL_BOUNDS,
    }

    @Serializable
    sealed interface Model {
        @Serializable
        @SerialName("Url")
        data class Url(@SerialName("url") val url: String) : Model

        @Serializable
        @SerialName("ArrayOfBytes")
        data class ArrayOfBytes(@SerialName("byteArray") val byteArray: ByteArray) : Model {
            override fun equals(other: Any?): Boolean =
                this === other || (other is ArrayOfBytes && byteArray.contentEquals(other.byteArray))

            override fun hashCode(): Int = byteArray.contentHashCode()
        }

        @Serializable
        @SerialName("Base64")
        data class Base64(@SerialName("base64") val base64: String) : Model
    }
}

