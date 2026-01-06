package dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile

import dev.catbit.mosaic.client.ui.sdui.foundation.models.InsertionPosition
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState

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

    fun removeChildren(ids: List<String>) {
        tiles.removeAll { it.id in ids }
        hasChildrenCountChanged = true
    }

    fun addChild(
        child: TileUIStateProducer<*>,
        where: InsertionPosition = InsertionPosition.End
    ) {
        tiles.add(
            element = child,
            index = where.toIndex(tiles)
        )
        hasChildrenCountChanged = true
    }

    fun addChildren(
        children: List<TileUIStateProducer<*>>,
        where: InsertionPosition = InsertionPosition.End
    ) {
        tiles.addAll(
            elements = children,
            index = where.toIndex(tiles)
        )
        hasChildrenCountChanged = true
    }

    fun wipeChildren() {
        tiles.clear()
        hasChildrenCountChanged = true
    }
}