package dev.catbit.mosaic.client.ui.model

sealed interface WindowInsetsUIModel {
    data object SystemBars : WindowInsetsUIModel
    data object CaptionBar : WindowInsetsUIModel
    data object StatusBar : WindowInsetsUIModel
    data object NavigationBar : WindowInsetsUIModel
    data object Ime : WindowInsetsUIModel
    data object DisplayCutout : WindowInsetsUIModel
    data object Waterfall : WindowInsetsUIModel
}