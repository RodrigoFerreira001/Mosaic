package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParagraph
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParam
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParamsTable
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.FloatingActionButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.defaultFloatingActionButon
import dev.catbit.mosaic.server.builder.tile.builders.buttons.largeFloatingActionButon
import dev.catbit.mosaic.server.builder.tile.builders.buttons.mediumFloatingActionButon
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row

object FloatingActionButtonTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "FloatingActionButton"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Buttons",
                description = "FAB Material 3 exibindo um único ícone, em três tamanhos — a ação primária e mais persistente de uma tela."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use para a ação de maior destaque da tela (ex: compor, adicionar, criar), que deve " +
                    "ficar acessível mesmo com a tela rolando. Diferente do Button, o FAB só tem ícone " +
                    "— sem rótulo de texto."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("icon", "IconSchema", "Obrigatório."),
                    ShowroomParam("size", "Size", "defaultFloatingActionButon() (padrão), mediumFloatingActionButon(), largeFloatingActionButon()."),
                    ShowroomParam("enabled", "Boolean", "Padrão true. Nota: não chega ao Compose — o FAB renderizado sempre é clicável."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                FloatingActionButton(
                    id = "addFab",
                    icon = icon("add"),
                    size = mediumFloatingActionButon(),
                    events = {
                        Navigate(trigger = EventTriggers.onClick(), screenId = "create_item")
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Os 3 tamanhos, um DisplaySnackbar por clique") {
                Row(arrangement = arrangeHorizontallySpacedBy(16), alignment = alignVerticallyToCenter()) {
                    FloatingActionButton(
                        icon = icon("add"),
                        size = defaultFloatingActionButon(),
                        events = {
                            DisplaySnackbar(trigger = EventTriggers.onClick(), message = "defaultFloatingActionButon()")
                        }
                    )
                    FloatingActionButton(
                        icon = icon("edit"),
                        size = mediumFloatingActionButon(),
                        events = {
                            DisplaySnackbar(trigger = EventTriggers.onClick(), message = "mediumFloatingActionButon()")
                        }
                    )
                    FloatingActionButton(
                        icon = icon("create"),
                        size = largeFloatingActionButon(),
                        events = {
                            DisplaySnackbar(trigger = EventTriggers.onClick(), message = "largeFloatingActionButon()")
                        }
                    )
                }
            }

            ShowroomNote("enabled = false não desabilita o FAB no Compose atual — trate isso na lógica de eventos do servidor, não como garantia visual.")

            ShowroomRelated(
                names = listOf("IconButton", "Button", "BottomAppBar"),
                destination = "tileDetails"
            )
        }
    }
}
