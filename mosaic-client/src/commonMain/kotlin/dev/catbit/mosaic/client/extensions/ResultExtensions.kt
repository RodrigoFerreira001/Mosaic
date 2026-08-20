package dev.catbit.mosaic.client.extensions

import dev.catbit.mosaic.client.exceptions.NetworkResponseException
import io.ktor.client.statement.*
import io.ktor.http.*

/** Runs [riskyBlock], wrapping any thrown [Throwable] into a failed [Result] instead of letting it
 * propagate — the standard way a use case in `mosaic-client/.../domain` turns a throwing call into a
 * `Result`-returning one. */
suspend fun <T> safeResult(
    riskyBlock: suspend () -> T
) = try {
    Result.success(riskyBlock())
} catch (e: Throwable) {
    Result.failure(e)
}

/**
 * Runs [riskyBlock] (an HTTP call) via [safeResult], additionally treating a non-2xx response as a
 * failure — turning it into a failed [Result] carrying a [NetworkResponseException] (with the
 * response's status and body text) rather than a successful [Result] wrapping an error response.
 */
suspend fun safeNetworkCall(
    riskyBlock: suspend () -> HttpResponse
): Result<HttpResponse> = safeResult {
    riskyBlock()
}.mapCatching { response ->
    if (response.status.isSuccess())
        response
    else throw NetworkResponseException(
        status = response.status,
        error = response.bodyAsText()
    )
}

/** Applies [block] to this [Result]'s exception, replacing it with the returned [Throwable] — a
 * no-op on a successful [Result]. Used to translate a lower-level exception (e.g. a raw
 * [NetworkResponseException]) into a more specific one before it reaches an `EventRunner`'s
 * `onFailure` handling. */
fun <T> Result<T>.mapException(
    block: (Throwable) -> Throwable
): Result<T> {
    return if (this.isFailure) {
        exceptionOrNull()?.let {
            Result.failure(block(it))
        } ?: this
    } else this
}

/** Discards this [Result]'s success value, collapsing it to `Result<Unit>` — preserves the original
 * exception on failure. Used when a use case's caller only cares whether the operation succeeded,
 * not what it produced. */
fun <T> Result<T>.emptyMap(): Result<Unit> {
    return if (isFailure) {
        Result.failure(exceptionOrNull()!!)
    } else Result.success(Unit)
}

