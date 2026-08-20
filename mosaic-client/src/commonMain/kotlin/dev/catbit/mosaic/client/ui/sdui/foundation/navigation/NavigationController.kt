package dev.catbit.mosaic.client.ui.sdui.foundation.navigation

import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavBackStack
import dev.catbit.mosaic.client.ui.sdui.foundation.graph.ScreenNavKey
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable

/**
 * Thin wrapper over one Navigation3 [NavBackStack] — the object registered in [NavigatorsHolder]
 * under a `navigatorId`, and what `Navigate`/`NavigateUp`/`NavigateClearingStack` actually operate
 * on. One instance per backstack — the app's own root one (built in `MosaicApplication`) and one per
 * currently-composed `NestedNavigationGraph` tile.
 *
 * @property backStack the underlying Navigation3 back stack this controller wraps.
 */
@Stable
class NavigationController(
    val backStack: NavBackStack<ScreenNavKey>
) {
    /**
     * Clears this backstack entirely and pushes [destination] as its only entry — the mechanism
     * behind `NavigateClearingStack`.
     *
     * @param destination screen id to navigate to.
     * @param navigationData navigation arguments the destination receives.
     * @param launchSingleTop when `true` (the default) and [destination] is already the current top
     * entry, this call is a no-op — the stack isn't cleared and no duplicate entry is pushed.
     */
    fun navigateClearingStack(
        destination: String,
        navigationData: Map<String, AnySerializable>? = null,
        launchSingleTop: Boolean = true
    ) {

        if (launchSingleTop && backStack.lastOrNull()?.id == destination) return

        backStack.clear()

        backStack.add(
            ScreenNavKey(
                id = destination,
                navigationData = ScreenNavKey.NavigationData(navigationData)
            )
        )
    }

    /**
     * Pushes [destination] onto this backstack — the mechanism behind `Navigate`.
     *
     * @param destination screen id to navigate to.
     * @param navigationData navigation arguments the destination receives.
     * @param poppingUpTo when non-null, pops entries up to [PopUpTo.target] (inclusive or not, per
     * [PopUpTo.inclusive]) *before* pushing [destination] — a no-op for the popping step if
     * [PopUpTo.target] isn't found on the stack; the push to [destination] still happens either way.
     * @param launchSingleTop when `true` (the default) and [destination] is already the current top
     * entry, this call is a no-op entirely (the `poppingUpTo` step doesn't run either).
     */
    fun navigate(
        destination: String,
        navigationData: Map<String, AnySerializable>? = null,
        poppingUpTo: PopUpTo? = null,
        launchSingleTop: Boolean = true
    ) {

        if (launchSingleTop && backStack.lastOrNull()?.id == destination) return

        poppingUpTo?.let {
            val index = backStack
                .indexOfLast { it.id == poppingUpTo.target }
                .takeIf { it != -1 } ?: return@let

            backStack.subList(
                fromIndex = if (poppingUpTo.inclusive) index else index + 1,
                toIndex = backStack.size
            ).clear()
        }

        backStack.add(
            ScreenNavKey(
                id = destination,
                navigationData = ScreenNavKey.NavigationData(navigationData)
            )
        )
    }

    /** Pops the last entry off this backstack, if any — the mechanism behind `NavigateUp`. A no-op
     * if the stack is already empty. */
    fun goBack() {
        backStack.removeLastOrNull()
    }
}

/**
 * Where to pop a backstack up to before pushing a new destination — `Navigate`'s `popUpTo` parameter.
 *
 * @property target screen id to pop up to.
 * @property inclusive when `true`, [target] itself is also popped; when `false`, popping stops right
 * after (leaving [target] as the new top before the push).
 */
data class PopUpTo(
    val target: String,
    val inclusive: Boolean
)

/** Builds a [PopUpTo] — the DSL helper behind `Navigate`'s `popUpTo = poppingUpTo(...)` parameter. */
fun poppingUpTo(
    target: String,
    inclusive: Boolean = false
) = PopUpTo(
    target = target,
    inclusive = inclusive
)