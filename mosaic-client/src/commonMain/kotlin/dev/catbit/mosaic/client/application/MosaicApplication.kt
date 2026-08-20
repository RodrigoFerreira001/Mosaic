package dev.catbit.mosaic.client.application

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import dev.catbit.mosaic.client.di.MosaicModules
import dev.catbit.mosaic.client.extensions.toContentTransform
import dev.catbit.mosaic.client.generated.resources.Res
import dev.catbit.mosaic.client.generated.resources.ic_mosaic_logo
import dev.catbit.mosaic.client.generated.resources.mosaic_failure_details
import dev.catbit.mosaic.client.generated.resources.mosaic_failure_retry
import dev.catbit.mosaic.client.generated.resources.mosaic_failure_title
import dev.catbit.mosaic.client.logger.DefaultMosaicLogger
import dev.catbit.mosaic.client.logger.MosaicLogger
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbol
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbolFontsConfig
import dev.catbit.mosaic.client.ui.effects.SingleEffect
import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventTriggerDefinition
import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.client.ui.sdui.foundation.graph.ScreenNavKey
import dev.catbit.mosaic.client.ui.sdui.foundation.navigation.NavigationController
import dev.catbit.mosaic.client.ui.sdui.foundation.navigation.NavigatorsHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.MosaicScreen
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.ScreenExtrasHolder
import dev.catbit.mosaic.client.ui.theme.MosaicColors
import dev.catbit.mosaic.client.ui.theme.MosaicTheme
import dev.catbit.mosaic.client.ui.theme.MosaicTypography
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module

/**
 * Root composable of a Mosaic client app — sets up the Koin DI graph (via `MosaicModules`),
 * installs the app's theme, fetches the initial navigation graph, and renders either the loading
 * splash, the failure screen, or the real navigation UI depending on
 * [MosaicApplicationStateHolder]'s current state. Every screen, tile and event in the app ultimately
 * renders/runs inside the Koin scope and `CompositionLocal`s this function establishes — nothing in
 * `mosaic-client` works meaningfully outside it.
 *
 * @param applicationId identifies this app instance — used, among other things, as a Koin
 * qualifier (`named("APPLICATION_ID")`).
 * @param baseUrl base URL every relative network call (`SendNetworkRequest`, `GetScreen`, etc.)
 * resolves against.
 * @param themeConfig color scheme, shapes, typography and Material Symbol font config. Defaults to
 * [mosaicThemeConfig]'s own Material defaults.
 * @param dependencyInjectionConfig custom tile/event/trigger definitions, logger, Koin module and
 * drawable resources. Defaults to [mosaicDependencyInjectionConfig]'s own empty defaults (no custom
 * tiles/events, [dev.catbit.mosaic.client.logger.DefaultMosaicLogger], no drawables).
 * @param appSplash shown while the initial navigation graph is being fetched — rendered inside a
 * `BoxScope`, so it can be freely aligned/sized.
 */
@OptIn(KoinExperimentalAPI::class)
@Composable
fun MosaicApplication(
    applicationId: String,
    baseUrl: String,
    themeConfig: MosaicThemeConfig = mosaicThemeConfig(),
    dependencyInjectionConfig: MosaicDependencyInjectionConfig = mosaicDependencyInjectionConfig(),
    appSplash: @Composable BoxScope.() -> Unit
) {
    PlatformWrapper {
        KoinApplication(
            configuration = koinConfiguration {
                modules(
                    MosaicModules(
                        applicationId = applicationId,
                        baseUrl = baseUrl,
                        additionalModule = dependencyInjectionConfig.additionalKoinModule,
                        logger = dependencyInjectionConfig.logger,
                        colorScheme = themeConfig.colorScheme,
                        tileDefinitions = dependencyInjectionConfig.tileDefinitions,
                        eventDefinitions = dependencyInjectionConfig.eventDefinitions,
                        eventTriggerDefinitions = dependencyInjectionConfig.eventTriggerDefinition,
                        additionalSerializersModule = dependencyInjectionConfig.additionalSerializersModule,
                        drawableResources = dependencyInjectionConfig.drawableResources,
                    ).modules
                )
            },
            logLevel = Level.INFO
        ) {
            val stateHolder = koinViewModel<MosaicApplicationStateHolder>()
            val colors = koinInject<MosaicColors>()

            stateHolder.bindScreenLifecycle()

            MosaicTheme(
                colors = colors,
                shapes = themeConfig.shapes,
                typography = themeConfig.typography,
                materialSymbolFontsConfig = themeConfig.materialSymbolFontsConfig
            ) {
                val uiState by stateHolder.uiState.collectAsState()

                MosaicApplicationContent(
                    uiState = uiState,
                    onEvent = { stateHolder.onEvent(it) },
                    appSplash = appSplash
                )
            }
        }
    }
}

/**
 * Every knob a consuming app can turn on `mosaic-client`'s DI graph — the whole surface for
 * registering custom tiles/events/triggers, swapping the logger, adding Koin bindings of your own
 * (including a custom [dev.catbit.mosaic.client.ui.sdui.foundation.data_processor.DataProcessor] via
 * [additionalKoinModule]), and supplying bundled drawables.
 *
 * @property additionalKoinModule extra Koin bindings the app needs — the way to register a custom
 * [dev.catbit.mosaic.client.ui.sdui.foundation.data_processor.DataProcessor] (`single { MyProcessor }
 * bind DataProcessor::class`) or any other dependency a custom `TileRenderer`/`EventRunner` wants to
 * `get<T>()`.
 * @property logger replaces the app-wide [MosaicLogger] every framework log call routes through.
 * @property tileDefinitions custom `TileDefinition`s, merged with the built-in ones.
 * @property eventDefinitions custom `EventDefinition`s, merged with the built-in ones.
 * @property eventTriggerDefinition custom `EventTriggerDefinition`s, merged with the built-in ones.
 * @property additionalSerializersModule extra polymorphic serializers unrelated to tile/event schema
 * registration (which is always automatic from the 3 definition lists above) — only needed if a
 * field type itself needs its own serializer.
 * @property drawableResources names an `Image` tile's `resourceName` can resolve to, backing
 * [dev.catbit.mosaic.client.ui.sdui.foundation.resources.DrawableResourcesHolder].
 */
@Immutable
data class MosaicDependencyInjectionConfig(
    val additionalKoinModule: Module,
    val logger: MosaicLogger,
    val tileDefinitions: List<TileDefinition<out TileSchema>>,
    val eventDefinitions: List<EventDefinition<out EventSchema>>,
    val eventTriggerDefinition: List<EventTriggerDefinition<out EventTrigger>>,
    val additionalSerializersModule: SerializersModule,
    val drawableResources: Map<String, DrawableResource>,
)

/** Builds a [MosaicDependencyInjectionConfig], every parameter defaulting to "nothing extra" — an
 * app that doesn't extend Mosaic at all can call `MosaicApplication(...)` without passing this
 * parameter. */
fun mosaicDependencyInjectionConfig(
    additionalKoinModule: Module = module { },
    logger: MosaicLogger = DefaultMosaicLogger(),
    tileDefinitions: List<TileDefinition<out TileSchema>> = emptyList(),
    eventDefinitions: List<EventDefinition<out EventSchema>> = emptyList(),
    eventTriggerDefinition: List<EventTriggerDefinition<out EventTrigger>> = emptyList(),
    additionalSerializersModule: SerializersModule = SerializersModule { },
    drawableResources: Map<String, DrawableResource> = emptyMap(),
) = MosaicDependencyInjectionConfig(
    additionalKoinModule = additionalKoinModule,
    logger = logger,
    tileDefinitions = tileDefinitions,
    eventDefinitions = eventDefinitions,
    eventTriggerDefinition = eventTriggerDefinition,
    additionalSerializersModule = additionalSerializersModule,
    drawableResources = drawableResources
)

/**
 * The app's visual theme — colors, shapes, typography and Material Symbol font settings, applied via
 * `MosaicTheme` inside `MosaicApplication`.
 *
 * @property colorScheme light/dark Material `ColorScheme` pair — the initial defaults `SetTheme`/
 * `ResetTheme` swap between/away from at runtime.
 * @property shapes Material `Shapes` for every built-in tile that reads from `MaterialTheme.shapes`.
 * @property typography Material `Typography` — what `TypographySchema.toTextStyle()` resolves
 * against.
 * @property materialSymbolFontsConfig variable-font axis settings for every Material Symbol glyph in
 * the app.
 */
@Stable
data class MosaicThemeConfig(
    val colorScheme: MosaicColorScheme,
    val shapes: Shapes,
    val typography: Typography,
    val materialSymbolFontsConfig: MaterialSymbolFontsConfig,
)

/**
 * A light/dark pair of Material `ColorScheme`s — the initial values `MosaicColors` starts from,
 * before any `SetTheme`/`ResetTheme` event swaps them at runtime.
 *
 * @property lightColorScheme the app's light theme.
 * @property darkColorScheme the app's dark theme.
 */
@Stable
data class MosaicColorScheme(
    val lightColorScheme: ColorScheme,
    val darkColorScheme: ColorScheme,
)

/** Builds a [MosaicThemeConfig], defaulting every parameter to Material 3's own out-of-the-box
 * defaults (`lightColorScheme()`/`darkColorScheme()`, `MaterialTheme.shapes`, [MosaicTypography], a
 * default [MaterialSymbolFontsConfig]) when left `null`. */
@Composable
fun mosaicThemeConfig(
    colorScheme: MosaicColorScheme? = null,
    shapes: Shapes? = null,
    typography: Typography? = null,
    materialSymbolFontsConfig: MaterialSymbolFontsConfig? = null,
) = MosaicThemeConfig(
    colorScheme = colorScheme ?: MosaicColorScheme(
        lightColorScheme = lightColorScheme(),
        darkColorScheme = darkColorScheme()
    ),
    shapes = shapes ?: MaterialTheme.shapes,
    typography = typography ?: MosaicTypography(),
    materialSymbolFontsConfig = materialSymbolFontsConfig ?: MaterialSymbolFontsConfig(),
)

/** Switches between the 3 app-level [State]s — loading splash, real navigation UI, or failure
 * screen. */
@Composable
private fun MosaicApplicationContent(
    uiState: State,
    onEvent: (Event) -> Unit,
    appSplash: @Composable BoxScope.() -> Unit
) {
    when (uiState) {
        is State.Displaying -> MosaicApplicationSuccessContent(
            uiState = uiState,
            onEvent = onEvent
        )

        is State.Loading -> MosaicApplicationLoadingContent(appSplash)
        is State.Failure -> MosaicApplicationFailureContent(
            uiState = uiState,
            onEvent = onEvent
        )
    }
}

/**
 * The real app once the initial graph loaded — builds the root [NavigationController], registers it
 * in [NavigatorsHolder] under the conventional id `"root"`, and renders `NavDisplay`, resolving each
 * screen's transition (per-entry override, falling back to the graph's default) and rendering each
 * `ScreenNavKey` entry via `MosaicScreen`.
 */
@Composable
private fun MosaicApplicationSuccessContent(
    uiState: State.Displaying,
    onEvent: (Event) -> Unit
) {
    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(ScreenNavKey::class, ScreenNavKey.serializer())
                }
            }
        },
        ScreenNavKey(uiState.graph.startEntryId)
    )

    @Suppress("UNCHECKED_CAST")
    val navigationController = remember {
        NavigationController(backStack as NavBackStack<ScreenNavKey>)
    }

    val navigatorsHolder = koinInject<NavigatorsHolder>()

    SingleEffect {
        navigatorsHolder.registerNavigator(
            navigatorId = "root",
            navigationController = navigationController
        )
    }

    val screenExtrasHolder = koinInject<ScreenExtrasHolder>()

    NavDisplay(
        modifier = Modifier.fillMaxSize(),
        backStack = backStack,
        onBack = { navigationController.goBack() },
        transitionSpec = {
            val targetKey = targetState.key as? ScreenNavKey
            val entryTransition =
                targetKey?.let { screenExtrasHolder.getExtraOrNull(it.id)?.transition }
            val resolved = entryTransition ?: uiState.graph.defaultTransition
            resolved?.toContentTransform()
                ?: (EnterTransition.None togetherWith ExitTransition.None)
        },
        popTransitionSpec = {
            val initialKey = initialState.key as? ScreenNavKey
            val entryTransition =
                initialKey?.let { screenExtrasHolder.getExtraOrNull(it.id)?.popTransition }
            val resolved = entryTransition ?: uiState.graph.defaultPopTransition
            resolved?.toContentTransform()
                ?: (EnterTransition.None togetherWith ExitTransition.None)
        },
        predictivePopTransitionSpec = {
            val initialKey = initialState.key as? ScreenNavKey
            val entryTransition =
                initialKey?.let { screenExtrasHolder.getExtraOrNull(it.id)?.predictivePopTransition }
            val resolved = entryTransition ?: uiState.graph.defaultPredictivePopTransition
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
                    parent = null
                )
            }
        }
    )
}

/** Renders [appSplash] plus the "Powered by Mosaic" footer, shown while the initial graph is
 * loading. */
@Composable
private fun MosaicApplicationLoadingContent(
    appSplash: @Composable BoxScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(bottom = 24.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                appSplash()
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Powered by",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
                Image(
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                        .height(64.dp),
                    imageVector = vectorResource(Res.drawable.ic_mosaic_logo),
                    contentDescription = "Mosaic logo"
                )
            }
        }
    }
}

/** App-level failure screen, shown when fetching the initial graph fails — a retry button that
 * dispatches `Event.OnTryAgainClick`, disabled and spinning while [State.Failure.loading] is
 * `true`. */
@Composable
private fun MosaicApplicationFailureContent(
    uiState: State.Failure,
    onEvent: (Event) -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MaterialSymbol(
            iconName = "error",
            size = 72.dp,
            tint = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.mosaic_failure_title),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.mosaic_failure_details),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth(),
            onClick = {
                onEvent(Event.OnTryAgainClick)
            },
            enabled = !uiState.loading
        ) {
            if (uiState.loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    strokeCap = StrokeCap.Round,
                    color = LocalContentColor.current
                )
            } else {
                Text(
                    text = stringResource(Res.string.mosaic_failure_retry)
                )
            }

        }
    }
}

@Composable
@Preview(name = "Loading - PIXEL_9", device = Devices.PIXEL_9)
@Preview(name = "Loading - TABLET", device = Devices.TABLET)
@Preview(name = "Loading - DESKTOP", device = Devices.DESKTOP)
private fun MosaicApplicationLoadingContentPreview() {
    MosaicTheme {
        MosaicApplicationLoadingContent(
            appSplash = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red)
                )
            }
        )
    }
}

@Composable
@Preview(name = "Failure - PIXEL_9", device = Devices.PIXEL_9)
@Preview(name = "Failure - TABLET", device = Devices.TABLET)
@Preview(name = "Failure - DESKTOP", device = Devices.DESKTOP)
private fun MosaicApplicationFailureContentPreview() {
    MosaicTheme {
        MosaicApplicationFailureContent(
            uiState = State.Failure(),
            onEvent = {}
        )
    }
}

/** Platform-specific root wrapping (`expect`/`actual`) — e.g. installing platform-required
 * ambient providers before the rest of [MosaicApplication] composes. */
@Composable
internal expect fun PlatformWrapper(
    content: @Composable () -> Unit
)