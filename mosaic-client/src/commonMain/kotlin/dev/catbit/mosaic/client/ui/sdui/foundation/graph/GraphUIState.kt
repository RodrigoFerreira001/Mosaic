package dev.catbit.mosaic.client.ui.sdui.foundation.graph

data class GraphUIState(
    val id: String,
    val entries: List<Entry>,
    val startEntryId: String
) {
    data class Entry(
        val screenId: String
    )
}
