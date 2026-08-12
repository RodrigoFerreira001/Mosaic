package dev.catbit.mosaic.client.exceptions

class OverlayAlreadyAddedException(overlayId: String) : Throwable("Overlay $overlayId already added")