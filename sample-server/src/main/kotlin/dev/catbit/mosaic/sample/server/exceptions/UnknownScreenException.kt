package dev.catbit.mosaic.sample.server.exceptions

class UnknownScreenException(screenId: String) : Exception("Unknown screen id: $screenId")
