package dev.catbit.mosaic.client.ui.state.builder.producer

import dev.catbit.mosaic.client.ui.state.builder.UIStateBuilder
import dev.catbit.mosaic.core.mapping.Mapper
import dev.catbit.mosaic.core.mapping.mapListTo
import dev.catbit.mosaic.core.mapping.mapTo

class UIStateBuilderProducerScope(
    private val producers: List<UIStateBuilderProducer<*, *>>,
    val mapper: Mapper
) {
    inline fun <reified T : Any> Any.mapTo() = this.mapTo<T>(mapper)
    inline fun <reified T : Any> List<Any>.mapListTo() = mapListTo<T>(mapper)

    @Suppress("UNCHECKED_CAST")
    fun <T : UIStateBuilder<*>> produceBuilder(data: Any): T = producers
        .firstOrNull { it.canProduce(data) }
        ?.let { uIStateBuilderProducer ->
            with(uIStateBuilderProducer) {
                produce(data)
            } as T
        } ?: throw IllegalArgumentException("Can't produce UIStateBuilder for $data")
}