package dev.catbit.mosaic.sample

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.application.MosaicApplication
import dev.catbit.mosaic.sample.ui.theme.lightColorScheme

@Composable
fun App() {
    MosaicApplication(
        applicationId = "MosaicSample",
        baseUrl = "http://192.168.3.83:8080",
        colorScheme = lightColorScheme
    )
}