package dev.catbit.mosaic.core.data.schemas.tile.tiles.menu

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnMenuItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders an anchor — [tiles], laid out with `Box` semantics and carrying [style] and
 * [visibility] — with a Material 3 `DropdownMenu` attached to it. The menu lists one
 * `DropdownMenuItem` per entry in [items], each showing its
 * [MenuItem.label] plus the optional [MenuItem.leadingIcon] and [MenuItem.trailingIcon]. The
 * menu uses the large theme shape, respects the system bars and is capped at 400dp tall
 * (scrolling beyond that).
 *
 * **Open state:** [expanded] drives the menu. Dismissing it (tapping outside or pressing back)
 * dispatches a local `MenuTileEvents.OnToggleMenu` and the holder flips [expanded], so closing
 * by gesture needs no server round trip. Opening and closing from the server side is done with
 * `ToggleMenuEventSchema` pointing at this tile's [id] — typically wired to the anchor's click
 * to open, and to an item's click chain to close after acting on the selection.
 *
 * **Triggers dispatched:**
 * - `OnMenuItemClickEventTrigger` — fired when an item is tapped, carrying that item's
 *   [MenuItem.id], so events can be wired per item.
 *
 * **Notes:** [style] and [visibility] apply to the anchor only — the dropdown itself is
 * positioned by Material and is not affected by them.
 */
@Immutable
@Triggers([OnMenuItemClickEventTrigger::class])
@Serializable
@SerialName("Menu")
data class MenuTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("items") val items: SerializableImmutableList<MenuItem>,
    @SerialName("expanded") val expanded: Boolean
) : TileSchema {

    @Serializable
    data class MenuItem(
        @SerialName("id") val id: String,
        @SerialName("label") val label: String,
        @SerialName("leadingIcon") val leadingIcon: IconSchema? = null,
        @SerialName("trailingIcon") val trailingIcon: IconSchema? = null
    )
}