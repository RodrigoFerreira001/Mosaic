@file:OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)

package dev.catbit.mosaic.core.extensions

import kotlin.reflect.KClass
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleCollector
import kotlinx.serialization.serializerOrNull

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
fun Any?.toJsonElement(
    json: Json? = null
): JsonElement = when (this) {
    null -> JsonNull
    is JsonElement -> this
    is String -> JsonPrimitive(this)
    is Number -> JsonPrimitive(this)
    is Boolean -> JsonPrimitive(this)
    is Map<*, *> -> {
        val jsonMap = entries.associate { (key, value) ->
            key.toString() to value.toJsonElement(json)
        }
        JsonObject(jsonMap)
    }

    is Iterable<*> -> {
        val jsonList = map { it.toJsonElement(json) }
        JsonArray(jsonList)
    }

    is Array<*> -> {
        val jsonList = map { it.toJsonElement(json) }
        JsonArray(jsonList)
    }

    is Pair<*, *> -> JsonObject(
        mapOf(
            "first" to first.toJsonElement(json),
            "second" to second.toJsonElement(json)
        )
    )

    is Triple<*, *, *> -> JsonObject(
        mapOf(
            "first" to first.toJsonElement(json),
            "second" to second.toJsonElement(json),
            "third" to third.toJsonElement(json)
        )
    )

    else -> {
        json?.let { json ->
            json
                .serializersModule
                .getSubclassToBaseClassMapping()[this::class]
                ?.let { superKClass ->
                    json.encodeToJsonElement(
                        serializer = PolymorphicSerializer(superKClass) as SerializationStrategy<Any>,
                        value = this
                    )
                }
        } ?: this::class.serializerOrNull()?.let { serializer ->
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

private fun SerializersModule.getSubclassToBaseClassMapping(): Map<KClass<*>, KClass<*>> {
    val baseToSuperMapping = mutableMapOf<KClass<*>, KClass<*>>()

    dumpTo(object : SerializersModuleCollector {
        override fun <T : Any> contextual(
            kClass: KClass<T>,
            provider: (List<KSerializer<*>>) -> KSerializer<*>
        ) = Unit

        override fun <Base : Any, Sub : Base> polymorphic(
            baseClass: KClass<Base>,
            actualClass: KClass<Sub>,
            actualSerializer: KSerializer<Sub>
        ) {
            baseToSuperMapping[actualClass] = baseClass
        }

        override fun <Base : Any> polymorphicDefaultSerializer(
            baseClass: KClass<Base>,
            defaultSerializerProvider: (value: Base) -> SerializationStrategy<Base>?
        ) = Unit

        override fun <Base : Any> polymorphicDefaultDeserializer(
            baseClass: KClass<Base>,
            defaultDeserializerProvider: (String?) -> DeserializationStrategy<Base>?
        ) = Unit
    })

    return baseToSuperMapping
}
