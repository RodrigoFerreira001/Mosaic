package dev.catbit.mosaic.server.builder.event.builders.networking

import dev.catbit.mosaic.core.data.schemas.event.events.networking.SendNetworkRequestEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.network.TimeoutsSchema
import dev.catbit.mosaic.core.serialization.MosaicSerializer
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SendNetworkRequestTimeoutsTest {

    private val serializer = MosaicSerializer()

    private fun roundTrip(event: SendNetworkRequestEventSchema) = serializer.json.decodeFromJsonElement(
        SendNetworkRequestEventSchema.serializer(),
        serializer.encodeEventToJsonElement(event)
    )

    private fun buildRequest(timeouts: TimeoutsSchema?) = EventSchemaBuilderScope()
        .apply {
            SendNetworkRequest(
                id = "request",
                trigger = EventTriggers.onClick(),
                url = "https://example.com",
                method = httpPut(),
                timeouts = timeouts
            )
        }
        .build()
        .single() as SendNetworkRequestEventSchema

    @Test
    fun `timeout builder maps every value`() {
        assertEquals(
            TimeoutsSchema(
                requestTimeoutMillis = 1,
                connectTimeoutMillis = 2,
                socketTimeoutMillis = 3
            ),
            timeout(
                requestTimeoutMillis = 1,
                connectTimeoutMillis = 2,
                socketTimeoutMillis = 3
            )
        )
    }

    @Test
    fun `timeouts survive a serialization round trip`() {
        val event = buildRequest(
            timeouts = timeout(
                requestTimeoutMillis = 60_000,
                socketTimeoutMillis = 60_000
            )
        )

        val json = serializer.encodeEventToJsonElement(event)
        val timeoutsJson = json.jsonObject.getValue("timeouts").jsonObject

        assertEquals(60_000, timeoutsJson.getValue("requestTimeoutMillis").jsonPrimitive.long)
        assertEquals(60_000, timeoutsJson.getValue("socketTimeoutMillis").jsonPrimitive.long)
        assertEquals(event, roundTrip(event))
    }

    @Test
    fun `missing timeouts decode as null`() {
        val event = buildRequest(timeouts = null)

        assertNull(roundTrip(event).timeouts)
    }
}
