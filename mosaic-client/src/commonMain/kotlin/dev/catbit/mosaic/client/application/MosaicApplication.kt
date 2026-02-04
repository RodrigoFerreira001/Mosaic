package dev.catbit.mosaic.client.application

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ColorScheme
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
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import dev.catbit.mosaic.client.di.MosaicModules
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbol
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbolFontsConfig
import dev.catbit.mosaic.client.ui.effects.SingleEffect
import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.client.ui.sdui.foundation.graph.ScreenNavKey
import dev.catbit.mosaic.client.ui.sdui.foundation.navigation.NavigationController
import dev.catbit.mosaic.client.ui.sdui.foundation.navigation.NavigatorHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.OverlayContainer
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.MosaicScreen
import dev.catbit.mosaic.client.ui.theme.MosaicTheme
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.KoinMultiplatformApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.Module
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class)
@Composable
fun MosaicApplication(
    applicationId: String,
    baseUrl: String,
    additionalKoinModule: Module = module { },
    tileDefinitions: List<TileDefinition<out TileSchema>> = emptyList(),
    eventDefinitions: List<EventDefinition<out EventSchema>> = emptyList(),
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    materialSymbolFontsConfig: MaterialSymbolFontsConfig = MaterialSymbolFontsConfig(),
) {
    KoinMultiplatformApplication(
        config = koinConfiguration {
            modules(
                MosaicModules(
                    applicationId = applicationId,
                    baseUrl = baseUrl,
                    additionalModule = additionalKoinModule,
                    tileDefinitions = tileDefinitions,
                    eventDefinitions = eventDefinitions
                ).modules
            )
        }
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
                onEvent = { stateHolder.onEvent(it) }
            )
        }
    }
}

@Composable
private fun MosaicApplicationContent(
    uiState: State,
    onEvent: (Event) -> Unit
) {
    when (uiState) {
        is State.Displaying -> MosaicApplicationSuccessContent(
            uiState = uiState,
            onEvent = onEvent
        )

        State.Loading -> MosaicApplicationLoadingContent()
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

    SingleEffect {
        NavigatorHolder.registerNavigator(
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
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<ScreenNavKey> {
                    MosaicScreen(
                        screenId = it.id,
                        navigationData = it.navigationData
                    )
                }
            }
        )
    }
}

@Composable
private fun MosaicApplicationLoadingContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun MosaicApplicationFailureContent(
    uiState: State.Failure,
    onEvent: (Event) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MaterialSymbol("error")
        Text(uiState.title)
        Text(uiState.details)
        Button(
            onClick = { onEvent(Event.OnTryAgainClick) }
        ) {
            Text("Tentar novamente")
        }
    }
}
