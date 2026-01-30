package dev.catbit.mosaic.client.domain.screen

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.responses.screen.ScreenResponse
import dev.catbit.mosaic.core.data.schemas.event.events.menu.ToggleMenuEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateUpEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog.DismissDialogEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog.DisplayDialogEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.AddTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.ArrangementSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.MarginSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.SizeSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.WindowInsetsSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.ButtonTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.ColumnTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TextFieldTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.menu.MenuTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.text.TextTileSchema
import dev.catbit.mosaic.core.domain.base.UseCase
import dev.catbit.mosaic.core.extensions.randomUuid

class GetScreenUseCase : UseCase<ScreenResponse, GetScreenUseCase.Params>() {

    override suspend fun execute(params: Params): Result<ScreenResponse> {
        return Result.success(
            when (params.screenId) {
                "home" -> ScreenResponse(
                    id = "home",
                    tiles = listOf(
                        ColumnTileSchema(
                            id = "COLUNA",
                            tiles = listOf(
                                TextTileSchema(
                                    id = randomUuid(),
                                    events = null,
                                    style = StyleSchema(
                                        size = SizeSchema(
                                            width = SizeSchema.Behavior.Horizontal.Fill,
                                            height = SizeSchema.Behavior.Vertical.Wrap
                                        ),
                                        margin = MarginSchema(
                                            start = 24,
                                            end = 24,
                                            top = 0,
                                            bottom = 0
                                        )
                                    ),
                                    visibility = TileSchema.Visibility.VISIBLE,
                                    text = "Bem vindo à tela ${params.screenId}"
                                ),
                                ButtonTileSchema(
                                    id = randomUuid(),
                                    text = "Ir para segunda tela",
                                    visibility = TileSchema.Visibility.VISIBLE,
                                    loading = false,
                                    style = StyleSchema(
                                        size = SizeSchema(
                                            width = SizeSchema.Behavior.Horizontal.Wrap,
                                            height = SizeSchema.Behavior.Vertical.Wrap
                                        )
                                    ),
                                    events = listOf(
                                        NavigateEventSchema(
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
                                MenuTileSchema(
                                    id = "MENU",
                                    expanded = false,
                                    events = listOf(
                                        AddTilesEventSchema(
                                            id = randomUuid(),
                                            groupingTileId = "CONTAINER",
                                            tiles = listOf(
                                                TextTileSchema(
                                                    id = randomUuid(),
                                                    events = null,
                                                    style = StyleSchema(
                                                        size = SizeSchema(
                                                            width = SizeSchema.Behavior.Horizontal.Fill,
                                                            height = SizeSchema.Behavior.Vertical.Wrap
                                                        ),
                                                        margin = MarginSchema(
                                                            start = 24,
                                                            end = 24,
                                                            top = 0,
                                                            bottom = 0
                                                        )
                                                    ),
                                                    visibility = TileSchema.Visibility.VISIBLE,
                                                    text = "Fui adicionado"
                                                ),
                                            ),
                                            position = AddTilesEventSchema.InsertionPosition.End,
                                            trigger = EventTriggers.onMenuItemClick("ADD_TILE"),
                                            events = null
                                        ),
                                        ToggleMenuEventSchema(
                                            id = randomUuid(),
                                            trigger = EventTriggers.onMenuItemClick("ADD_TILE"),
                                            menuId = "MENU",
                                            events = null
                                        )
                                    ),
                                    visibility = TileSchema.Visibility.VISIBLE,
                                    style = StyleSchema(
                                        size = SizeSchema(
                                            width = SizeSchema.Behavior.Horizontal.Wrap,
                                            height = SizeSchema.Behavior.Vertical.Wrap
                                        )
                                    ),
                                    items = listOf(
                                        MenuTileSchema.MenuItem(
                                            id = "ADD_TILE",
                                            label = "Add tile",
                                            leadingIcon = IconSchema(
                                                name = "add",
                                                color = ColorSchema.Theme(ColorSchema.Theme.Color.PRIMARY)
                                            )
                                        )
                                    ),
                                    tiles = listOf(
                                        ButtonTileSchema(
                                            id = randomUuid(),
                                            text = "Exibir menu",
                                            visibility = TileSchema.Visibility.VISIBLE,
                                            loading = false,
                                            style = StyleSchema(
                                                size = SizeSchema(
                                                    width = SizeSchema.Behavior.Horizontal.Wrap,
                                                    height = SizeSchema.Behavior.Vertical.Wrap
                                                )
                                            ),
                                            events = listOf(
                                                ToggleMenuEventSchema(
                                                    id = randomUuid(),
                                                    trigger = EventTriggers.onClick(),
                                                    menuId = "MENU",
                                                    events = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                ButtonTileSchema(
                                    id = randomUuid(),
                                    text = "Abrir BottomSheet",
                                    visibility = TileSchema.Visibility.VISIBLE,
                                    loading = false,
                                    style = StyleSchema(
                                        size = SizeSchema(
                                            width = SizeSchema.Behavior.Horizontal.Wrap,
                                            height = SizeSchema.Behavior.Vertical.Wrap
                                        )
                                    ),
                                    events = listOf(
                                        DisplayDialogEventSchema(
                                            id = randomUuid(),
                                            trigger = EventTriggers.onClick(),
                                            events = null,
                                            tiles = listOf(
                                                TextTileSchema(
                                                    id = randomUuid(),
                                                    events = null,
                                                    style = StyleSchema(
                                                        size = SizeSchema(
                                                            width = SizeSchema.Behavior.Horizontal.Fill,
                                                            height = SizeSchema.Behavior.Vertical.Wrap
                                                        ),
                                                        margin = MarginSchema(
                                                            start = 24,
                                                            end = 24,
                                                            top = 0,
                                                            bottom = 0
                                                        )
                                                    ),
                                                    visibility = TileSchema.Visibility.VISIBLE,
                                                    text = "Olá, sou uma linda BottomSheet"
                                                ),
                                                TextFieldTileSchema(
                                                    id = randomUuid(),
                                                    visibility = TileSchema.Visibility.VISIBLE,
                                                    style = StyleSchema(
                                                        size = SizeSchema(
                                                            width = SizeSchema.Behavior.Horizontal.Fill,
                                                            height = SizeSchema.Behavior.Vertical.Wrap
                                                        ),
                                                        margin = MarginSchema(
                                                            start = 24,
                                                            end = 24,
                                                            top = 0,
                                                            bottom = 0
                                                        )
                                                    ),
                                                    events = null,
                                                    value = ""
                                                ),
                                                ButtonTileSchema(
                                                    id = randomUuid(),
                                                    text = "Adicionar tile",
                                                    visibility = TileSchema.Visibility.VISIBLE,
                                                    loading = false,
                                                    style = StyleSchema(
                                                        size = SizeSchema(
                                                            width = SizeSchema.Behavior.Horizontal.Wrap,
                                                            height = SizeSchema.Behavior.Vertical.Wrap
                                                        )
                                                    ),
                                                    events = listOf(
                                                        AddTilesEventSchema(
                                                            id = randomUuid(),
                                                            groupingTileId = "CONTAINER",
                                                            tiles = listOf(
                                                                TextTileSchema(
                                                                    id = randomUuid(),
                                                                    events = null,
                                                                    style = StyleSchema(
                                                                        size = SizeSchema(
                                                                            width = SizeSchema.Behavior.Horizontal.Fill,
                                                                            height = SizeSchema.Behavior.Vertical.Wrap
                                                                        ),
                                                                        margin = MarginSchema(
                                                                            start = 24,
                                                                            end = 24,
                                                                            top = 0,
                                                                            bottom = 0
                                                                        )
                                                                    ),
                                                                    visibility = TileSchema.Visibility.VISIBLE,
                                                                    text = "Fui adicionado"
                                                                ),
                                                            ),
                                                            position = AddTilesEventSchema.InsertionPosition.End,
                                                            trigger = EventTriggers.onClick(),
                                                            events = null
                                                        )
                                                    )
                                                ),
                                                ButtonTileSchema(
                                                    id = randomUuid(),
                                                    text = "Fechar",
                                                    visibility = TileSchema.Visibility.VISIBLE,
                                                    loading = false,
                                                    style = StyleSchema(
                                                        size = SizeSchema(
                                                            width = SizeSchema.Behavior.Horizontal.Wrap,
                                                            height = SizeSchema.Behavior.Vertical.Wrap
                                                        )
                                                    ),
                                                    events = listOf(
                                                        DismissDialogEventSchema(
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
                                TextFieldTileSchema(
                                    id = randomUuid(),
                                    visibility = TileSchema.Visibility.VISIBLE,
                                    style = StyleSchema(
                                        size = SizeSchema(
                                            width = SizeSchema.Behavior.Horizontal.Fill,
                                            height = SizeSchema.Behavior.Vertical.Wrap
                                        ),
                                        margin = MarginSchema(
                                            start = 24,
                                            end = 24,
                                            top = 0,
                                            bottom = 0
                                        )
                                    ),
                                    events = null,
                                    value = ""
                                ),
                                ButtonTileSchema(
                                    id = randomUuid(),
                                    text = "Adicionar tile",
                                    visibility = TileSchema.Visibility.VISIBLE,
                                    loading = false,
                                    style = StyleSchema(
                                        size = SizeSchema(
                                            width = SizeSchema.Behavior.Horizontal.Wrap,
                                            height = SizeSchema.Behavior.Vertical.Wrap
                                        )
                                    ),
                                    events = listOf(
                                        AddTilesEventSchema(
                                            id = randomUuid(),
                                            groupingTileId = "CONTAINER",
                                            tiles = listOf(
                                                TextTileSchema(
                                                    id = randomUuid(),
                                                    events = null,
                                                    style = StyleSchema(
                                                        size = SizeSchema(
                                                            width = SizeSchema.Behavior.Horizontal.Fill,
                                                            height = SizeSchema.Behavior.Vertical.Wrap
                                                        ),
                                                        margin = MarginSchema(
                                                            start = 24,
                                                            end = 24,
                                                            top = 0,
                                                            bottom = 0
                                                        )
                                                    ),
                                                    visibility = TileSchema.Visibility.VISIBLE,
                                                    text = "Fui adicionado"
                                                ),
                                            ),
                                            position = AddTilesEventSchema.InsertionPosition.End,
                                            trigger = EventTriggers.onClick(),
                                            events = null
                                        )
                                    )
                                ),
                                ColumnTileSchema(
                                    id = "CONTAINER",
                                    tiles = listOf(),
                                    events = null,
                                    style = StyleSchema(
                                        size = SizeSchema(
                                            width = SizeSchema.Behavior.Horizontal.Fill,
                                            height = SizeSchema.Behavior.Vertical.Fill
                                        )
                                    ),
                                    visibility = TileSchema.Visibility.VISIBLE,
                                    arrangement = ArrangementSchema.Vertical.Top,
                                    alignment = AlignmentSchema.Horizontal.Center,
                                    isScrollable = false,
                                    lazyRender = false
                                )
                            ),
                            events = null,
                            style = StyleSchema(
                                size = SizeSchema(
                                    width = SizeSchema.Behavior.Horizontal.Fill,
                                    height = SizeSchema.Behavior.Vertical.Fill
                                ),
                                windowInsets = WindowInsetsSchema.StatusBar
                            ),
                            visibility = TileSchema.Visibility.VISIBLE,
                            arrangement = ArrangementSchema.Vertical.Top,
                            alignment = AlignmentSchema.Horizontal.Center,
                            isScrollable = true,
                            lazyRender = false
                        )
                    ),
                    navigationDrawerTiles = null,
                    events = null
                )

                else -> ScreenResponse(
                    id = "second",
                    tiles = listOf(
                        ButtonTileSchema(
                            id = randomUuid(),
                            text = "Voltar",
                            visibility = TileSchema.Visibility.VISIBLE,
                            loading = false,
                            style = StyleSchema(
                                size = SizeSchema(
                                    width = SizeSchema.Behavior.Horizontal.Wrap,
                                    height = SizeSchema.Behavior.Vertical.Wrap
                                )
                            ),
                            events = listOf(
                                NavigateUpEventSchema(
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