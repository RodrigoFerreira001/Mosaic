package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import androidx.compose.runtime.Stable
import dev.catbit.mosaic.core.data.schemas.animation.ContentTransitionSchema
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.collections.immutable.ImmutableList

@Stable
class ScreenExtrasHolder {

    private val extras = mutableMapOf<String, Extra>()

    fun registerExtra(
        screenId: String,
        initialTiles: ImmutableList<TileSchema>,
        initialEvents: ImmutableList<EventSchema>,
        failureTiles: ImmutableList<TileSchema>,
        failureEvents: ImmutableList<EventSchema>,
        transition: ContentTransitionSchema? = null,
        popTransition: ContentTransitionSchema? = null,
        predictivePopTransition: ContentTransitionSchema? = null,
    ) {
        extras[screenId] = Extra(
            initialTiles = initialTiles,
            initialEvents = initialEvents,
            failureTiles = failureTiles,
            failureEvents = failureEvents,
            transition = transition,
            popTransition = popTransition,
            predictivePopTransition = predictivePopTransition,
        )
    }

    fun getExtra(screenId: String) = extras.getValue(screenId)

    fun getExtraOrNull(screenId: String) = extras[screenId]

    fun removeExtra(screenId: String) {
        extras.remove(screenId)
    }

    data class Extra(
        val initialTiles: ImmutableList<TileSchema>,
        val initialEvents: ImmutableList<EventSchema>,
        val failureTiles: ImmutableList<TileSchema>,
        val failureEvents: ImmutableList<EventSchema>,
        val transition: ContentTransitionSchema? = null,
        val popTransition: ContentTransitionSchema? = null,
        val predictivePopTransition: ContentTransitionSchema? = null
    )
}