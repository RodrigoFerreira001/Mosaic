package dev.catbit.mosaic.client.ui.foundation.state_producer_builder

import dev.catbit.mosaic.client.ui.foundation.state_producer.UIStateProducer
import dev.catbit.mosaic.core.mapping.Mapper
import dev.catbit.mosaic.core.mapping.mapListTo
import dev.catbit.mosaic.core.mapping.mapTo

class UIStateProducerBuilderScope(
    private val producers: List<UIStateProducerBuilder<*, *>>,
    val mapper: Mapper
) {
    inline fun <reified T : Any> Any.mapTo() = this.mapTo<T>(mapper)
    inline fun <reified T : Any> List<Any>.mapListTo() = mapListTo<T>(mapper)

    @Suppress("UNCHECKED_CAST")
    fun <T : UIStateProducer<*>> buildProducer(data: Any): T = producers
        .firstOrNull { it.canBuild(data) }
        ?.let { uIStateBuilderProducer ->
            with(uIStateBuilderProducer) {
                build(data)
            } as T
        } ?: throw IllegalArgumentException("Can't build UIStateProducer for $data")
}