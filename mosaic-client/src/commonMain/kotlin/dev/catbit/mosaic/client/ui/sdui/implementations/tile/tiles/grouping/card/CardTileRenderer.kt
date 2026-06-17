package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.card

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.visible
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalColumnScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalLazyItemScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.CardTileSchema

object CardTileRenderer : TileRenderer<CardTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: CardTileSchema,
    ) {
        with(tileSchema) {

            val modifier = Modifier
                .visible(isVisible())
                .styledWith(style)

            val onClick = { triggerEvent(EventTriggers.onClick()) }

            val content: @Composable ColumnScope.() -> Unit = {
                CompositionLocalProvider(
                    LocalColumnScope provides this,
                    LocalLazyItemScope provides null
                ) {
                    RenderChildren(tiles)
                }
            }

            when (kind) {
                CardTileSchema.Kind.DEFAULT -> Card(
                    modifier = modifier,
                    onClick = onClick,
                    content = content
                )

                CardTileSchema.Kind.ELEVATED -> ElevatedCard(
                    modifier = modifier,
                    onClick = onClick,
                    content = content
                )

                CardTileSchema.Kind.OUTLINED -> OutlinedCard(
                    modifier = modifier,
                    onClick = onClick,
                    content = content
                )
            }
        }
    }
}
