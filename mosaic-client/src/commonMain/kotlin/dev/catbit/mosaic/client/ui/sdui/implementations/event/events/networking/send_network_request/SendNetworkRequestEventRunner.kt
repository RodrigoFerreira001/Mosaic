package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.send_network_request

import dev.catbit.mosaic.client.domain.send_request.SendNetworkRequestUseCase
import dev.catbit.mosaic.client.extensions.toKtorHttpMethod
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SendNetworkRequestEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.extensions.toAny
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsBytes
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.JsonElement

object SendNetworkRequestEventRunner : EventRunner<SendNetworkRequestEventSchema> {

    override fun EventRunningScope.runEvent(event: SendNetworkRequestEventSchema) {
        with(event) {
            runSuspendOnStateHolderScope {

                onTrigger(EventTriggers.onStart())

                get<SendNetworkRequestUseCase>()(
                    SendNetworkRequestUseCase.Params(
                        url = url,
                        httpMethod = method.toKtorHttpMethod(),
                        headers = headers ?: if (useIncomingDataForHeaders) incomingData.asMapString() else null,
                        body = body ?: if (!useIncomingDataForHeaders) incomingData else null
                    )
                )
                    .onSuccess { response ->

                        val data = when {
                            response.contentType()?.match(ContentType.Application.Json) == true -> {
                                response.body<JsonElement>().toAny()
                            }

                            else -> response.bodyAsBytes()
                        }

                        onTrigger(
                            eventTrigger = if (response.status.isSuccess()) EventTriggers.onSuccess() else EventTriggers.onFailure(),
                            data = data
                        )

                        onTrigger(
                            eventTrigger = EventTriggers.onNetworkResponse(response.status.value),
                            data = data
                        )
                    }
                    .onFailure { failure ->
                        onTrigger(
                            eventTrigger = EventTriggers.onFailure(),
                            data = failure
                        )
                    }
            }
        }
    }
}