package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.assist_chip

import androidx.compose.foundation.layout.visible
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.extensions.iconOrNull
import dev.catbit.mosaic.client.ui.composables.icon.Icon
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.AssistChipTileSchema

object AssistChipTileRenderer : TileRenderer<AssistChipTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: AssistChipTileSchema,
    ) {
        with(tileSchema) {
            val modifier = Modifier
                .visible(isVisible())
                .styledWith(style)

            val label: @Composable () -> Unit = { Text(text) }
            val leading: @Composable (() -> Unit)? = leadingIcon?.iconOrNull()
            val trailing: @Composable (() -> Unit)? = trailingIcon?.iconOrNull()
            val onClick = { triggerEvent(EventTriggers.onClick()) }

            when (variant) {
                AssistChipTileSchema.Variant.DEFAULT ->
                    AssistChip(
                        modifier = modifier,
                        onClick = onClick,
                        label = label,
                        enabled = enabled,
                        leadingIcon = leading,
                        trailingIcon = trailing,
                    )

                AssistChipTileSchema.Variant.ELEVATED ->
                    ElevatedAssistChip(
                        modifier = modifier,
                        onClick = onClick,
                        label = label,
                        enabled = enabled,
                        leadingIcon = leading,
                        trailingIcon = trailing,
                    )
            }
        }
    }
}
