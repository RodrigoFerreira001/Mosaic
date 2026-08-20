package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.tabs

import androidx.compose.foundation.layout.visible
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.extensions.iconOrNull
import dev.catbit.mosaic.client.extensions.textOrNull
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.TabsTileSchema

object TabsTileRenderer : TileRenderer<TabsTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: TabsTileSchema,
    ) {
        with(tileSchema) {

            if (tabItems.isEmpty()) return@with

            val modifier = Modifier
                .visible(isVisible())
                .styledWith(style)

            val tabs: @Composable () -> Unit = {
                tabItems.forEach { tab ->
                    val icon = tab.icon.iconOrNull()

                    Tab(
                        selected = tab.id == selectedTabId,
                        onClick = {
                            triggerEvent(EventTriggers.onTabItemClick(tab.id))
                            dispatchEvent(TabsTileEvents.OnTabClicked(tab.id))
                        },
                        text = tab.label.textOrNull().badgedWith(
                            badgeText = tab.badgeText.takeIf { icon == null }
                        ),
                        icon = icon.badgedWith(badgeText = tab.badgeText)
                    )
                }
            }

            val selectedTabIndex = tabItems
                .indexOfFirst { it.id == selectedTabId }
                .coerceAtLeast(0)

            when (tabType) {
                TabsTileSchema.Type.PRIMARY ->
                    if (scrollable)
                        PrimaryScrollableTabRow(
                            modifier = modifier,
                            selectedTabIndex = selectedTabIndex,
                            tabs = tabs
                        )
                    else
                        PrimaryTabRow(
                            modifier = modifier,
                            selectedTabIndex = selectedTabIndex,
                            tabs = tabs
                        )

                TabsTileSchema.Type.SECONDARY ->
                    if (scrollable)
                        SecondaryScrollableTabRow(
                            modifier = modifier,
                            selectedTabIndex = selectedTabIndex,
                            tabs = tabs
                        )
                    else
                        SecondaryTabRow(
                            modifier = modifier,
                            selectedTabIndex = selectedTabIndex,
                            tabs = tabs
                        )
            }
        }
    }

    private fun (@Composable () -> Unit)?.badgedWith(
        badgeText: String?
    ): (@Composable () -> Unit)? {
        val content = this ?: return null
        if (badgeText == null) return content

        return {
            BadgedBox(
                badge = {
                    if (badgeText.isEmpty()) Badge()
                    else Badge { Text(badgeText) }
                },
                content = { content() }
            )
        }
    }
}
