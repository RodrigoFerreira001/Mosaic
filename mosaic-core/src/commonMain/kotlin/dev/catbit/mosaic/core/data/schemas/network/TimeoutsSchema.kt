package dev.catbit.mosaic.core.data.schemas.network

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Per-request timeout overrides for the networking events that talk to a backend
 * (`SendNetworkRequest`, `GetScreen`, `RefreshScreen`), mapped one-to-one onto Ktor's `HttpTimeout`
 * on the client. Every field is optional: a `null` keeps the client's default for that timeout.
 *
 * @property requestTimeoutMillis whole call, from sending the request to receiving the full response.
 * @property connectTimeoutMillis time allowed to establish the connection.
 * @property socketTimeoutMillis maximum inactivity between two data packets — including the wait
 * for the response headers, so it must cover the time the server takes to answer.
 */
@Immutable
@Serializable
data class TimeoutsSchema(
    @SerialName("requestTimeoutMillis") val requestTimeoutMillis: Long? = null,
    @SerialName("connectTimeoutMillis") val connectTimeoutMillis: Long? = null,
    @SerialName("socketTimeoutMillis") val socketTimeoutMillis: Long? = null,
)
