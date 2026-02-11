package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastData
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.DataHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.ScreenBehaviorsHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesEditor
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesEventDispatcher
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesOverlaysEditor
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlin.reflect.KClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

data class EventRunningScope(
    val screenId: String,
    val triggerOwner: EventSchema,
    val incomingData: Any? = null,
    private val koinScope: Scope,
    private val eventManager: EventManager,
    private val stateHolderCoroutineScope: CoroutineScope?,
    private val screenCoroutineScope: CoroutineScope?,
    val tilesEditor: TilesEditor,
    val tilesEventDispatcher: TilesEventDispatcher,
    val tilesOverlaysEditor: TilesOverlaysEditor,
    val dataHolder: DataHolder,
    val screenBehaviorsHolder: ScreenBehaviorsHolder,
) {

    fun runSuspendOnScreenScope(
        block: suspend () -> Unit
    ) {
        screenCoroutineScope?.launch {
            supervisorScope { block() }
        }
    }

    fun runSuspendOnStateHolderScope(
        block: suspend () -> Unit
    ) {
        stateHolderCoroutineScope?.launch {
            supervisorScope { block() }
        }
    }

    fun onTrigger(
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

    fun runEventInline(
        eventSchema: EventSchema,
        data: Any? = null
    ) {
        eventManager.runEvent(
            eventSchema = eventSchema,
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