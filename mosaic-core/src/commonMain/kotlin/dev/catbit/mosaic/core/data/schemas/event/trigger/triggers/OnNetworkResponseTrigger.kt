package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("OnNetworkResponse")
data class OnNetworkResponseTrigger(
    @SerialName("httpCode") val httpCode: Int
) : EventTrigger