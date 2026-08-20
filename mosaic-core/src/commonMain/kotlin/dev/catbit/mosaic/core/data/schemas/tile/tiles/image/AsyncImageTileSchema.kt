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
 * Renders a Coil `AsyncImage` that loads its content from [model], which can be a remote
 * [Model.Url], raw [Model.ArrayOfBytes], or a [Model.Base64] string (decoded on the client
 * before being handed to Coil). [contentScale] maps onto the Compose `ContentScale` of the same
 * name, and [contentDescription], [alpha], [clipToBounds] and [alignment] are forwarded as-is.
 *
 * **Triggers dispatched:**
 * - `OnAsyncImageLoadStartEventTrigger` — when Coil enters its loading state.
 * - `OnAsyncImageLoadSuccessEventTrigger` — when the image has been decoded and drawn.
 * - `OnAsyncImageLoadFailureEventTrigger` — when the load fails.
 *
 * These fire on every load state change, including reloads after the [model] changes, so they
 * may fire more than once over the tile's lifetime.
 *
 * **Notes:** the tile is not clickable and fires no display trigger — wrap it in a `Box` or
 * `Card` when you need tap handling. There is no built-in placeholder or error image: render
 * one yourself by reacting to the load triggers.
 */
@Immutable
@Triggers(
    [
        OnAsyncImageLoadStartEventTrigger::class,
        OnAsyncImageLoadSuccessEventTrigger::class,
        OnAsyncImageLoadFailureEventTrigger::class,
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

