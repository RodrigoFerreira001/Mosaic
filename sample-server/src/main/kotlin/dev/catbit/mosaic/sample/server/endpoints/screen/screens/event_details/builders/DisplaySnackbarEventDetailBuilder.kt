package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.snackbar.SnackbarDurationSchema
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
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object DisplaySnackbarEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DisplaySnackbar"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Overlays",
                description = "Exibe um snackbar Material 3 com mensagem, ação opcional e duração " +
                    "configurável — feedback breve pra confirmações, erros e avisos de undo."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use pra mensagens curtas de feedback: confirmação de sucesso, aviso de erro, prompt de " +
                    "\"desfazer\". Com actionLabel definido, o snackbar ganha um botão de ação que dispara " +
                    "onSnackbarAction() quando tocado; sem action, ele só dispara onSnackbarDismissed() ao " +
                    "sumir (por timeout, swipe, ou dismiss programático)."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("message", "String", "Obrigatório. Texto do snackbar."),
                    ShowroomParam("duration", "SnackbarDurationSchema", "Short (padrão). Short, Long ou Indefinite."),
                    ShowroomParam("actionLabel", "String?", "null (padrão). Se definido, mostra um botão de ação."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                DisplaySnackbar(
                    trigger = EventTriggers.onFailure(),
                    message = "Something went wrong",
                    duration = SnackbarDurationSchema.Long,
                    actionLabel = "Retry",
                    events = {
                        SendNetworkRequest(trigger = EventTriggers.onSnackbarAction(), url = "/api/retry", method = HttpMethod.POST)
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Dispare snackbars com e sem ação") {
                SimpleText(
                    id = "display_snackbar_feedback",
                    text = "(nenhuma ação registrada ainda)"
                )
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Snackbar simples",
                        buttonType = outlinedButton(),
                        events = {
                            DisplaySnackbar(
                                trigger = EventTriggers.onClick(),
                                message = "Ação concluída com sucesso",
                                duration = SnackbarDurationSchema.Short
                            )
                        }
                    )
                    Button(
                        text = "Snackbar com ação \"Desfazer\"",
                        buttonType = outlinedButton(),
                        events = {
                            DisplaySnackbar(
                                trigger = EventTriggers.onClick(),
                                message = "Item removido",
                                duration = SnackbarDurationSchema.Long,
                                actionLabel = "Desfazer",
                                events = {
                                    UpdateTiles(
                                        trigger = EventTriggers.onSnackbarAction(),
                                        updates = {
                                            update(
                                                "display_snackbar_feedback",
                                                inlineTileUpdateData("text" to "onSnackbarAction() disparado — \"Desfazer\" foi tocado ✓")
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                }
                ShowroomNote(
                    "Toque em \"Desfazer\" dentro do próprio snackbar (não no botão que o abriu) pra ver o " +
                        "texto acima mudar — só onSnackbarAction() atualiza o tile; deixar o snackbar sumir " +
                        "sozinho dispara onSnackbarDismissed(), que este exemplo não trata."
                )
            }

            ShowroomRelated(
                names = listOf("DismissSnackbar", "DisplayDialog", "DisplayBottomSheet"),
                destination = "eventDetails"
            )
        }
    }
}
