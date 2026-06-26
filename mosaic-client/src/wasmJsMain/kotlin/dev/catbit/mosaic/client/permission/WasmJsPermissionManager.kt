package dev.catbit.mosaic.client.permission

import dev.catbit.mosaic.client.ui.sdui.foundation.permission.PermissionManager
import dev.catbit.mosaic.client.ui.sdui.foundation.permission.PermissionResult
import dev.catbit.mosaic.core.data.schemas.event.events.security.RequestPermissionEventSchema.Permissions
import kotlin.js.Promise
import kotlinx.coroutines.await

class WasmJsPermissionManager : PermissionManager {

    override suspend fun requestPermissions(permissions: List<Permissions>): PermissionResult {
        for (permission in permissions) {
            val result = permission.request()
            if (result != PermissionResult.Granted) return result
        }
        return PermissionResult.Granted
    }

    private suspend fun Permissions.request(): PermissionResult = when (this) {
        Permissions.CAMERA -> requestMedia(video = true, audio = false)
        Permissions.MICROPHONE -> requestMedia(video = false, audio = true)
        Permissions.NOTIFICATION -> requestNotification()
        Permissions.LOCATION -> requestGeolocation()
        Permissions.GALLERY,
        Permissions.STORAGE,
        Permissions.CONTACTS -> PermissionResult.Granted
    }

    private suspend fun requestMedia(video: Boolean, audio: Boolean): PermissionResult =
        try {
            @Suppress("UNCHECKED_CAST")
            (jsRequestUserMedia(video, audio) as Promise<JsAny?>).await()
            PermissionResult.Granted
        } catch (e: Throwable) {
            PermissionResult.Denied
        }

    private suspend fun requestNotification(): PermissionResult {
        @Suppress("UNCHECKED_CAST")
        val permission = (jsRequestNotificationPermission() as Promise<JsString>).await()
        return if (permission.toString() == "granted") PermissionResult.Granted else PermissionResult.Denied
    }

    private suspend fun requestGeolocation(): PermissionResult =
        try {
            @Suppress("UNCHECKED_CAST")
            (jsRequestGeolocation() as Promise<JsAny?>).await()
            PermissionResult.Granted
        } catch (e: Throwable) {
            PermissionResult.Denied
        }
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun jsRequestUserMedia(video: Boolean, audio: Boolean): JsAny =
    js("navigator.mediaDevices.getUserMedia({ video: video, audio: audio }).then(function(s) { s.getTracks().forEach(function(t) { t.stop(); }); return null; })")

@OptIn(ExperimentalWasmJsInterop::class)
private fun jsRequestNotificationPermission(): JsAny =
    js("Notification.requestPermission()")

@OptIn(ExperimentalWasmJsInterop::class)
private fun jsRequestGeolocation(): JsAny =
    js("new Promise(function(resolve, reject) { navigator.geolocation.getCurrentPosition(resolve, reject); })")
