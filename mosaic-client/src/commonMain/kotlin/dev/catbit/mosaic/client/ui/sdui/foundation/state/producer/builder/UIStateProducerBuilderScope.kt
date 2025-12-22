package dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder

import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.UIStateProducer
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.extensions.toJsonElement
import dev.catbit.mosaic.core.mapping.Mapper
import dev.catbit.mosaic.core.serialization.MosaicSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import org.koin.core.scope.Scope
import kotlin.reflect.KClass

class UIStateProducerBuilderScope(
    private val uiStateProducerBuilders: Map<KClass<*>, UIStateProducerBuilder<*, *>>,
    private val mapper: Mapper,
    private val serializer: MosaicSerializer,
    val koinScope: Scope,
    val registerEvents: (eventsOwnerId: String, events: List<EventModel>) -> Unit, // TODO utilizar uma interface
    val unregisterEvents: (eventsOwnerId: String) -> Unit // TODO utilizar uma interface, algo como EventBridge, sei lá
) {
    inline fun <reified T : Any> Any.mapTo() = map(this, T::class)

    fun <T : Any> map(source: Any, target: KClass<T>): T = mapper.map(source, target)

    inline fun <reified T : Any> List<Any>.mapListTo() = mapList(this, T::class)

    fun <T : Any> mapList(source: List<Any>, target: KClass<T>): List<T> =
        source.map { mapper.map(it, target) }

    inline fun <reified T : Any> decode(data: Any): T = decode(serializer(), data)

    fun <T> decode(strategy: KSerializer<T>, data: Any): T =
        serializer.decodeFromJsonElement(strategy, data.toJsonElement())

    inline fun <reified T : Any> decodeOrNull(data: Any?): T? = decodeOrNull(serializer(), data)

    fun <T> decodeOrNull(strategy: KSerializer<T>, data: Any?): T? = data?.let {
        runCatching { serializer.decodeFromJsonElement(strategy, data.toJsonElement()) }.getOrNull()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : UIStateProducer<*>> buildProducer(data: Any): T =
        uiStateProducerBuilders[data::class]?.let { uIStateBuilderProducer ->
            with(uIStateBuilderProducer) {
                build(data)
            } as T
        } ?: throw IllegalArgumentException("Can't build UIStateProducer for $data")
}