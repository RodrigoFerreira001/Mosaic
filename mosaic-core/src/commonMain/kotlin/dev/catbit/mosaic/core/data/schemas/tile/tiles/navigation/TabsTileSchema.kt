package dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTabItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 tab row with one `Tab` per entry in [tabItems]. [tabType] picks the
 * emphasis — [Type.PRIMARY] uses the primary tab rows, [Type.SECONDARY] the secondary ones —
 * and [scrollable] chooses between the fixed row (`false`) and the scrollable one (`true`).
 * Each tab shows its [TabItem.label] and [TabItem.icon], both optional. The selected tab is the
 * one whose [TabItem.id] equals [selectedTabId].
 *
 * **Badges:** when [TabItem.badgeText] is non-null the tab gets a `BadgedBox` — attached to the
 * icon when the tab has one, otherwise to the label. An empty string renders the small dot
 * badge; any other value renders as the badge's text. A `null` value renders no badge.
 *
 * **Selection:** tapping a tab dispatches a local `TabsTileEvents.OnTabClicked` and the holder
 * stores the new [selectedTabId], so the indicator moves immediately without a server round
 * trip. The tile only tracks the selection — swapping the content shown below the tabs is up to
 * the events wired to the tab clicks.
 *
 * **Triggers dispatched:**
 * - `OnTabItemClickEventTrigger` — fired when a tab is tapped, carrying that tab's [TabItem.id],
 *   so events can be wired per tab.
 *
 * **Notes:** when [selectedTabId] matches none of the [tabItems] the first tab is highlighted as
 * a fallback. An empty [tabItems] renders nothing at all.
 */
@Immutable
@Triggers([OnTabItemClickEventTrigger::class])
@Serializable
@SerialName("Tabs")
data class TabsTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("selectedTabId") val selectedTabId: String,
    @SerialName("tabItems") val tabItems: SerializableImmutableList<TabItem>,
    @SerialName("tabType") val tabType: Type,
    @SerialName("scrollable") val scrollable: Boolean
) : TileSchema {

    enum class Type {
        PRIMARY, SECONDARY
    }

    @Serializable
    data class TabItem(
        @SerialName("id") val id: String,
        @SerialName("label") val label: String?,
        @SerialName("icon") val icon: IconSchema?,
        @SerialName("badgeText") val badgeText: String?
    )
}
