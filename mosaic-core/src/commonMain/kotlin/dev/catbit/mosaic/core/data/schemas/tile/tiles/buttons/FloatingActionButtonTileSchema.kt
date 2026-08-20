package dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 floating action button sized by [size]:
 * [Size.DEFAULT] → `SmallFloatingActionButton`, [Size.MEDIUM] → `FloatingActionButton`,
 * [Size.LARGE] → `LargeFloatingActionButton`. Renders [icon] with its color, size and style
 * applied.
 *
 * **No `enabled`/`loading`:** the Material 3 FAB composables intentionally have no `enabled`
 * parameter — per Material Design guidance, a FAB represents a screen's primary action, and a
 * disabled-but-visible FAB fights that emphasis. Use this tile's inherited [visibility] to hide
 * the FAB entirely when its action isn't currently available, instead of disabling it in place.
 *
 * **Triggers dispatched:**
 * - `OnClickEventTrigger` — fired when the FAB is tapped.
 */
@Immutable
@Triggers([OnClickEventTrigger::class])
@Serializable
@SerialName("FloatingActionButton")
data class FloatingActionButtonTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("icon") val icon: IconSchema,
    @SerialName("size") val size: Size,
) : TileSchema {

    enum class Size {
        DEFAULT, MEDIUM, LARGE
    }
}
