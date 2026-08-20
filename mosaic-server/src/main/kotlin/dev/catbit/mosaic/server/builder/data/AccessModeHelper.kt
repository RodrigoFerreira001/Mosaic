package dev.catbit.mosaic.server.builder.data

import dev.catbit.mosaic.core.data.schemas.event.data.AccessModeSchema
import kotlinx.collections.immutable.toImmutableList

/** Reads (or writes) every value held by the data source, returned as a full map. */
fun fullAccessMode() = AccessModeSchema.Full

/**
 * Reads (or writes) a specific set of keys from the data source, returned as a map keyed by
 * [dataIds].
 *
 * @param dataIds Keys to read/write.
 * @param allowMissingData Whether a missing key is silently skipped instead of failing the read. Defaults to false.
 * @param unwrapValuesToList Whether each value is unwrapped into a flat list instead of kept as-is (useful when every value is itself a single-element collection). Defaults to false.
 */
fun batchAccessMode(
    dataIds: List<String>,
    allowMissingData: Boolean = false,
    unwrapValuesToList: Boolean = false,
) = AccessModeSchema.Batch(
    dataIds = dataIds.toImmutableList(),
    allowMissingData = allowMissingData,
    unwrapValuesToList = unwrapValuesToList
)

/** Reads (or writes) exactly one key, identified by [dataId], from the data source. */
fun singleAccessMode(
    dataId: String
) = AccessModeSchema.Single(
    dataId = dataId
)