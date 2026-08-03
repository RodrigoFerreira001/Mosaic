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
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object SetIncomingDataToNetworkParamsHolderUrlEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "SetIncomingDataToNetworkParamsHolderUrl"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Networking",
                description = "Guarda o incomingData (deve ser String) como URL da requisição no " +
                    "NetworkParametersHolder, disponível para SendFile quando seu url é null."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use pra alimentar um evento de upload (SendFile) com uma URL assinada gerada dinamicamente " +
                    "pelo seu backend — o padrão típico é: o servidor pede uma URL assinada (SendNetworkRequest), " +
                    "extrai a URL da resposta (TransformData), guarda como URL no holder " +
                    "(SetIncomingDataToNetworkParamsHolderUrl), e então dispara o SendFile de fato, que lê essa " +
                    "URL guardada em vez de ter uma fixa no schema. incomingData nulo ou que não seja String " +
                    "dispara onFailure."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("—", "—", "Nenhum além de trigger/events — o valor guardado é sempre o incomingData atual, que deve ser String."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                // incomingData = resposta do backend com {"uploadUrl": "https://storage.googleapis.com/..."}
                TransformData(trigger = EventTriggers.onSuccess(), template = "<|uploadUrl|>", events = {
                    SetIncomingDataToNetworkParamsHolderUrl(trigger = EventTriggers.onSuccess(), events = {
                        SendFile(trigger = EventTriggers.onSuccess(), method = HttpMethod.PUT, contentType = "video/mp4")
                    })
                })
                """
            )

            ShowroomSectionTitle("Por que não há demo interativa aqui")
            SimpleText(
                text = "Este evento só faz sentido dentro do padrão de \"URL assinada\": um backend real que " +
                    "gera uma URL temporária de upload direto pro storage (GCS/S3). Este sample-server não tem " +
                    "esse backend, e simular com um serviço de terceiros exigiria de fato subir um arquivo pra " +
                    "algum lugar — o que não é apropriado para uma demo de showroom. O código acima mostra o " +
                    "encadeamento real e correto; veja SendFile e SetIncomingDataToNetworkParamsHolderBody pra " +
                    "demos que rodam de verdade.",
                typography = typographyBodyMedium(),
                color = color(themeColorOnSurfaceVariant())
            )

            ShowroomNote(
                "Sem este evento, SendFile exige uma url fixa no próprio schema — útil só quando a URL de " +
                    "destino é conhecida em tempo de build, não gerada em tempo de execução por request."
            )

            ShowroomRelated(
                names = listOf("SendFile", "TransformData", "DownloadFile"),
                destination = "eventDetails"
            )
        }
    }
}
