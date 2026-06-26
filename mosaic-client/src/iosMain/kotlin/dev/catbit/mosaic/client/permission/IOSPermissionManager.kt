package dev.catbit.mosaic.client.permission

import dev.catbit.mosaic.client.ui.sdui.foundation.permission.PermissionManager
import dev.catbit.mosaic.client.ui.sdui.foundation.permission.PermissionResult
import dev.catbit.mosaic.core.data.schemas.event.events.security.RequestPermissionEventSchema.Permissions
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeAudio
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.Contacts.CNContactStore
import platform.Contacts.CNEntityType
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.Foundation.NSError
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHPhotoLibrary
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNUserNotificationCenter
import platform.darwin.NSObject
import kotlin.coroutines.resume

class IOSPermissionManager : PermissionManager {

    override suspend fun requestPermissions(permissions: List<Permissions>): PermissionResult {
        for (permission in permissions) {
            val result = permission.request()
            if (result != PermissionResult.Granted) return result
        }
        return PermissionResult.Granted
    }

    private suspend fun Permissions.request(): PermissionResult = when (this) {
        Permissions.CAMERA -> requestAVMedia(AVMediaTypeVideo!!)
        Permissions.MICROPHONE -> requestAVMedia(AVMediaTypeAudio!!)
        Permissions.GALLERY -> requestGallery()
        Permissions.LOCATION -> requestLocation()
        Permissions.NOTIFICATION -> requestNotification()
        Permissions.CONTACTS -> requestContacts()
        Permissions.STORAGE -> PermissionResult.Granted
    }

    private suspend fun requestAVMedia(mediaType: String): PermissionResult =
        suspendCancellableCoroutine { continuation ->
            when (AVCaptureDevice.authorizationStatusForMediaType(mediaType)) {
                AVAuthorizationStatusAuthorized -> continuation.resume(PermissionResult.Granted)
                AVAuthorizationStatusDenied -> continuation.resume(PermissionResult.Denied)
                AVAuthorizationStatusNotDetermined -> {
                    AVCaptureDevice.requestAccessForMediaType(mediaType) { granted ->
                        continuation.resume(
                            if (granted) PermissionResult.Granted else PermissionResult.Denied
                        )
                    }
                }
                else -> continuation.resume(PermissionResult.Denied)
            }
        }

    private suspend fun requestGallery(): PermissionResult =
        suspendCancellableCoroutine { continuation ->
            when (PHPhotoLibrary.authorizationStatus()) {
                PHAuthorizationStatusAuthorized -> continuation.resume(PermissionResult.Granted)
                PHAuthorizationStatusDenied -> continuation.resume(PermissionResult.Denied)
                PHAuthorizationStatusNotDetermined -> {
                    PHPhotoLibrary.requestAuthorization { status ->
                        continuation.resume(
                            if (status == PHAuthorizationStatusAuthorized) PermissionResult.Granted
                            else PermissionResult.Denied
                        )
                    }
                }
                else -> continuation.resume(PermissionResult.Denied)
            }
        }

    private suspend fun requestLocation(): PermissionResult =
        suspendCancellableCoroutine { continuation ->
            val manager = CLLocationManager()
            val delegate = LocationDelegate { status ->
                continuation.resume(
                    if (status == kCLAuthorizationStatusAuthorizedWhenInUse ||
                        status == kCLAuthorizationStatusAuthorizedAlways
                    ) PermissionResult.Granted
                    else PermissionResult.Denied
                )
            }
            manager.delegate = delegate
            when (CLLocationManager.authorizationStatus()) {
                kCLAuthorizationStatusAuthorizedWhenInUse,
                kCLAuthorizationStatusAuthorizedAlways -> continuation.resume(PermissionResult.Granted)
                kCLAuthorizationStatusDenied -> continuation.resume(PermissionResult.Denied)
                kCLAuthorizationStatusNotDetermined -> manager.requestWhenInUseAuthorization()
                else -> continuation.resume(PermissionResult.Denied)
            }
        }

    private suspend fun requestNotification(): PermissionResult =
        suspendCancellableCoroutine { continuation ->
            val options = UNAuthorizationOptionAlert or UNAuthorizationOptionBadge or UNAuthorizationOptionSound
            UNUserNotificationCenter.currentNotificationCenter()
                .requestAuthorizationWithOptions(options) { granted, _: NSError? ->
                    continuation.resume(
                        if (granted) PermissionResult.Granted else PermissionResult.Denied
                    )
                }
        }

    private suspend fun requestContacts(): PermissionResult =
        suspendCancellableCoroutine { continuation ->
            CNContactStore().requestAccessForEntityType(CNEntityType.CNEntityTypeContacts) { granted, _: NSError? ->
                continuation.resume(
                    if (granted) PermissionResult.Granted else PermissionResult.Denied
                )
            }
        }

    private class LocationDelegate(
        private val onStatus: (CLAuthorizationStatus) -> Unit
    ) : NSObject(), CLLocationManagerDelegateProtocol {
        override fun locationManager(
            manager: CLLocationManager,
            didChangeAuthorizationStatus: CLAuthorizationStatus
        ) {
            if (didChangeAuthorizationStatus != kCLAuthorizationStatusNotDetermined) {
                onStatus(didChangeAuthorizationStatus)
            }
        }
    }
}
