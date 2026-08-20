package dev.catbit.mosaic.client.ui.sdui.foundation.data_holder

/**
 * App-wide, in-memory data store — survives navigation between screens but is lost when the app
 * process dies (no persistence). Backs the server DSL's `applicationPlainData()` (flat key-value) and
 * `applicationSegmentedData(segmentId)` (namespaced key-value) data sources, read/written by
 * `GetData`/`UpdateData`/`RemoveData` and evaluated by `EvaluateData`.
 *
 * A single Koin `single` instance ([DefaultApplicationDataHolder]) backs the whole app; contrast with
 * [ScreenDataHolder], whose plain/segmented data is scoped to one screen instead.
 */
interface ApplicationDataHolder {

    /**
     * Writes [data] under [dataId] in the flat (non-segmented) store, overwriting any existing value.
     * The mechanism behind `UpdateData` targeting `applicationPlainData()`.
     */
    fun addPlainData(
        data: Any?,
        dataId: String
    )

    /**
     * Writes [data] under [dataId] within the namespace [segmentId], overwriting any existing value —
     * creating the segment if it doesn't exist yet. The mechanism behind `UpdateData` targeting
     * `applicationSegmentedData(segmentId)`.
     */
    fun addSegmentedData(
        data: Any?,
        segmentId: String,
        dataId: String
    )

    /** Removes the flat entry stored under [dataId], if any. The mechanism behind `RemoveData`
     * targeting `applicationPlainData()`. */
    fun removePlainData(
        dataId: String
    )

    /** Removes the entry stored under [dataId] within [segmentId], if any. The mechanism behind
     * `RemoveData` targeting `applicationSegmentedData(segmentId)`. */
    fun removeSegmentedData(
        segmentId: String,
        dataId: String
    )

    /**
     * Reads the flat entry stored under [dataId].
     *
     * @return the stored value, or `null` if nothing is stored under [dataId].
     */
    fun getPlainData(
        dataId: String
    ): Any?

    /** Every flat entry currently stored, keyed by data id. The mechanism behind `GetData`'s
     * `fullAccessMode()` over `applicationPlainData()`. */
    fun getAllPlainData(): Map<String, Any?>

    /**
     * Reads the entry stored under [dataId] within [segmentId].
     *
     * @return the stored value, or `null` if [segmentId] doesn't exist or holds nothing under
     * [dataId].
     */
    fun getSegmentedData(
        dataId: String,
        segmentId: String
    ): Any?

    /**
     * Every entry currently stored within [segmentId], keyed by data id. The mechanism behind
     * `GetData`'s `fullAccessMode()` over `applicationSegmentedData(segmentId)`.
     *
     * @return the segment's entries, or `null` if [segmentId] has never been written to.
     */
    fun getAllSegmentedData(
        segmentId: String
    ): Map<String, Any?>?

    /** Clears every flat entry. The mechanism behind `RemoveData`'s `fullAccessMode()` over
     * `applicationPlainData()`. */
    fun wipePlainData()

    /** Clears every entry within [segmentId], leaving other segments untouched. */
    fun wipeSegmentedData(
        segmentId: String
    )

    /** Clears every segment entirely — every namespace, not just one. */
    fun wipeSegmentedData()
}

/**
 * Default, purely in-memory [ApplicationDataHolder] implementation — two mutable maps, one flat and
 * one keyed by segment id, with no persistence and no locking beyond Kotlin's own `MutableMap`
 * semantics. This is the implementation `MosaicModules.applicationModule` binds by default; there is
 * no built-in alternative.
 */
class DefaultApplicationDataHolder : ApplicationDataHolder {

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

