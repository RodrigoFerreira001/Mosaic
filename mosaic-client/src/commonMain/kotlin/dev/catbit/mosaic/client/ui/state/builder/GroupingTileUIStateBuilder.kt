package dev.catbit.mosaic.client.ui.state.builder

import dev.catbit.mosaic.client.ui.state.tile.TileUIState

abstract class GroupingTileUIStateBuilder<out T : TileUIState> : TileUIStateBuilder<T>() {
    protected abstract val tiles: MutableList<TileUIStateBuilder<*>>

    private fun shouldRebildAnyChildren() = tiles.any { it.shouldRebuild() }
    private var hasChildrenCountChanged: Boolean = false

    override fun shouldRebuild() = (shouldRebildAnyChildren() || hasChildrenCountChanged).also {
        if (it) hasChildrenCountChanged = false
    }

    fun getChild(id: String): TileUIStateBuilder<*>? =
        tiles.firstOrNull { it.id == id }
            ?: tiles.firstNotNullOfOrNull { (it as? GroupingTileUIStateBuilder<*>)?.getChild(id) }

    fun removeChild(id: String) {
        tiles.removeAll { it.id == id }
        hasChildrenCountChanged = true
    }

    fun appendChild(child: TileUIStateBuilder<*>) {
        tiles.add(child)
        hasChildrenCountChanged = true
    }
}