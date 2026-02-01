package dev.catbit.mosaic.client.extensions

import dev.catbit.mosaic.client.exceptions.NetworkResponseException
import io.ktor.client.statement.*
import io.ktor.http.*

suspend fun <T> safeResult(
    riskyBlock: suspend () -> T
) = try {
    Result.success(riskyBlock())
} catch (e: Throwable) {
    Result.failure(e)
}

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

fun <T> Result<T>.mapException(
    block: (Throwable) -> Throwable
): Result<T> {
    return if (this.isFailure) {
        exceptionOrNull()?.let {
            Result.failure(block(it))
        } ?: this
    } else this
}

fun <T> Result<T>.emptyMap(): Result<Unit> {
    return if (isFailure) {
        Result.failure(exceptionOrNull()!!)
    } else Result.success(Unit)
}

