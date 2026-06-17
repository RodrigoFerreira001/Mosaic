package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.adaptive_visibility

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXTRA_LARGE_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_LARGE_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.AdaptiveVisibilityTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.AdaptiveVisibilityTileSchema.HeightBreakpoint
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.AdaptiveVisibilityTileSchema.HeightVisibility
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.AdaptiveVisibilityTileSchema.WidthBreakpoint
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.AdaptiveVisibilityTileSchema.WidthVisibility

object AdaptiveVisibilityTileRenderer : TileRenderer<AdaptiveVisibilityTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: AdaptiveVisibilityTileSchema
    ) {
        with(tileSchema) {
            val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
            val widthSatisfied = windowSizeClass.satisfies(widthVisibility)
            val heightSatisfied = windowSizeClass.satisfies(heightVisibility)

            LaunchedEffect(widthSatisfied) {
                triggerEvent(
                    if (widthSatisfied) EventTriggers.onWidthBreakpointSatisfied()
                    else EventTriggers.onWidthBreakpointNotSatisfied()
                )
            }

            LaunchedEffect(heightSatisfied) {
                triggerEvent(
                    if (heightSatisfied) EventTriggers.onHeightBreakpointSatisfied()
                    else EventTriggers.onHeightBreakpointNotSatisfied()
                )
            }

            if (widthSatisfied && heightSatisfied) {
                RenderChildren(tiles)
            }
        }
    }

    private fun WindowSizeClass.satisfies(widthVisibility: WidthVisibility) =
        when (widthVisibility) {
            is WidthVisibility.VisibleFrom -> currentWidthRank() > widthVisibility.breakpoint.rank()
            is WidthVisibility.VisibleUntil -> currentWidthRank() <= widthVisibility.breakpoint.rank()
        }

    private fun WindowSizeClass.satisfies(heightVisibility: HeightVisibility) =
        when (heightVisibility) {
            is HeightVisibility.VisibleFrom -> currentHeightRank() > heightVisibility.breakpoint.rank()
            is HeightVisibility.VisibleUntil -> currentHeightRank() <= heightVisibility.breakpoint.rank()
        }

    private fun WindowSizeClass.currentWidthRank() = when {
        isWidthAtLeastBreakpoint(WIDTH_DP_EXTRA_LARGE_LOWER_BOUND) -> WidthBreakpoint.ExtraLarge.rank()
        isWidthAtLeastBreakpoint(WIDTH_DP_LARGE_LOWER_BOUND) -> WidthBreakpoint.Large.rank()
        isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND) -> WidthBreakpoint.Expanded.rank()
        isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND) -> WidthBreakpoint.Medium.rank()
        else -> WidthBreakpoint.Compact.rank()
    }

    private fun WindowSizeClass.currentHeightRank() = when {
        isHeightAtLeastBreakpoint(HEIGHT_DP_EXPANDED_LOWER_BOUND) -> HeightBreakpoint.Expanded.rank()
        isHeightAtLeastBreakpoint(HEIGHT_DP_MEDIUM_LOWER_BOUND) -> HeightBreakpoint.Medium.rank()
        else -> HeightBreakpoint.Compact.rank()
    }

    private fun WidthBreakpoint.rank() = when (this) {
        WidthBreakpoint.Compact -> 0
        WidthBreakpoint.Medium -> 1
        WidthBreakpoint.Expanded -> 2
        WidthBreakpoint.Large -> 3
        WidthBreakpoint.ExtraLarge -> 4
    }

    private fun HeightBreakpoint.rank() = when (this) {
        HeightBreakpoint.Compact -> 0
        HeightBreakpoint.Medium -> 1
        HeightBreakpoint.Expanded -> 2
    }
}
