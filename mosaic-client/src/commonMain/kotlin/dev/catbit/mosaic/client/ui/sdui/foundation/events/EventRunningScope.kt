package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.client.logger.Level
import dev.catbit.mosaic.client.logger.MosaicLogger
import dev.catbit.mosaic.client.ui.sdui.foundation.screen_tiles_broadcast.ScreenTilesBroadcastData
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.DataHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.ScreenBehaviorsHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesEditor
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesEventDispatcher
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesOverlaysEditor
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesValueProducer
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlin.reflect.KClass
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import org.koin.mp.KoinPlatform

data class EventRunningScope(
    val screenId: String,
    val triggerOwner: EventSchema,
    val incomingData: Any? = null,
    private val koinScope: Scope,
    private val eventManager: EventManager,
    val tilesEditor: TilesEditor,
    val tilesEventDispatcher: TilesEventDispatcher,
    val tilesOverlaysEditor: TilesOverlaysEditor,
    val tilesValueProducer: TilesValueProducer,
    val dataHolder: DataHolder,
    val screenBehaviorsHolder: ScreenBehaviorsHolder,
) {

    suspend fun onTrigger(
        eventTrigger: EventTrigger,
        data: Any? = null
    ) {
        triggerOwner.events
            ?.filter { it.trigger == eventTrigger }
            ?.forEach { eventSchema ->
                eventManager.runEvent(
                    eventSchema = eventSchema,
                    data = data
                )
            }
    }

    suspend fun runEventInline(
        eventSchema: EventSchema,
        data: Any? = null
    ) {
        eventManager.runEvent(
            eventSchema = eventSchema,
            data = data
        )
    }

    fun broadcastData(
        data: ScreenTilesBroadcastData
    ) {
        screenBehaviorsHolder.broadcastData(data)
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
//    ): T = koinScope.get(clazz, qualifier, parameters)
    ): T = KoinPlatform.getKoin().get(clazz, qualifier, parameters)

    inline fun <reified T : Any> getAll(): List<T> = getAll(T::class)

    fun <T : Any> getAll(
        clazz: KClass<T>,
    ): List<T> = koinScope.getAll(clazz)

    inline fun <reified T : Any> getOrNull(
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition? = null,
    ): T? = getOrNull(T::class, qualifier, parameters)

    fun <T : Any> getOrNull(
        clazz: KClass<T>,
        qualifier: Qualifier? = null,
        parameters: ParametersDefinition? = null
    ): T? = koinScope.getOrNull(clazz, qualifier, parameters)

    @Suppress("UNCHECKED_CAST")
    fun Any?.asMapAny() = this as? Map<String, AnySerializable>

    @Suppress("UNCHECKED_CAST")
    fun Any?.asMapString(): Map<String, String>? =
        asMapAny()?.filterValues { it is String } as? Map<String, String>

    fun logError(
        throwable: Throwable,
        tag: String,
    ) {
        koinScope.get<MosaicLogger>().error("$tag: ${throwable.stackTraceToString()}")
    }

    fun log(
        level: Level,
        msg: String
    ) {
        koinScope.get<MosaicLogger>().log(level, msg)
    }
}