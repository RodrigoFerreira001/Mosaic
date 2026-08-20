package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder

import dev.catbit.mosaic.client.logger.Level
import dev.catbit.mosaic.client.logger.MosaicLogger
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilderManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilderManager
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.extensions.toJsonElement
import dev.catbit.mosaic.core.serialization.MosaicSerializer
import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

/**
 * Scope handed as the extension receiver to every [dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder.build]
 * and [dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder.build] call —
 * the toolbox a `HolderBuilder` uses to turn a [TileSchema]/[EventSchema] into its live `Holder`
 * counterpart.
 *
 * Its central job is letting a builder recurse into its own children without needing a reference to
 * the app-wide [TileHolderBuilderManager]/[EventHolderBuilderManager] directly — [buildTileHolder]/
 * [buildEventHolder] (and their list-oriented [buildEventHolders]/[buildTileHolders] extensions) look
 * up the right builder for each child schema's own concrete class and build it, which is how a
 * container tile's children, or an event's own nested `events`, end up as fully-built holders too.
 * It also exposes [decode]/[decodeOrNull] (for a builder that needs to interpret an opaque
 * `AnySerializable` field as a concrete type) and the same Koin [get]/[getOrNull]/[logError]/[log]
 * helpers available elsewhere in the framework.
 *
 * One `BuilderScope` instance is created per screen (owned by that screen's `TilesManager`) and
 * reused for every holder built while that screen is alive.
 */
class BuilderScope(
    private val tileHolderBuilderManager: TileHolderBuilderManager,
    private val eventHolderBuilderManager: EventHolderBuilderManager,
    private val serializer: MosaicSerializer,
    private val koinScope: Scope
) {

    /**
     * Looks up the [dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder]
     * registered for [tileSchema]'s own concrete class and builds it.
     *
     * @param tileSchema the schema to build a holder for.
     * @return the newly built `TileHolder`.
     */
    fun buildTileHolder(tileSchema: TileSchema): TileHolder<*> =
        with(tileHolderBuilderManager) { build(tileSchema) }

    /**
     * Looks up the [dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder]
     * registered for [eventSchema]'s own concrete class and builds it.
     *
     * @param eventSchema the schema to build a holder for.
     * @return the newly built `EventHolder`.
     */
    fun buildEventHolder(eventSchema: EventSchema): EventHolder<*> =
        with(eventHolderBuilderManager) { build(eventSchema) }

    /**
     * Builds a holder for every entry in this list via [buildEventHolder] — the standard way a
     * `TileHolderBuilder`/`EventHolderBuilder` turns its own schema's `events` field into the
     * `events` constructor argument of the `Holder` it's building.
     *
     * @receiver the schema's own declared events, or `null` if it declares none.
     * @return a mutable list of built holders, in the same order — empty (not `null`) when the
     * receiver is `null`, so callers never need a separate null-check on the result.
     */
    fun List<EventSchema>?.buildEventHolders(): MutableList<EventHolder<*>> =
        this?.map { eventModel -> buildEventHolder(eventModel) }?.toMutableList() ?: mutableListOf()

    /**
     * Builds a holder for every entry in this list via [buildTileHolder] — the standard way a
     * container tile's `TileHolderBuilder` turns its own schema's `tiles` field into the `tiles`
     * constructor argument of the `TileHolder` it's building.
     *
     * @receiver the schema's own declared children, or `null` if it declares none.
     * @return a mutable list of built holders, in the same order — empty (not `null`) when the
     * receiver is `null`.
     */
    fun List<TileSchema>?.buildTileHolders(): MutableList<TileHolder<*>> =
        this?.map { tileModel -> buildTileHolder(tileModel) }?.toMutableList() ?: mutableListOf()

    // Serializer helpers

    /**
     * Decodes [data] as [T] via the framework's [MosaicSerializer], using [T]'s own generated
     * `KSerializer` — for a builder that needs to interpret an opaque `AnySerializable` field
     * (already a raw JSON-shaped value, not a JSON string) as a concrete type.
     *
     * @param data the raw value to decode — typically an `AnySerializable` field's own content.
     * @return the decoded [T].
     * @throws Exception if [data] doesn't match [T]'s expected shape — see [decodeOrNull] for a
     * non-throwing variant.
     */
    inline fun <reified T : Any> decode(data: Any): T = decode(serializer(), data)

    /**
     * Non-reified overload of [decode], for a caller supplying its own [KSerializer] rather than
     * relying on [T]'s generated one.
     *
     * @param strategy the serializer to decode with.
     * @param data the raw value to decode.
     * @return the decoded [T].
     */
    fun <T> decode(strategy: KSerializer<T>, data: Any): T =
        serializer.decodeFromJsonElement(strategy, data.toJsonElement())

    /**
     * Same as [decode], but returns `null` instead of throwing when [data] is `null` or doesn't
     * match [T]'s expected shape.
     *
     * @param data the raw value to decode, or `null`.
     * @return the decoded [T], or `null` if [data] is `null` or decoding failed.
     */
    inline fun <reified T : Any> decodeOrNull(data: Any?): T? = decodeOrNull(serializer(), data)

    /**
     * Non-reified overload of [decodeOrNull].
     *
     * @param strategy the serializer to decode with.
     * @param data the raw value to decode, or `null`.
     * @return the decoded [T], or `null` if [data] is `null` or decoding failed.
     */
    fun <T> decodeOrNull(strategy: KSerializer<T>, data: Any?): T? = data?.let {
        runCatching { serializer.decodeFromJsonElement(strategy, data.toJsonElement()) }.getOrNull()
    }

    // Injection helpers

    /**
     * Resolves an instance of [T] from the app's Koin scope — the way a `HolderBuilder` reaches a
     * dependency it needs at construction time.
     *
     * @param qualifier optional Koin qualifier.
     * @param parameters optional Koin parameters.
     * @return the resolved instance.
     */
    inline fun <reified T : Any> get(
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition? = null,
    ): T = get(T::class, qualifier, parameters)

    /**
     * Non-reified overload of [get].
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
     * Same as [get], but returns `null` instead of throwing when no binding is registered for [T].
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
     * Logs [throwable] at error level via the app's [MosaicLogger], prefixed with [tag].
     *
     * @param tag short label identifying which builder/step produced the error. Defaults to
     * `"MosaicCommonError"` for call sites that don't need a more specific one.
     * @param throwable the error to log.
     */
    fun logError(
        tag: String = "MosaicCommonError",
        throwable: Throwable
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