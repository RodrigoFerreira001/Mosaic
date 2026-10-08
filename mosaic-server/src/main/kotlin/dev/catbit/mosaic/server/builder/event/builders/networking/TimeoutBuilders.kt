package dev.catbit.mosaic.server.builder.event.builders.networking

import dev.catbit.mosaic.core.data.schemas.network.TimeoutsSchema

/**
 * Timeout overrides for `SendNetworkRequest`, `GetScreen` and `RefreshScreen`, all in milliseconds.
 * Any value left `null` keeps the client's default for that timeout.
 *
 * @param requestTimeoutMillis whole call, from sending the request to receiving the full response.
 * @param connectTimeoutMillis time allowed to establish the connection.
 * @param socketTimeoutMillis maximum inactivity between two data packets — including the wait for
 * the response headers, so it must cover the time the server takes to answer.
 */
fun timeout(
    requestTimeoutMillis: Long? = null,
    connectTimeoutMillis: Long? = null,
    socketTimeoutMillis: Long? = null,
): TimeoutsSchema = TimeoutsSchema(
    requestTimeoutMillis = requestTimeoutMillis,
    connectTimeoutMillis = connectTimeoutMillis,
    socketTimeoutMillis = socketTimeoutMillis,
)
