package dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRegister
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.UIStateProducer
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.extensions.toJsonElement
import dev.catbit.mosaic.core.mapping.Mapper
import dev.catbit.mosaic.core.serialization.MosaicSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import kotlin.reflect.KClass

class UIStateProducerBuilderScope(
    private val uiStateProducerBuilders: Map<KClass<*>, UIStateProducerBuilder<*, *>>,
    private val mapper: Mapper,
    private val serializer: MosaicSerializer,
    private val koinScope: Scope,
    private val eventRegister: EventRegister
) {
    @Suppress("UNCHECKED_CAST")
    fun <T : UIStateProducer<*>> buildProducer(data: Any): T =
        uiStateProducerBuilders[data::class]?.let { uIStateBuilderProducer ->
            with(uIStateBuilderProducer) {
                build(data)
            } as T
        } ?: throw IllegalArgumentException("Can't build UIStateProducer for $data")

    fun registerEvents(
        eventsOwnerId: String,
        events: List<EventModel>
    ) {
        eventRegister.registerEvents(eventsOwnerId, events)
    }
    
    fun unregisterEvents(eventsOwnerId: String) {
        eventRegister.unregisterEvents(eventsOwnerId)
    }
    
    // Map access helpers
    
    inline fun <reified T : Any> Any.mapTo() = map(this, T::class)

    fun <T : Any> map(source: Any, target: KClass<T>): T = mapper.map(source, target)

    inline fun <reified T : Any> List<Any>.mapListTo() = mapList(this, T::class)

    fun <T : Any> mapList(source: List<Any>, target: KClass<T>): List<T> =
        source.map { mapper.map(it, target) }

    // Serialization helpers
    
    inline fun <reified T : Any> decode(data: Any): T = decode(serializer(), data)

    fun <T> decode(strategy: KSerializer<T>, data: Any): T =
        serializer.decodeFromJsonElement(strategy, data.toJsonElement())

    inline fun <reified T : Any> decodeOrNull(data: Any?): T? = decodeOrNull(serializer(), data)

    fun <T> decodeOrNull(strategy: KSerializer<T>, data: Any?): T? = data?.let {
        runCatching { serializer.decodeFromJsonElement(strategy, data.toJsonElement()) }.getOrNull()
    }
    
    // Scope helpers

    inline fun <reified T : Any> get(
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition? = null,
    ): T = get(T::class, qualifier, parameters)

    fun <T : Any> get(
        clazz: KClass<T>,
        qualifier: Qualifier? = null,
        parameters: ParametersDefinition? = null
    ): T = koinScope.get(clazz, qualifier, parameters)

    inline fun <reified T : Any> getOrNull(
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition? = null,
    ): T? = getOrNull(T::class, qualifier, parameters)

    fun <T : Any> getOrNull(
        clazz: KClass<T>,
        qualifier: Qualifier? = null,
        parameters: ParametersDefinition? = null
    ): T? = koinScope.getOrNull(clazz, qualifier, parameters)
}