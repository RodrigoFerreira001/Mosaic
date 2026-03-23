package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.app_bars.top_app_bar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.visible
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars.TopAppBarTileSchema

object TopAppBarTileRenderer : TileRenderer<TopAppBarTileSchema> {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: TopAppBarTileSchema,
    ) {
        with(tileSchema) {

            val modifier = Modifier
                .visible(isVisible())
                .styledWith(style)

            val titleComposition: @Composable () -> Unit = { RenderChild(title) }

            val navigateIconComposition: @Composable () -> Unit =
                navigationIcon?.let { { RenderChild(it) } } ?: {}

            val actionsComposition: @Composable RowScope.() -> Unit =
                actions?.let { { RenderChildren(it) } } ?: {}

            when (barStyle) {
                TopAppBarTileSchema.TopAppBarStyle.DEFAULT -> TopAppBar(
                    modifier = modifier,
                    title = titleComposition,
                    navigationIcon = navigateIconComposition,
                    actions = actionsComposition
                )

                TopAppBarTileSchema.TopAppBarStyle.CENTER_ALIGNED -> CenterAlignedTopAppBar(
                    modifier = modifier,
                    title = titleComposition,
                    navigationIcon = navigateIconComposition,
                    actions = actionsComposition
                )

                TopAppBarTileSchema.TopAppBarStyle.MEDIUM -> MediumTopAppBar(
                    modifier = modifier,
                    title = titleComposition,
                    navigationIcon = navigateIconComposition,
                    actions = actionsComposition
                )

                TopAppBarTileSchema.TopAppBarStyle.LARGE -> LargeTopAppBar(
                    modifier = modifier,
                    title = titleComposition,
                    navigationIcon = navigateIconComposition,
                    actions = actionsComposition
                )
            }
        }
    }
}
