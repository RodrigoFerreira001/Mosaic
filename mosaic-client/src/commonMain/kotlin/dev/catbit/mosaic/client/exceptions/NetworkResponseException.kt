package dev.catbit.mosaic.client.exceptions

import io.ktor.http.HttpStatusCode

/**
 * Wraps a non-2xx HTTP response so it can travel as a [Throwable] through `safeNetworkCall`
 * (`extensions/ResultExtensions.kt`) — carries the real status code and raw body text, which is what
 * lets `SendNetworkRequest`/`UploadFile`'s `onNetworkFailure(code)` matching (and `GetScreen`'s
 * equivalent) branch on the exact status.
 *
 * @property status the HTTP status code of the failing response.
 * @property error the raw response body text.
 */
data class NetworkResponseException(
    val status: HttpStatusCode,
    val error: String
) : Throwable()