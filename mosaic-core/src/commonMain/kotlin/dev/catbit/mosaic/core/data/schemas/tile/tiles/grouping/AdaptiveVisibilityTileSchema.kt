package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDisplayEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnHeightBreakpointNotSatisfiedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnHeightBreakpointSatisfiedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnWidthBreakpointNotSatisfiedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnWidthBreakpointSatisfiedEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Conditionally renders [tiles] based on the current window size class, read from
 * `currentWindowAdaptiveInfoV2()`. The children are composed only when **both**
 * [widthVisibility] and [heightVisibility] are satisfied; otherwise nothing is emitted at all —
 * the children are not composed, not merely hidden.
 *
 * **Breakpoint comparison:** the current window is ranked against the Material window size class
 * bounds (width: Compact < Medium < Expanded < Large < ExtraLarge; height: Compact < Medium <
 * Expanded). [WidthVisibility.VisibleFrom] / [HeightVisibility.VisibleFrom] are satisfied when
 * the current rank is **above** the given breakpoint (exclusive), so `VisibleFrom(Medium)` shows
 * from Expanded upwards. [WidthVisibility.VisibleUntil] / [HeightVisibility.VisibleUntil] are
 * satisfied when the current rank is at or below the given breakpoint (inclusive), so
 * `VisibleUntil(Medium)` covers Compact and Medium.
 *
 * **Triggers dispatched:**
 * - `OnDisplayEventTrigger` — fired once when the tile enters composition (keyed by tile id),
 *   regardless of whether the breakpoints are satisfied.
 * - `OnWidthBreakpointSatisfiedEventTrigger` / `OnWidthBreakpointNotSatisfiedEventTrigger` —
 *   fired on first composition and on every change of the width condition.
 * - `OnHeightBreakpointSatisfiedEventTrigger` / `OnHeightBreakpointNotSatisfiedEventTrigger` —
 *   fired on first composition and on every change of the height condition.
 *
 * The width and height triggers are independent: one may report "satisfied" while the other
 * reports "not satisfied", and in that case the children still stay hidden.
 *
 * **Notes:** when both conditions hold, the children are hosted in a `Box` carrying [style] and
 * [visibility], so they are stacked rather than emitted into the parent's scope.
 */
@Immutable
@Triggers(
    [
        OnDisplayEventTrigger::class,
        OnWidthBreakpointSatisfiedEventTrigger::class,
        OnWidthBreakpointNotSatisfiedEventTrigger::class,
        OnHeightBreakpointSatisfiedEventTrigger::class,
        OnHeightBreakpointNotSatisfiedEventTrigger::class,
    ]
)
@Serializable
@SerialName("AdaptiveVisibility")
data class AdaptiveVisibilityTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("width_visibility") val widthVisibility: WidthVisibility,
    @SerialName("height_visibility") val heightVisibility: HeightVisibility,
) : TileSchema {

    @Serializable
    sealed interface WidthVisibility {

        @Serializable
        @SerialName("visible_from")
        data class VisibleFrom(
            @SerialName("breakpoint") val breakpoint: WidthBreakpoint
        ) : WidthVisibility

        @Serializable
        @SerialName("visible_until")
        data class VisibleUntil(
            @SerialName("breakpoint") val breakpoint: WidthBreakpoint
        ) : WidthVisibility
    }

    @Serializable
    sealed interface HeightVisibility {

        @Serializable
        @SerialName("visible_from")
        data class VisibleFrom(
            @SerialName("breakpoint") val breakpoint: HeightBreakpoint
        ) : HeightVisibility

        @Serializable
        @SerialName("visible_until")
        data class VisibleUntil(
            @SerialName("breakpoint") val breakpoint: HeightBreakpoint
        ) : HeightVisibility
    }

    @Serializable
    sealed interface WidthBreakpoint {

        @Serializable
        @SerialName("compact")
        data object Compact : WidthBreakpoint

        @Serializable
        @SerialName("medium")
        data object Medium : WidthBreakpoint

        @Serializable
        @SerialName("expanded")
        data object Expanded : WidthBreakpoint

        @Serializable
        @SerialName("large")
        data object Large : WidthBreakpoint

        @Serializable
        @SerialName("extra_large")
        data object ExtraLarge : WidthBreakpoint
    }

    @Serializable
    sealed interface HeightBreakpoint {

        @Serializable
        @SerialName("compact")
        data object Compact : HeightBreakpoint

        @Serializable
        @SerialName("medium")
        data object Medium : HeightBreakpoint

        @Serializable
        @SerialName("expanded")
        data object Expanded : HeightBreakpoint
    }
}
