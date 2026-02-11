package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.send_network_request

import dev.catbit.mosaic.client.domain.send_request.SendNetworkRequestUseCase
import dev.catbit.mosaic.client.exceptions.NetworkResponseException
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
import kotlinx.serialization.json.JsonElement

object SendNetworkRequestEventRunner : EventRunner<SendNetworkRequestEventSchema> {

    override fun EventRunningScope.runEvent(event: SendNetworkRequestEventSchema) {
        with(event) {
            getOrNull<SendNetworkRequestUseCase>()?.let { sendNetworkRequestUseCase ->
                runSuspendOnStateHolderScope {

                    onTrigger(EventTriggers.onStart())

                    sendNetworkRequestUseCase(
                        SendNetworkRequestUseCase.Params(
                            url = url,
                            httpMethod = method.toKtorHttpMethod(),
                            headers = headers ?: incomingData.asMapString(),
                            body = body ?: incomingData
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
                                eventTrigger = EventTriggers.onSuccess(),
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

                            (failure as? NetworkResponseException)?.let {
                                onTrigger(
                                    eventTrigger = EventTriggers.onNetworkResponse(failure.status.value),
                                    data = failure
                                )
                            }
                        }
                }
            }
        }
    }
}