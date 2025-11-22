@file:OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)

package dev.catbit.mosaic.core.extensions

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.float
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.long
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.serializerOrNull
import kotlin.collections.component1
import kotlin.collections.component2

fun JsonElement.toAny(): Any? = when (this) {
    is JsonArray -> map { it.toAny() }
    is JsonObject -> mapValues { (_, value) -> value.toAny() }
    is JsonPrimitive -> when {
        booleanOrNull != null -> boolean
        intOrNull != null -> int
        doubleOrNull != null -> double
        longOrNull != null -> long
        floatOrNull != null -> float
        else -> content
    }

    JsonNull -> null
}

@Suppress("UNCHECKED_CAST")
fun Any?.toJsonElement(): JsonElement {

    if (this == null) return JsonNull
    val serializer = this::class.serializerOrNull()

    return when {
        serializer != null -> {
            Json.encodeToJsonElement(
                serializer = serializer as KSerializer<Any>,
                value = this
            )
        }

        else -> {
            when (val rawObj = this) {
                is Map<*, *> -> buildJsonObject {
                    rawObj.forEach { (key, value) ->
                        put(key.toString(), value.toJsonElement())
                    }
                }

                is Array<*> -> buildJsonArray {
                    addAll(rawObj.map { element -> element.toJsonElement() })
                }

                is Collection<*> -> buildJsonArray {
                    addAll(rawObj.map { element -> element.toJsonElement() })
                }

                is Pair<*, *> -> buildJsonObject {
                    put("first", rawObj.first.toJsonElement())
                    put("second", rawObj.second.toJsonElement())
                }

                is Triple<*, *, *> -> buildJsonObject {
                    put("first", rawObj.first.toJsonElement())
                    put("second", rawObj.second.toJsonElement())
                    put("third", rawObj.third.toJsonElement())
                }

                else -> throw SerializationException("Object is not serializable: $rawObj")
            }
        }
    }
}