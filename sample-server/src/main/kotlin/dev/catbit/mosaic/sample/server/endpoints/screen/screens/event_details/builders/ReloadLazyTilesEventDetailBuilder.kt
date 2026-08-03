package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

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
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainer
import dev.catbit.mosaic.server.builder.event.builders.tiles.ReloadLazyTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.LazyColumn
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyLabelSmall

object ReloadLazyTilesEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "ReloadLazyTiles"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Tile Management",
                description = "Sinaliza um LazyColumn/LazyRow para descartar o conteúdo e buscar tiles de novo, " +
                    "resetando a paginação — o reload correto para listas preguiçosas."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use depois de uma mutação de dado (criar/apagar/atualizar) que deveria fazer uma lista " +
                    "preguiçosa buscar tudo de novo desde o início. Prefira este evento a WipeTiles para " +
                    "listas do tipo LazyColumn/LazyRow porque ele deixa o próprio tile gerenciar seu ciclo de " +
                    "reload — incluindo o reset de paginação — em vez de apenas apagar os filhos visíveis."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("lazyTileId", "String", "Obrigatório. ID do tile preguiçoso (LazyColumn/LazyRow) a recarregar."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                ReloadLazyTiles(
                    trigger = EventTriggers.onSuccess(),
                    lazyTileId = "environment_list"
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Dispare o broadcast de reload numa lista preguiçosa") {
                SimpleText(
                    id = "reload_lazy_count",
                    text = "Nenhum reload disparado ainda",
                    typography = typographyBodyMedium()
                )
                LazyColumn(
                    id = "reload_lazy_list",
                    style = { size(width = fillHorizontally(), height = fixedVertically(160)) },
                    arrangement = arrangeVerticallySpacedBy(8)
                ) {
                    ReloadLazyRow(label = "Item preguiçoso 1")
                    ReloadLazyRow(label = "Item preguiçoso 2")
                    ReloadLazyRow(label = "Item preguiçoso 3")
                }
                Button(
                    text = "Recarregar com ReloadLazyTiles",
                    buttonType = filledTonalButton(),
                    events = {
                        ReloadLazyTiles(
                            trigger = EventTriggers.onClick(),
                            lazyTileId = "reload_lazy_list",
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update(
                                            tileId = "reload_lazy_count",
                                            updateData = inlineTileUpdateData("text" to "Broadcast de reload enviado ✓ (veja a nota abaixo)")
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "Nesta demo o LazyColumn tem filhos fixos declarados pelo servidor, não uma fonte de " +
                        "dados remota real (isso seria um LazyTiles, com seu próprio endpoint). O broadcast " +
                        "de ReloadLazyTiles é enviado de verdade, mas como não há nada novo para buscar, o " +
                        "efeito visível aqui é só o reset de scroll — em produção, com um LazyTiles apontando " +
                        "para um endpoint, o conteúdo inteiro seria descartado e buscado do zero."
                )
            }

            ShowroomRelated(
                names = listOf("LazyTiles", "WipeTiles", "ReplaceTiles"),
                destination = "eventDetails"
            )
        }
    }
}

private fun TileSchemaBuilderScope.ReloadLazyRow(label: String) {
    Row(
        style = {
            size(width = fillHorizontally(), height = wrapVertically())
            padding(horizontal = 12, vertical = 10)
            clip(roundedCornerShape(all = 12))
            background(color(themeColorSurfaceContainer()))
        },
        arrangement = arrangeHorizontallySpacedBy(8),
        alignment = alignVerticallyToCenter()
    ) {
        Icon(icon = icon("cloud_sync", size = 18, color = color(themeColorOnSurfaceVariant())))
        SimpleText(text = label, typography = typographyLabelSmall())
    }
}
