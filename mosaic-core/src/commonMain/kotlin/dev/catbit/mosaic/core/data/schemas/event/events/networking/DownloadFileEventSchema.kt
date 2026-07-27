package dev.catbit.mosaic.core.data.schemas.event.events.networking

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadFinishEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadProgressEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Downloads a file from [url] via [method] straight into the device's public/general storage —
 * the system Downloads location, visible in the OS file manager (Files app, Finder, Explorer,
 * the device's Downloads app) — as opposed to [DownloadFileToMemoryEventSchema] (kept only in
 * memory) or [DownloadFileToDiskEventSchema] (the app's private sandbox storage).
 *
 * **This event behaves differently per platform — this is a real OS-level constraint, not an
 * implementation gap:**
 * - **Android:** delegated entirely to the system [android.app.DownloadManager] — silent, no
 *   dialog, shows a system download notification, lands in the public Downloads folder.
 *   `DownloadManager` only supports plain `GET` requests: [method]/[body] combinations other than
 *   `GET` with no body are not supported on this platform.
 * - **iOS:** there is no app-writable public "Downloads" location on iOS — the OS sandboxes every
 *   app. This event presents `FileKit.openFileSaver()` (a `UIDocumentPickerViewController` export
 *   dialog), requiring one tap from the user to choose a destination (iCloud Drive, "On My
 *   iPhone", or another Files-app provider). If the user cancels, [OnFailureEventTrigger] fires
 *   with no data — same convention as [OpenFilePickerEventSchema] cancellation.
 * - **JVM/Desktop:** written silently, no dialog, directly into the user's `~/Downloads` folder.
 * - **wasmJs/Web:** triggers the browser's native download flow (`FileKit.download(...)`), which
 *   lands in the browser's configured Downloads location.
 *
 * **incomingData consumed:** Not consumed directly. To pass data as request body or headers,
 * use [SetIncomingDataToNetworkParamsHolderBodyEventSchema] or
 * [SetIncomingDataToNetworkParamsHolderHeadersEventSchema] before this event.
 *
 * **Request body/headers resolution:** Same mechanism as [SendNetworkRequestEventSchema]:
 * - Body: schema [body] ?? holder.body
 * - Headers: holder.headers + schema.headers (schema takes precedence on collision)
 * The holder is always consumed on execution. Not honored by the Android `DownloadManager` path
 * beyond forwarding [headers] to the request.
 *
 * **Triggers fired:**
 * - [OnStartEventTrigger] — immediately before the download is dispatched.
 * - [OnDownloadProgressEventTrigger] — incomingData becomes an `Int` 0–100 completion percentage.
 *   On Android this is polled from `DownloadManager.Query()` (coarser-grained) rather than
 *   reported per network chunk like the other platforms/events.
 * - [OnDownloadFinishEventTrigger] — fired once the file is fully saved; incomingData becomes
 *   [targetFileName] (a `String`).
 * - [OnSuccessEventTrigger] — fired after a successful save; incomingData becomes [targetFileName].
 * - [OnDownloadFailureEventTrigger] — fired if the download/save fails, or the user cancels the
 *   iOS save dialog (no data in the cancel case); incomingData becomes the `Throwable` otherwise.
 * - [OnFailureEventTrigger] — fired for general failures (e.g. pre-request setup errors);
 *   incomingData becomes the `Throwable`, or no data on user cancellation.
 *
 * **Failure scenarios:**
 * - Non-2xx HTTP status code: throws `NetworkResponseException`; fires [OnDownloadFailureEventTrigger].
 * - Any network/IO error during streaming or writing to public storage: fires
 *   [OnDownloadFailureEventTrigger].
 * - User cancels the iOS save dialog: fires [OnFailureEventTrigger] with no data.
 *
 * **Notes:**
 * - [targetFileName] is the display name the file will have in the user's Downloads location,
 *   including its extension.
 * - [mimeType] is optional; used to file the entry correctly in Android's `DownloadManager`/
 *   `MediaStore` and as a hint for the iOS save dialog. Falls back to inference from
 *   [targetFileName]'s extension when null.
 */
@Immutable
@Triggers(
    [
        OnStartEventTrigger::class,
        OnDownloadProgressEventTrigger::class,
        OnDownloadFinishEventTrigger::class,
        OnSuccessEventTrigger::class,
        OnDownloadFailureEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("DownloadFile")
data class DownloadFileEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("url") val url: String,
    @SerialName("method") val method: HttpMethod,
    @SerialName("body") val body: AnySerializable?,
    @SerialName("headers") val headers: Map<String, String>?,
    @SerialName("targetFileName") val targetFileName: String,
    @SerialName("mimeType") val mimeType: String?
) : EventSchema
