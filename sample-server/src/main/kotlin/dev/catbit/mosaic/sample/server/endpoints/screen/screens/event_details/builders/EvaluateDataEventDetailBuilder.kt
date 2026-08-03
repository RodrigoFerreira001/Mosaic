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
import dev.catbit.mosaic.server.builder.color.themeColorOnErrorContainer
import dev.catbit.mosaic.server.builder.color.themeColorOnTertiaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorErrorContainer
import dev.catbit.mosaic.server.builder.color.themeColorTertiaryContainer
import dev.catbit.mosaic.server.builder.event.builders.data.EvaluateData
import dev.catbit.mosaic.server.builder.event.builders.data.incomingData
import dev.catbit.mosaic.server.builder.event.builders.data.matchesRegex
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.inputs.TextField
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object EvaluateDataEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "EvaluateData"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Data",
                description = "Avalia uma árvore de expressão booleana e ramifica a cadeia — onSuccess " +
                    "quando true, onFailure quando false ou em erro de avaliação."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use pra qualquer ramificação condicional: validação de formulário, feature flags, checagem " +
                    "de papel de usuário, checagem de nulo, regex, comparação numérica. incomingData() " +
                    "referencia o incomingData atual como sujeito da expressão; dataSourceData(fonte, modo) lê " +
                    "de um data store como sujeito. and/or fazem curto-circuito; incompatibilidade de tipo " +
                    "resolve como false (não lança exceção)."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("expression", "EvaluateDataEventSchema.Expression", "Obrigatório. Árvore de expressão booleana — veja os helpers abaixo."),
                )
            )
            ShowroomParagraph(
                "Principais operadores sobre um Data: .isNull(), .isNotNull(), .isEqualsTo(valor), " +
                    ".isBlank(), .matchesRegex(regex), .isBiggerThan()/.isSmallerThan() (numéricos), " +
                    ".containsKey(chave), .valueAtKey(chave).<operador>() para encadear um operador no valor " +
                    "de uma chave de mapa."
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                EvaluateData(
                    trigger = EventTriggers.onSuccess(),
                    expression = incomingData().valueAtKey("email").matchesRegex("^[\\w.+]+@[\\w]+\\.[a-z]{2,}$")
                        and incomingData().valueAtKey("password").isLengthBiggerThanOrEquals(8),
                    events = {
                        SendNetworkRequest(trigger = EventTriggers.onSuccess(), url = "/auth/login", method = HttpMethod.POST)
                        DisplaySnackbar(trigger = EventTriggers.onFailure(), message = "Invalid email or password too short")
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Digite um e-mail e veja a validação em tempo real") {
                TextField(
                    id = "evaluate_data_email_input",
                    placeholder = "seu@email.com",
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    events = {
                        EvaluateData(
                            trigger = EventTriggers.onTextChanged(),
                            expression = incomingData().matchesRegex("^[\\w.+-]+@[\\w-]+\\.[a-z]{2,}$"),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update(
                                            "evaluate_data_feedback",
                                            inlineTileUpdateData(
                                                "text" to "E-mail válido ✓",
                                                "color" to "#1B5E20"
                                            )
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update(
                                            "evaluate_data_feedback",
                                            inlineTileUpdateData(
                                                "text" to "Formato de e-mail inválido",
                                                "color" to "#B3261E"
                                            )
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
                SimpleText(
                    id = "evaluate_data_feedback",
                    text = "Digite algo acima",
                    typography = typographyBodyMedium()
                )
                ShowroomNote(
                    "incomingData() aqui referencia o texto atual do TextField (entregue como incomingData " +
                        "pelo trigger onTextChanged()), avaliado direto contra a regex — sem precisar de um " +
                        "GetData intermediário, já que o próprio trigger já carrega o valor."
                )
            }

            ShowroomRelated(
                names = listOf("TransformData", "GetData", "TextField"),
                destination = "eventDetails"
            )
        }
    }
}
