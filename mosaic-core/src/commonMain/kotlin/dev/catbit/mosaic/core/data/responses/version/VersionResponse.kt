package dev.catbit.mosaic.core.data.responses.version

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The wire shape of a cache-version check — the backend's current content version, compared against
 * the client's own locally cached version (`CheckCacheVersionUseCase`) to decide whether cached
 * screens/graph should be invalidated. Consumed only at the repository layer.
 *
 * @property version the backend's current content version — any change from the last known value
 * invalidates the local cache.
 */
@Serializable
data class VersionResponse(
    @SerialName("version")
    val version: Long
)
