package dev.catbit.mosaic.client.domain

import dev.catbit.mosaic.client.domain.base.UseCase
import dev.catbit.mosaic.core.data.color.ColorModel
import dev.catbit.mosaic.core.data.event.events.menu.ToggleMenuEventModel
import dev.catbit.mosaic.core.data.event.events.navigation.NavigateEventModel
import dev.catbit.mosaic.core.data.event.events.navigation.NavigateUpEventModel
import dev.catbit.mosaic.core.data.event.events.overlays.dialog.DismissDialogEventModel
import dev.catbit.mosaic.core.data.event.events.overlays.dialog.DisplayDialogEventModel
import dev.catbit.mosaic.core.data.event.events.tiles.AddTilesEventModel
import dev.catbit.mosaic.core.data.screen.ScreenModel
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.placement.AlignmentModel
import dev.catbit.mosaic.core.data.tile.placement.ArrangementModel
import dev.catbit.mosaic.core.data.tile.style.MarginModel
import dev.catbit.mosaic.core.data.tile.style.SizeModel
import dev.catbit.mosaic.core.data.tile.style.StyleModel
import dev.catbit.mosaic.core.data.tile.style.WindowInsetsModel
import dev.catbit.mosaic.core.data.tile.tiles.buttons.ButtonTileModel
import dev.catbit.mosaic.core.data.tile.tiles.containers.ColumnTileModel
import dev.catbit.mosaic.core.data.tile.tiles.inputs.TextFieldTileModel
import dev.catbit.mosaic.core.data.tile.tiles.text.TextTileModel
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.core.data.event_trigger.EventTriggers
import dev.catbit.mosaic.core.data.icon.IconModel
import dev.catbit.mosaic.core.data.tile.tiles.menu.MenuTileModel

class GetScreenUseCase : UseCase<ScreenModel, GetScreenUseCase.Params>() {

    override suspend fun execute(params: Params): Result<ScreenModel> {
        return Result.success(
            when (params.screenId) {
                "home" -> ScreenModel(
                    id = "home",
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
                                    text = "Bem vindo à tela ${params.screenId}"
                                ),
                                ButtonTileModel(
                                    id = randomUuid(),
                                    text = "Ir para segunda tela",
                                    visibility = TileModel.Visibility.VISIBLE,
                                    loading = false,
                                    style = StyleModel(
                                        size = SizeModel(
                                            width = SizeModel.Behavior.Horizontal.Wrap,
                                            height = SizeModel.Behavior.Vertical.Wrap
                                        )
                                    ),
                                    events = listOf(
                                        NavigateEventModel(
                                            id = randomUuid(),
                                            destination = "second",
                                            navigatorId = "root",
                                            popUpTo = null,
                                            data = null,
                                            trigger = EventTriggers.onClick(),
                                            events = null
                                        )
                                    )
                                ),
                                MenuTileModel(
                                    id = "MENU",
                                    expanded = false,
                                    events = listOf(
                                        AddTilesEventModel(
                                            id = randomUuid(),
                                            groupingTileId = "CONTAINER",
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
                                                    text = "Fui adicionado"
                                                ),
                                            ),
                                            position = AddTilesEventModel.InsertionPosition.End,
                                            trigger = EventTriggers.onMenuItemClick("ADD_TILE"),
                                            events = null
                                        ),
                                        ToggleMenuEventModel(
                                            id = randomUuid(),
                                            trigger = EventTriggers.onMenuItemClick("ADD_TILE"),
                                            menuId = "MENU",
                                            events = null
                                        )
                                    ),
                                    visibility = TileModel.Visibility.VISIBLE,
                                    style = StyleModel(
                                        size = SizeModel(
                                            width = SizeModel.Behavior.Horizontal.Wrap,
                                            height = SizeModel.Behavior.Vertical.Wrap
                                        )
                                    ),
                                    items = listOf(
                                        MenuTileModel.MenuItem(
                                            id = "ADD_TILE",
                                            label = "Add tile",
                                            leadingIcon = IconModel(
                                                name = "add",
                                                color = ColorModel.Theme(ColorModel.Theme.Color.PRIMARY)
                                            )
                                        )
                                    ),
                                    tiles = listOf(
                                        ButtonTileModel(
                                            id = randomUuid(),
                                            text = "Exibir menu",
                                            visibility = TileModel.Visibility.VISIBLE,
                                            loading = false,
                                            style = StyleModel(
                                                size = SizeModel(
                                                    width = SizeModel.Behavior.Horizontal.Wrap,
                                                    height = SizeModel.Behavior.Vertical.Wrap
                                                )
                                            ),
                                            events = listOf(
                                                ToggleMenuEventModel(
                                                    id = randomUuid(),
                                                    trigger = EventTriggers.onClick(),
                                                    menuId = "MENU",
                                                    events = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                ButtonTileModel(
                                    id = randomUuid(),
                                    text = "Abrir BottomSheet",
                                    visibility = TileModel.Visibility.VISIBLE,
                                    loading = false,
                                    style = StyleModel(
                                        size = SizeModel(
                                            width = SizeModel.Behavior.Horizontal.Wrap,
                                            height = SizeModel.Behavior.Vertical.Wrap
                                        )
                                    ),
                                    events = listOf(
                                        DisplayDialogEventModel(
                                            id = randomUuid(),
                                            trigger = EventTriggers.onClick(),
                                            events = null,
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
                                                    text = "Olá, sou uma linda BottomSheet"
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
                                                    events = null,
                                                    value = ""
                                                ),
                                                ButtonTileModel(
                                                    id = randomUuid(),
                                                    text = "Adicionar tile",
                                                    visibility = TileModel.Visibility.VISIBLE,
                                                    loading = false,
                                                    style = StyleModel(
                                                        size = SizeModel(
                                                            width = SizeModel.Behavior.Horizontal.Wrap,
                                                            height = SizeModel.Behavior.Vertical.Wrap
                                                        )
                                                    ),
                                                    events = listOf(
                                                        AddTilesEventModel(
                                                            id = randomUuid(),
                                                            groupingTileId = "CONTAINER",
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
                                                                    text = "Fui adicionado"
                                                                ),
                                                            ),
                                                            position = AddTilesEventModel.InsertionPosition.End,
                                                            trigger = EventTriggers.onClick(),
                                                            events = null
                                                        )
                                                    )
                                                ),
                                                ButtonTileModel(
                                                    id = randomUuid(),
                                                    text = "Fechar",
                                                    visibility = TileModel.Visibility.VISIBLE,
                                                    loading = false,
                                                    style = StyleModel(
                                                        size = SizeModel(
                                                            width = SizeModel.Behavior.Horizontal.Wrap,
                                                            height = SizeModel.Behavior.Vertical.Wrap
                                                        )
                                                    ),
                                                    events = listOf(
                                                        DismissDialogEventModel(
                                                            id = randomUuid(),
                                                            trigger = EventTriggers.onClick(),
                                                            events = null
                                                        )
                                                    )
                                                ),
                                            ),
                                            isCancellable = false,
                                            usePlatformDefaultWidth = true
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
                                    events = null,
                                    value = ""
                                ),
                                ButtonTileModel(
                                    id = randomUuid(),
                                    text = "Adicionar tile",
                                    visibility = TileModel.Visibility.VISIBLE,
                                    loading = false,
                                    style = StyleModel(
                                        size = SizeModel(
                                            width = SizeModel.Behavior.Horizontal.Wrap,
                                            height = SizeModel.Behavior.Vertical.Wrap
                                        )
                                    ),
                                    events = listOf(
                                        AddTilesEventModel(
                                            id = randomUuid(),
                                            groupingTileId = "CONTAINER",
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
                                                    text = "Fui adicionado"
                                                ),
                                            ),
                                            position = AddTilesEventModel.InsertionPosition.End,
                                            trigger = EventTriggers.onClick(),
                                            events = null
                                        )
                                    )
                                ),
                                ColumnTileModel(
                                    id = "CONTAINER",
                                    tiles = listOf(),
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
                                    isScrollable = false,
                                    lazyRender = false
                                )
                            ),
                            events = null,
                            style = StyleModel(
                                size = SizeModel(
                                    width = SizeModel.Behavior.Horizontal.Fill,
                                    height = SizeModel.Behavior.Vertical.Fill
                                ),
                                windowInsets = WindowInsetsModel.StatusBar
                            ),
                            visibility = TileModel.Visibility.VISIBLE,
                            arrangement = ArrangementModel.Vertical.Top,
                            alignment = AlignmentModel.Horizontal.Center,
                            isScrollable = true,
                            lazyRender = false
                        )
                    ),
                    navigationDrawerTiles = null,
                    events = null
                )

                else -> ScreenModel(
                    id = "second",
                    tiles = listOf(
                        ButtonTileModel(
                            id = randomUuid(),
                            text = "Voltar",
                            visibility = TileModel.Visibility.VISIBLE,
                            loading = false,
                            style = StyleModel(
                                size = SizeModel(
                                    width = SizeModel.Behavior.Horizontal.Wrap,
                                    height = SizeModel.Behavior.Vertical.Wrap
                                )
                            ),
                            events = listOf(
                                NavigateUpEventModel(
                                    id = randomUuid(),
                                    navigatorId = "root",
                                    trigger = EventTriggers.onClick(),
                                    events = null
                                )
                            )
                        )
                    ),
                    navigationDrawerTiles = null,
                    events = null
                )
            }
        )
    }

    data class Params(
        val screenId: String,
        val data: Map<String, Any>?
    )
}