package dev.catbit.mosaic.core.data.schemas.tile.tiles.image

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a single Material Symbol described by [icon], honouring its name, color, size and
 * style. This is the standalone icon tile — [style] is applied to the icon's own layout node.
 *
 * **Triggers dispatched:** none. The tile emits no trigger of its own — not even
 * `OnDisplayEventTrigger` — and is not clickable, so any `events` declared on it are never
 * fired. Use `IconButton` when you need a tappable icon, or wrap this tile in a clickable
 * container.
 */
@Immutable
@Serializable
@SerialName("Icon")
data class IconTileSchema(
    override val id: String,
    override val events: SerializableImmutableList<EventSchema>?,
    override val style: StyleSchema,
    override val searchableTerms: SerializableImmutableList<String>?,
    override val visibility: TileSchema.Visibility,
    val icon: IconSchema
) : TileSchema
