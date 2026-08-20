package dev.catbit.mosaic.client.ui.sdui.foundation.navigation

/**
 * App-wide lookup table of [NavigationController]s by `navigatorId` — lets `Navigate`/`NavigateUp`/
 * `NavigateClearingStack` trigger navigation on any registered backstack by name, from any event
 * chain, without the backstack itself (which only really exists near the top of the Compose tree)
 * being threaded down as a parameter. A single Koin `single`. `MosaicApplication` registers the app's
 * own root backstack under the conventional id `"root"`; a custom container tile hosting its own
 * nested navigation graph (the way `NestedNavigationGraph` does) registers its own backstack the
 * same way, under whatever `navigatorId` the DSL author chose for it.
 */
class NavigatorsHolder {
    private val navigators = mutableMapOf<String, NavigationController>()

    /**
     * Registers [navigationController] under [navigatorId], so `Navigate`/`NavigateUp`/
     * `NavigateClearingStack` events targeting that id can find it. Overwrites any controller
     * previously registered under the same id.
     */
    fun registerNavigator(
        navigatorId: String,
        navigationController: NavigationController
    ) {
        navigators[navigatorId] = navigationController
    }

    /**
     * Removes whatever [NavigationController] is registered under [navigatorId], if any — called
     * when the owning backstack (a `NestedNavigationGraph` tile, or the app root) leaves composition,
     * so navigation events targeting it correctly fail afterward instead of reaching a stale
     * controller.
     */
    fun unregisterNavigator(
        navigatorId: String
    ) {
        navigators.remove(navigatorId)
    }

    /**
     * Looks up the [NavigationController] registered under [navigatorId].
     *
     * @return the matching controller, or `null` if nothing is currently registered under that id —
     * the case every navigation event's own `OnFailure` handling guards against.
     */
    operator fun get(navigatorId: String) = navigators[navigatorId]
}