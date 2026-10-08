package dev.catbit.mosaic.client.data.data_sources.network

import dev.catbit.mosaic.core.data.schemas.network.TimeoutsSchema

/**
 * `XMLHttpRequest.timeout` value for [TimeoutsSchema] on this target. XHR only has a whole-request
 * timeout, so only [TimeoutsSchema.requestTimeoutMillis] applies — the connect and socket values
 * are ignored, like Ktor's Js engine does. `0` means no timeout (the XHR default).
 */
internal fun TimeoutsSchema?.toXhrTimeoutMillis(): Int =
    this?.requestTimeoutMillis?.coerceIn(0, Int.MAX_VALUE.toLong())?.toInt() ?: 0
