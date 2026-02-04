package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema

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
    data class OnUIEvent(val event: UIEvent) : Event
}

sealed interface Effect