package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnDropdownListItemSelected")
/** Fires on `DropdownList` when an item is picked, carrying the picked option's id as incoming
 * data.
 * @property id id of the picked option. */
data class OnDropdownListItemSelectedEventTrigger(
    @SerialName("id")
    val id: String
) : EventTrigger
