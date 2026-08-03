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
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.inputs.TextField
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

object TransformDataEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "TransformData"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Data",
                description = "Remodela o incomingData aplicando um template com placeholders <|caminho|>, " +
                    "resolvidos a partir do próprio incomingData."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use quando você precisa extrair ou reestruturar parte do incomingData antes de passá-lo " +
                    "adiante — extrair um único campo de um mapa de resposta de rede, ou montar um novo " +
                    "formato de mapa. <|caminho.para.valor|> navega por chaves em notação de ponto; " +
                    "<|items[0].nome|> acessa índices de lista; <||> sozinho preserva o incomingData inteiro " +
                    "com seu tipo nativo (Int, Boolean, List...), mas se o placeholder aparecer misturado com " +
                    "texto numa string, o valor é coagido para String."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("template", "AnySerializable", "String/Map/List com placeholders <|caminho|>."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                TransformData(
                    trigger = EventTriggers.onSuccess(),
                    template = mapOf("greeting" to "Hello, <|user.name|>!", "userId" to "<|user.id|>"),
                    events = {
                        UpdateTiles(trigger = EventTriggers.onSuccess(), updates = {
                            update("greeting_tile", incomingTileUpdateData())
                        })
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Digite seu nome e veja o template ser resolvido ao vivo") {
                TextField(
                    id = "transform_data_name_input",
                    placeholder = "Seu nome",
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    events = {
                        TransformData(
                            trigger = EventTriggers.onTextChanged(),
                            template = mapOf("greeting" to "Olá, <||>!"),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update(
                                            tileId = "transform_data_greeting",
                                            updateData = incomingTileUpdateData()
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
                SimpleText(
                    id = "transform_data_greeting",
                    text = "Olá, !",
                    typography = typographyTitleMedium()
                )
                ShowroomNote(
                    "Repare no template: mapOf(\"greeting\" to \"Olá, <||>!\") — aqui <||> está misturado com " +
                        "texto (\"Olá, \" + valor + \"!\"), então mesmo que o TextField entregasse algo que " +
                        "não fosse String, o resultado final seria coagido para texto antes de virar o novo " +
                        "valor do tile \"greeting\" via incomingTileUpdateData()."
                )
            }

            ShowroomRelated(
                names = listOf("EvaluateData", "GetData", "UpdateTiles"),
                destination = "eventDetails"
            )
        }
    }
}
