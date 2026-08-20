package dev.catbit.mosaic.sample.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.application.MosaicApplication
import dev.catbit.mosaic.client.application.MosaicColorScheme
import dev.catbit.mosaic.client.application.mosaicDependencyInjectionConfig
import dev.catbit.mosaic.client.application.mosaicThemeConfig
import dev.catbit.mosaic.sample.client.sdui.tiles.code.code_viewer.CodeViewerTileDefinition
import dev.catbit.mosaic.sample.client.sdui.tiles.navigation.adaptive_navigation.AdaptiveNavigationTileDefinition
import dev.catbit.mosaic.sample.client.sdui.triggers.OnAdaptiveNavigationItemClickEventTriggerDefinition
import dev.catbit.mosaic.sample.client.ui.theme.SampleTypography
import dev.catbit.mosaic.sample.client.ui.theme.darkColorScheme
import dev.catbit.mosaic.sample.client.ui.theme.lightColorScheme
import mosaic.sample_client.shared.generated.resources.Res
import mosaic.sample_client.shared.generated.resources.ic_mosaic_logo
import org.jetbrains.compose.resources.vectorResource

@Composable
fun App() {
    MosaicApplication(
        applicationId = "MosaicSample",
        // Public sample-server, hosted via Firebase Hosting -> Cloud Run (see /firebase.json).
        // For local development against a machine on your LAN, temporarily swap this back to
        // something like "http://192.168.x.x:9090" — the sample-server itself always listens on
        // 9090 locally (see sample-server/.../Application.kt).
        baseUrl = "https://mosaicsampleserver.web.app",
        dependencyInjectionConfig = mosaicDependencyInjectionConfig(
            tileDefinitions = listOf(
                AdaptiveNavigationTileDefinition,
                CodeViewerTileDefinition,
            ),
            eventTriggerDefinition = listOf(
                OnAdaptiveNavigationItemClickEventTriggerDefinition
            ),
            drawableResources = mapOf(
                "mosaic_logo" to Res.drawable.ic_mosaic_logo
            )
        ),
        themeConfig = mosaicThemeConfig(
            colorScheme = MosaicColorScheme(
                lightColorScheme = lightColorScheme,
                darkColorScheme = darkColorScheme
            ),
            typography = SampleTypography()
        ),
        appSplash = {
            Image(
                modifier = Modifier.height(128.dp),
                imageVector = vectorResource(Res.drawable.ic_mosaic_logo),
                contentDescription = "Mosaic logo"
            )
        }
    )
}