package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastData
import dev.catbit.mosaic.core.data.tile.TileModel

interface ScreenBehaviorsHolder {
    fun refresh() // TODO Implement
    fun displayDialog(
        isCancellable: Boolean,
        usePlatformDefaultWidth: Boolean,
        tiles: List<TileModel>
    ) // TODO Implement
    fun closeDialog() // TODO Implement
    fun displayBottomSheet(
        isCancellable: Boolean,
        tiles: List<TileModel>
    ) // TODO Implement
    fun closeBottomSheet() // TODO Implement
    fun displaySnackbar() // TODO Implement
    fun closeSnackbar() // TODO Implement
    fun displayDrawer() // TODO Implement
    fun closeDrawer() // TODO Implement
    fun displayMenu() // TODO Implement
    fun closeMenu() // TODO Implement
    fun requestPermission() // TODO Implement
    fun sendNotification() // TODO Implement
    fun getData()
    fun setData()
    fun removeData()
    fun broadcastData(data: BroadcastData)
}