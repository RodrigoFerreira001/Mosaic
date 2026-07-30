package dev.catbit.mosaic.sample.server.endpoints.screen

import dev.catbit.mosaic.core.data.responses.screen.ScreenResponse
import dev.catbit.mosaic.sample.server.exceptions.UnknownScreenException
import io.ktor.server.routing.RoutingCall

class ScreenBuilderManager(
    private val builders: List<ScreenBuilder>
) {
    suspend fun RoutingCall.build(
        screenId: String
    ): ScreenResponse = builders
        .firstOrNull { it.canBuild(screenId) }
        ?.let { builder -> with(builder) { build() } }
        ?: throw UnknownScreenException(screenId)
}
