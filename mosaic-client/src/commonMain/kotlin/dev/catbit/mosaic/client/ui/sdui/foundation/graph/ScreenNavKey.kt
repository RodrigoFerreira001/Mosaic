package dev.catbit.mosaic.client.ui.sdui.foundation.graph

import androidx.navigation3.runtime.NavKey
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScreenNavKey(
    @SerialName("id")
    val id: String,
    @SerialName("navigationData")
    val navigationData: Map<String, AnySerializable>? = null
) : NavKey