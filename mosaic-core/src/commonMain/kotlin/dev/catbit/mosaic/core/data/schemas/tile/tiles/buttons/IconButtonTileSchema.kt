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
 * Renders a Material 3 icon button in one of four visual styles controlled by [buttonType].
 * The button displays a single [icon] with no label.
 *
 * **Updatable fields (via UpdateTiles):** `icon`, `buttonType`, `loading`, `enabled`, `visibility`, `style`.
 *
 * **Triggers dispatched:**
 * - [OnClickEventTrigger] — fired when the user taps the icon button.
 *
 * **Notes:** The [icon] field is required (non-nullable). [buttonType] maps to [IconButton]
 * (DEFAULT), [FilledIconButton] (FILLED), [FilledTonalIconButton] (FILLED_TONAL), or
 * [OutlinedIconButton] (OUTLINED). When [loading] is `true` the icon is replaced by a
 * [CircularProgressIndicator], same behavior as [ButtonTileSchema.loading]. The [enabled] flag
 * controls whether the button is interactive; a disabled button receives no touch events and
 * the icon is rendered with reduced opacity by the Material theme.
 */
@Immutable
@Triggers(
    [
        OnClickEventTrigger::class
    ]
)
@Serializable
@SerialName("IconButton")
data class IconButtonTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("icon") val icon: IconSchema,
    @SerialName("buttonType") val buttonType: Type = Type.DEFAULT,
    @SerialName("loading") val loading: Boolean = false,
    @SerialName("enabled") val enabled: Boolean,
) : TileSchema {

    enum class Type {
        DEFAULT,
        FILLED,
        FILLED_TONAL,
        OUTLINED,
    }
}
