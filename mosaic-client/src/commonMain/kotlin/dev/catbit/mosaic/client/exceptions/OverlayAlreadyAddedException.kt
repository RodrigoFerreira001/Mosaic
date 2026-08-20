package dev.catbit.mosaic.client.exceptions

/**
 * Thrown when `DisplayBottomSheet`/`DisplayModalBottomSheet`/`DisplayDialog` is called with an id
 * already in use by a currently-showing overlay of the same kind — the failure behind those 3
 * events' `onFailure`.
 *
 * @param overlayId the id that was already in use, included in the message.
 */
class OverlayAlreadyAddedException(overlayId: String) : Throwable("Overlay $overlayId already added")