package dev.catbit.mosaic.server.builder.data

import dev.catbit.mosaic.core.data.schemas.event.data.AccessModeSchema
import kotlinx.collections.immutable.toImmutableList

fun fullAccessMode() = AccessModeSchema.Full

fun batchAccessMode(
    dataIds: List<String>,
    allowMissingData: Boolean = false,
    unwrapValuesToList: Boolean = false,
) = AccessModeSchema.Batch(
    dataIds = dataIds.toImmutableList(),
    allowMissingData = allowMissingData,
    unwrapValuesToList = unwrapValuesToList
)

fun singleAccessMode(
    dataId: String
) = AccessModeSchema.Single(
    dataId = dataId
)