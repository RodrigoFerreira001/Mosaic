package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.adaptive_navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import dev.catbit.mosaic.client.extensions.textOrNull
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbol
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
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.AdaptiveNavigationTileSchema
import kotlinx.coroutines.launch
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.koinInject
import dev.catbit.mosaic.client.ui.composables.icon.Icon as MosaicIcon

private const val NAVIGATION_BAR_MAX_ITEMS = 5

object AdaptiveNavigationTileRenderer : TileRenderer<AdaptiveNavigationTileSchema> {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: AdaptiveNavigationTileSchema,
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
                    screenExtrasHolder.registerExtra(
                        screenId = entry.id,
                        initialTiles = entry.initialTiles,
                        initialEvents = entry.initialEvents,
                        failureTiles = entry.failureTiles,
                        failureEvents = entry.failureEvents
                    )
                }
            }

            DisposableEffect(navigatorId) {
                onDispose {
                    navigatorsHolder.unregisterNavigator(navigatorId)
                    entries.forEach { screenExtrasHolder.removeExtra(it.id) }
                }
            }

            val isCompact = !currentWindowAdaptiveInfo()
                .windowSizeClass
                .isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)

            val outerModifier = Modifier
                .fillMaxSize()
                .visible(isVisible())
                .styledWith(style)

            when {
                isCompact && entries.size > NAVIGATION_BAR_MAX_ITEMS -> {
                    val drawerState = rememberDrawerState(DrawerValue.Closed)
                    val coroutineScope = rememberCoroutineScope()

                    ModalNavigationDrawer(
                        modifier = outerModifier,
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet {
                                entries.forEach { entry ->
                                    NavigationDrawerItem(
                                        selected = selectedEntryId == entry.id,
                                        onClick = {
                                            coroutineScope.launch { drawerState.close() }
                                            navigationController.navigate(entry.id)
                                            triggerEvent(EventTriggers.onAdaptiveNavigationItemClick(entry.id))
                                            dispatchEvent(AdaptiveNavigationTileEvents.OnItemClicked(entry.id))
                                        },
                                        icon = {
                                            MosaicIcon(
                                                schema = entry.icon,
                                                filled = selectedEntryId == entry.id
                                            )
                                        },
                                        label = { entry.label?.let { Text(it) } }
                                    )
                                }
                            }
                        }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            TopAppBar(
                                title = {},
                                navigationIcon = {
                                    IconButton(
                                        onClick = { coroutineScope.launch { drawerState.open() } }
                                    ) {
                                        MaterialSymbol("menu")
                                    }
                                }
                            )
                            with(this@Render) {
                                NavigationDisplay(
                                    modifier = Modifier.weight(1f),
                                    backStack = backStack,
                                    navigationController = navigationController
                                )
                            }
                        }
                    }
                }

                isCompact -> {
                    Column(
                        modifier = outerModifier
                    ) {
                        with(this@Render) {
                            NavigationDisplay(
                                modifier = Modifier.weight(1f),
                                backStack = backStack,
                                navigationController = navigationController
                            )
                        }
                        NavigationBar {
                            entries.forEach { entry ->
                                NavigationBarItem(
                                    selected = selectedEntryId == entry.id,
                                    onClick = {
                                        navigationController.navigate(entry.id)
                                        triggerEvent(EventTriggers.onAdaptiveNavigationItemClick(entry.id))
                                        dispatchEvent(AdaptiveNavigationTileEvents.OnItemClicked(entry.id))
                                    },
                                    icon = { MosaicIcon(schema = entry.icon, filled = selectedEntryId == entry.id) },
                                    label = entry.label.textOrNull(centered = true)
                                )
                            }
                        }
                    }
                }

                else -> {
                    Row(modifier = outerModifier) {
                        NavigationRail(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
                            header = header?.let { header -> @Composable { RenderChild(header) } }
                        ) {
                            entries.forEach { entry ->
                                NavigationRailItem(
                                    selected = selectedEntryId == entry.id,
                                    onClick = {
                                        navigationController.navigate(entry.id)
                                        triggerEvent(EventTriggers.onAdaptiveNavigationItemClick(entry.id))
                                        dispatchEvent(AdaptiveNavigationTileEvents.OnItemClicked(entry.id))
                                    },
                                    icon = { MosaicIcon(schema = entry.icon, filled = selectedEntryId == entry.id) },
                                    label = entry.label.textOrNull(centered = true)
                                )
                            }

                            footer?.let {
                                Spacer(Modifier.weight(1f))
                                RenderChild(it)
                            }
                        }
                        with(this@Render) {
                            NavigationDisplay(
                                modifier = Modifier.weight(1f),
                                backStack = backStack,
                                navigationController = navigationController
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun TileRenderingScope.NavigationDisplay(
        modifier: Modifier,
        backStack: NavBackStack<NavKey>,
        navigationController: NavigationController
    ) {
        NavDisplay(
            modifier = modifier,
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
