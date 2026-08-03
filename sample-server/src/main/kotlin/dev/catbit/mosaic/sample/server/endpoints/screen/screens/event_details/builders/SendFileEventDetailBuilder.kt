package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
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
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object SendFileEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "SendFile"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Networking",
                description = "Envia um arquivo como binário bruto (sem multipart) para uma URL — desenhado " +
                    "para o padrão de URL assinada, onde o arquivo vai direto pro storage (GCS/S3)."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use para upload de arquivos usando uma URL pré-assinada obtida do seu backend. O incomingData " +
                    "precisa ser um ByteArray (por exemplo, vindo do onDownloadFinish de um DownloadFile, ou " +
                    "das bytes capturadas por TakePicture/GetImageFromGallery) — incomingData nulo ou que não " +
                    "seja ByteArray dispara onFailure. O contentType deve bater com o content-type usado ao " +
                    "assinar a URL no backend."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("url", "String?", "null (padrão). schema ?? holder.url (definido por SetIncomingDataToNetworkParamsHolderUrl)."),
                    ShowroomParam("method", "HttpMethod", "Obrigatório. Normalmente PUT para URLs assinadas."),
                    ShowroomParam("headers", "Map<String, String>?", "Opcional."),
                    ShowroomParam("contentType", "String?", "Opcional, ex: \"image/jpeg\" — deve bater com o content-type usado ao assinar a URL."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                SendFile(
                    trigger = EventTriggers.onSuccess(), // incomingData = ByteArray, vindo de TakePicture ou GetImageFromGallery
                    url = null, // vem do NetworkParamsHolder, definido por SetIncomingDataToNetworkParamsHolderUrl
                    method = HttpMethod.PUT,
                    contentType = "image/jpeg"
                )
                """
            )

            ShowroomSectionTitle("Por que não há demo interativa aqui")
            SimpleText(
                text = "Um upload real exigiria uma URL assinada de verdade, apontando pra um bucket de " +
                    "storage real (GCS/S3) — não é algo que este sample-server, sem esse backend, consiga " +
                    "demonstrar de forma honesta. O código acima é o padrão real de uso: capture bytes com " +
                    "TakePicture/GetImageFromGallery, peça uma URL assinada ao seu backend com " +
                    "SendNetworkRequest, guarde-a com SetIncomingDataToNetworkParamsHolderUrl, e então dispare " +
                    "este SendFile.",
                typography = typographyBodyMedium(),
                color = color(themeColorOnSurfaceVariant())
            )

            ShowroomNote(
                "Diferente de SendNetworkRequest, o body aqui não é um Map/JSON — é sempre o ByteArray bruto " +
                    "do incomingData, enviado como o corpo exato da requisição, sem qualquer serialização ou " +
                    "envelope multipart."
            )

            ShowroomRelated(
                names = listOf("SetIncomingDataToNetworkParamsHolderUrl", "TakePicture", "GetImageFromGallery"),
                destination = "eventDetails"
            )
        }
    }
}
