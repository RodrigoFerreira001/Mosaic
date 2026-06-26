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
 * Renders its child tiles only when the current window size, observed via
 * [currentWindowAdaptiveInfoV2], satisfies the configured breakpoint constraints.
 *
 * Each axis declares one constraint mode:
 * - [WidthVisibility.VisibleFrom] / [HeightVisibility.VisibleFrom] — visible **above** the given
 *   breakpoint (exclusive). `VisibleFrom(Compact)` means "visible from Medium onwards";
 *   `VisibleFrom(Medium)` means "visible from Expanded onwards".
 * - [WidthVisibility.VisibleUntil] / [HeightVisibility.VisibleUntil] — visible at the given
 *   breakpoint and **below** (inclusive). `VisibleUntil(Medium)` includes Compact and Medium.
 *
 * The children are rendered only when **both** axes are satisfied; otherwise nothing is composed.
 * The default for each axis is `VisibleUntil(ExtraLarge)` / `VisibleUntil(Expanded)`,
 * which makes the tile visible across all breakpoints on that axis.
 *
 * **Notes:** This is a transparent (logical) container — it does not create a layout node of
 * its own, so its children participate directly in the parent layout and the inherited [style]
 * field is not applied.
 *
 * **Triggers dispatched:** `OnDisplayEventTrigger` — fired once when the tile enters
 * composition. `OnWidthBreakpointSatisfiedEventTrigger` /
 * `OnWidthBreakpointNotSatisfiedEventTrigger` and `OnHeightBreakpointSatisfiedEventTrigger` /
 * `OnHeightBreakpointNotSatisfiedEventTrigger` — fired on the first evaluation and whenever the
 * evaluation result of the corresponding axis changes.
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
