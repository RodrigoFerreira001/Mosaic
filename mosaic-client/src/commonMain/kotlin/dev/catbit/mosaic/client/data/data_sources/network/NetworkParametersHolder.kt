package dev.catbit.mosaic.client.data.data_sources.network

import dev.catbit.mosaic.core.serialization.serializers.AnySerializable

class NetworkParametersHolder {
    private var body: AnySerializable? = null
    private var headers: Map<String, String>? = null

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

    fun consume() = (body to headers).also {
        body = null
        headers = null
    }
}