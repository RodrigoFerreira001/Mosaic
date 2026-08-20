package dev.catbit.mosaic.server.builder.tile.builders.navigation

import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.TabsTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class TabsTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val selectedTabId: String,
    private val tabItems: TabItemSchemaBuilderScope.() -> Unit,
    private val tabType: TabsTileSchema.Type,
    private val scrollable: Boolean
) : TileSchemaBuilder<TabsTileSchema>() {

    override fun build() = TabsTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        selectedTabId = selectedTabId,
        tabItems = TabItemSchemaBuilderScope().apply(tabItems).build(),
        tabType = tabType,
        scrollable = scrollable
    )
}

/**
 * Renders a Material 3 tab row with one tab per entry declared via [tabItems] (built with
 * `addTab`). [tabType] picks the emphasis — primary or secondary — and [scrollable] chooses
 * between a fixed row and a scrollable one. Each tab shows its label and icon, both optional; the
 * selected tab is the one whose id equals [selectedTabId] — when it matches none of [tabItems]
 * the first tab is highlighted as a fallback, and an empty [tabItems] renders nothing at all.
 * When a tab's badge text is set (via `addTab`), it gets a badge attached to its icon (or its
 * label when it has none) — an empty string renders a small dot badge, any other value renders
 * as the badge's text. Tapping a tab flips [selectedTabId] locally on the client (no server round
 * trip needed for the indicator to move); the tile only tracks the selection, so swapping the
 * content shown below the tabs is up to the events wired to the tab clicks. Dispatches
 * `onTabItemClick(tabId)` when a tab is tapped, so events can be wired per tab.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (e.g. `onTabItemClick`).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param selectedTabId Id of the currently selected tab.
 * @param tabType Emphasis of the tab row — [primaryTabs] or [secondaryTabs]. Defaults to primary.
 * @param scrollable Whether the tab row scrolls horizontally instead of being fixed-width. Defaults to false.
 * @param tabItems Tab entries, declared with `addTab`.
 */
fun TileSchemaBuilderScope.Tabs(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    selectedTabId: String,
    tabType: TabsTileSchema.Type = primaryTabs(),
    scrollable: Boolean = false,
    tabItems: TabItemSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        TabsTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            selectedTabId = selectedTabId,
            tabItems = tabItems,
            tabType = tabType,
            scrollable = scrollable
        )
    )
}

/** Primary tab row emphasis. */
fun primaryTabs() = TabsTileSchema.Type.PRIMARY

/** Secondary tab row emphasis. */
fun secondaryTabs() = TabsTileSchema.Type.SECONDARY

class TabItemSchemaBuilder(
    private val id: String,
    private val label: String? = null,
    private val icon: IconSchema? = null,
    private val badgeText: String? = null
) : GenericBuilder<TabsTileSchema.TabItem>() {

    override fun build() = TabsTileSchema.TabItem(
        id = id,
        label = label,
        icon = icon,
        badgeText = badgeText
    )
}

class TabItemSchemaBuilderScope :
    GenericBuilderScope<TabsTileSchema.TabItem, TabItemSchemaBuilder>() {

    /**
     * Declares one tab of a `Tabs` row.
     *
     * @param id Identifier matched against `selectedTabId` and carried by `onTabItemClick` when this tab is tapped.
     * @param label Text shown for this tab. Defaults to none.
     * @param icon Icon shown for this tab. Defaults to none.
     * @param badgeText Badge attached to the icon (or the label when there's no icon) — empty string for a dot badge, any other value as text, `null` for no badge. Defaults to none.
     */
    fun addTab(
        id: String,
        label: String? = null,
        icon: IconSchema? = null,
        badgeText: String? = null
    ) {
        addBuilder(
            TabItemSchemaBuilder(
                id = id,
                label = label,
                icon = icon,
                badgeText = badgeText
            )
        )
    }
}
