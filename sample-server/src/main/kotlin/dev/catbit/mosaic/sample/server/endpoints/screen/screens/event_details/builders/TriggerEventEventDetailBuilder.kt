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
import dev.catbit.mosaic.server.builder.event.builders.event.TriggerEvent
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row

object TriggerEventEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "TriggerEvent"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Event Flow",
                description = "Salta para outro event por eventId, em qualquer ponto da mesma árvore de events da tile — permite reaproveitar uma \"sub-rotina\" a partir de múltiplos triggers."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "TriggerEvent não carrega lógica própria: ele apenas dispara o event identificado por " +
                    "eventId, como se o trigger original desse event tivesse disparado agora. É útil para " +
                    "centralizar uma sequência (ex.: \"salvar formulário\") e chamá-la a partir de vários " +
                    "botões diferentes sem duplicar a árvore de events."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("eventId", "String", "Obrigatório. id do event alvo — precisa existir na mesma árvore de events da tile."),
                    ShowroomParam("trigger", "EventTrigger", "Obrigatório. Quando este TriggerEvent deve disparar."),
                    ShowroomParam("events", "EventSchemaBuilderScope.() -> Unit", "Opcional. Events filhos encadeados após o disparo — não substituem os events do alvo."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Column(id = "root") {
                    events = {
                        // "sub-rotina" reutilizável
                        DisplaySnackbar(
                            id = "notify_saved",
                            trigger = EventTriggers.inline(),
                            message = "Salvo com sucesso!"
                        )
                    }
                }

                Button(
                    text = "Salvar",
                    events = {
                        TriggerEvent(
                            trigger = EventTriggers.onClick(),
                            eventId = "notify_saved"
                        )
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Dois botões chamando o mesmo event por eventId") {
                Column(
                    id = "trigger_event_demo_root",
                    events = {
                        // "sub-rotina" registrada uma única vez, chamada pelos dois botões abaixo
                        DisplaySnackbar(
                            id = "trigger_event_subroutine",
                            trigger = EventTriggers.inline(),
                            message = "Sub-rotina \"trigger_event_subroutine\" executada!"
                        )
                    }
                ) {
                    Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                        Button(
                            text = "Botão A → dispara subrotina",
                            events = {
                                TriggerEvent(
                                    trigger = EventTriggers.onClick(),
                                    eventId = "trigger_event_subroutine"
                                )
                            }
                        )
                        Button(
                            text = "Botão B → dispara a mesma subrotina",
                            buttonType = outlinedButton(),
                            events = {
                                TriggerEvent(
                                    trigger = EventTriggers.onClick(),
                                    eventId = "trigger_event_subroutine"
                                )
                            }
                        )
                    }
                }
            }

            ShowroomNote(
                "O event alvo (eventId) precisa estar registrado na mesma árvore de events da tile — " +
                    "não é possível apontar para um event de outra tile ou outra screen."
            )

            ShowroomRelated(
                names = listOf("RunEvents", "RunCancellableEvents", "CancelEvents"),
                destination = "eventDetails"
            )
        }
    }
}
