package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.client.exceptions.OverlayAlreadyAddedException
import dev.catbit.mosaic.client.exceptions.OverlayNotExistsException
import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileGroupEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.immutableMapTo
import dev.catbit.mosaic.core.extensions.immutableMapValuesTo

class ScreenTileHolder(
    override val id: String,
    override var tile: ScreenTileSchema,
    override val events: MutableList<EventHolder<*>>,
    override val tiles: MutableList<TileHolder<*>>,
    private val navigationDrawerTiles: List<TileHolder<*>>?
) : TileHolder<ScreenTileSchema>() {

    private val stackableOverlays: LinkedHashMap<String, StackableOverlay> = linkedMapOf()

    private val searchableOverlayTiles: List<TileHolder<*>>
        get() = stackableOverlays.values
            .reversed()
            .filterNot { it.isDismissing }
            .flatMap { it.tiles }

    override fun getTileHolder(
        tileId: String,
        includeEventsOnSearch: Boolean
    ): TileHolder<*>? =
        if (tileId == id) this
        else searchableOverlayTiles
            .firstNotNullOfOrNull { it.getTileHolder(tileId, includeEventsOnSearch) }
            ?: navigationDrawerTiles?.firstNotNullOfOrNull {
                it.getTileHolder(
                    tileId,
                    includeEventsOnSearch
                )
            }
            ?: tiles.firstNotNullOfOrNull { it.getTileHolder(tileId, includeEventsOnSearch) }
            ?: if (includeEventsOnSearch) events.firstNotNullOfOrNull { it.getTileHolder(tileId) } else null

    override fun getEventHolder(eventId: String): EventHolder<*>? =
        searchableOverlayTiles
            .firstNotNullOfOrNull { it.getEventHolder(eventId) }
            ?: navigationDrawerTiles?.firstNotNullOfOrNull { it.getEventHolder(eventId) }
            ?: events.firstNotNullOfOrNull { it.getEventHolder(eventId) }
            ?: tiles.firstNotNullOfOrNull { it.getEventHolder(eventId) }

    override fun getTileHoldersByGroupEvent(
        event: TileGroupEvent
    ): List<TileHolder<*>> = mutableListOf<TileHolder<*>>().apply {
        searchableOverlayTiles.flatMap { it.getTileHoldersByGroupEvent(event) }.let(::addAll)
        navigationDrawerTiles.orEmpty().flatMap { it.getTileHoldersByGroupEvent(event) }
            .let(::addAll)
        tiles.flatMap { it.getTileHoldersByGroupEvent(event) }.let(::addAll)
    }

    override fun getEventsByTrigger(
        eventTrigger: EventTrigger
    ): List<EventSchema> = events
        .asSequence()
        .filter { it.trigger == eventTrigger }
        .map { it.get() }
        .plus(tiles.mapNotNull { it.getEventsByTrigger(eventTrigger) }.flatten())
        .plus(
            searchableOverlayTiles.mapNotNull { it.getEventsByTrigger(eventTrigger) }.flatten()
        )
        .plus(
            navigationDrawerTiles?.mapNotNull { it.getEventsByTrigger(eventTrigger) }?.flatten()
                .orEmpty()
        )
        .toList()

    override fun isDirty(): Boolean = super.isDirty()
            || navigationDrawerTiles?.any { it.isDirty() } == true
            || stackableOverlays.values.any { overlay -> overlay.tiles.any { it.isDirty() } }

    override fun getTileSchema() = tile.copy(
        tiles = tiles.immutableMapTo { it.get() },
        events = events.immutableMapTo { it.get() },
        navigationDrawerTiles = navigationDrawerTiles?.immutableMapTo { it.get() },
        stackableOverlays = stackableOverlays.immutableMapValuesTo { it.toSchema() }
    )

    fun addBottomSheet(
        id: String,
        tiles: List<TileHolder<*>>,
        isCancellable: Boolean,
        fill: Boolean,
        allowsPartialExpansion: Boolean
    ) {
        addOverlay(
            overlayId = id,
            overlay = StackableOverlay.BottomSheet(
                tiles = tiles,
                isCancellable = isCancellable,
                fill = fill,
                allowsPartialExpansion = allowsPartialExpansion,
            )
        )
    }

    fun addModalBottomSheet(
        id: String,
        tiles: List<TileHolder<*>>,
        isCancellable: Boolean,
        fill: Boolean,
        allowsPartialExpansion: Boolean
    ) {
        addOverlay(
            overlayId = id,
            overlay = StackableOverlay.ModalBottomSheet(
                tiles = tiles,
                isCancellable = isCancellable,
                fill = fill,
                allowsPartialExpansion = allowsPartialExpansion,
            )
        )
    }

    fun addDialog(
        id: String,
        tiles: List<TileHolder<*>>,
        isCancellable: Boolean,
        usePlatformDefaultWidth: Boolean
    ) {
        addOverlay(
            overlayId = id,
            overlay = StackableOverlay.Dialog(
                tiles = tiles,
                isCancellable = isCancellable,
                usePlatformDefaultWidth = usePlatformDefaultWidth,
            )
        )
    }

    private fun addOverlay(
        overlayId: String,
        overlay: StackableOverlay
    ) {
        if (stackableOverlays.contains(overlayId)) throw OverlayAlreadyAddedException(overlayId)

        stackableOverlays[overlayId] = overlay
    }

    fun dismissOverlay(
        overlayId: String
    ) {
        val overlay = stackableOverlays[overlayId] ?: throw OverlayNotExistsException(overlayId)

        stackableOverlays[overlayId] = overlay.markDismissing()
    }

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        if (event is ScreenTileEvents) {
            when (event) {
                is ScreenTileEvents.OnDismissOverlayFinished -> {
                    stackableOverlays.remove(event.overlayId)
                }
            }
        }
    }

    @Immutable
    sealed class StackableOverlay {

        abstract val tiles: List<TileHolder<*>>
        abstract val isCancellable: Boolean
        abstract val isDismissing: Boolean

        fun markDismissing(): StackableOverlay = when (this) {
            is BottomSheet -> copy(isDismissing = true)
            is ModalBottomSheet -> copy(isDismissing = true)
            is Dialog -> copy(isDismissing = true)
        }

        data class BottomSheet(
            override val tiles: List<TileHolder<*>>,
            override val isCancellable: Boolean,
            override val isDismissing: Boolean = false,
            val fill: Boolean,
            val allowsPartialExpansion: Boolean
        ) : StackableOverlay()

        data class ModalBottomSheet(
            override val tiles: List<TileHolder<*>>,
            override val isCancellable: Boolean,
            override val isDismissing: Boolean = false,
            val fill: Boolean,
            val allowsPartialExpansion: Boolean
        ) : StackableOverlay()

        data class Dialog(
            override val tiles: List<TileHolder<*>>,
            override val isCancellable: Boolean,
            override val isDismissing: Boolean = false,
            val usePlatformDefaultWidth: Boolean
        ) : StackableOverlay()

        fun toSchema() = when (this) {
            is BottomSheet -> ScreenTileSchema.StackableOverlay.BottomSheet(
                tiles = tiles.immutableMapTo { it.get() },
                isCancellable = isCancellable,
                isDismissing = isDismissing,
                fill = fill,
                allowsPartialExpansion = allowsPartialExpansion,
            )

            is ModalBottomSheet -> ScreenTileSchema.StackableOverlay.ModalBottomSheet(
                tiles = tiles.immutableMapTo { it.get() },
                isCancellable = isCancellable,
                isDismissing = isDismissing,
                fill = fill,
                allowsPartialExpansion = allowsPartialExpansion,
            )

            is Dialog -> ScreenTileSchema.StackableOverlay.Dialog(
                tiles = tiles.immutableMapTo { it.get() },
                isCancellable = isCancellable,
                isDismissing = isDismissing,
                usePlatformDefaultWidth = usePlatformDefaultWidth,
            )
        }
    }
}