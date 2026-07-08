@file:OptIn(ExperimentalWasmJsInterop::class)

package dev.catbit.mosaic.client.camera

import dev.catbit.mosaic.client.ui.sdui.foundation.camera.CameraManager
import kotlin.js.Promise
import kotlinx.coroutines.await
import org.khronos.webgl.Int8Array
import org.khronos.webgl.toByteArray

class WasmJsCameraManager : CameraManager {
    override suspend fun takePicture(): ByteArray? =
        runCatching { jsOpenCamera().await<Int8Array?>()?.toByteArray() }.getOrNull()
}

@JsFun(
    """() => new Promise((resolve) => {
    const overlay = document.createElement('div');
    overlay.style.cssText = 'position:fixed;inset:0;z-index:2147483647;background:#000;' +
        'display:flex;flex-direction:column;align-items:center;justify-content:center;';

    const video = document.createElement('video');
    video.autoplay = true;
    video.playsInline = true;
    video.style.cssText = 'max-width:100%;max-height:80%;';

    const controls = document.createElement('div');
    controls.style.cssText = 'margin-top:16px;display:flex;gap:12px;';

    const captureBtn = document.createElement('button');
    captureBtn.textContent = 'Capturar';
    const cancelBtn = document.createElement('button');
    cancelBtn.textContent = 'Cancelar';

    controls.appendChild(captureBtn);
    controls.appendChild(cancelBtn);
    overlay.appendChild(video);
    overlay.appendChild(controls);
    document.body.appendChild(overlay);

    let stream = null;
    let settled = false;

    function finish(value) {
        if (settled) return;
        settled = true;
        if (stream) stream.getTracks().forEach((t) => t.stop());
        overlay.remove();
        resolve(value);
    }

    if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) {
        finish(null);
    } else {
        navigator.mediaDevices.getUserMedia({ video: { facingMode: 'environment' } })
            .then((s) => {
                stream = s;
                video.srcObject = s;
            })
            .catch(() => finish(null));
    }

    cancelBtn.onclick = () => finish(null);

    captureBtn.onclick = () => {
        const canvas = document.createElement('canvas');
        canvas.width = video.videoWidth || 1280;
        canvas.height = video.videoHeight || 720;
        canvas.getContext('2d').drawImage(video, 0, 0, canvas.width, canvas.height);
        canvas.toBlob((blob) => {
            if (!blob) { finish(null); return; }
            blob.arrayBuffer().then((buf) => finish(new Int8Array(buf)));
        }, 'image/png');
    };
})"""
)
private external fun jsOpenCamera(): Promise<Int8Array?>
