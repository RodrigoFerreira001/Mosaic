package dev.catbit.mosaic.sample

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.application.MosaicApplication
import dev.catbit.mosaic.sample.ui.theme.lightColorScheme

@Composable
fun App() {
    MosaicApplication(
        applicationId = "MosaicSample",
        baseUrl = "http://localhost:8080",
        colorScheme = lightColorScheme
    )
}