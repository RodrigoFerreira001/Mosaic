package dev.catbit.mosaic.client.exceptions

class OverlayNotExistsException(overlayId: String) : Throwable("Overlay $overlayId not exists")