package dev.catbit.mosaic.server.builder.event.builders.file

import dev.catbit.mosaic.core.data.schemas.event.events.file.FileOutputType

// ── FileOutputType helpers ─────────────────────────────────────────────────────

/** Reads the whole file into memory and delivers it as a `ByteArray`. */
fun arrayOfBytes(): FileOutputType = FileOutputType.ArrayOfBytes

/** Delivers a chunked `Flow<ByteArray>`, without ever holding the full file in memory. */
fun flowOfBytes(): FileOutputType = FileOutputType.FlowOfBytes

/** Delivers a reference to the file (`PlatformFile`) without reading its contents. */
fun platformFile(): FileOutputType = FileOutputType.PlatformFile

/** Reads the file, decodes it as JSON, and delivers it as `Map<String, AnySerializable?>`. */
fun mapObject(): FileOutputType = FileOutputType.MapObject

/** Reads the whole file into memory and delivers it as a base64-encoded `String`. */
fun base64(): FileOutputType = FileOutputType.Base64
