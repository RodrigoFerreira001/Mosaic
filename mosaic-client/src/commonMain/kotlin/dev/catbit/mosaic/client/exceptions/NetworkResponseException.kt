package dev.catbit.mosaic.client.exceptions

import io.ktor.http.HttpStatusCode

data class NetworkResponseException(
    val status: HttpStatusCode,
    val error: String
) : Throwable()