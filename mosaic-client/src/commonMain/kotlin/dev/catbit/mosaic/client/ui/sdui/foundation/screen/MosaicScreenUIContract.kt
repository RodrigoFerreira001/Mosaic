package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.coroutines.CoroutineScope

sealed interface State {

    val rootTile: TileSchema?

    data class Initial(
        override val rootTile: TileSchema? = null
    ) : State

    data class Displaying(
        override val rootTile: TileSchema? = null
    ) : State

    data class Failure(
        override val rootTile: TileSchema? = null
    ) : State
}

sealed interface Event {
    data class OnUIEvent(
        val event: UIEvent
    ) : Event

    data class OnScreenCoroutineScopeSet(
        val coroutineScope: CoroutineScope
    ) : Event
}

sealed interface Effect