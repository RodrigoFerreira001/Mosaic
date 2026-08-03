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
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignToBottomEnd
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.badges.Badge
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row

object BadgeTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Badge"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Badge",
                description = "Badge Material 3 — um pontinho ou uma pílula colorida com texto. content = null vira um ponto; com texto, vira pílula."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Não é interativo por si só e normalmente vive dentro de um Box junto de um ícone. " +
                    "Contadores de notificação, indicadores de não-lido, marcadores de status."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("content", "String?", "null = só o ponto; texto = pílula com esse texto."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Box(id = "notifBox") {
                    IconButton(icon = icon("notifications"), events = { Navigate(trigger = EventTriggers.onClick(), destination = "notifications") })
                    Badge(id = "notifBadge", content = "3")
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Alterne entre ponto e pílula com contagem real") {
                Box(style = { size(width = fixedHorizontally(56), height = fixedVertically(56)) }) {
                    IconButton(icon = icon("notifications"))
                    Box(style = { size(width = fixedHorizontally(56), height = fixedVertically(56)) }, alignment = alignToBottomEnd()) {
                        Badge(id = "badge_demo", content = null)
                    }
                }
                Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                    Button(
                        text = "Ponto",
                        events = {
                            UpdateTiles(trigger = EventTriggers.onClick(), updates = { update(tileId = "badge_demo", updateData = inlineTileUpdateData("content" to null)) })
                        }
                    )
                    Button(
                        text = "3",
                        events = {
                            UpdateTiles(trigger = EventTriggers.onClick(), updates = { update(tileId = "badge_demo", updateData = inlineTileUpdateData("content" to "3")) })
                        }
                    )
                    Button(
                        text = "99+",
                        events = {
                            UpdateTiles(trigger = EventTriggers.onClick(), updates = { update(tileId = "badge_demo", updateData = inlineTileUpdateData("content" to "99+")) })
                        }
                    )
                }
            }

            ShowroomRelated(
                names = listOf("Box", "Icon", "IconButton"),
                destination = "tileDetails"
            )
        }
    }
}
