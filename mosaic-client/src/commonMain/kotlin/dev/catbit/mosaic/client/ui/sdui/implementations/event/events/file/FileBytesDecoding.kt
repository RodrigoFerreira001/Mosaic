package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file

import dev.catbit.mosaic.core.extensions.toAny
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

/** Decodes raw JSON bytes into a `Map<String, AnySerializable?>`, as used by [FileOutputType.MapObject]. */
internal fun ByteArray.decodeAsJsonMap(): Map<String, Any?> =
    Json.decodeFromString(JsonObject.serializer(), decodeToString()).mapValues { it.value.toAny() }
