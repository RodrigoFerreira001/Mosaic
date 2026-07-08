package dev.catbit.mosaic.client.camera

import dev.catbit.mosaic.client.ui.sdui.foundation.camera.CameraManager
import kotlin.coroutines.resume
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSData
import platform.UIKit.UIApplication
import platform.UIKit.UIGraphicsImageRenderer
import platform.UIKit.UIImage
import platform.UIKit.UIImageOrientation
import platform.UIKit.UIImagePNGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UISceneActivationStateForegroundActive
import platform.UIKit.UIViewController
import platform.UIKit.UIWindowScene
import platform.darwin.NSObject
import platform.posix.memcpy

class IOSCameraManager : CameraManager {

    // Keeps a strong reference alive for the duration of the pick — UIImagePickerController.delegate is weak.
    private var pickerDelegate: NSObject? = null

    override suspend fun takePicture(): ByteArray? = withContext(Dispatchers.Main) {
        if (!UIImagePickerController.isSourceTypeAvailable(
                UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
            )
        ) {
            return@withContext null
        }

        val presenter = topMostViewController() ?: return@withContext null

        val image = suspendCancellableCoroutine<UIImage?> { continuation ->
            val delegate = CameraPickerDelegate { picked ->
                pickerDelegate = null
                continuation.resume(picked)
            }
            pickerDelegate = delegate

            val pickerController = UIImagePickerController().apply {
                sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
                this.delegate = delegate
            }

            presenter.presentViewController(pickerController, animated = true, completion = null)
        }

        image?.let { toPngBytes(it.rotateToUprightOrientation()) }
    }

    // UIImagePNGRepresentation has no orientation metadata (unlike JPEG's EXIF tag) to fall back
    // on, so the camera's raw sensor rotation must be baked into the pixels before encoding.
    @OptIn(ExperimentalForeignApi::class)
    private fun UIImage.rotateToUprightOrientation(): UIImage {
        if (imageOrientation == UIImageOrientation.UIImageOrientationUp) return this

        val renderer = UIGraphicsImageRenderer(size = size)
        return renderer.imageWithActions { _ ->
            drawInRect(size.useContents { CGRectMake(0.0, 0.0, width, height) })
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun toPngBytes(image: UIImage): ByteArray? {
        val data: NSData = UIImagePNGRepresentation(image) ?: return null
        return ByteArray(data.length.toInt()).apply {
            usePinned { pinned ->
                memcpy(pinned.addressOf(0), data.bytes, data.length)
            }
        }
    }

    private fun topMostViewController(): UIViewController? {
        val keyWindow = UIApplication.sharedApplication.connectedScenes
            .filterIsInstance<UIWindowScene>()
            .firstOrNull { it.activationState == UISceneActivationStateForegroundActive }
            ?.keyWindow

        var top = keyWindow?.rootViewController
        while (top?.presentedViewController != null) {
            top = top.presentedViewController
        }
        return top
    }
}

private class CameraPickerDelegate(
    private val onImagePicked: (UIImage?) -> Unit
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        picker.dismissViewControllerAnimated(true, null)
        onImagePicked(image)
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true, null)
        onImagePicked(null)
    }
}
