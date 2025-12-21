package dev.catbit.mosaic.client.ui.sdui.foundation.models

sealed interface SharedHorizontalArea {
    data class Defined(
        val totalWidth: Float,
        val columns: Int,
        val gutter: Int,
        val horizontalPadding: Int
    ) : SharedHorizontalArea

    data object Undefined : SharedHorizontalArea
}