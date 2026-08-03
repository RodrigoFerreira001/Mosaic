package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParagraph
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParam
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParamsTable
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.inputs.TextField
import dev.catbit.mosaic.server.builder.tile.builders.inputs.outlinedTextField
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object TextFieldTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "TextField"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Inputs",
                description = "Campo de texto Material 3 (filled ou outlined) com decoração rica e configuração completa de teclado."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "value é totalmente controlado pelo servidor: a cada OnTextChanged, o servidor " +
                    "precisa reenviar value via UpdateTiles pra manter o texto exibido em sincronia — " +
                    "senão o campo simplesmente ignora o que o usuário digitou visualmente na próxima renderização."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("value", "String", "Padrão \"\". Texto atual, controlado pelo servidor."),
                    ShowroomParam("label / placeholder", "String?", "Rótulo flutuante e texto de apoio quando vazio."),
                    ShowroomParam("supportingText", "String?", "Texto auxiliar/erro abaixo do campo."),
                    ShowroomParam("kind", "Kind", "outlinedTextField() (padrão) ou filledTextField()."),
                    ShowroomParam("state", "State", "NORMAL (padrão) ou ERROR — ativa o estilo de erro do Material."),
                    ShowroomParam("keyboardOptions", "KeyboardOptions?", "Tipo de teclado, capitalização, ação do IME — via keyboardOptions(...)."),
                    ShowroomParam("maxLines / minLines", "Int", "Controle de altura do campo."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                TextField(
                    id = "email",
                    placeholder = "E-mail",
                    maxLines = 1,
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onTextChanged(),
                            updates = {
                                update(
                                    tileId = "email",
                                    updateData = inlineTileUpdateData(
                                        mapOf("state" to "NORMAL", "supportingText" to null)
                                    )
                                )
                            }
                        )
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Digite — o preview abaixo espelha o value em tempo real") {
                TextField(
                    id = "text_field_demo_input",
                    label = "Seu nome",
                    placeholder = "Digite aqui",
                    kind = outlinedTextField(),
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onTextChanged(),
                            updates = {
                                update(
                                    tileId = "text_field_demo_preview",
                                    updateData = mappedIncomingTileUpdateData("text" to "<//>")
                                )
                            }
                        )
                    }
                )
                SimpleText(
                    id = "text_field_demo_preview",
                    text = "O preview aparece aqui conforme você digita"
                )
            }

            ShowroomRelated(
                names = listOf("Checkbox", "DropdownList", "SearchBar"),
                destination = "tileDetails"
            )
        }
    }
}
