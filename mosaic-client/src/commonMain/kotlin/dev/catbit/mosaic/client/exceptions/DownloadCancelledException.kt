package dev.catbit.mosaic.client.exceptions

/**
 * Thrown when the user cancels a platform-native save dialog (e.g. iOS's `FileKit.openFileSaver()`
 * document picker) instead of a real transfer/write failure — used by
 * [dev.catbit.mosaic.core.data.schemas.event.events.networking.DownloadFileEventSchema]'s runner
 * to fire `onFailure()` with no data, matching the convention already used by
 * `OpenFilePickerEventRunner` for picker cancellation.
 */
class DownloadCancelledException : Throwable("User cancelled the save dialog")
