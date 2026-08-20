package dev.catbit.mosaic.client.extensions

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.Flow

/**
 * Streams this [PlatformFile]'s content as a [Flow] of [chunkSize]-byte chunks, without loading the
 * whole file into memory at once — the mechanism behind `GetFile`'s `flowOfBytes()` output type,
 * meant for large files where `arrayOfBytes()` would be wasteful. Actual reading is platform-specific
 * (`expect`/`actual`), since each platform's own file APIs differ.
 *
 * @param chunkSize size of each emitted [ByteArray], in bytes. Defaults to 64 KiB.
 */
expect fun PlatformFile.asChunkedFlow(chunkSize: Int = 64 * 1024): Flow<ByteArray>
