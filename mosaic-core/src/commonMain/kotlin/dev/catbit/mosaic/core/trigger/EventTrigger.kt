package dev.catbit.mosaic.core.trigger

import kotlinx.serialization.Serializable

@Serializable
open class EventTrigger(
    @Serializable
    val name: String
)