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
import dev.catbit.mosaic.server.builder.event.builders.event.TriggerEvent
import dev.catbit.mosaic.server.builder.event.builders.event.UpdateEvents
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

object UpdateEventsEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "UpdateEvents"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        val targetEventId = randomId()

        ShowroomScaffold {
            ShowroomHero(
                category = "Tile Management",
                description = "Corrige o incomingData de um evento já registrado sem round-trip de rede — " +
                    "útil pra pré-popular um evento com dado de contexto antes de disparar."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use quando você precisa injetar dado de contexto num evento em tempo de execução — por " +
                    "exemplo, guardar o id do item selecionado no holder de um evento de exclusão quando o " +
                    "usuário toca num item da lista, antes de disparar esse evento de exclusão de fato. Um " +
                    "eventId desconhecido é ignorado silenciosamente; as atualizações são aplicadas na ordem " +
                    "da lista, sem rollback em caso de falha parcial."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("updates", "UpdateEventsUpdateBuilderScope.() -> Unit", "Obrigatório. Um update(eventId, data) por evento a corrigir."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                UpdateEvents(
                    trigger = EventTriggers.onClick(),
                    updates = {
                        update(
                            eventId = deleteEventId,
                            data = mapOf("itemId" to item.id)
                        )
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Pré-popule o incomingData de um evento e depois dispare-o") {
                SimpleText(
                    id = "update_events_target_text",
                    text = "(nenhum valor definido ainda)",
                    typography = typographyTitleMedium(),
                    // Evento "alvo" anexado a este próprio tile: trigger = inline() nunca dispara
                    // sozinho; só é executado quando TriggerEvent o invoca abaixo, usando o
                    // incomingData que UpdateEvents tiver escrito nele.
                    events = {
                        UpdateTiles(
                            id = targetEventId,
                            trigger = EventTriggers.inline(),
                            updates = {
                                update(
                                    tileId = "update_events_target_text",
                                    updateData = incomingTileUpdateData()
                                )
                            }
                        )
                    }
                )
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Definir \"Valor A\"",
                        buttonType = outlinedButton(),
                        events = {
                            UpdateEvents(
                                trigger = EventTriggers.onClick(),
                                updates = {
                                    update(
                                        eventId = targetEventId,
                                        data = mapOf("text" to "Valor A ✓")
                                    )
                                },
                                events = {
                                    TriggerEvent(trigger = EventTriggers.onSuccess(), eventId = targetEventId)
                                }
                            )
                        }
                    )
                    Button(
                        text = "Definir \"Valor B\"",
                        buttonType = outlinedButton(),
                        events = {
                            UpdateEvents(
                                trigger = EventTriggers.onClick(),
                                updates = {
                                    update(
                                        eventId = targetEventId,
                                        data = mapOf("text" to "Valor B ✓")
                                    )
                                },
                                events = {
                                    TriggerEvent(trigger = EventTriggers.onSuccess(), eventId = targetEventId)
                                }
                            )
                        }
                    )
                }
                ShowroomNote(
                    "O UpdateTiles \"alvo\" acima tem trigger = EventTriggers.inline() — ele nunca dispara " +
                        "sozinho. UpdateEvents grava um novo incomingData nele, e só então TriggerEvent o " +
                        "executa de fato, usando esse valor recém-gravado para atualizar o texto."
                )
            }

            ShowroomRelated(
                names = listOf("TriggerEvent", "RunEvents", "UpdateTiles"),
                destination = "eventDetails"
            )
        }
    }
}
