package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnClick")
/** Fires when an interactive tile is tapped — the single most common trigger in the catalog,
 * declared on nearly every clickable tile (`Button`, `IconButton`, chips, `Card`, containers with a
 * declared click handler, etc.). */
object OnClickEventTrigger : EventTrigger