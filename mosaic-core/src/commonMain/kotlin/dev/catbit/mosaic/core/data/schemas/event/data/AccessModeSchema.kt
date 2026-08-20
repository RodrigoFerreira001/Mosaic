package dev.catbit.mosaic.core.data.schemas.event.data

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * How much of a [DataSourceSchema] a `GetData`/`RemoveData` reading (or a `RemoveData` deletion)
 * targets — paired with a `DataSourceSchema` inside `GetData.readings`/`RemoveData.deletions`, via
 * the DSL's `fullAccessMode()`/`batchAccessMode(...)`/`singleAccessMode(...)` helpers.
 */
@Immutable
@Serializable
sealed interface AccessModeSchema {
    /** Every entry in the source. On `GetData`, produces a map keyed by data id; on `RemoveData`,
     * wipes the entire source. */
    @Serializable
    @SerialName("Full")
    data object Full : AccessModeSchema

    /**
     * Several entries by id.
     *
     * @property dataIds ids of the entries to read/delete.
     * @property allowMissingData (read only) when `false`, a missing id fails the whole `GetData`
     * with `DataNotFoundException`; when `true`, a missing id is simply omitted from the result.
     * @property unwrapValuesToList (read only) when `true`, the result is a plain `List` of values
     * in [dataIds] order instead of a `Map` keyed by id — only takes effect when no other reading in
     * the same `GetData` call resolves to a map shape.
     */
    @Serializable
    @SerialName("Batch")
    data class Batch(
        @SerialName("dataIds") val dataIds: SerializableImmutableList<String>,
        @SerialName("allowMissingData") val allowMissingData: Boolean,
        @SerialName("unwrapValuesToList") val unwrapValuesToList: Boolean,
    ) : AccessModeSchema

    /**
     * One entry by id — on `GetData`, a `null` value fails the whole reading with
     * `DataNotFoundException`.
     *
     * @property dataId id of the entry to read/delete.
     */
    @Serializable
    @SerialName("Single")
    data class Single(
        @SerialName("dataId") val dataId: String
    ) : AccessModeSchema

}