package dev.catbit.mosaic.client.data.data_sources.network

import dev.catbit.mosaic.core.serialization.serializers.AnySerializable

class NetworkParametersHolder {
    private var body: AnySerializable? = null
    private var headers: Map<String, String>? = null
    private var url: String? = null

    fun setBody(
        body: AnySerializable
    ) {
        this.body = body
    }

    fun setHeaders(
        headers: Map<String, String>
    ) {
        this.headers = headers
    }

    fun setUrl(
        url: String
    ) {
        this.url = url
    }

    fun consume() = NetworkParameters(
        body = body,
        headers = headers,
        url = url
    ).also {
        body = null
        headers = null
        url = null
    }

    data class NetworkParameters(
        val body: AnySerializable?,
        val headers: Map<String, String>?,
        val url: String?
    )
}
