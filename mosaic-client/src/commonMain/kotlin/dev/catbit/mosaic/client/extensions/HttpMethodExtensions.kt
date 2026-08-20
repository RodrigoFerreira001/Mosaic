package dev.catbit.mosaic.client.extensions

import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import io.ktor.http.HttpMethod as KtorHttpMethod

/** Converts the wire-format [HttpMethod] into Ktor's own `HttpMethod` — every networking event
 * (`SendNetworkRequest`, `UploadFile`, the download events, `GetScreen`/`RefreshScreen`) resolves its
 * `method` through this before issuing the real request. */
fun HttpMethod.toKtorHttpMethod() = when (this) {
    HttpMethod.GET -> KtorHttpMethod.Get
    HttpMethod.POST -> KtorHttpMethod.Post
    HttpMethod.PUT -> KtorHttpMethod.Put
    HttpMethod.DELETE -> KtorHttpMethod.Delete
    HttpMethod.PATCH -> KtorHttpMethod.Patch
    HttpMethod.HEAD -> KtorHttpMethod.Head
    HttpMethod.OPTIONS -> KtorHttpMethod.Options
    HttpMethod.TRACE -> KtorHttpMethod.Trace
    HttpMethod.QUERY -> KtorHttpMethod.Query
}