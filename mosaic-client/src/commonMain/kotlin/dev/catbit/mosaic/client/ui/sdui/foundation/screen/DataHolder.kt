package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import dev.catbit.mosaic.client.ui.sdui.foundation.screen.DataHolder.Source

interface DataHolder {

    fun addData(
        data: Any,
        source: Source
    )

    fun getData(
        source: Source
    ): Any?

    fun removeData(
        source: Source
    )

    fun wipePlainData()

    fun wipeSegmentedData(
        segmentId: String
    )

    fun wipeSegmentedData()

    sealed interface Source {
        data class Plain(
            val dataId: String
        ) : Source

        data class Segmented(
            val segmentId: String,
            val dataId: String
        ) : Source
    }
}

class DefaultDataHolder : DataHolder {

    private val plainData: MutableMap<String, Any> = mutableMapOf()
    private val segmentedData: MutableMap<String, MutableMap<String, Any>> = mutableMapOf()

    override fun addData(
        data: Any,
        source: Source
    ) {
        when (source) {
            is Source.Plain -> plainData[source.dataId] = data
            is Source.Segmented -> segmentedData.getOrPut(source.segmentId) { mutableMapOf() }[source.dataId] = data
        }
    }

    override fun getData(
        source: Source
    ): Any? = when (source) {
        is Source.Plain -> plainData[source.dataId]
        is Source.Segmented -> segmentedData[source.segmentId]?.get(source.dataId)
    }

    override fun removeData(
        source: Source
    ) {
        when (source) {
            is Source.Plain -> plainData.remove(source.dataId)
            is Source.Segmented -> segmentedData[source.segmentId]?.remove(source.dataId)
        }
    }

    override fun wipePlainData() {
        plainData.clear()
    }

    override fun wipeSegmentedData(
        segmentId: String
    ) {
        segmentedData[segmentId]?.clear()
    }

    override fun wipeSegmentedData() {
        segmentedData.clear()
    }
}

