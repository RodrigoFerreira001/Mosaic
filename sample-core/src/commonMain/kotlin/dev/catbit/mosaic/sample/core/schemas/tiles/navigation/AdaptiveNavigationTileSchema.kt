package dev.catbit.mosaic.sample.core.schemas.tiles.navigation

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.animation.ContentTransitionSchema
import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEntryChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEntrySetEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NestedNavigationGraphTileSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import dev.catbit.mosaic.sample.core.schemas.triggers.OnAdaptiveNavigationItemClickEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders a window-size-adaptive navigation shell that automatically selects the appropriate
 * navigation pattern at runtime:
 * - **Compact width, more than 5 entries** → [ModalNavigationDrawer] with a hamburger-menu
 *   [TopAppBar] to open it.
 * - **Compact width, up to 5 entries** → [NavigationBar] at the bottom.
 * - **Medium/Expanded width** → [NavigationRail] on the left side with [primaryAction] in
 *   its header slot.
 *
 * In all layouts the content area is an embedded [NavDisplay] (Navigation 3) back-stack of
 * [MosaicScreen] destinations. The navigator is registered in [NavigatorsHolder] under
 * [navigatorId]. Each [NavigationEntry] declares the screen it maps to along with its initial
 * and failure tile/event trees and optional per-entry transitions.
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `entries: List<NavigationEntry>`,
 * `hiddenEntries: List<NestedNavigationGraphTileSchema.Entry>?`,
 * `startEntryId: String`, `selectedEntryId: String`, `primaryAction: TileSchema?`,
 * `topBar: TopBarSchema?`, `defaultTransition: ContentTransitionSchema?`,
 * `defaultPopTransition: ContentTransitionSchema?`,
 * `defaultPredictivePopTransition: ContentTransitionSchema?`
 *
 * **Hidden entries:** [hiddenEntries] registers additional destinations under the same
 * [navigatorId]/[ScreenExtrasHolder] as [entries], reusing [NestedNavigationGraphTileSchema.Entry]
 * (screen id + tile/event trees, no icon/label). They are reachable via `navigationController.navigate`
 * but are never rendered as items in the [NavigationBar]/[NavigationRail]/drawer.
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
 * In compact layouts [primaryAction] is rendered as a floating button above the
 * [NavigationBar] / content area (bottom-end, 16 dp padding). In the rail layout it occupies
 * the [NavigationRail] header slot. [topBar] is rendered as a [TopAppBar] above the content
 * area in all layouts; in the drawer layout the hamburger icon is prepended automatically.
 */
@Triggers(
    [
        OnAdaptiveNavigationItemClickEventTrigger::class,
        OnNavigationEntryChangedEventTrigger::class,
        OnNavigationEntrySetEventTrigger::class,
    ]
)
@Immutable
@Serializable
@SerialName("AdaptiveNavigation")
data class AdaptiveNavigationTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("navigatorId") val navigatorId: String,
    @SerialName("entries") val entries: SerializableImmutableList<NavigationEntry>,
    @SerialName("hiddenEntries") val hiddenEntries: SerializableImmutableList<NestedNavigationGraphTileSchema.Entry>? = null,
    @SerialName("startEntryId") val startEntryId: String,
    @SerialName("selectedEntryId") val selectedEntryId: String,
    @SerialName("topBar") val topBar: TopBarSchema,
    @SerialName("primaryAction") val primaryAction: TileSchema? = null,
    @SerialName("defaultTransition") val defaultTransition: ContentTransitionSchema? = null,
    @SerialName("defaultPopTransition") val defaultPopTransition: ContentTransitionSchema? = null,
    @SerialName("defaultPredictivePopTransition") val defaultPredictivePopTransition: ContentTransitionSchema? = null,
) : TileSchema {

    @Immutable
    @Serializable
    data class TopBarSchema(
        @SerialName("title") val title: TileSchema,
        @SerialName("subtitle") val subtitle: SerializableImmutableList<TileSchema>? = null,
        @SerialName("actions") val actions: SerializableImmutableList<TileSchema>? = null,
        @SerialName("barStyle") val barStyle: BarStyle = BarStyle.DEFAULT,
        @SerialName("backgroundColor") val backgroundColor: ColorSchema? = null,
    ) {
        enum class BarStyle { DEFAULT, CENTER_ALIGNED, MEDIUM, LARGE }
    }

    @Immutable
    @Serializable
    data class NavigationEntry(
        @SerialName("id") val id: String,
        @SerialName("icon") val icon: IconSchema,
        @SerialName("label") val label: String?,
        @SerialName("initialTiles") val initialTiles: SerializableImmutableList<TileSchema>,
        @SerialName("initialEvents") val initialEvents: SerializableImmutableList<EventSchema>,
        @SerialName("failureTiles") val failureTiles: SerializableImmutableList<TileSchema>,
        @SerialName("failureEvents") val failureEvents: SerializableImmutableList<EventSchema>,
        @SerialName("transition") val transition: ContentTransitionSchema? = null,
        @SerialName("popTransition") val popTransition: ContentTransitionSchema? = null,
        @SerialName("predictivePopTransition") val predictivePopTransition: ContentTransitionSchema? = null,
    )
}
