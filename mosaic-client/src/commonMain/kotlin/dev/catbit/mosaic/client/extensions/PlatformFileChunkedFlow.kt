package dev.catbit.mosaic.client.extensions

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.Flow

expect fun PlatformFile.asChunkedFlow(chunkSize: Int = 64 * 1024): Flow<ByteArray>
