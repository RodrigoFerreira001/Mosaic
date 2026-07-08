package dev.catbit.mosaic.client.permission

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import dev.catbit.mosaic.client.ui.sdui.foundation.permission.PermissionManager
import dev.catbit.mosaic.client.ui.sdui.foundation.permission.PermissionResult
import dev.catbit.mosaic.core.data.schemas.event.events.security.RequestPermissionEventSchema.Permissions
import kotlinx.coroutines.CompletableDeferred
import java.util.UUID

class AndroidPermissionManager(private val activityProvider: () -> ComponentActivity) : PermissionManager {

    override suspend fun requestPermissions(
        permissions: List<Permissions>
    ): PermissionResult {
        val activity = activityProvider()
        val androidPermissions = permissions.flatMap { it.toAndroidPermissions() }

        if (androidPermissions.isEmpty()) return PermissionResult.Granted

        val deferred = CompletableDeferred<Map<String, Boolean>>()
        val key = "mosaic_perm_${UUID.randomUUID()}"

        val launcher = activity.activityResultRegistry.register(
            key,
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            deferred.complete(result)
        }

        launcher.launch(androidPermissions.toTypedArray())

        val result = deferred.await()
        launcher.unregister()

        val deniedPermissions = result.filter { !it.value }.keys

        return when {
            deniedPermissions.isEmpty() -> PermissionResult.Granted
            deniedPermissions.any { activity.shouldShowRequestPermissionRationale(it) } -> PermissionResult.Rationale
            else -> PermissionResult.Denied
        }
    }

    private fun Permissions.toAndroidPermissions(): List<String> = when (this) {
        Permissions.CAMERA -> listOf(android.Manifest.permission.CAMERA)
        Permissions.MICROPHONE -> listOf(android.Manifest.permission.RECORD_AUDIO)
        Permissions.LOCATION -> listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        Permissions.CONTACTS -> listOf(android.Manifest.permission.READ_CONTACTS)
        Permissions.NOTIFICATION -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            emptyList()
        }
        Permissions.GALLERY -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                android.Manifest.permission.READ_MEDIA_IMAGES,
                android.Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            listOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        Permissions.STORAGE -> if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            listOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            emptyList()
        }
    }
}
