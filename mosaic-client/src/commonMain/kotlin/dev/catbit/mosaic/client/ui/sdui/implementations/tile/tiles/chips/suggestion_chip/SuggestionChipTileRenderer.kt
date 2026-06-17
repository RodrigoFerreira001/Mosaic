package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.suggestion_chip

import androidx.compose.foundation.layout.visible
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.extensions.iconOrNull
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.SuggestionChipTileSchema

object SuggestionChipTileRenderer : TileRenderer<SuggestionChipTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: SuggestionChipTileSchema,
    ) {
        with(tileSchema) {
            val modifier = Modifier
                .visible(isVisible())
                .styledWith(style)

            val label: @Composable () -> Unit = { Text(text) }
            val leadingIcon: @Composable (() -> Unit)? = icon?.iconOrNull()
            val onClick = { triggerEvent(EventTriggers.onClick()) }

            when (variant) {
                SuggestionChipTileSchema.Variant.DEFAULT ->
                    SuggestionChip(
                        modifier = modifier,
                        onClick = onClick,
                        label = label,
                        enabled = enabled,
                        icon = leadingIcon,
                    )

                SuggestionChipTileSchema.Variant.ELEVATED ->
                    ElevatedSuggestionChip(
                        modifier = modifier,
                        onClick = onClick,
                        label = label,
                        enabled = enabled,
                        icon = leadingIcon,
                    )
            }
        }
    }
}
