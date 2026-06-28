package dev.catbit.mosaic.sample

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.application.MosaicApplication

@Composable
fun App() {
    MosaicApplication(
        applicationId = "MosaicSample",
        baseUrl = "http://192.168.3.84:9090",
        appSplash = {
            Text("Sample App")
        }
    )
}