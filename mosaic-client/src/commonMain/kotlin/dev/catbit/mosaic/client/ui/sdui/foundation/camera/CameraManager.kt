package dev.catbit.mosaic.client.ui.sdui.foundation.camera

interface CameraManager {
    /**
     * Opens the device camera and returns the captured photo as raw PNG bytes (lossless), or
     * null if cancelled/unavailable. Callers apply their own compression/format conversion on
     * top of this — implementations must not bake in any quality/format decision of their own.
     */
    suspend fun takePicture(): ByteArray?
}
