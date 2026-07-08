package dev.catbit.mosaic.client.camera

import com.github.sarxos.webcam.Webcam
import com.github.sarxos.webcam.WebcamPanel
import dev.catbit.mosaic.client.ui.sdui.foundation.camera.CameraManager
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JPanel
import javax.swing.SwingUtilities
import kotlinx.coroutines.CompletableDeferred

class JvmCameraManager : CameraManager {

    override suspend fun takePicture(): ByteArray? {
        val webcam = Webcam.getDefault() ?: return null
        val deferred = CompletableDeferred<BufferedImage?>()

        SwingUtilities.invokeLater {
            webcam.open()

            val panel = WebcamPanel(webcam).apply {
                isFPSDisplayed = false
                isMirrored = true
            }

            val dialog = JDialog(null as java.awt.Frame?, "Tirar foto", true)
            val captureButton = JButton("Capturar")
            val cancelButton = JButton("Cancelar")

            captureButton.addActionListener {
                deferred.complete(webcam.image)
                dialog.dispose()
            }
            cancelButton.addActionListener {
                deferred.complete(null)
                dialog.dispose()
            }

            val controls = JPanel(FlowLayout()).apply {
                add(captureButton)
                add(cancelButton)
            }

            dialog.layout = BorderLayout()
            dialog.add(panel, BorderLayout.CENTER)
            dialog.add(controls, BorderLayout.SOUTH)
            dialog.pack()
            dialog.setLocationRelativeTo(null)
            dialog.isVisible = true
        }

        val image = deferred.await()
        webcam.close()

        return image?.let {
            ByteArrayOutputStream().use { stream ->
                ImageIO.write(it, "png", stream)
                stream.toByteArray()
            }
        }
    }
}
