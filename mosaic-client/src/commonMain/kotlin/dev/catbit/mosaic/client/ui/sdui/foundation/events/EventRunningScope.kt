package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.client.logger.Level
import dev.catbit.mosaic.client.logger.MosaicLogger
import dev.catbit.mosaic.client.ui.sdui.foundation.screen_tiles_broadcast.ScreenTilesBroadcastData
import dev.catbit.mosaic.client.ui.sdui.foundation.data_holder.ScreenDataHolder
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

/**
 * Receiver passed to every [dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner.runEvent]
 * call — one fresh instance per event execution, built by [EventManager.runEvent] right before
 * dispatching to the matching [EventRunner].
 *
 * It carries this event's own identity ([triggerOwner]) and the payload it received from whatever
 * fired it ([incomingData]), and exposes every collaborator a runner needs: continuing the chain by
 * firing one of this event's own outgoing triggers ([onTrigger]), running an arbitrary [EventSchema]
 * outside trigger matching ([runEventInline]/[runEventsInline]), publishing on the screen's broadcast
 * channel ([broadcastData]), editing/reading the live tile tree ([tilesEditor], [tilesEventDispatcher],
 * [tilesOverlaysEditor], [tilesValueProducer]), reading/writing screen-scoped in-memory data
 * ([screenDataHolder]), reaching further screen-level behaviors ([screenBehaviorsHolder]), and
 * resolving arbitrary dependencies from the app's Koin scope ([get]/[getOrNull]/[getAll]).
 *
 * @property screenId id of the screen this event is running on.
 * @property triggerOwner the [EventSchema] currently executing — its own `events` list is what
 * [onTrigger] filters against.
 * @property incomingData the payload this event received from whatever triggered it — the previous
 * event's outgoing data via [onTrigger], or `null` when nothing was passed (e.g. this event's own
 * `trigger` fired directly off a tile interaction with no payload).
 * @property tilesEditor surface for mutating the live tile tree by id (add/remove/replace/update
 * tiles) — backs events like `AddTiles`, `RemoveTiles`, `UpdateTiles`.
 * @property tilesEventDispatcher surface for routing local `TileEvent`/`TileGroupEvent` instances and
 * for looking up/patching [EventSchema]s already registered on the tree by id — backs `TriggerEvent`,
 * `UpdateEvents`.
 * @property tilesOverlaysEditor surface for adding/dismissing id-addressed overlays (bottom sheet,
 * modal bottom sheet, dialog) — backs `DisplayBottomSheet`, `DismissDialog`, and their siblings.
 * @property tilesValueProducer surface for reading a value a specific tile currently produces, by
 * tile id and key — backs the `Tile` data source used by `GetData`/`EvaluateData`.
 * @property screenDataHolder screen-scoped in-memory data storage (plain, segmented, navigation) —
 * backs `screenPlainData()`/`screenSegmentedData()`/`screenNavigationData()` reads and writes.
 * @property screenBehaviorsHolder further screen-level behaviors this event may need, beyond the tile
 * tree and data holders exposed directly above.
 */
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
    val screenDataHolder: ScreenDataHolder,
    val screenBehaviorsHolder: ScreenBehaviorsHolder,
) {

    /**
     * Runs whichever of [triggerOwner]'s own `events` declare a `trigger` structurally equal to
     * [eventTrigger] — the whole mechanism behind event chaining. Filtering is a plain equality check
     * (`data class` equality, no listener registry); when no child matches, this call is a no-op
     * beyond the debug log it always emits first.
     *
     * Each matching child event is run in turn via [EventManager.runEvent], receiving [data] as its
     * own [incomingData] — this is how a value produced by one event (a network response, a read
     * result, a transformed payload) flows to whichever child actually consumes it.
     *
     * @param eventTrigger the outgoing trigger this event is firing right now — matched against
     * [triggerOwner]'s declared children.
     * @param data value passed downstream as each matching child's `incomingData`. `null` when this
     * outcome carries no payload.
     */
    suspend fun onTrigger(
        eventTrigger: EventTrigger,
        data: Any? = null
    ) {
        log(
            level = Level.DEBUG,
            msg = """
                @===============>
                Event ${triggerOwner::class.simpleName}#${triggerOwner.id} says:
                trigger: ${eventTrigger::class.simpleName}
                triggerStr: $eventTrigger
                incomingData: $incomingData
                <===============@
            """.trimIndent()
        )
        triggerOwner.events
            ?.filter { it.trigger == eventTrigger }
            ?.forEach { eventSchema ->
                eventManager.runEvent(
                    eventSchema = eventSchema,
                    data = data
                )
            }
    }

    /**
     * Runs [eventSchema] immediately, bypassing trigger matching entirely — [eventSchema] does not
     * need its own `trigger` to match anything declared on [triggerOwner].
     *
     * This is how `TriggerEvent` re-runs an [EventSchema] looked up elsewhere in the screen's tile
     * tree by id, and how `RunEvents`/`RunCancellableEvents` run their own `events` list as an
     * unconditional payload rather than a trigger-filtered chain.
     *
     * @param eventSchema the event to run right now.
     * @param data value passed as [eventSchema]'s own `incomingData`. `null` when nothing is forwarded.
     */
    suspend fun runEventInline(
        eventSchema: EventSchema,
        data: Any? = null
    ) {
        eventManager.runEvent(
            eventSchema = eventSchema,
            data = data
        )
    }

    /**
     * Runs every entry in [eventsSchema] in order, each via the same unconditional path as
     * [runEventInline] — no trigger filtering, every entry runs regardless of the others' outcome.
     *
     * @param eventsSchema the events to run, in order.
     * @param data value passed as every entry's own `incomingData`. `null` when nothing is forwarded.
     */
    suspend fun runEventsInline(
        eventsSchema: List<EventSchema>,
        data: Any? = null
    ) {
        eventManager.runEvents(
            eventSchemas = eventsSchema,
            data = data
        )
    }

    /**
     * Publishes [data] on this screen's own broadcast channel, via [screenBehaviorsHolder] — the
     * mechanism behind `ScrollColumnTile`/`ScrollRowTile`/`ScrollPagerTile` and the overlay
     * open/close/dismiss commands (`DisplayNavigationDrawer`, `DisplaySnackbar`, and their
     * dismiss/close counterparts), all of which are fire-and-forget commands addressed to a tile id
     * or to "whatever is listening on this screen" rather than a direct tile-tree mutation.
     *
     * @param data the broadcast payload to publish.
     */
    fun broadcastData(
        data: ScreenTilesBroadcastData
    ) {
        screenBehaviorsHolder.broadcastData(data)
    }

    // Scope helpers

    /**
     * Resolves an instance of [T] from the app's Koin scope — the way a runner reaches any
     * framework-provided singleton (`NetworkParametersHolder`, `DataMailer`,
     * `CancellableEventsHolder`, use cases, etc.) or any dependency registered through
     * `MosaicDependencyInjectionConfig.additionalKoinModule`.
     *
     * @param qualifier optional Koin qualifier, when more than one binding exists for [T].
     * @param parameters optional Koin parameters, forwarded to a `factory`/`single` definition that
     * expects them.
     * @return the resolved instance.
     */
    inline fun <reified T : Any> get(
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition? = null,
    ): T = get(T::class, qualifier, parameters)

    /**
     * Non-reified overload of [get], for call sites that only have a runtime [KClass] to resolve
     * from (rather than a compile-time reified type).
     *
     * @param clazz the class to resolve.
     * @param qualifier optional Koin qualifier.
     * @param parameters optional Koin parameters.
     * @return the resolved instance.
     */
    fun <T : Any> get(
        clazz: KClass<T>,
        qualifier: Qualifier? = null,
        parameters: ParametersDefinition? = null
    ): T = koinScope.get(clazz, qualifier, parameters)

    /**
     * Resolves every binding registered for [T] in the app's Koin scope, rather than a single one —
     * useful when several implementations of the same interface are meant to all run (e.g. every
     * `DataProcessor`).
     *
     * @return every resolved instance of [T], in Koin's own registration order.
     */
    inline fun <reified T : Any> getAll(): List<T> = getAll(T::class)

    /**
     * Non-reified overload of [getAll].
     *
     * @param clazz the class to resolve every binding for.
     * @return every resolved instance of [clazz].
     */
    fun <T : Any> getAll(
        clazz: KClass<T>,
    ): List<T> = koinScope.getAll(clazz)

    /**
     * Same as [get], but returns `null` instead of throwing when no binding is registered for [T] —
     * useful for an optional collaborator a custom event may or may not have wired up.
     *
     * @param qualifier optional Koin qualifier.
     * @param parameters optional Koin parameters.
     * @return the resolved instance, or `null` if none is registered.
     */
    inline fun <reified T : Any> getOrNull(
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition? = null,
    ): T? = getOrNull(T::class, qualifier, parameters)

    /**
     * Non-reified overload of [getOrNull].
     *
     * @param clazz the class to resolve.
     * @param qualifier optional Koin qualifier.
     * @param parameters optional Koin parameters.
     * @return the resolved instance, or `null` if none is registered.
     */
    fun <T : Any> getOrNull(
        clazz: KClass<T>,
        qualifier: Qualifier? = null,
        parameters: ParametersDefinition? = null
    ): T? = koinScope.getOrNull(clazz, qualifier, parameters)

    /**
     * Casts this value to `Map<String, AnySerializable?>`, or returns `null` if it isn't a map —
     * the common shape [incomingData] takes when an upstream event produced a JSON object (e.g. a
     * parsed network response body, or a `GetData` result assembled from several readings).
     */
    @Suppress("UNCHECKED_CAST")
    fun Any?.asMapAny() = this as? Map<String, AnySerializable?>

    /**
     * Narrows the result of [asMapAny] to only the entries whose value is a [String], dropping every
     * other entry — used by events that stage `incomingData` as HTTP headers or query parameters
     * (`SetIncomingDataToNetworkParamsHolderHeaders`/`...QueryParameters`), where non-string values
     * can't be forwarded as-is.
     *
     * @return the map narrowed to `Map<String, String>`, or `null` if this value isn't a map at all.
     */
    @Suppress("UNCHECKED_CAST")
    fun Any?.asMapString(): Map<String, String>? =
        asMapAny()?.filterValues { it is String } as? Map<String, String>

    /**
     * Logs [throwable] at error level via the app's [MosaicLogger], prefixed with [tag] — the
     * standard way every built-in [EventRunner] records a failure before firing its own `onFailure`
     * trigger.
     *
     * @param throwable the error to log.
     * @param tag short label identifying which runner/step produced the error, shown in the log line.
     */
    fun logError(
        throwable: Throwable,
        tag: String,
    ) {
        koinScope.get<MosaicLogger>().error("$tag: ${throwable.stackTraceToString()}")
    }

    /**
     * Logs [msg] at [level] via the app's [MosaicLogger].
     *
     * @param level severity of this log entry.
     * @param msg the message to log.
     */
    fun log(
        level: Level,
        msg: String
    ) {
        koinScope.get<MosaicLogger>().log(level, msg)
    }
}