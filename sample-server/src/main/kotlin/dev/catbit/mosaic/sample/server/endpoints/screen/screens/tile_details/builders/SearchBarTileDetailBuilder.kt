package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
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
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.search.SearchBar
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object SearchBarTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "SearchBar"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Search",
                description = "Barra de busca Material 3 com placeholder, ícone opcional e botão de limpar animado embutido."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Altura fixa de 56dp. query é controlado pelo servidor — é preciso reenviar via " +
                    "UpdateTiles a cada onQueryChanged pra manter sincronizado. trailingIcon existe no " +
                    "schema mas não é renderizado pelo renderer atual."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("query", "String", "Padrão \"\". Controlado pelo servidor."),
                    ShowroomParam("placeholder", "String?", "Opcional."),
                    ShowroomParam("leadingIcon", "( -> Unit)?", "Tile renderizado como ícone à esquerda."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                SearchBar(
                    id = "searchBar",
                    placeholder = "Buscar ambientes...",
                    leadingIcon = { Icon(icon = icon("search")) },
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onQueryChanged(),
                            updates = {
                                update(tileId = "searchBar", updateData = incomingTileUpdateData())
                                update(tileId = "environmentList", updateData = incomingTileUpdateData())
                            }
                        )
                        UpdateTiles(
                            trigger = EventTriggers.onQueryCleared(),
                            updates = { update(tileId = "searchBar", updateData = inlineTileUpdateData("query" to "")) }
                        )
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Digite — o eco abaixo reflete onQueryChanged de verdade") {
                SearchBar(
                    id = "search_bar_demo",
                    placeholder = "Buscar algo...",
                    leadingIcon = { Icon(icon = icon("search")) },
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onQueryChanged(),
                            updates = {
                                update(tileId = "search_bar_demo", updateData = incomingTileUpdateData())
                            }
                        )
                        UpdateTiles(
                            trigger = EventTriggers.onQueryCleared(),
                            updates = {
                                update(tileId = "search_bar_demo", updateData = inlineTileUpdateData("query" to ""))
                            }
                        )
                    }
                )
                SimpleText(text = "OnSearch dispara ao apertar a tecla de busca do teclado")
            }

            ShowroomRelated(
                names = listOf("TextField", "SuggestionChip", "Column"),
                destination = "tileDetails"
            )
        }
    }
}
