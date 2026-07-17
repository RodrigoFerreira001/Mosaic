package dev.catbit.mosaic.sample.client.sdui.tiles.navigation.adaptive_navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.visible
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
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
import dev.catbit.mosaic.client.extensions.toComposeColor
import dev.catbit.mosaic.client.extensions.toContentTransform
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
import dev.catbit.mosaic.sample.core.schemas.tiles.navigation.AdaptiveNavigationTileSchema
import dev.catbit.mosaic.sample.core.schemas.triggers.OnAdaptiveNavigationItemClickEventTrigger
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

            remember(entries, hiddenEntries, navigatorId) {
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
                        failureEvents = entry.failureEvents,
                        transition = entry.transition,
                        popTransition = entry.popTransition,
                        predictivePopTransition = entry.predictivePopTransition,
                    )
                }

                hiddenEntries?.forEach { entry ->
                    screenExtrasHolder.registerExtra(
                        screenId = entry.screenId,
                        initialTiles = entry.initialTiles,
                        initialEvents = entry.initialEvents,
                        failureTiles = entry.failureTiles,
                        failureEvents = entry.failureEvents,
                        transition = entry.transition,
                        popTransition = entry.popTransition,
                        predictivePopTransition = entry.predictivePopTransition,
                    )
                }
            }

            DisposableEffect(navigatorId) {
                onDispose {
                    navigatorsHolder.unregisterNavigator(navigatorId)
                    entries.forEach { screenExtrasHolder.removeExtra(it.id) }
                    hiddenEntries?.forEach { screenExtrasHolder.removeExtra(it.screenId) }
                }
            }

            val isCompact by rememberUpdatedState(
                !currentWindowAdaptiveInfoV2()
                    .windowSizeClass
                    .isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)
            )

            val outerModifier = Modifier
                .fillMaxSize()
                .visible(isVisible())
                .styledWith(style)

            when {
                isCompact && entries.size > NAVIGATION_BAR_MAX_ITEMS -> {

                    val drawerState = rememberDrawerState(DrawerValue.Closed)
                    val coroutineScope = rememberCoroutineScope()

                    ModalNavigationDrawer(
                        modifier = Modifier
                            .fillMaxSize(),
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet {
                                Spacer(Modifier.size(24.dp))
                                entries.forEach { entry ->
                                    NavigationDrawerItem(
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp)
                                            .height(56.dp),
                                        selected = selectedEntryId == entry.id,
                                        onClick = {
                                            coroutineScope.launch { drawerState.close() }
                                            triggerEvent(
                                                OnAdaptiveNavigationItemClickEventTrigger(
                                                    entry.id
                                                )
                                            )
                                            dispatchEvent(
                                                AdaptiveNavigationTileEvents.OnItemClicked(
                                                    entry.id
                                                )
                                            )
                                        },
                                        icon = {
                                            MosaicIcon(
                                                schema = entry.icon,
                                                filled = selectedEntryId == entry.id
                                            )
                                        },
                                        label = {
                                            entry.label?.let { Text(it) }
                                        }
                                    )
                                }
                                Spacer(Modifier.size(24.dp))
                            }
                        }
                    ) {
                        Box(
                            modifier = outerModifier,
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                AdaptiveTopBar(
                                    modifier = Modifier.fillMaxWidth(),
                                    topBar = topBar,
                                    menuIcon = {
                                        IconButton(
                                            onClick = {
                                                coroutineScope.launch {
                                                    drawerState.open()
                                                }
                                            }
                                        ) {
                                            MaterialSymbol(
                                                iconName = "menu",
                                                size = 24.dp
                                            )
                                        }
                                    }
                                )

                                key(Unit) {
                                    NavDisplay(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth(),
                                        backStack = navigationController.backStack,
                                        onBack = { navigationController.goBack() },
                                        transitionSpec = {
                                            val targetKey = targetState.key as? ScreenNavKey
                                            val entryTransition =
                                                targetKey?.let {
                                                    screenExtrasHolder.getExtraOrNull(
                                                        it.id
                                                    )?.transition
                                                }
                                            val resolved =
                                                entryTransition ?: tileSchema.defaultTransition
                                            resolved?.toContentTransform()
                                                ?: (EnterTransition.None togetherWith ExitTransition.None)
                                        },
                                        popTransitionSpec = {
                                            val initialKey = initialState.key as? ScreenNavKey
                                            val entryTransition =
                                                initialKey?.let {
                                                    screenExtrasHolder.getExtraOrNull(
                                                        it.id
                                                    )?.popTransition
                                                }
                                            val resolved =
                                                entryTransition ?: tileSchema.defaultPopTransition
                                            resolved?.toContentTransform()
                                                ?: (EnterTransition.None togetherWith ExitTransition.None)
                                        },
                                        predictivePopTransitionSpec = {
                                            val initialKey = initialState.key as? ScreenNavKey
                                            val entryTransition =
                                                initialKey?.let {
                                                    screenExtrasHolder.getExtraOrNull(
                                                        it.id
                                                    )?.predictivePopTransition
                                                }
                                            val resolved = entryTransition
                                                ?: tileSchema.defaultPredictivePopTransition
                                            resolved?.toContentTransform()
                                                ?: (EnterTransition.None togetherWith ExitTransition.None)
                                        },
                                        entryDecorators = listOf(
                                            rememberSaveableStateHolderNavEntryDecorator(),
                                            rememberViewModelStoreNavEntryDecorator()
                                        ),
                                        entryProvider = entryProvider {
                                            entry<ScreenNavKey> {
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

                            primaryAction?.let {
                                Box(
                                    modifier = Modifier.padding(
                                        bottom = 16.dp,
                                        end = 16.dp
                                    )
                                ) {
                                    RenderChild(it)
                                }
                            }
                        }
                    }
                }

                isCompact -> {
                    Column(
                        modifier = outerModifier
                    ) {

                        AdaptiveTopBar(
                            modifier = Modifier.fillMaxWidth(),
                            topBar = topBar,
                        )

                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.BottomEnd
                        ) {

                            key(Unit) {
                                NavDisplay(
                                    modifier = Modifier.fillMaxSize(),
                                    backStack = navigationController.backStack,
                                    onBack = { navigationController.goBack() },
                                    transitionSpec = {
                                        val targetKey = targetState.key as? ScreenNavKey
                                        val entryTransition =
                                            targetKey?.let { screenExtrasHolder.getExtraOrNull(it.id)?.transition }
                                        val resolved =
                                            entryTransition ?: tileSchema.defaultTransition
                                        resolved?.toContentTransform()
                                            ?: (EnterTransition.None togetherWith ExitTransition.None)
                                    },
                                    popTransitionSpec = {
                                        val initialKey = initialState.key as? ScreenNavKey
                                        val entryTransition =
                                            initialKey?.let { screenExtrasHolder.getExtraOrNull(it.id)?.popTransition }
                                        val resolved =
                                            entryTransition ?: tileSchema.defaultPopTransition
                                        resolved?.toContentTransform()
                                            ?: (EnterTransition.None togetherWith ExitTransition.None)
                                    },
                                    predictivePopTransitionSpec = {
                                        val initialKey = initialState.key as? ScreenNavKey
                                        val entryTransition =
                                            initialKey?.let { screenExtrasHolder.getExtraOrNull(it.id)?.predictivePopTransition }
                                        val resolved =
                                            entryTransition
                                                ?: tileSchema.defaultPredictivePopTransition
                                        resolved?.toContentTransform()
                                            ?: (EnterTransition.None togetherWith ExitTransition.None)
                                    },
                                    entryDecorators = listOf(
                                        rememberSaveableStateHolderNavEntryDecorator(),
                                        rememberViewModelStoreNavEntryDecorator()
                                    ),
                                    entryProvider = entryProvider {
                                        entry<ScreenNavKey> {
                                            MosaicScreen(
                                                screenId = it.id,
                                                navigationData = it.navigationData,
                                                parent = LocalTilesManager.current
                                            )
                                        }
                                    }
                                )
                            }

                            primaryAction?.let {
                                Box(
                                    modifier = Modifier.padding(
                                        bottom = 16.dp,
                                        end = 16.dp
                                    )
                                ) {
                                    RenderChild(it)
                                }
                            }
                        }

                        NavigationBar(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            entries.forEach { entry ->
                                NavigationBarItem(
                                    selected = selectedEntryId == entry.id,
                                    onClick = {
                                        triggerEvent(
                                            OnAdaptiveNavigationItemClickEventTrigger(entry.id)
                                        )
                                        dispatchEvent(
                                            AdaptiveNavigationTileEvents.OnItemClicked(
                                                entry.id
                                            )
                                        )
                                    },
                                    icon = {
                                        MosaicIcon(
                                            schema = entry.icon,
                                            filled = selectedEntryId == entry.id
                                        )
                                    },
                                    label = entry.label.textOrNull(centered = true)
                                )
                            }
                        }
                    }
                }

                else -> {
                    Row {

                        NavigationRail(
                            modifier = Modifier
                                .padding(
                                    horizontal = 8.dp,
                                    vertical = 16.dp
                                )
                                .fillMaxHeight(),
                            header = primaryAction?.let { action ->
                                @Composable {
                                    RenderChild(action)
                                }
                            }
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.Center
                            ) {
                                entries.forEach { entry ->
                                    NavigationRailItem(
                                        selected = selectedEntryId == entry.id,
                                        onClick = {
                                            triggerEvent(
                                                OnAdaptiveNavigationItemClickEventTrigger(entry.id)
                                            )
                                            dispatchEvent(
                                                AdaptiveNavigationTileEvents.OnItemClicked(
                                                    entry.id
                                                )
                                            )
                                        },
                                        icon = {
                                            MosaicIcon(
                                                schema = entry.icon,
                                                filled = selectedEntryId == entry.id
                                            )
                                        },
                                        label = entry.label.textOrNull(centered = true)
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            NavDisplay(
                                modifier = Modifier
                                    .fillMaxSize(),
                                backStack = backStack,
                                onBack = { navigationController.goBack() },
                                transitionSpec = {
                                    val targetKey = targetState.key as? ScreenNavKey
                                    val entryTransition =
                                        targetKey?.let { screenExtrasHolder.getExtraOrNull(it.id)?.transition }
                                    val resolved = entryTransition ?: tileSchema.defaultTransition
                                    resolved?.toContentTransform()
                                        ?: (EnterTransition.None togetherWith ExitTransition.None)
                                },
                                popTransitionSpec = {
                                    val initialKey = initialState.key as? ScreenNavKey
                                    val entryTransition =
                                        initialKey?.let { screenExtrasHolder.getExtraOrNull(it.id)?.popTransition }
                                    val resolved =
                                        entryTransition ?: tileSchema.defaultPopTransition
                                    resolved?.toContentTransform()
                                        ?: (EnterTransition.None togetherWith ExitTransition.None)
                                },
                                predictivePopTransitionSpec = {
                                    val initialKey = initialState.key as? ScreenNavKey
                                    val entryTransition =
                                        initialKey?.let { screenExtrasHolder.getExtraOrNull(it.id)?.predictivePopTransition }
                                    val resolved =
                                        entryTransition ?: tileSchema.defaultPredictivePopTransition
                                    resolved?.toContentTransform()
                                        ?: (EnterTransition.None togetherWith ExitTransition.None)
                                },
                                entryDecorators = listOf(
                                    rememberSaveableStateHolderNavEntryDecorator(),
                                    rememberViewModelStoreNavEntryDecorator()
                                ),
                                entryProvider = entryProvider {
                                    entry<ScreenNavKey> {
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
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun TileRenderingScope.AdaptiveTopBar(
        modifier: Modifier = Modifier,
        topBar: AdaptiveNavigationTileSchema.TopBarSchema,
        menuIcon: (@Composable () -> Unit)? = null
    ) {
        val titleSlot: @Composable () -> Unit = {
            RenderChild(topBar.title)
        }
        val actionsSlot: @Composable RowScope.() -> Unit = {
            topBar.actions?.let { RenderChildren(it) }
        }
        val navigationIconSlot: @Composable () -> Unit = { menuIcon?.invoke() }
        val colors = TopAppBarDefaults.topAppBarColors(
            containerColor = topBar.backgroundColor?.toComposeColor()
                ?: TopAppBarDefaults.topAppBarColors().containerColor
        )

        Column(
            modifier = modifier
        ) {
            when (topBar.barStyle) {
                AdaptiveNavigationTileSchema.TopBarSchema.BarStyle.CENTER_ALIGNED ->
                    CenterAlignedTopAppBar(
                        modifier = Modifier.fillMaxWidth(),
                        title = titleSlot,
                        navigationIcon = navigationIconSlot,
                        actions = actionsSlot,
                        colors = colors,
                    )

                AdaptiveNavigationTileSchema.TopBarSchema.BarStyle.MEDIUM ->
                    MediumTopAppBar(
                        modifier = Modifier.fillMaxWidth(),
                        title = titleSlot,
                        navigationIcon = navigationIconSlot,
                        actions = actionsSlot,
                        colors = colors,
                    )

                AdaptiveNavigationTileSchema.TopBarSchema.BarStyle.LARGE ->
                    LargeTopAppBar(
                        modifier = Modifier.fillMaxWidth(),
                        title = titleSlot,
                        navigationIcon = navigationIconSlot,
                        actions = actionsSlot,
                        colors = colors,
                    )

                AdaptiveNavigationTileSchema.TopBarSchema.BarStyle.DEFAULT ->
                    TopAppBar(
                        modifier = Modifier.fillMaxWidth(),
                        title = titleSlot,
                        navigationIcon = navigationIconSlot,
                        actions = actionsSlot,
                        colors = colors,
                    )
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                topBar.subtitle?.let { RenderChildren(it) }
            }
        }
    }
}
