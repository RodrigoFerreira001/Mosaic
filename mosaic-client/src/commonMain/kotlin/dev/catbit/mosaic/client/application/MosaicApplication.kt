package dev.catbit.mosaic.client.application

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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
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
import dev.catbit.mosaic.client.generated.resources.Res
import dev.catbit.mosaic.client.generated.resources.mosaic_failure_details
import dev.catbit.mosaic.client.generated.resources.mosaic_failure_retry
import dev.catbit.mosaic.client.generated.resources.mosaic_failure_title
import org.jetbrains.compose.resources.stringResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import dev.catbit.mosaic.client.di.MosaicModules
import dev.catbit.mosaic.client.generated.resources.ic_mosaic_logo
import dev.catbit.mosaic.client.logger.DefaultMosaicLogger
import dev.catbit.mosaic.client.logger.MosaicLogger
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbol
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbolFontsConfig
import dev.catbit.mosaic.client.ui.effects.SingleEffect
import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.client.ui.sdui.foundation.graph.ScreenNavKey
import dev.catbit.mosaic.client.ui.sdui.foundation.navigation.NavigationController
import dev.catbit.mosaic.client.ui.sdui.foundation.navigation.NavigatorsHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.OverlayContainer
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.MosaicScreen
import dev.catbit.mosaic.client.ui.theme.MosaicTheme
import dev.catbit.mosaic.client.ui.theme.MosaicTypography
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class)
@Composable
fun MosaicApplication(
    applicationId: String,
    baseUrl: String,
    additionalKoinModule: Module = module { },
    logger: MosaicLogger = DefaultMosaicLogger(),
    tileDefinitions: List<TileDefinition<out TileSchema>> = emptyList(),
    eventDefinitions: List<EventDefinition<out EventSchema>> = emptyList(),
    appSplash: @Composable BoxScope.() -> Unit,
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MosaicTypography(),
    materialSymbolFontsConfig: MaterialSymbolFontsConfig = MaterialSymbolFontsConfig(),
) {
    KoinApplication(
        configuration = koinConfiguration {
            modules(
                MosaicModules(
                    applicationId = applicationId,
                    baseUrl = baseUrl,
                    additionalModule = additionalKoinModule,
                    logger = logger,
                    tileDefinitions = tileDefinitions,
                    eventDefinitions = eventDefinitions
                ).modules
            )
        },
        logLevel = Level.INFO
    ) {
        val stateHolder = koinViewModel<MosaicApplicationStateHolder>()

        stateHolder.bindScreenLifecycle()

        MosaicTheme(
            colorScheme = colorScheme,
            shapes = shapes,
            typography = typography,
            materialSymbolFontsConfig = materialSymbolFontsConfig
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

    OverlayContainer(
        modifier = Modifier.fillMaxSize(),
    ) {
        NavDisplay(
            modifier = Modifier.fillMaxSize(),
            backStack = backStack,
            onBack = { navigationController.goBack() },
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
}

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
                modifier = Modifier.size(128.dp),
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
            modifier = Modifier.fillMaxWidth(),
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
