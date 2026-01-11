package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastData
import dev.catbit.mosaic.core.data.tile.TileModel

interface ScreenBehaviorsHolder {
    fun refresh()
    fun displayDialog( // TODO implementar EventTrigger de ON_DISPLAY
        isCancellable: Boolean,
        usePlatformDefaultWidth: Boolean,
        tiles: List<TileModel>
    )
    fun dismissDialog() // TODO implementar EventTrigger de ON_DISMISS
    fun displayBottomSheet( // TODO implementar EventTrigger de ON_DISPLAY
        isCancellable: Boolean,
        fill: Boolean,
        tiles: List<TileModel>
    )
    fun dismissBottomSheet() // TODO implementar EventTrigger de ON_DISMISS
    fun displayNavigationDrawer() // TODO implementar EventTrigger de ON_DISPLAY
    fun dismissNavigationDrawer() // TODO implementar EventTrigger de ON_DISMISS
    fun displaySnackbar( // TODO Completar com demais coisas
        message: String
    )
    fun closeSnackbar()
    fun broadcastData(data: BroadcastData)
}