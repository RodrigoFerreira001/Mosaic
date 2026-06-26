package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.filter_chip

import androidx.compose.foundation.layout.visible
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.extensions.iconOrNull
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.FilterChipTileSchema

object FilterChipTileRenderer : TileRenderer<FilterChipTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: FilterChipTileSchema,
    ) {
        with(tileSchema) {
            val modifier = Modifier
                .visible(isVisible())
                .styledWith(style)

            val label: @Composable () -> Unit = { Text(text) }
            val leading: @Composable (() -> Unit)? = leadingIcon?.iconOrNull()
            val trailing: @Composable (() -> Unit)? = trailingIcon?.iconOrNull()
            val onClick = {
                val newSelected = !selected
                triggerEvent(if (newSelected) EventTriggers.onCheck() else EventTriggers.onUncheck())
                triggerEvent(EventTriggers.onCheckChanged())
                dispatchEvent(FilterChipTileEvents.OnCheckChanged(newSelected))
            }

            when (variant) {
                FilterChipTileSchema.Variant.DEFAULT ->
                    FilterChip(
                        modifier = modifier,
                        selected = selected,
                        onClick = onClick,
                        label = label,
                        enabled = enabled,
                        leadingIcon = leading,
                        trailingIcon = trailing,
                    )

                FilterChipTileSchema.Variant.ELEVATED ->
                    ElevatedFilterChip(
                        modifier = modifier,
                        selected = selected,
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
