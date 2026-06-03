package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLongPressEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders a stacking container that overlays its child tiles on top of each other using
 * Compose's [Box] layout. Children are aligned according to [alignment].
 *
 * **Updatable fields (via UpdateTiles):** `tiles: List<TileSchema>`, `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `alignment: AlignmentSchema.TwoDimensional`
 *
 * **Triggers dispatched:** `OnClickEventTrigger` and `OnLongPressEventTrigger` — fired when
 * the box itself is tapped or long-pressed (requires events to be wired on the schema).
 *
 * **Notes:** Unlike [ColumnTileSchema] and [RowTileSchema], this tile does not support
 * scrolling and does not expose a scoped CompositionLocal for child scope modifiers.
 */
@Triggers(
    [
        OnClickEventTrigger::class,
        OnLongPressEventTrigger::class
    ]
)
@Serializable
@SerialName("Box")
data class BoxTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: List<TileSchema>,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("alignment") val alignment: AlignmentSchema.TwoDimensional
) : TileSchema