package dev.catbit.mosaic.client.ui.sdui.foundation.camera

/**
 * Platform abstraction over the device camera — the collaborator behind the `TakePicture` event.
 * Bound once per platform target in that target's own `platformModule` (e.g. `AndroidCameraManager`
 * on Android), so `TakePictureEventRunner` can reach it via `get<CameraManager>()` without knowing
 * which platform it's running on. Compression/resize (`TakePicture.compression`/`resize`) are applied
 * afterward by the runner, never by an implementation of this interface.
 */
interface CameraManager {
    /**
     * Opens the device camera and returns the captured photo as raw PNG bytes (lossless), or
     * null if cancelled/unavailable. Callers apply their own compression/format conversion on
     * top of this — implementations must not bake in any quality/format decision of their own.
     */
    suspend fun takePicture(): ByteArray?
}
