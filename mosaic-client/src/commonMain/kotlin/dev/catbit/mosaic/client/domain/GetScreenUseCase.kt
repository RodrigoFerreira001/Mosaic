package dev.catbit.mosaic.client.domain

import dev.catbit.mosaic.client.domain.base.UseCase
import dev.catbit.mosaic.core.data.event.events.NavigateEventModel
import dev.catbit.mosaic.core.data.screen.ScreenModel
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.placement.AlignmentModel
import dev.catbit.mosaic.core.data.tile.placement.ArrangementModel
import dev.catbit.mosaic.core.data.tile.style.SizeModel
import dev.catbit.mosaic.core.data.tile.style.StyleModel
import dev.catbit.mosaic.core.data.tile.tiles.buttons.ButtonTileModel
import dev.catbit.mosaic.core.data.tile.tiles.grouping.ColumnTileModel
import dev.catbit.mosaic.core.data.tile.tiles.inputs.TextFieldTileModel
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.core.trigger.EventTriggers
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class GetScreenUseCase : UseCase<ScreenModel, GetScreenUseCase.Params>() {

    override suspend fun execute(params: Params): Result<ScreenModel> {
        delay(1.seconds)
        return Result.success(
            ScreenModel(
                tiles = listOf(
                    ColumnTileModel(
                        id = randomUuid(),
                        tiles = listOf(
                            ButtonTileModel(
                                id = randomUuid(),
                                events = listOf(
                                    NavigateEventModel(
                                        id = randomUuid(),
                                        url = "http://",
                                        trigger = EventTriggers.OnClick,
                                        events = null
                                    )
                                ),
                                text = "Clique aqui",
                                visibility = TileModel.Visibility.VISIBLE,
                                loading = false,
                                style = StyleModel(
                                    size = SizeModel(
                                        width = SizeModel.Behavior.Horizontal.Wrap,
                                        height = SizeModel.Behavior.Vertical.Wrap
                                    )
                                )
                            ),
                            TextFieldTileModel(
                                id = randomUuid(),
                                visibility = TileModel.Visibility.VISIBLE,
                                style = StyleModel(
                                    size = SizeModel(
                                        width = SizeModel.Behavior.Horizontal.Wrap,
                                        height = SizeModel.Behavior.Vertical.Wrap
                                    )
                                ),
                                events = listOf(
                                    NavigateEventModel(
                                        id = randomUuid(),
                                        url = "http://",
                                        trigger = EventTriggers.OnTextChanged,
                                        events = null
                                    )
                                ),
                                value = null
                            )
                        ),
                        events = null,
                        style = StyleModel(
                            size = SizeModel(
                                width = SizeModel.Behavior.Horizontal.Fill,
                                height = SizeModel.Behavior.Vertical.Fill
                            )
                        ),
                        visibility = TileModel.Visibility.VISIBLE,
                        arrangement = ArrangementModel.Vertical.Top,
                        alignment = AlignmentModel.Horizontal.Center,
                    )
                ),
                events = null
            )
        )
    }

    data class Params(
        val screenId: String,
        val data: Map<String, Any>?
    )
}