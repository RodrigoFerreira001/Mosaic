package dev.catbit.mosaic.client.exceptions

/**
 * Thrown when an event is looked up by id somewhere in the screen's tile tree and none carries that
 * id — the failure behind `TriggerEvent`'s and `UpdateEvents`' `onFailure`, and `TilesManager`'s own
 * `updateEventHolder`.
 */
class EventNotFoundException(
    override val message: String?
) : Throwable()
