package dev.catbit.mosaic.client.ui.sdui.foundation.data_holder

/**
 * Screen-scoped, in-memory data store — same flat/segmented shape as [ApplicationDataHolder], but one
 * fresh instance per screen, discarded when the screen leaves the back stack. Backs the server DSL's
 * `screenPlainData()`/`screenSegmentedData(segmentId)` data sources, plus the read-only
 * `screenNavigationData()` for arguments the screen was navigated to with. Reached in event execution
 * directly as `EventRunningScope.screenDataHolder` — there's no `get<ScreenDataHolder>()` Koin lookup,
 * since a new instance is created per screen rather than being a single app-wide binding.
 */
interface ScreenDataHolder {

    /** Writes [data] under [dataId] in this screen's flat store, overwriting any existing value. The
     * mechanism behind `UpdateData` targeting `screenPlainData()`. */
    fun addPlainData(
        data: Any?,
        dataId: String
    )

    /** Writes [data] under [dataId] within this screen's [segmentId] namespace, overwriting any
     * existing value. The mechanism behind `UpdateData` targeting `screenSegmentedData(segmentId)`. */
    fun addSegmentedData(
        data: Any?,
        segmentId: String,
        dataId: String
    )

    /** Removes the flat entry stored under [dataId], if any. */
    fun removePlainData(
        dataId: String
    )

    /** Removes the entry stored under [dataId] within [segmentId], if any. */
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

    /** Every flat entry currently stored on this screen, keyed by data id. */
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
     * Every entry currently stored within [segmentId], keyed by data id.
     *
     * @return the segment's entries, or `null` if [segmentId] has never been written to.
     */
    fun getAllSegmentedData(
        segmentId: String
    ): Map<String, Any?>?

    /**
     * Reads the navigation argument [dataId] this screen was opened with — read-only, there is no
     * `add`/`remove` counterpart, since navigation arguments are fixed for the lifetime of a screen
     * instance. The mechanism behind `GetData`'s `screenNavigationData()` data source.
     *
     * @return the argument's value, or `null` if this screen wasn't opened with an argument under
     * that id.
     */
    fun getNavigationData(
        dataId: String
    ): Any?

    /** Every navigation argument this screen was opened with. */
    fun getAllNavigationData(): Map<String, Any>

    /** Clears every flat entry on this screen. */
    fun wipePlainData()

    /** Clears every entry within [segmentId] on this screen, leaving other segments untouched. */
    fun wipeSegmentedData(
        segmentId: String
    )

    /** Clears every segment on this screen entirely — every namespace, not just one. */
    fun wipeSegmentedData()
}

/**
 * Default, purely in-memory [ScreenDataHolder] implementation — one instance per screen, built by
 * `MosaicModules.stateHolder`'s `viewModel` factory and handed [navigationData] at construction time
 * (the arguments the screen was navigated to with, if any). Like [DefaultApplicationDataHolder], it's
 * two plain mutable maps with no persistence.
 *
 * @param navigationData the arguments this screen instance was opened with — what [getNavigationData]/
 * [getAllNavigationData] read from. Immutable for the screen's whole lifetime.
 */
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

