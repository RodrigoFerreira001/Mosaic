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
    is JsonNull -> null
    is JsonArray -> map { it.toAny() }
    is JsonObject -> mapValues { it.value.toAny() }
    is JsonPrimitive -> {
        if (isString) {
            content
        } else {
            booleanOrNull
                ?: intOrNull
                ?: longOrNull
                ?: doubleOrNull
                ?: content
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun Any?.toJsonElement(): JsonElement = when (this) {
    null -> JsonNull
    is JsonElement -> this // Já é um JsonElement, não faz nada
    is String -> JsonPrimitive(this)
    is Number -> JsonPrimitive(this)
    is Boolean -> JsonPrimitive(this)
    is Map<*, *> -> {
        val jsonMap = entries.associate { (key, value) ->
            key.toString() to value.toJsonElement()
        }
        JsonObject(jsonMap)
    }
    is Iterable<*> -> {
        val jsonList = map { it.toJsonElement() }
        JsonArray(jsonList)
    }
    is Array<*> -> {
        val jsonList = map { it.toJsonElement() }
        JsonArray(jsonList)
    }
    is Pair<*, *> -> JsonObject(
        mapOf(
            "first" to first.toJsonElement(),
            "second" to second.toJsonElement()
        )
    )
    is Triple<*, *, *> -> JsonObject(
        mapOf(
            "first" to first.toJsonElement(),
            "second" to second.toJsonElement(),
            "third" to third.toJsonElement()
        )
    )
    else -> {
        this::class.serializerOrNull()?.let { serializer ->
            Json.encodeToJsonElement(
                serializer = serializer as KSerializer<Any>,
                value = this
            )
        } ?: throw SerializationException(
            "Cannot safely serialize type ${this::class.simpleName} to JsonElement dynamically in Kotlin Multiplatform. " +
                    "Provide a specific KSerializer or convert it to a primitive/map first."
        )
    }
}