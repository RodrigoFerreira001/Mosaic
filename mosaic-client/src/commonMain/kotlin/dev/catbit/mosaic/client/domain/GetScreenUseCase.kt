package dev.catbit.mosaic.client.domain

import dev.catbit.mosaic.client.domain.base.UseCase
import dev.catbit.mosaic.core.data.event.events.NavigateEventModel
import dev.catbit.mosaic.core.data.event.events.scroll.column.ScrollTileColumnEventModel
import dev.catbit.mosaic.core.data.screen.ScreenModel
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.placement.AlignmentModel
import dev.catbit.mosaic.core.data.tile.placement.ArrangementModel
import dev.catbit.mosaic.core.data.tile.style.MarginModel
import dev.catbit.mosaic.core.data.tile.style.SizeModel
import dev.catbit.mosaic.core.data.tile.style.StyleModel
import dev.catbit.mosaic.core.data.tile.tiles.buttons.ButtonTileModel
import dev.catbit.mosaic.core.data.tile.tiles.grouping.ColumnTileModel
import dev.catbit.mosaic.core.data.tile.tiles.inputs.TextFieldTileModel
import dev.catbit.mosaic.core.data.tile.tiles.text.TextTileModel
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.core.trigger.EventTriggers

class GetScreenUseCase : UseCase<ScreenModel, GetScreenUseCase.Params>() {

    override suspend fun execute(params: Params): Result<ScreenModel> {
        return Result.success(
            ScreenModel(
                tiles = listOf(
                    ColumnTileModel(
                        id = "COLUNA",
                        tiles = listOf(
                            TextTileModel(
                                id = randomUuid(),
                                events = null,
                                style = StyleModel(
                                    size = SizeModel(
                                        width = SizeModel.Behavior.Horizontal.Fill,
                                        height = SizeModel.Behavior.Vertical.Wrap
                                    ),
                                    margin = MarginModel(
                                        start = 24,
                                        end = 24,
                                        top = 0,
                                        bottom = 0
                                    )
                                ),
                                visibility = TileModel.Visibility.VISIBLE,
                                text = "Isso é um texto"
                            ),
                            ButtonTileModel(
                                id = randomUuid(),
                                events = listOf(
                                    ScrollTileColumnEventModel(
                                        tileId = "COLUNA",
                                        where = ScrollTileColumnEventModel.Where.Bottom,
                                        id = randomUuid(),
                                        trigger = EventTriggers.OnClick,
                                        events = null,
                                        smooth = false
                                    ),
                                    NavigateEventModel(
                                        id = randomUuid(),
                                        url = "http://",
                                        trigger = EventTriggers.OnClick,
                                        events = null
                                    )
                                ),
                                text = "Rolar para o fim",
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
                                        width = SizeModel.Behavior.Horizontal.Fill,
                                        height = SizeModel.Behavior.Vertical.Wrap
                                    ),
                                    margin = MarginModel(
                                        start = 24,
                                        end = 24,
                                        top = 0,
                                        bottom = 0
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
                        ) + (0..25).map {
                            TextTileModel(
                                id = randomUuid(),
                                events = null,
                                style = StyleModel(
                                    size = SizeModel(
                                        width = SizeModel.Behavior.Horizontal.Fill,
                                        height = SizeModel.Behavior.Vertical.Wrap
                                    ),
                                    margin = MarginModel(
                                        start = 24,
                                        end = 24,
                                        top = 0,
                                        bottom = 0
                                    )
                                ),
                                visibility = TileModel.Visibility.VISIBLE,
                                text = "Número $it"
                            )
                        },
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
                        isScrollable = true,
                        lazyRender = false
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