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
import dev.catbit.mosaic.server.builder.event.builders.screen.ChangeScreenState
import dev.catbit.mosaic.server.builder.event.builders.screen.failureState
import dev.catbit.mosaic.server.builder.event.builders.screen.initialState
import dev.catbit.mosaic.server.builder.event.builders.screen.successState
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object ChangeScreenStateEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "ChangeScreenState"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Screen",
                description = "Transiciona a tela imediatamente para Success, Failure ou Initial, sem fazer " +
                    "nenhuma chamada de rede."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use para dirigir manualmente as transições de estado da tela: aplicar um ScreenModel já " +
                    "recebido de um GetScreen anterior, voltar a mostrar o loading (initialState()), ou " +
                    "forçar a tela de erro (failureState()) — o botão \"Tentar novamente\" da tela de falha " +
                    "deste próprio app usa initialState() para reiniciar o ciclo de carregamento."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("state", "State", "Obrigatório. successState(data?), failureState() ou initialState()."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                ChangeScreenState(trigger = EventTriggers.onClick(), state = initialState(), events = {
                    GetScreen(trigger = EventTriggers.onSuccess(), events = {
                        ChangeScreenState(trigger = EventTriggers.onSuccess(), state = successState())
                        ChangeScreenState(trigger = EventTriggers.onFailure(), state = failureState())
                    })
                })
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Force cada estado de tela manualmente") {
                SimpleText(
                    text = "Esta tela em si não vai mudar de estado (o eventDetails não tem tratamento " +
                        "visual de Failure/Initial próprio) — em vez disso, veja o resultado no console/log " +
                        "de eventos do cliente ao clicar em cada botão.",
                    typography = typographyBodyMedium(),
                    color = color(themeColorOnSurfaceVariant())
                )
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "successState()",
                        buttonType = outlinedButton(),
                        events = {
                            ChangeScreenState(trigger = EventTriggers.onClick(), state = successState())
                        }
                    )
                    Button(
                        text = "failureState()",
                        buttonType = outlinedButton(),
                        events = {
                            ChangeScreenState(trigger = EventTriggers.onClick(), state = failureState())
                        }
                    )
                    Button(
                        text = "initialState()",
                        buttonType = outlinedButton(),
                        events = {
                            ChangeScreenState(trigger = EventTriggers.onClick(), state = initialState())
                        }
                    )
                }
                ShowroomNote(
                    "successState(data = null) tenta usar o incomingData atual (aqui, null vindo de " +
                        "onClick()) como o novo ScreenModel da tela — por isso o botão successState() não " +
                        "muda nada visível: não há um ScreenModel de verdade para aplicar nesta demo. Em " +
                        "produção, successState() normalmente vem encadeado depois de um GetScreen(onSuccess()), " +
                        "cujo incomingData É o ScreenModel esperado."
                )
            }

            ShowroomRelated(
                names = listOf("GetScreen", "RefreshScreen"),
                destination = "eventDetails"
            )
        }
    }
}
