package dev.catbit.mosaic.client.application

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.di.MosaicModules
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbolFontsConfig
import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.MosaicScreen
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.theme.MosaicTheme
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.tile.TileModel
import org.koin.compose.KoinApplication

@Composable
fun MosaicApplication(
    tileDefinitions: List<TileDefinition<out TileModel, out TileUIState>> = emptyList(),
    eventDefinitions: List<EventDefinition<out EventModel>> = emptyList(),
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    materialSymbolFontsConfig: MaterialSymbolFontsConfig = MaterialSymbolFontsConfig(),
) {
    KoinApplication(
        application = {
            modules(
                MosaicModules(
                    tileDefinitions = tileDefinitions,
                    eventDefinitions = eventDefinitions
                ).modules
            )
        }
    ) {
        MosaicTheme(
            colorScheme = colorScheme,
            shapes = shapes,
            typography = typography,
            materialSymbolFontsConfig = materialSymbolFontsConfig
        ) {
            // TODO Introduzir aqui mecanismo de navegação
            MosaicScreen("teste", null)
        }
    }
}