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
import dev.catbit.mosaic.server.builder.event.builders.event.RunEvents
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledButton
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object RunEventsEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "RunEvents"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Tile Management",
                description = "Executa todos os eventos filhos incondicionalmente, agrupando uma cadeia sob um " +
                    "único trigger — sem o encadeamento semântico onSuccess/onFailure entre eles."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use quando vários eventos independentes precisam disparar a partir do mesmo trigger e o " +
                    "encadeamento onSuccess/onFailure entre eles seria enganoso — por exemplo, atualizar um " +
                    "tile, gravar um dado e mostrar um snackbar, todos como reação direta a um clique, sem " +
                    "que um dependa do resultado do outro. RunEvents não consome nem transforma o incomingData: " +
                    "ele passa adiante, intacto, para cada evento filho."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("—", "—", "RunEvents não recebe parâmetros além de trigger/events."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                RunEvents(
                    trigger = EventTriggers.onClick(),
                    events = {
                        UpdateTiles(trigger = EventTriggers.inline(), updates = { /* ... */ })
                        UpdateData(trigger = EventTriggers.inline(), updates = { /* ... */ })
                        DisplaySnackbar(trigger = EventTriggers.inline(), message = "Saved")
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Clique e veja os 2 eventos disparando juntos") {
                SimpleText(
                    id = "run_events_status",
                    text = "Aguardando clique...",
                    typography = typographyBodyMedium()
                )
                Button(
                    text = "Disparar events com RunEvents",
                    buttonType = filledButton(),
                    events = {
                        RunEvents(
                            trigger = EventTriggers.onClick(),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.inline(),
                                    updates = {
                                        update(
                                            tileId = "run_events_status",
                                            updateData = inlineTileUpdateData("text" to "Tile atualizado ✓")
                                        )
                                    }
                                )
                                DisplaySnackbar(
                                    trigger = EventTriggers.inline(),
                                    message = "Snackbar disparado junto — sem esperar o UpdateTiles acima"
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "Repare que o UpdateTiles e o DisplaySnackbar usam EventTriggers.inline() — nenhum dos " +
                        "dois depende do sucesso do outro. É isso que diferencia RunEvents de encadear " +
                        "eventos via onSuccess/onFailure."
                )
            }

            ShowroomRelated(
                names = listOf("TriggerEvent", "UpdateEvents", "AddTiles"),
                destination = "eventDetails"
            )
        }
    }
}
