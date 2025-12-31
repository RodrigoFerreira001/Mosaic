package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastData

interface ScreenBehaviors {
    fun refresh() // TODO Implement
    fun navigate() // TODO Implement
    fun goBack() // TODO Implement
    fun displayDialog() // TODO Implement
    fun closeDialog() // TODO Implement
    fun displayBottomSheet() // TODO Implement
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