package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDisplayEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLongPressEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Compose `Box` that stacks its [tiles] on top of each other in declaration order
 * (later children paint above earlier ones), positioning all of them with [alignment].
 *
 * **Triggers dispatched:**
 * - `OnDisplayEventTrigger` — fired once when the tile enters composition (keyed by tile id).
 * - `OnClickEventTrigger` — fired when the box is tapped.
 * - `OnLongPressEventTrigger` — fired when the box is long-pressed.
 *
 * Both are wired only when the matching event is declared on this tile; with neither declared,
 * the box is not made interactive at all.
 *
 * **Notes:** unlike [ColumnTileSchema] and [RowTileSchema], this tile is never scrollable and
 * publishes no scope CompositionLocal, so children cannot use column/row scope modifiers such
 * as `weight`.
 */
@Immutable
@Triggers(
    [
        OnDisplayEventTrigger::class,
        OnClickEventTrigger::class,
        OnLongPressEventTrigger::class,
    ]
)
@Serializable
@SerialName("Box")
data class BoxTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("alignment") val alignment: AlignmentSchema.TwoDimensional
) : TileSchema