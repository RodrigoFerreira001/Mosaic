package dev.catbit.mosaic.core.trigger

import kotlinx.serialization.Serializable

@Serializable
open class Trigger(
    @Serializable
    val name: String
)