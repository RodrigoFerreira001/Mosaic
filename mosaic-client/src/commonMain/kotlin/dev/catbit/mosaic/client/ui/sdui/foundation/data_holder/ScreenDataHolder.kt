package dev.catbit.mosaic.client.ui.sdui.foundation.data_holder

interface ScreenDataHolder {

    fun addPlainData(
        data: Any?,
        dataId: String
    )

    fun addSegmentedData(
        data: Any?,
        segmentId: String,
        dataId: String
    )

    fun removePlainData(
        dataId: String
    )

    fun removeSegmentedData(
        segmentId: String,
        dataId: String
    )

    fun getPlainData(
        dataId: String
    ): Any?

    fun getAllPlainData(): Map<String, Any?>

    fun getSegmentedData(
        dataId: String,
        segmentId: String
    ): Any?

    fun getAllSegmentedData(
        segmentId: String
    ): Map<String, Any?>?

    fun getNavigationData(
        dataId: String
    ): Any?

    fun getAllNavigationData(): Map<String, Any>

    fun wipePlainData()

    fun wipeSegmentedData(
        segmentId: String
    )

    fun wipeSegmentedData()
}

class DefaultScreenDataHolder(
    private val navigationData: Map<String, Any>
) : ScreenDataHolder {

    private val plainData: MutableMap<String, Any?> = mutableMapOf()
    private val segmentedData: MutableMap<String, MutableMap<String, Any?>> = mutableMapOf()

    override fun addPlainData(
        data: Any?,
        dataId: String
    ) {
        plainData[dataId] = data
    }

    override fun addSegmentedData(
        data: Any?,
        segmentId: String,
        dataId: String
    ) {
        segmentedData.getOrPut(segmentId) { mutableMapOf() }[dataId] = data
    }

    override fun removePlainData(dataId: String) {
        plainData.remove(dataId)
    }

    override fun removeSegmentedData(
        segmentId: String,
        dataId: String
    ) {
        segmentedData[segmentId]?.remove(dataId)
    }

    override fun getPlainData(dataId: String): Any? =
        plainData[dataId]

    override fun getAllPlainData(): Map<String, Any?> =
        plainData.toMap()

    override fun getSegmentedData(dataId: String, segmentId: String): Any? =
        segmentedData[segmentId]?.get(dataId)

    override fun getAllSegmentedData(segmentId: String): Map<String, Any?>? =
        segmentedData[segmentId]?.toMap()

    override fun getNavigationData(dataId: String): Any? =
        navigationData[dataId]

    override fun getAllNavigationData(): Map<String, Any> =
        navigationData.toMap()

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

