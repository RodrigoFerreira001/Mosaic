package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema

@Immutable
interface TileRenderer<T : TileSchema> {

    @Stable
    @Composable
    fun TileRenderingScope.Render(
        tileSchema: T
    )
}