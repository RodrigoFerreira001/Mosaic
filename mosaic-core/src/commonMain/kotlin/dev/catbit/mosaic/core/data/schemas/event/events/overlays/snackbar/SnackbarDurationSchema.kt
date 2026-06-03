package dev.catbit.mosaic.core.data.schemas.event.events.overlays.snackbar

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SnackbarDurationSchema {
    @SerialName("Short") Short,
    @SerialName("Long") Long,
    @SerialName("Indefinite") Indefinite
}
