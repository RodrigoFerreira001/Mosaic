package dev.catbit.mosaic.client.ui.sdui.foundation.navigation

class NavigatorsHolder {
    private val navigators = mutableMapOf<String, NavigationController>()

    fun registerNavigator(
        navigatorId: String,
        navigationController: NavigationController
    ) {
        navigators[navigatorId] = navigationController
    }

    fun unregisterNavigator(
        navigatorId: String
    ) {
        navigators.remove(navigatorId)
    }

    operator fun get(navigatorId: String) = navigators[navigatorId]
}