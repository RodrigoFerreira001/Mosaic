package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.client.logger.MosaicLogger
import dev.catbit.mosaic.client.ui.sdui.foundation.data_holder.ScreenDataHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.ScreenBehaviorsHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesEditor
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesEventDispatcher
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesEventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesOverlaysEditor
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesValueProducer
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.runSafely
import org.koin.core.scope.Scope

/**
 * Owns the running of one screen's event chains — the counterpart of `TilesManager` on the event
 * side, one instance per screen. Given an [EventTrigger] to look up, or an [EventSchema]/list of them
 * to run directly, it builds a fresh [EventRunningScope] wired to that screen's collaborators and
 * dispatches to the matching [dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner] via
 * [eventRunnerManager].
 *
 * `EventManager` and `TilesManager` are constructed together for a screen but depend on each other
 * (an event needs to edit the tile tree; a tile's local interaction needs to run events) — that
 * circular dependency is resolved by constructing `EventManager` with only [screenId],
 * [eventRunnerManager] and [koinScope] up front, then wiring in the `TilesManager`-provided
 * collaborators immediately after via [attachTilesEditor]/[attachTilesOverlaysEditor]/
 * [attachTilesEventHolder]/[attachTilesEventDispatcher]/[attachTilesValueProducer], plus
 * [attachScreenBehaviors]/[attachDataHolder] for the screen-level ones. Every [runEvent] call is
 * expected to happen only after all of these have been attached.
 *
 * @param screenId id of the screen this manager runs events for — forwarded as
 * [EventRunningScope.screenId] on every run.
 * @param eventRunnerManager registry resolving an [EventSchema]'s concrete class to its
 * [dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner].
 * @param koinScope the Koin scope every built [EventRunningScope] resolves dependencies from, and
 * that [runEvent] itself uses to reach a [MosaicLogger] for its own top-level error handling.
 */
class EventManager(
    private val screenId: String,
    private val eventRunnerManager: EventRunnerManager,
    private val koinScope: Scope
) {
    private lateinit var tilesEditor: TilesEditor
    private lateinit var tilesOverlaysEditor: TilesOverlaysEditor
    private lateinit var tilesEventDispatcher: TilesEventDispatcher
    private lateinit var tilesEventHolder: TilesEventHolder
    private lateinit var screenBehaviorsHolder: ScreenBehaviorsHolder
    private lateinit var screenDataHolder: ScreenDataHolder
    private lateinit var tilesValueProducer: TilesValueProducer

    /** Wires in the [TilesEditor] every subsequently-run event's [EventRunningScope] exposes as
     * `tilesEditor`. Must be called (typically right after construction, by the screen's
     * `TilesManager`) before [runEvent] is used. */
    fun attachTilesEditor(tilesEditor: TilesEditor) {
        this.tilesEditor = tilesEditor
    }

    /** Wires in the [TilesOverlaysEditor] every subsequently-run event's [EventRunningScope] exposes
     * as `tilesOverlaysEditor`. */
    fun attachTilesOverlaysEditor(tilesOverlaysEditor: TilesOverlaysEditor) {
        this.tilesOverlaysEditor = tilesOverlaysEditor
    }

    /** Wires in the [TilesEventHolder] [triggerEvents] uses to look up which registered events match
     * a given trigger. */
    fun attachTilesEventHolder(tilesEventHolder: TilesEventHolder) {
        this.tilesEventHolder = tilesEventHolder
    }

    /** Wires in the [ScreenBehaviorsHolder] every subsequently-run event's [EventRunningScope]
     * exposes as `screenBehaviorsHolder`. */
    fun attachScreenBehaviors(screenBehaviorsHolder: ScreenBehaviorsHolder) {
        this.screenBehaviorsHolder = screenBehaviorsHolder
    }

    /** Wires in the [ScreenDataHolder] every subsequently-run event's [EventRunningScope] exposes as
     * `screenDataHolder`. */
    fun attachDataHolder(screenDataHolder: ScreenDataHolder) {
        this.screenDataHolder = screenDataHolder
    }

    /** Wires in the [TilesEventDispatcher] every subsequently-run event's [EventRunningScope]
     * exposes as `tilesEventDispatcher`. */
    fun attachTilesEventDispatcher(tilesEventDispatcher: TilesEventDispatcher) {
        this.tilesEventDispatcher = tilesEventDispatcher
    }

    /** Wires in the [TilesValueProducer] every subsequently-run event's [EventRunningScope] exposes
     * as `tilesValueProducer`. */
    fun attachTilesValueProducer(tilesValueProducer: TilesValueProducer) {
        this.tilesValueProducer = tilesValueProducer
    }

    /**
     * Looks up every currently-registered [EventSchema] on this screen whose `trigger` equals
     * [trigger] (via [tilesEventHolder]) and runs each one — the entry point for a screen-level
     * trigger like `onDisplay()`, which isn't tied to a single tile's own `events` list the way
     * [dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope.triggerEvent] is.
     *
     * @param trigger the trigger to look up matching events for.
     * @param data value passed as every matching event's `incomingData`. `null` when nothing is
     * forwarded.
     */
    suspend fun triggerEvents(
        trigger: EventTrigger,
        data: Any? = null
    ) {
        tilesEventHolder
            .getEventsByTrigger(trigger)
            ?.forEach { eventSchema ->
                runEvent(
                    eventSchema = eventSchema,
                    data = data
                )
            }
    }

    /**
     * Runs every entry in [eventSchemas] in order via [runEvent] — used when a caller already has the
     * exact list of events to run and doesn't need [triggerEvents]' trigger lookup.
     *
     * @param eventSchemas the events to run, in order.
     * @param data value passed as every entry's `incomingData`. `null` when nothing is forwarded.
     */
    suspend fun runEvents(
        eventSchemas: List<EventSchema>,
        data: Any? = null
    ) {
        eventSchemas.forEach { eventSchema ->
            runEvent(
                eventSchema = eventSchema,
                data = data
            )
        }
    }

    /**
     * Builds a fresh [EventRunningScope] for [eventSchema] — wired to this manager's own [screenId],
     * [koinScope], and every collaborator attached via the `attach*` methods above, plus this
     * `EventManager` itself (so the scope's own `onTrigger`/`runEventInline`/`runEventsInline` can
     * recurse back through [runEvent]) — and dispatches to the
     * [dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner] registered for [eventSchema]'s
     * concrete class, via [eventRunnerManager].
     *
     * The whole call is wrapped in `runSafely`: an exception thrown anywhere inside the runner (one
     * that isn't already caught and turned into an `onFailure` trigger by the runner itself) is
     * logged and swallowed here rather than propagating — so one failing event doesn't crash the
     * screen or abort whatever chain called it.
     *
     * @param eventSchema the event to run.
     * @param data value passed as [eventSchema]'s own `incomingData`. `null` when nothing is
     * forwarded.
     */
    suspend fun runEvent(
        eventSchema: EventSchema,
        data: Any? = null
    ) {
        runSafely(
            onError = {
                koinScope.get<MosaicLogger>().error("EventManager.runEvent: ${it.stackTraceToString()}")
            }
        ) {
            with(eventRunnerManager) {
                EventRunningScope(
                    screenId = screenId,
                    triggerOwner = eventSchema,
                    incomingData = data,
                    eventManager = this@EventManager,
                    tilesEditor = tilesEditor,
                    tilesOverlaysEditor = tilesOverlaysEditor,
                    tilesEventDispatcher = tilesEventDispatcher,
                    screenDataHolder = screenDataHolder,
                    screenBehaviorsHolder = screenBehaviorsHolder,
                    koinScope = koinScope,
                    tilesValueProducer = tilesValueProducer
                ).runEvent(eventSchema)
            }
        }
    }
}