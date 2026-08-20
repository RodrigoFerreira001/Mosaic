package dev.catbit.mosaic.client.domain.send_request

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod

/**
 * Sends a general-purpose HTTP request and returns the raw response — backs `SendNetworkRequest`,
 * the general-purpose way to talk to a backend from inside an event chain. Reachable via
 * `get<SendNetworkRequestUseCase>()`.
 */
class SendNetworkRequestUseCase(
    private val repository: MosaicRepository
) : UseCase<HttpResponse, SendNetworkRequestUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.sendHttpRequest(
            url = url,
            headers = headers,
            body = body,
            httpMethod = httpMethod,
            timeoutMillis = timeoutMillis
        )
    }

    /**
     * @property url endpoint.
     * @property headers request headers.
     * @property body request body.
     * @property httpMethod HTTP method.
     * @property timeoutMillis request timeout override.
     */
    data class Params(
        val url: String,
        val headers: Map<String, String>? = null,
        val body: Any?,
        val httpMethod: HttpMethod,
        val timeoutMillis: Long? = null
    )
}