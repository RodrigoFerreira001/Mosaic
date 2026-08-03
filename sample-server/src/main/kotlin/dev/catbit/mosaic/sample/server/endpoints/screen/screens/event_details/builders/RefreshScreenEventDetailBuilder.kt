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
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.screen.RefreshScreen
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledButton

object RefreshScreenEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "RefreshScreen"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Screen",
                description = "Recarrega a tela atual do zero — reseta para Initial, busca a definição no " +
                    "servidor e aplica o resultado automaticamente, sem passos manuais."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use para o padrão simples de puxar-para-atualizar ou \"tentar novamente\", quando o objetivo " +
                    "é rebuscar e re-renderizar a tela inteira sem gerenciar transições de estado manualmente. " +
                    "A tela volta para Initial antes da requisição começar (mostrando o loading implícito), e " +
                    "GetScreen + ChangeScreenState já estão embutidos — diferente de GetScreen sozinho, que " +
                    "só entrega o dado sem aplicá-lo."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("method", "HttpMethod", "GET (padrão)."),
                    ShowroomParam("body", "AnySerializable?", "Opcional. Corpo da requisição."),
                    ShowroomParam("headers", "Map<String, String>?", "Opcional. Mesclado com os headers do holder."),
                    ShowroomParam("timeoutMillis", "Long?", "Opcional. Override de timeout em ms."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                RefreshScreen(
                    trigger = EventTriggers.onPull(),
                    events = {
                        StopRefreshing(trigger = EventTriggers.onSuccess(), tileId = "ptr_container")
                        StopRefreshing(trigger = EventTriggers.onFailure(), tileId = "ptr_container")
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Recarregue esta própria página com RefreshScreen") {
                ShowroomParagraph(
                    "Clicar no botão abaixo dispara um RefreshScreen real: a tela inteira volta a Initial " +
                        "(um piscar breve de loading), busca a definição no servidor de novo e a re-renderiza " +
                        "— exatamente como um pull-to-refresh faria, só que disparado por clique em vez de " +
                        "arrastar."
                )
                Button(
                    text = "Recarregar com RefreshScreen",
                    buttonType = filledButton(),
                    events = {
                        RefreshScreen(
                            trigger = EventTriggers.onClick(),
                            events = {
                                DisplaySnackbar(
                                    trigger = EventTriggers.onFailure(),
                                    message = "Falha ao recarregar a tela"
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "Não há um snackbar de sucesso aqui de propósito: quando RefreshScreen aplica a nova " +
                        "definição, a tela inteira é substituída — incluindo este próprio card de demo — " +
                        "então o \"resultado\" visível é a página inteira recarregando diante dos seus olhos."
                )
            }

            ShowroomRelated(
                names = listOf("GetScreen", "ChangeScreenState", "StopRefreshing"),
                destination = "eventDetails"
            )
        }
    }
}
