package dev.catbit.mosaic.client.extensions

import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import io.ktor.http.HttpMethod as KtorHttpMethod

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