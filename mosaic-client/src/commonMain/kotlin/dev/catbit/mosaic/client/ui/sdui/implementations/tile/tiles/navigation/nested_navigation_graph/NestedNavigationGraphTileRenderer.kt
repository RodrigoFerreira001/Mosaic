package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.nested_navigation_graph

import androidx.compose.foundation.layout.visible
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.graph.ScreenNavKey
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalTilesManager
import dev.catbit.mosaic.client.ui.sdui.foundation.navigation.NavigationController
import dev.catbit.mosaic.client.ui.sdui.foundation.navigation.NavigatorsHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.MosaicScreen
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.ScreenExtrasHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NestedNavigationGraphTileSchema
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.koinInject

object NestedNavigationGraphTileRenderer : TileRenderer<NestedNavigationGraphTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: NestedNavigationGraphTileSchema,
    ) {
        with(tileSchema) {
            val backStack = rememberNavBackStack(
                configuration = SavedStateConfiguration {
                    serializersModule = SerializersModule {
                        polymorphic(NavKey::class) {
                            subclass(ScreenNavKey::class, ScreenNavKey.serializer())
                        }
                    }
                },
                ScreenNavKey(startEntryId)
            )

            @Suppress("UNCHECKED_CAST")
            val navigationController = remember {
                NavigationController(backStack as NavBackStack<ScreenNavKey>)
            }

            val navigatorsHolder = koinInject<NavigatorsHolder>()
            val screenExtrasHolder = koinInject<ScreenExtrasHolder>()

            remember(entries, navigatorId) {

                navigatorsHolder.registerNavigator(
                    navigatorId = navigatorId,
                    navigationController = navigationController
                )

                entries.forEach { entry ->
                    with(entry) {
                        screenExtrasHolder.registerExtra(
                            screenId = screenId,
                            initialTiles = initialTiles,
                            initialEvents = initialEvents,
                            failureTiles = failureTiles,
                            failureEvents = failureEvents
                        )
                    }
                }
            }

            DisposableEffect(this) {
                onDispose {
                    navigatorsHolder.unregisterNavigator(navigatorId)
                    entries.forEach { entry ->
                        screenExtrasHolder.removeExtra(entry.screenId)
                    }
                }
            }

            NavDisplay(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style),
                backStack = backStack,
                onBack = { navigationController.goBack() },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                ),
                entryProvider = entryProvider {
                    entry<ScreenNavKey> {
                        LaunchedEffect(it.id) {
                            triggerEvent(EventTriggers.onNavigationEntrySet(it.id))
                        }
                        MosaicScreen(
                            screenId = it.id,
                            navigationData = it.navigationData,
                            parent = LocalTilesManager.current
                        )
                    }
                }
            )
        }
    }
}
