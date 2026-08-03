package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.extensions.randomId
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
import dev.catbit.mosaic.server.builder.event.builders.event.CancelEvents
import dev.catbit.mosaic.server.builder.event.builders.event.RunCancellableEvents
import dev.catbit.mosaic.server.builder.event.builders.time.StartTimeLoop
import dev.catbit.mosaic.server.builder.event.builders.time.seconds
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object CancelEventsEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "CancelEvents"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        val cancellableId = randomId()

        ShowroomScaffold {
            ShowroomHero(
                category = "Events / Meta",
                description = "Interrompe a execução de uma cadeia de eventos cancelável, identificada pelo " +
                    "mesmo cancellableEventId usado ao iniciá-la com RunCancellableEvents."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use pra parar um processo de longa duração antes que ele termine sozinho — por exemplo, um " +
                    "StartTimeLoop rodando dentro de um RunCancellableEvents, cancelado quando o usuário sai " +
                    "da tela ou toca em \"parar\". CancelEvents sempre dispara onSuccess() imediatamente, " +
                    "independente de haver ou não uma cadeia com aquele id rodando no momento."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("cancellableEventId", "String", "Obrigatório. Mesmo id passado ao RunCancellableEvents que iniciou a cadeia a cancelar."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                val loopId = randomId()

                RunCancellableEvents(
                    trigger = EventTriggers.onClick(),
                    cancellableEventId = loopId,
                    events = {
                        StartTimeLoop(trigger = EventTriggers.inline(), timeData = seconds(1), events = {
                            UpdateTiles(trigger = EventTriggers.onTimeLoop(), updates = { /* ... */ })
                        })
                    }
                )

                CancelEvents(trigger = EventTriggers.onClick(), cancellableEventId = loopId)
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Inicie um loop cancelável e interrompa-o") {
                SimpleText(
                    id = "cancel_events_status",
                    text = "Parado",
                    typography = typographyBodyMedium()
                )
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Iniciar loop cancelável",
                        events = {
                            RunCancellableEvents(
                                trigger = EventTriggers.onClick(),
                                cancellableEventId = cancellableId,
                                events = {
                                    UpdateTiles(
                                        trigger = EventTriggers.inline(),
                                        updates = {
                                            update(
                                                "cancel_events_status",
                                                inlineTileUpdateData("text" to "Rodando...")
                                            )
                                        }
                                    )
                                    StartTimeLoop(
                                        trigger = EventTriggers.inline(),
                                        timeData = seconds(1),
                                        events = {
                                            UpdateTiles(
                                                trigger = EventTriggers.onTimeLoop(),
                                                updates = {
                                                    update(
                                                        "cancel_events_status",
                                                        inlineTileUpdateData("text" to "Tique recebido — ainda rodando")
                                                    )
                                                }
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                    Button(
                        text = "Cancelar com CancelEvents",
                        buttonType = outlinedButton(),
                        events = {
                            CancelEvents(
                                trigger = EventTriggers.onClick(),
                                cancellableEventId = cancellableId,
                                events = {
                                    UpdateTiles(
                                        trigger = EventTriggers.onSuccess(),
                                        updates = {
                                            update(
                                                "cancel_events_status",
                                                inlineTileUpdateData("text" to "Cancelado ✓")
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                }
                ShowroomNote(
                    "Depois de cancelar, o texto para de mudar mesmo que um segundo se passe — o " +
                        "StartTimeLoop de dentro do RunCancellableEvents foi interrompido de verdade, não só " +
                        "\"pausado visualmente\"."
                )
            }

            ShowroomRelated(
                names = listOf("RunCancellableEvents", "StartTimeLoop", "TriggerEvent"),
                destination = "eventDetails"
            )
        }
    }
}
