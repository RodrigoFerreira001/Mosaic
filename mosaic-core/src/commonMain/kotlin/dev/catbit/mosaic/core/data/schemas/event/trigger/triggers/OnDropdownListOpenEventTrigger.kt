package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnDropdownListOpen")
/** Fires on `DropdownList` when its anchor is tapped while the menu is closed. */
data object OnDropdownListOpenEventTrigger : EventTrigger
