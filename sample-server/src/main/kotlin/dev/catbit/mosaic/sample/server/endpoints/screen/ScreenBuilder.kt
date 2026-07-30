package dev.catbit.mosaic.sample.server.endpoints.screen

import dev.catbit.mosaic.core.data.responses.screen.ScreenResponse
import io.ktor.server.routing.RoutingCall

/**
 * One implementation per screen id. [ScreenBuilderManager] picks the first builder whose
 * [canBuild] matches the requested `screenId` and delegates to its [build].
 */
interface ScreenBuilder {
    fun canBuild(screenId: String): Boolean
    suspend fun RoutingCall.build(): ScreenResponse
}
