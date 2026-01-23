package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastData
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.DataHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.ScreenBehaviorsHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.state.manager.TilesEditor
import dev.catbit.mosaic.client.ui.sdui.foundation.state.manager.TilesEventDispatcher
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import kotlin.reflect.KClass

data class EventRunningScope(
    val triggerOwnerId: String,
    val incomingData: Any? = null,
    private val koinScope: Scope,
    private val eventManager: EventManager,
    val tilesEditor: TilesEditor,
    val tilesEventDispatcher: TilesEventDispatcher,
    val dataHolder: DataHolder,
    val screenBehaviorsHolder: ScreenBehaviorsHolder,
) {

    suspend fun triggerEvent(
        eventTrigger: EventTrigger,
        data: Any? = null
    ) {
        eventManager.triggerEvent(
            eventOwnerId = triggerOwnerId,
            trigger = eventTrigger,
            data = data,
        )
    }

    suspend fun runEventInline(
        eventModel: EventModel,
        data: Any? = null
    ) {
        eventManager.runEvent(
            eventModel = eventModel,
            data = data
        )
    }

    fun broadcastData(
        data: BroadcastData
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

    @Suppress("UNCHECKED_CAST")
    fun Any?.asMapAny() = this as? Map<String, AnySerializable>
}