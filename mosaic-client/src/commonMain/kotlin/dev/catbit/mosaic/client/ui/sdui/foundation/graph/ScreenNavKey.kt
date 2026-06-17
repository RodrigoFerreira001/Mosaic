package dev.catbit.mosaic.client.ui.sdui.foundation.graph

import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScreenNavKey(
    @SerialName("id")
    val id: String,
    @SerialName("navigationData")
    val navigationData: NavigationData = NavigationData(null)
) : NavKey {

    @Immutable
    @Serializable
    data class NavigationData(
        @SerialName("data")
        val data: Map<String, AnySerializable>? = null
    )
}