package dev.catbit.mosaic.client.extensions

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.source
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.io.buffered

actual fun PlatformFile.asChunkedFlow(chunkSize: Int): Flow<ByteArray> = flow {
    source().buffered().use { src ->
        val buffer = ByteArray(chunkSize)
        while (!src.exhausted()) {
            val read = src.readAtMostTo(buffer)
            if (read > 0) emit(buffer.copyOfRange(0, read))
        }
    }
}
