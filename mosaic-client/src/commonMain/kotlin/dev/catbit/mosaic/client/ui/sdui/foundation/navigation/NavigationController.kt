package dev.catbit.mosaic.client.ui.sdui.foundation.navigation

import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavBackStack
import dev.catbit.mosaic.client.ui.sdui.foundation.graph.ScreenNavKey
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable

@Stable
class NavigationController(
    val backStack: NavBackStack<ScreenNavKey>
) {
    fun navigate(
        destination: String,
        navigationData: Map<String, AnySerializable>? = null,
        poppingUpTo: PopUpTo? = null
    ) {

        if (backStack.lastOrNull()?.id == destination) return

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

    fun goBack() {
        backStack.removeLastOrNull()
    }
}

data class PopUpTo(
    val target: String,
    val inclusive: Boolean
)

fun poppingUpTo(
    target: String,
    inclusive: Boolean = false
) = PopUpTo(
    target = target,
    inclusive = inclusive
)