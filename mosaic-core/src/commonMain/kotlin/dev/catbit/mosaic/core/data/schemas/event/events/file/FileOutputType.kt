package dev.catbit.mosaic.core.data.schemas.event.events.file

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Controls the shape of the data delivered as incomingData by file-reading events. */
@Serializable
enum class FileOutputType {
    /** Reads the whole file into memory and delivers it as a [ByteArray]. */
    @SerialName("ArrayOfBytes")
    ArrayOfBytes,

    /** Delivers a chunked `Flow<ByteArray>`, without ever holding the full file in memory. */
    @SerialName("FlowOfBytes")
    FlowOfBytes,

    /** Delivers a reference to the file (`PlatformFile`) without reading its contents. */
    @SerialName("PlatformFile")
    PlatformFile,

    /** Reads the file, decodes it as JSON, and delivers it as `Map<String, AnySerializable?>`. */
    @SerialName("MapObject")
    MapObject,

    /** Reads the whole file into memory and delivers it as a base64-encoded [String]. */
    @SerialName("Base64")
    Base64,
}
