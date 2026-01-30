package dev.catbit.mosaic.client.domain.send_request

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod

class SendNetworkRequestUseCase(
    private val repository: MosaicRepository
) : UseCase<HttpResponse, SendNetworkRequestUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.sendHttpRequest(
            url = url,
            headers = headers,
            body = body,
            httpMethod = httpMethod
        )
    }

    data class Params(
        val url: String,
        val headers: Map<String, String>? = null,
        val body: Any?,
        val httpMethod: HttpMethod
    )
}