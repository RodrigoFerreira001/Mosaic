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
 * Renders a Material 3 Floating Action Button (FAB) that displays a single [icon]. The visual
 * footprint is controlled by [size].
 *
 * **Updatable fields (via UpdateTiles):** `icon`, `size`, `enabled`, `visibility`, `style`.
 *
 * **Triggers dispatched:**
 * - [OnClickEventTrigger] — fired when the user taps the FAB.
 *
 * **Notes:** [size] maps to three Compose variants: `DEFAULT` renders a [SmallFloatingActionButton],
 * `MEDIUM` renders a standard [FloatingActionButton], and `LARGE` renders a
 * [LargeFloatingActionButton]. Despite the schema carrying an [enabled] field, the current
 * renderer does not pass `enabled` to the underlying composable — the FAB is always interactive
 * at the Compose level regardless of the schema value. [icon] is required and rendered via the
 * shared [Icon] composable using [icon.name].
 */
@Immutable
@Triggers(
    [
        OnClickEventTrigger::class
    ]
)
@Serializable
@SerialName("FloatingActionButton")
data class FloatingActionButtonTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("icon") val icon: IconSchema,
    @SerialName("size") val size: Size,
    @SerialName("enabled") val enabled: Boolean,
) : TileSchema {

    enum class Size {
        DEFAULT, MEDIUM, LARGE
    }
}
