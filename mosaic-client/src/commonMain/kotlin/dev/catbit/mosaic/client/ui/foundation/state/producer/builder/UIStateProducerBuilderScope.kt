package dev.catbit.mosaic.client.ui.foundation.state.producer.builder

import dev.catbit.mosaic.client.ui.foundation.state.producer.UIStateProducer
import dev.catbit.mosaic.core.extensions.toJsonElement
import dev.catbit.mosaic.core.mapping.Mapper
import dev.catbit.mosaic.core.mapping.mapListTo
import dev.catbit.mosaic.core.mapping.mapTo
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class UIStateProducerBuilderScope(
    private val producers: List<UIStateProducerBuilder<*, *>>,
    val mapper: Mapper,
    val json: Json
) {
    inline fun <reified T : Any> Any.mapTo() = this.mapTo<T>(mapper)
    inline fun <reified T : Any> List<Any>.mapListTo() = mapListTo<T>(mapper)

    @OptIn(InternalSerializationApi::class)
    inline fun <reified T : Any> decode(data: Any): T =
        json.decodeFromJsonElement(T::class.serializer(), data.toJsonElement())

    @OptIn(InternalSerializationApi::class)
    inline fun <reified T : Any> decodeOrNull(data: Any?): T? = data?.let {
        runCatching { json.decodeFromJsonElement(T::class.serializer(), data.toJsonElement()) }.getOrNull()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : UIStateProducer<*>> buildProducer(data: Any): T = producers
        .firstOrNull { it.canBuild(data) }
        ?.let { uIStateBuilderProducer ->
            with(uIStateBuilderProducer) {
                build(data)
            } as T
        } ?: throw IllegalArgumentException("Can't build UIStateProducer for $data")
}