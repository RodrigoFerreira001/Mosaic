package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilderManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilderManager
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.extensions.toJsonElement
import dev.catbit.mosaic.core.serialization.MosaicSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import kotlin.reflect.KClass

class BuilderScope(
    private val tileHolderBuilderManager: TileHolderBuilderManager,
    private val eventHolderBuilderManager: EventHolderBuilderManager,
    private val serializer: MosaicSerializer,
    private val koinScope: Scope
) {

    fun buildTileHolder(tileSchema: TileSchema) = with(tileHolderBuilderManager) { build(tileSchema) }
    fun buildEventHolder(eventSchema: EventSchema) = with(eventHolderBuilderManager) { build(eventSchema) }

    // Serializer helpers

    inline fun <reified T : Any> decode(data: Any): T = decode(serializer(), data)

    fun <T> decode(strategy: KSerializer<T>, data: Any): T =
        serializer.decodeFromJsonElement(strategy, data.toJsonElement())

    inline fun <reified T : Any> decodeOrNull(data: Any?): T? = decodeOrNull(serializer(), data)

    fun <T> decodeOrNull(strategy: KSerializer<T>, data: Any?): T? = data?.let {
        runCatching { serializer.decodeFromJsonElement(strategy, data.toJsonElement()) }.getOrNull()
    }

    // Injection helpers

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