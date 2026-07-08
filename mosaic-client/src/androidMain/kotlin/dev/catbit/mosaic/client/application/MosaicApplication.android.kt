package dev.catbit.mosaic.client.application

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import dev.catbit.mosaic.client.ui.effects.SingleEffect
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.init
import org.koin.compose.koinInject
import org.koin.mp.KoinPlatform

@Composable
internal actual fun PlatformWrapper(
    content: @Composable (() -> Unit)
) {
    val activity = LocalActivity.current as ComponentActivity

    LaunchedEffect(activity) {
        FileKit.init(activity)
    }

    DisposableEffect(activity) {
        ActivityHolder.provideActivity(activity)
        onDispose {
            ActivityHolder.release()
        }
    }

    content()
}