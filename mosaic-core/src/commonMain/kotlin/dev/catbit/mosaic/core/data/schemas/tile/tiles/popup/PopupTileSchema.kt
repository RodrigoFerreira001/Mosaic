package dev.catbit.mosaic.core.data.schemas.tile.tiles.popup

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders an anchor — [tiles], laid out with `Box` semantics and carrying [style] and
 * [visibility] — with a Compose `Popup` containing [popupTiles] floating over it. The popup is
 * only composed while [expanded] is `true`.
 *
 * **Positioning:** [alignment] places the popup relative to the anchor's bounds, and
 * [offsetX] / [offsetY] are dp offsets applied on top of that. `Top*` and `Bottom*` alignments
 * put the popup fully above/below the anchor with [offsetY] as the gap; `CenterStart` /
 * `CenterEnd` put it fully to the side with [offsetX] as the gap (mirrored under RTL); the
 * corner alignments flush-align the popup's matching edge with the anchor's, with [offsetX] as a
 * plain translation. The final position is clamped to the window, so a popup can never be pushed
 * off screen.
 *
 * **Dismissal:** [focusable], [dismissOnBackPress] and [dismissOnClickOutside] map onto Compose
 * `PopupProperties`. When a dismissal happens the renderer dispatches a local
 * `PopupTileEvents.OnTogglePopup` and the holder flips [expanded], so closing by gesture needs
 * no server round trip. Opening and closing from the server side is done with
 * `TogglePopupEventSchema` pointing at this tile's [id] — typically wired to the anchor's click.
 *
 * **Triggers dispatched:** none. The tile emits no trigger of its own, so any `events` declared
 * on it are never fired — wire events on the anchor and popup tiles instead.
 *
 * **Notes:** [style] and [visibility] apply to the anchor only — the popup renders in its own
 * window and is not affected by them. Its content is also unstyled and undecorated, so add a
 * `Card` or a styled `Box` inside [popupTiles] if you want a surface behind it.
 */
@Immutable
@Serializable
@SerialName("Popup")
data class PopupTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("popupTiles") val popupTiles: SerializableImmutableList<TileSchema>,
    @SerialName("expanded") val expanded: Boolean,
    @SerialName("alignment") val alignment: AlignmentSchema.TwoDimensional,
    @SerialName("offsetX") val offsetX: Int,
    @SerialName("offsetY") val offsetY: Int,
    @SerialName("focusable") val focusable: Boolean,
    @SerialName("dismissOnBackPress") val dismissOnBackPress: Boolean,
    @SerialName("dismissOnClickOutside") val dismissOnClickOutside: Boolean
) : TileSchema
