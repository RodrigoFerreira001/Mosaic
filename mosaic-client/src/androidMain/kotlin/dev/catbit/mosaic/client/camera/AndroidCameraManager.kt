package dev.catbit.mosaic.client.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import dev.catbit.mosaic.client.ui.sdui.foundation.camera.CameraManager
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.UUID
import kotlinx.coroutines.CompletableDeferred

class AndroidCameraManager(private val activityProvider: () -> ComponentActivity) : CameraManager {

    override suspend fun takePicture(): ByteArray? {
        val activity = activityProvider()
        val deferred = CompletableDeferred<Boolean>()
        val key = "mosaic_camera_${UUID.randomUUID()}"

        val outputDir = File(activity.cacheDir, "mosaic_camera").apply { mkdirs() }
        val outputFile = File(outputDir, "${UUID.randomUUID()}.jpg")
        val outputUri = FileProvider.getUriForFile(
            activity,
            "${activity.packageName}.mosaicfileprovider",
            outputFile
        )

        val launcher = activity.activityResultRegistry.register(
            key,
            ActivityResultContracts.TakePicture()
        ) { success ->
            deferred.complete(success)
        }

        launcher.launch(outputUri)

        val success = deferred.await()
        launcher.unregister()

        if (!success) {
            outputFile.delete()
            return null
        }

        return try {
            val bitmap = BitmapFactory.decodeFile(outputFile.absolutePath) ?: return null
            val rotatedBitmap = bitmap.rotateToExifOrientation(outputFile)
            ByteArrayOutputStream().use { stream ->
                rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.toByteArray()
            }
        } finally {
            outputFile.delete()
        }
    }

    private fun Bitmap.rotateToExifOrientation(file: File): Bitmap {
        val orientation = ExifInterface(file.absolutePath)
            .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        val rotationDegrees = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }

        if (rotationDegrees == 0f) return this

        val matrix = Matrix().apply { postRotate(rotationDegrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
}
