package dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.animation.ContentTransitionSchema
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnAdaptiveNavigationItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEntryChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEntrySetEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders a window-size-adaptive navigation shell that automatically selects the appropriate
 * navigation pattern at runtime:
 * - **Compact width, more than 5 entries** → [ModalNavigationDrawer] with a hamburger-menu
 *   [TopAppBar] to open it.
 * - **Compact width, up to 5 entries** → [NavigationBar] at the bottom.
 * - **Medium/Expanded width** → [NavigationRail] on the left side with an optional [header]
 *   tile at the top and an optional [footer] tile pushed to the bottom.
 *
 * In all layouts the content area is an embedded [NavDisplay] (Navigation 3) back-stack of
 * [MosaicScreen] destinations. The navigator is registered in [NavigatorsHolder] under
 * [navigatorId]. Each [NavigationEntry] declares the screen it maps to along with its initial
 * and failure tile/event trees and optional per-entry transitions.
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `entries: List<NavigationEntry>`,
 * `startEntryId: String`, `selectedEntryId: String`, `header: TileSchema?`,
 * `footer: TileSchema?`, `defaultTransition: ContentTransitionSchema?`,
 * `defaultPopTransition: ContentTransitionSchema?`,
 * `defaultPredictivePopTransition: ContentTransitionSchema?`
 *
 * **Triggers dispatched:**
 * - `OnAdaptiveNavigationItemClickEventTrigger` — fired when the user taps any navigation
 *   item, carrying the entry's [id].
 * - `OnNavigationEntrySetEventTrigger` — fired via [LaunchedEffect] each time a destination
 *   becomes active, carrying the entry's screen id.
 * - `OnNavigationEntryChangedEventTrigger` — declared in `@Triggers` for documentation and
 *   tooling purposes.
 *
 * **Notes:** The compact/expanded breakpoint is `WIDTH_DP_MEDIUM_LOWER_BOUND` from the
 * Adaptive WindowSizeClass API. The navigator and screen extras are cleaned up via a
 * [DisposableEffect] on [navigatorId]. The [selectedEntryId] controls which item appears
 * highlighted; actual navigation is performed by calling `navigationController.navigate`.
 * The [header] and [footer] tiles are only rendered in the rail layout.
 */
@Triggers(
    [
        OnAdaptiveNavigationItemClickEventTrigger::class,
        OnNavigationEntryChangedEventTrigger::class,
        OnNavigationEntrySetEventTrigger::class,
    ]
)
@Serializable
@SerialName("AdaptiveNavigation")
data class AdaptiveNavigationTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("navigatorId") val navigatorId: String,
    @SerialName("entries") val entries: List<NavigationEntry>,
    @SerialName("startEntryId") val startEntryId: String,
    @SerialName("selectedEntryId") val selectedEntryId: String,
    @SerialName("header") val header: TileSchema? = null,
    @SerialName("footer") val footer: TileSchema? = null,
    @SerialName("defaultTransition") val defaultTransition: ContentTransitionSchema? = null,
    @SerialName("defaultPopTransition") val defaultPopTransition: ContentTransitionSchema? = null,
    @SerialName("defaultPredictivePopTransition") val defaultPredictivePopTransition: ContentTransitionSchema? = null,
) : TileSchema {

    @Serializable
    data class NavigationEntry(
        @SerialName("id") val id: String,
        @SerialName("icon") val icon: IconSchema,
        @SerialName("label") val label: String?,
        @SerialName("initialTiles") val initialTiles: List<TileSchema>,
        @SerialName("initialEvents") val initialEvents: List<EventSchema>,
        @SerialName("failureTiles") val failureTiles: List<TileSchema>,
        @SerialName("failureEvents") val failureEvents: List<EventSchema>,
        @SerialName("transition") val transition: ContentTransitionSchema? = null,
        @SerialName("popTransition") val popTransition: ContentTransitionSchema? = null,
        @SerialName("predictivePopTransition") val predictivePopTransition: ContentTransitionSchema? = null,
    )
}
