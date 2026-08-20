package dev.catbit.mosaic.core.data.schemas.network

/** HTTP method used by every networking event (`SendNetworkRequest`, `UploadFile`, the download
 * events, `GetScreen`/`RefreshScreen`) — converted to Ktor's own `HttpMethod` via
 * `HttpMethod.toKtorHttpMethod()` on the client. A `GET` sent with a non-null body fails validation
 * client-side (`GetRequestWithBodyException`) rather than being sent as-is. */
enum class HttpMethod {
    GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS, TRACE, QUERY
}