package dev.catbit.mosaic.core.data.schemas.event.trigger

import androidx.compose.runtime.Immutable

/**
 * The wire contract every trigger condition implements — resolved polymorphically by
 * [dev.catbit.mosaic.core.serialization.MosaicSerializer] the same way [dev.catbit.mosaic.core.data.schemas.tile.TileSchema]/
 * [dev.catbit.mosaic.core.data.schemas.event.EventSchema] are.
 *
 * Every built-in trigger is a `data object` (parameterless, e.g. `OnSuccessEventTrigger`) or a
 * `data class` (parameterized, e.g. `OnNetworkResponseEventTrigger(httpCode)`,
 * `OnSystemBroadcastEventTrigger(broadcastId)`). Either way, a trigger carries **no behavior** —
 * it's matched purely by structural equality (`==`) against whatever value a tile/event fires, never
 * invoked or subscribed to. See [dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers]
 * for the DSL-facing constructor functions every built-in trigger has.
 */
@Immutable
interface EventTrigger