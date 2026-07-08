package dev.catbit.mosaic.core.data.schemas.event.events.security

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionRationaleEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionsAcquiredEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionsDeniedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Requests one or more runtime permissions from the user using each platform's native mechanism.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnPermissionsAcquiredEventTrigger] + [OnSuccessEventTrigger] — all permissions granted.
 * - [OnPermissionRationaleEventTrigger] — **Android only.** User denied once and
 *   `shouldShowRequestPermissionRationale` is `true`. Use to display an explanation
 *   before requesting again.
 * - [OnPermissionsDeniedEventTrigger] + [OnFailureEventTrigger] — permanently denied
 *   (Android second denial / "don't ask again"), or denied on iOS / Web.
 *
 * ---
 *
 * ⚠️ **App-side requirements — without these entries the request fails at runtime:**
 *
 * **Android `AndroidManifest.xml`** (declare only the permissions you use):
 * ```xml
 * <uses-permission android:name="android.permission.CAMERA" />
 * <uses-permission android:name="android.permission.RECORD_AUDIO" />
 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
 * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
 * <uses-permission android:name="android.permission.READ_CONTACTS" />
 * <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />           <!-- API 33+ -->
 * <uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />            <!-- API 33+ -->
 * <uses-permission android:name="android.permission.READ_MEDIA_VIDEO" />             <!-- API 33+ -->
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" android:maxSdkVersion="32" />
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="28" />
 * ```
 *
 * **iOS `Info.plist`** (the app crashes at runtime if a key is missing):
 * ```
 * NSCameraUsageDescription          → CAMERA
 * NSMicrophoneUsageDescription      → MICROPHONE
 * NSPhotoLibraryUsageDescription    → GALLERY
 * NSLocationWhenInUseUsageDescription → LOCATION
 * NSContactsUsageDescription        → CONTACTS
 * ```
 * `NOTIFICATION` on iOS does not require an `Info.plist` key.
 *
 * **Note on `GALLERY`:** only request this if you need broad, persistent gallery access (e.g.
 * reading `MediaStore` directly). Mosaic's `GetImageFromGallery` event picks images through the
 * Android Photo Picker / iOS `PHPickerViewController`, which need **no** storage or media
 * permission at all — don't request `GALLERY` just to use it.
 */
@Immutable
@Triggers(
    [
        OnPermissionsAcquiredEventTrigger::class,
        OnPermissionsDeniedEventTrigger::class,
        OnPermissionRationaleEventTrigger::class,
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("RequestPermission")
data class RequestPermissionEventSchema(
    override val id: String,
    override val trigger: EventTrigger,
    override val events: SerializableImmutableList<EventSchema>?,
    val permissions: SerializableImmutableList<Permissions>
) : EventSchema {

    enum class Permissions {
        CAMERA,
        GALLERY,
        STORAGE,
        MICROPHONE,
        LOCATION,
        NOTIFICATION,
        CONTACTS
    }
}
