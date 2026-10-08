package dev.catbit.mosaic.client.data.data_sources.network

import dev.catbit.mosaic.core.data.schemas.network.TimeoutsSchema
import io.ktor.client.plugins.timeout
import io.ktor.client.request.HttpRequestBuilder

/**
 * Applies the per-request overrides in [timeouts] to this Ktor request. Only the non-null values
 * are set; the others keep the `HttpClient` defaults installed in `MosaicModules`.
 */
internal fun HttpRequestBuilder.applyTimeouts(timeouts: TimeoutsSchema?) {
    timeouts ?: return
    timeout {
        timeouts.requestTimeoutMillis?.let { requestTimeoutMillis = it }
        timeouts.connectTimeoutMillis?.let { connectTimeoutMillis = it }
        timeouts.socketTimeoutMillis?.let { socketTimeoutMillis = it }
    }
}
