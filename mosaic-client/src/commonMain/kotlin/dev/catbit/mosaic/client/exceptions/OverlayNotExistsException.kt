package dev.catbit.mosaic.client.exceptions

/**
 * Thrown when `DismissBottomSheet`/`DismissModalBottomSheet`/`DismissDialog` is called with an id
 * that has no currently-showing overlay registered under it — the failure behind those 3 events'
 * `onFailure`.
 *
 * @param overlayId the id that wasn't found, included in the message.
 */
class OverlayNotExistsException(overlayId: String) : Throwable("Overlay $overlayId not exists")