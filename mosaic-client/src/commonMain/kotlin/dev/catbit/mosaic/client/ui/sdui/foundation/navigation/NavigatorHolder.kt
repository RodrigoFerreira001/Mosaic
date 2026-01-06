package dev.catbit.mosaic.client.ui.sdui.foundation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

object NavigatorHolder {
    private val navigators = mutableMapOf<String, NavBackStack<NavKey>>()

    fun registerNavigator(
        navigatorId: String,
        backStack: NavBackStack<NavKey>
    ) {
        navigators[navigatorId] = backStack
    }

    fun unregisterNavigator(
        navigatorId: String
    ) {
        navigators.remove(navigatorId)
    }

    operator fun get(navigatorId: String) = navigators[navigatorId]
}