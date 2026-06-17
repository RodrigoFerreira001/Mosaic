package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.ArrangementSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a horizontally-wrapping flow layout using Compose's [FlowRow]. Children are placed
 * left-to-right and wrap onto new rows when they exceed the available width. The maximum
 * number of items per row is capped by [maxItemsInEachRow].
 *
 * **Updatable fields (via UpdateTiles):** `tiles: SerializableImmutableList<TileSchema>`, `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `horizontalArrangement: ArrangementSchema.Horizontal`,
 * `verticalArrangement: ArrangementSchema.Vertical`, `maxItemsInEachRow: Int`
 *
 * **Triggers dispatched:** `OnClickEventTrigger` — fired when the flow row container is
 * tapped (requires events to be wired on the schema).
 *
 * **Notes:** The renderer exposes [LocalFlowRowScope] so that children that need
 * [FlowRowScope] modifiers (e.g. `fillMaxRowHeight`, `weight`) can access it. Children are
 * composed eagerly (not lazy).
 */
@Immutable
@Serializable
@SerialName("FlowRow")
data class FlowRowTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("horizontalArrangement") val horizontalArrangement: ArrangementSchema.Horizontal = ArrangementSchema.Horizontal.Start,
    @SerialName("verticalArrangement") val verticalArrangement: ArrangementSchema.Vertical = ArrangementSchema.Vertical.Top,
    @SerialName("maxItemsInEachRow") val maxItemsInEachRow: Int = Int.MAX_VALUE
) : TileSchema
