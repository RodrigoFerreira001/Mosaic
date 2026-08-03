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
import dev.catbit.mosaic.server.builder.event.builders.file.DeleteFile
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object DeleteFileEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DeleteFile"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "File System",
                description = "Apaga um arquivo armazenado localmente, identificado pelo nome — a contraparte " +
                    "de limpeza de SaveFile."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use pra limpar arquivos locais depois de um upload bem-sucedido, ou quando o cache do app é " +
                    "limpo manualmente. Apagar um arquivo inexistente dispara onFailure."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("fileName", "String", "Obrigatório. Nome do arquivo a apagar no armazenamento privado do app."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                DeleteFile(
                    trigger = EventTriggers.onSuccess(),
                    fileName = "avatar.jpg",
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Apague o arquivo salvo pelas demos de TakePicture/GetImageFromGallery/SaveFile") {
                SimpleText(
                    id = "delete_file_status",
                    text = "Toque no botão para tentar apagar mosaic_demo_photo.webp."
                )
                Button(
                    text = "Apagar mosaic_demo_photo.webp",
                    buttonType = outlinedButton(),
                    events = {
                        DeleteFile(
                            trigger = EventTriggers.onClick(),
                            fileName = "mosaic_demo_photo.webp",
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update("delete_file_status", inlineTileUpdateData("text" to "Apagado com sucesso ✓"))
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update(
                                            "delete_file_status",
                                            inlineTileUpdateData("text" to "Falha ao apagar — o arquivo já não existia")
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "Depois de apagar, tente o evento GetFile pra confirmar: a leitura vai falhar porque o " +
                        "arquivo não existe mais."
                )
            }

            ShowroomRelated(
                names = listOf("SaveFile", "GetFile"),
                destination = "eventDetails"
            )
        }
    }
}
