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
import dev.catbit.mosaic.server.builder.event.builders.screen.ChangeScreenState
import dev.catbit.mosaic.server.builder.event.builders.screen.GetScreen
import dev.catbit.mosaic.server.builder.event.builders.screen.successState
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object GetScreenEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "GetScreen"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Screen",
                description = "Busca a definição de uma tela no servidor e expõe o resultado como " +
                    "incomingData — sem aplicá-la automaticamente à tela atual."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use quando você precisa inspecionar o ScreenModel buscado antes de decidir como aplicá-lo, " +
                    "ou quando quer controlar manualmente a transição de estado (por exemplo, mostrar um " +
                    "loading customizado enquanto busca). Para o caso simples de \"recarregar a tela\", " +
                    "prefira RefreshScreen, que já encapsula GetScreen + ChangeScreenState. É assim que toda " +
                    "entry deste app (HomeScreenBuilder, cada aba do rail) carrega seu conteúdo inicial: " +
                    "GetScreen no onDisplay, seguido de ChangeScreenState(successState()) no onSuccess."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("method", "HttpMethod", "GET (padrão)."),
                    ShowroomParam("body", "AnySerializable?", "Opcional. Corpo da requisição; o schema vence sobre o holder."),
                    ShowroomParam("headers", "Map<String, String>?", "Opcional. Mesclado com os headers do holder."),
                    ShowroomParam("timeoutMillis", "Long?", "Opcional. Override de timeout em ms; null usa o padrão global do cliente (20s)."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                // Padrão usado em toda entry deste app (ver Graph.entry's initialEvents padrão)
                GetScreen(
                    trigger = EventTriggers.onDisplay(),
                    events = {
                        ChangeScreenState(trigger = EventTriggers.onSuccess(), state = successState())
                        ChangeScreenState(trigger = EventTriggers.onFailure(), state = failureState())
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Busque a definição desta própria tela sem trocar de tela") {
                SimpleText(
                    id = "get_screen_status",
                    text = "Ainda não buscado",
                    typography = typographyBodyMedium()
                )
                Button(
                    text = "Buscar com GetScreen (sem navegar)",
                    buttonType = filledTonalButton(),
                    events = {
                        GetScreen(
                            trigger = EventTriggers.onClick(),
                            events = {
                                DisplaySnackbar(
                                    trigger = EventTriggers.onSuccess(),
                                    message = "ScreenModel recebido como incomingData — não aplicado, só demonstrado"
                                )
                                DisplaySnackbar(
                                    trigger = EventTriggers.onFailure(),
                                    message = "Falha ao buscar a tela"
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "Note que este GetScreen NÃO tem um ChangeScreenState(successState()) encadeado — por " +
                        "isso a tela não pisca/recarrega ao clicar. É exatamente essa a diferença prática " +
                        "para RefreshScreen: GetScreen sozinho só busca e entrega o dado; quem aplica é você."
                )
            }

            ShowroomRelated(
                names = listOf("RefreshScreen", "ChangeScreenState", "Navigate"),
                destination = "eventDetails"
            )
        }
    }
}
