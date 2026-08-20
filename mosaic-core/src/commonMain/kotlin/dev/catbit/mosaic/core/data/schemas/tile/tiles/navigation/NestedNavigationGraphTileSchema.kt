package dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import dev.catbit.mosaic.core.data.schemas.animation.ContentTransitionSchema
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEntrySetEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Hosts a self-contained Navigation 3 `NavDisplay` inside a tile, so a region of a screen can
 * have its own back stack. The stack starts at [startEntryId] and every screen it can show is
 * declared in [entries].
 *
 * **Registration:** on first composition the tile registers a `NavigationController` under
 * [navigatorId] in the app's navigator holder — that id is how navigation events address this
 * graph — and registers each [Entry] (its initial tiles/events, failure tiles/events and
 * transitions) in the screen-extras holder. Both registrations are undone when the tile leaves
 * composition, so the graph and its entries only exist while the tile is on screen. Each entry
 * gets its own saveable state holder and `ViewModelStore`, so screen state survives navigating
 * back and forth within the graph.
 *
 * **Transitions:** for each navigation the per-entry [Entry.transition], [Entry.popTransition]
 * and [Entry.predictivePopTransition] win; when an entry does not define one, the graph-level
 * [defaultTransition] / [defaultPopTransition] / [defaultPredictivePopTransition] apply; when
 * neither is set the navigation is instantaneous (no enter or exit animation). The system back
 * gesture pops this graph's own stack.
 *
 * **Triggers dispatched:**
 * - `OnNavigationEntrySetEventTrigger` — fired whenever an entry is displayed, carrying the
 *   entry's `screenId`, so events can be wired per destination. It fires for the start
 *   destination too, and again whenever navigation returns to an entry.
 */
@Immutable
@Triggers([OnNavigationEntrySetEventTrigger::class])
@Serializable
@SerialName("NestedNavigationGraph")
data class NestedNavigationGraphTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("navigatorId") val navigatorId: String,
    @SerialName("entries") val entries: SerializableImmutableList<Entry>,
    @SerialName("startEntryId") val startEntryId: String,
    @SerialName("defaultTransition") val defaultTransition: ContentTransitionSchema? = null,
    @SerialName("defaultPopTransition") val defaultPopTransition: ContentTransitionSchema? = null,
    @SerialName("defaultPredictivePopTransition") val defaultPredictivePopTransition: ContentTransitionSchema? = null,
) : TileSchema {

    @Serializable
    data class Entry(
        @SerialName("screenId") val screenId: String,
        @SerialName("initialTiles") val initialTiles: SerializableImmutableList<TileSchema>,
        @SerialName("initialEvents") val initialEvents: SerializableImmutableList<EventSchema>,
        @SerialName("failureTiles") val failureTiles: SerializableImmutableList<TileSchema>,
        @SerialName("failureEvents") val failureEvents: SerializableImmutableList<EventSchema>,
        @SerialName("transition") val transition: ContentTransitionSchema? = null,
        @SerialName("popTransition") val popTransition: ContentTransitionSchema? = null,
        @SerialName("predictivePopTransition") val predictivePopTransition: ContentTransitionSchema? = null,
    )
}
