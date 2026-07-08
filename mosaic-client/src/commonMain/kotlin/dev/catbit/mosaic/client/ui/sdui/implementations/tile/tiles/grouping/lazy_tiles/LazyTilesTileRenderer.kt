package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.lazy_tiles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.visible
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.domain.send_request.SendNetworkRequestUseCase
import dev.catbit.mosaic.client.extensions.toKtorHttpMethod
import dev.catbit.mosaic.client.extensions.OnDisplayEffect
import dev.catbit.mosaic.client.logger.MosaicLogger
import dev.catbit.mosaic.client.ui.effects.SingleEffect
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyTilesTileSchema
import dev.catbit.mosaic.core.domain.base.IO
import dev.catbit.mosaic.core.serialization.MosaicSerializer
import io.ktor.client.call.body
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.builtins.ListSerializer
import org.koin.compose.koinInject

object LazyTilesTileRenderer : TileRenderer<LazyTilesTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: LazyTilesTileSchema,
    ) {

        OnDisplayEffect()

        val sendNetworkRequestUseCase = koinInject<SendNetworkRequestUseCase>()
        val mosaicSerializer = koinInject<MosaicSerializer>()
        val mosaicLogger = koinInject<MosaicLogger>()

        with(tileSchema) {
            Column(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style)
            ) {
                tiles?.let {
                    RenderChildren(it)
                } ?: run {
                    if (isFailureState) {
                        RenderChildren(failureTiles)
                    } else {
                        SingleEffect {
                            withContext(Dispatchers.IO) {
                                triggerEvent(EventTriggers.onLoadTilesStart())
                                sendNetworkRequestUseCase(
                                    SendNetworkRequestUseCase.Params(
                                        url = url,
                                        httpMethod = method.toKtorHttpMethod(),
                                        headers = headers,
                                        body = body
                                    )
                                )
                                    .onSuccess { response ->
                                        try {
                                            triggerEvent(EventTriggers.onLoadTilesSuccess())
                                            dispatchEvent(
                                                tileEvent = LazyTilesTileEvents.OnTilesLoadedSuccessfully(
                                                    tiles = mosaicSerializer.decodeFromJsonElement(
                                                        deserializer = ListSerializer(PolymorphicSerializer(TileSchema::class)),
                                                        element = response.body()
                                                    )
                                                )
                                            )
                                        } catch (e: Throwable) {
                                            mosaicLogger.error("LazyTilesTileRenderer: ${e.stackTraceToString()}")
                                            triggerEvent(
                                                trigger = EventTriggers.onLoadTilesFailure(),
                                                data = e
                                            )
                                            dispatchEvent(LazyTilesTileEvents.OnTilesLoadFailure)
                                        }
                                    }
                                    .onFailure {
                                        mosaicLogger.error("LazyTilesTileRenderer: ${it.stackTraceToString()}")
                                        triggerEvent(
                                            trigger = EventTriggers.onLoadTilesFailure(),
                                            data = it
                                        )
                                        dispatchEvent(LazyTilesTileEvents.OnTilesLoadFailure)
                                    }
                            }
                        }

                        RenderChildren(placeholderTiles)
                    }
                }
            }
        }
    }
}
