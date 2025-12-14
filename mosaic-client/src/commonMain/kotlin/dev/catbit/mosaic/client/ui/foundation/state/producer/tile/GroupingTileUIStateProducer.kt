package dev.catbit.mosaic.client.ui.foundation.state.producer.tile

import dev.catbit.mosaic.client.ui.foundation.state.tile.TileUIState

abstract class GroupingTileUIStateProducer<out T : TileUIState> : TileUIStateProducer<T>() {
    protected abstract val tiles: MutableList<TileUIStateProducer<*>>

    private fun shouldRebildAnyChildren() = tiles.any { it.shouldProduce() }
    private var hasChildrenCountChanged: Boolean = false

    override fun shouldProduce() = (shouldRebildAnyChildren() || hasChildrenCountChanged).also {
        if (it) hasChildrenCountChanged = false
    }

    fun getChild(id: String): TileUIStateProducer<*>? =
        tiles.firstOrNull { it.id == id }
            ?: tiles.firstNotNullOfOrNull { (it as? GroupingTileUIStateProducer<*>)?.getChild(id) }

    fun getParentOf(id: String): GroupingTileUIStateProducer<*>? =
        if (tiles.any { it.id == id }) this
        else tiles.firstNotNullOfOrNull { (it as? GroupingTileUIStateProducer<*>)?.getParentOf(id) }

    fun removeChild(id: String) {
        tiles.removeAll { it.id == id }
        hasChildrenCountChanged = true
    }

    fun appendChild(child: TileUIStateProducer<*>) {
        tiles.add(child)
        hasChildrenCountChanged = true
    }

    fun wipeChildren() {
        tiles.clear()
        hasChildrenCountChanged = true
    }
}