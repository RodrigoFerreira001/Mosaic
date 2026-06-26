package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import androidx.compose.runtime.Stable
import dev.catbit.mosaic.client.ui.sdui.foundation.data_holder.DefaultScreenDataHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.data_holder.ScreenDataHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventManager
import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.graph.ScreenNavKey
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.base.ScreenStateHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.screen_tiles_broadcast.ScreenTilesBroadcastChannel
import dev.catbit.mosaic.client.ui.sdui.foundation.screen_tiles_broadcast.ScreenTilesBroadcastData
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.TilesManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRendererManager
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileSchema
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Stable
class MosaicScreenStateHolder(
    private val initialTiles: ImmutableList<TileSchema>,
    private val initialEvents: ImmutableList<EventSchema>,
    private val failureTiles: ImmutableList<TileSchema>,
    private val failureEvents: ImmutableList<EventSchema>,
    private val navigationData: ScreenNavKey.NavigationData,
    private val eventManager: EventManager,
    val tilesManager: TilesManager,
    val tileRendererManager: TileRendererManager
) : ScreenStateHolder<State, Event, Effect>(),
    ScreenBehaviorsHolder,
    ScreenDataHolder by DefaultScreenDataHolder(navigationData.data.orEmpty()) {

    val screenBroadcastChannel = ScreenTilesBroadcastChannel()

    override val internalUIState = MutableStateFlow<State>(State.Initial())

    init {
        tilesManager.setup(
            tiles = initialTiles,
            events = initialEvents,
            onUpdateStateRequest = ::onUpdateStateRequest,
            state = ScreenTileSchema.State.INITIAL
        )
    }

    override fun setState(state: ScreenBehaviorsHolder.State) {
        when (state) {
            ScreenBehaviorsHolder.State.Initial -> {
                internalUIState.update { State.Initial() }
                tilesManager.setup(
                    tiles = initialTiles,
                    events = initialEvents,
                    onUpdateStateRequest = ::onUpdateStateRequest,
                    state = ScreenTileSchema.State.INITIAL
                )
            }

            ScreenBehaviorsHolder.State.Failure -> {
                internalUIState.update { State.Failure() }
                tilesManager.setup(
                    tiles = failureTiles,
                    events = failureEvents,
                    onUpdateStateRequest = ::onUpdateStateRequest,
                    state = ScreenTileSchema.State.FAILURE
                )
            }

            is ScreenBehaviorsHolder.State.Success -> {
                internalUIState.update { State.Displaying() }
                tilesManager.setup(
                    tiles = state.screenModel.tiles,
                    navigationDrawerTiles = state.screenModel.navigationDrawerTiles,
                    events = state.screenModel.events,
                    onUpdateStateRequest = ::onUpdateStateRequest,
                    state = ScreenTileSchema.State.DISPLAYING
                )
            }
        }
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.OnUIEvent -> onUIEvent(event)
        }
    }

    private fun onUIEvent(
        event: Event.OnUIEvent
    ) {
        when (event.event) {
            is UIEvent.TileEventHolderUIEvent ->
                tilesManager.onEvent(
                    tileId = event.event.tileId,
                    event = event.event.event
                )

            is UIEvent.TileGroupEventHolderUIEvent ->
                tilesManager.onGroupEvent(
                    event = event.event.event
                )

            is UIEvent.EventSchemaHolderUIEvent -> {
                stateHolderScope.launch {
                    eventManager.runEvents(
                        eventSchemas = event.event.events,
                        data = event.event.data
                    )
                }
            }
        }
    }

    private fun onUpdateStateRequest(rootTile: TileSchema) {
        internalUIState.update { currentState ->
            when (currentState) {
                is State.Displaying -> currentState.copy(rootTile = rootTile)
                is State.Failure -> currentState.copy(rootTile = rootTile)
                is State.Initial -> currentState.copy(rootTile = rootTile)
            }
        }
    }

    override fun broadcastData(data: ScreenTilesBroadcastData) {
        stateHolderScope.launch {
            screenBroadcastChannel.broadcast(data)
        }
    }
}