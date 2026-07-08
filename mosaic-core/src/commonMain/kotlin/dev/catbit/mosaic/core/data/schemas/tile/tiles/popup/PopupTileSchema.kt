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
 * Renders a [Box] wrapping its [tiles] (the anchor content, e.g. a button) plus a Compose
 * [Popup] rendered outside the normal layout flow when [expanded] is `true`. The [Popup]'s
 * content is [popupTiles] — arbitrary, free-form tiles (unlike [Menu], which is limited to a
 * fixed list of menu items).
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `tiles: SerializableImmutableList<TileSchema>`,
 * `popupTiles: SerializableImmutableList<TileSchema>`, `expanded: Boolean`,
 * `alignment: AlignmentSchema.TwoDimensional`, `offsetX: Int`, `offsetY: Int`,
 * `focusable: Boolean`, `dismissOnBackPress: Boolean`, `dismissOnClickOutside: Boolean`
 *
 * **Triggers dispatched:** none specific to this tile — standard triggers (`OnDisplay`,
 * `OnClick`, etc.) still apply to the tile as a whole.
 *
 * **Notes:** The open/closed state of the popup is server-driven via [expanded]. Because
 * Compose's `Popup` composable has no `expanded` parameter, the renderer only composes it while
 * [expanded] is `true`. When the user dismisses the popup (back press or tap outside, depending
 * on [dismissOnBackPress]/[dismissOnClickOutside]), the renderer dispatches the internal
 * `PopupTileEvents.OnTogglePopup` event, which the client-side tile holder uses to flip
 * [expanded] locally while a server update is requested. [alignment] and
 * [offsetX]/[offsetY] (in dp) control the popup's position relative to the anchor, mirroring
 * Compose's `Popup(alignment, offset, ...)` parameters. [focusable], [dismissOnBackPress] and
 * [dismissOnClickOutside] map directly to Compose's `PopupProperties`.
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
