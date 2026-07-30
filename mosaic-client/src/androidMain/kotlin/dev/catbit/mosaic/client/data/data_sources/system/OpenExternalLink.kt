package dev.catbit.mosaic.client.data.data_sources.system

import android.content.Intent
import androidx.core.net.toUri
import dev.catbit.mosaic.client.application.ActivityHolder

internal actual suspend fun openExternalLink(url: String) {
    val activity = ActivityHolder.getActivity()
    activity.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}
