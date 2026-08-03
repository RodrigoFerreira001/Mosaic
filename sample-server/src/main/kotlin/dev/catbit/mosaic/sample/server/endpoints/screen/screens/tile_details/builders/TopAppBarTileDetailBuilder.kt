package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars.TopAppBarTileSchema
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParagraph
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParam
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParamsTable
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.app_bars.TopAppBar
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object TopAppBarTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "TopAppBar"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "App Bars",
                description = "Barra de topo Material 3 em 4 estilos: DEFAULT, CENTER_ALIGNED, MEDIUM (colapsável) e LARGE (colapsável)."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "title, navigationIcon e actions são slots independentes — não fazem parte da árvore " +
                    "normal de RenderChildren. É exatamente esse tile que monta o app bar desta própria " +
                    "tela de detalhe que você está lendo agora."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("title", "TileSchemaBuilderScope.() -> Unit", "Obrigatório. Qualquer tile, geralmente SimpleText."),
                    ShowroomParam("navigationIcon", "( -> Unit)?", "Ícone à esquerda, geralmente um back button."),
                    ShowroomParam("actions", "( -> Unit)?", "Tiles de ação à direita."),
                    ShowroomParam("barStyle", "TopAppBarStyle", "DEFAULT (padrão), CENTER_ALIGNED, MEDIUM, LARGE."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                TopAppBar(
                    id = "topBar",
                    barStyle = TopAppBarTileSchema.TopAppBarStyle.DEFAULT,
                    navigationIcon = {
                        IconButton(icon = icon("arrow_back"), events = { NavigateUp(trigger = EventTriggers.onClick()) })
                    },
                    actions = {
                        IconButton(icon = icon("settings"), events = { Navigate(trigger = EventTriggers.onClick(), destination = "settings") })
                    }
                ) {
                    SimpleText(text = "Environments", typography = typographyTitleLarge())
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Título + navigationIcon + action, cada um clicável de verdade") {
                TopAppBar(
                    barStyle = TopAppBarTileSchema.TopAppBarStyle.CENTER_ALIGNED,
                    style = { size(width = fillHorizontally()) },
                    navigationIcon = {
                        IconButton(
                            icon = icon("menu"),
                            events = { DisplaySnackbar(trigger = EventTriggers.onClick(), message = "navigationIcon clicado") }
                        )
                    },
                    actions = {
                        IconButton(
                            icon = icon("more_vert"),
                            events = { DisplaySnackbar(trigger = EventTriggers.onClick(), message = "action clicada") }
                        )
                    }
                ) {
                    SimpleText(text = "CENTER_ALIGNED")
                }
            }

            ShowroomRelated(
                names = listOf("BottomAppBar", "IconButton", "NavigationBar"),
                destination = "tileDetails"
            )
        }
    }
}
