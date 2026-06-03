package dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.animation.ContentTransitionSchema
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEntryChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEntrySetEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders an embedded Navigation 3 [NavDisplay] that hosts a full navigation back-stack of
 * [MosaicScreen] destinations. Each [Entry] declares the screen it maps to, along with its
 * initial and failure tile/event trees and optional per-screen enter/pop/predictive-pop
 * transitions. The navigator is registered in [NavigatorsHolder] under [navigatorId] so that
 * server events (e.g. NavigateEvent) can drive it by id. Entries are also registered in
 * [ScreenExtrasHolder] so their tile/event trees are available when the corresponding screen
 * is pushed onto the stack.
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `entries: List<Entry>`, `startEntryId: String`,
 * `defaultTransition: ContentTransitionSchema?`, `defaultPopTransition: ContentTransitionSchema?`,
 * `defaultPredictivePopTransition: ContentTransitionSchema?`
 *
 * **Triggers dispatched:**
 * - `OnNavigationEntrySetEventTrigger` — fired via [LaunchedEffect] each time a new entry
 *   becomes the active destination, carrying the entry's screen id.
 * - `OnNavigationEntryChangedEventTrigger` — declared in `@Triggers` for documentation and
 *   tooling purposes; actual dispatch depends on event runner wiring.
 *
 * **Notes:** The navigator and screen extras are unregistered in a [DisposableEffect] when
 * the tile leaves the composition, preventing stale navigator references. Per-entry
 * transitions override the graph-level defaults when present. The back-stack key type is
 * [ScreenNavKey], which is serialized for saved-state restoration across process death.
 * ViewModel scope is preserved per entry via [rememberViewModelStoreNavEntryDecorator].
 */
@Triggers(
    [
        OnNavigationEntryChangedEventTrigger::class,
        OnNavigationEntrySetEventTrigger::class,
    ]
)
@Serializable
@SerialName("NestedNavigationGraph")
data class NestedNavigationGraphTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("navigatorId") val navigatorId: String,
    @SerialName("entries") val entries: List<Entry>,
    @SerialName("startEntryId") val startEntryId: String,
    @SerialName("defaultTransition") val defaultTransition: ContentTransitionSchema? = null,
    @SerialName("defaultPopTransition") val defaultPopTransition: ContentTransitionSchema? = null,
    @SerialName("defaultPredictivePopTransition") val defaultPredictivePopTransition: ContentTransitionSchema? = null,
) : TileSchema {

    @Serializable
    data class Entry(
        @SerialName("screenId") val screenId: String,
        @SerialName("initialTiles") val initialTiles: List<TileSchema>,
        @SerialName("initialEvents") val initialEvents: List<EventSchema>,
        @SerialName("failureTiles") val failureTiles: List<TileSchema>,
        @SerialName("failureEvents") val failureEvents: List<EventSchema>,
        @SerialName("transition") val transition: ContentTransitionSchema? = null,
        @SerialName("popTransition") val popTransition: ContentTransitionSchema? = null,
        @SerialName("predictivePopTransition") val predictivePopTransition: ContentTransitionSchema? = null,
    )
}
