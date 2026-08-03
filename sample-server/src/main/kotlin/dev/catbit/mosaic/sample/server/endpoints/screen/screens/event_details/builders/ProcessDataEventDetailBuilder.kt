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

object ProcessDataEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "ProcessData"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Data",
                description = "Delega o incomingData a um DataProcessor registrado no cliente, identificado " +
                    "por processWith — abre a porta pra lógica nativa que não cabe na DSL."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use quando o servidor precisa pedir um processamento que só existe no cliente e não pode " +
                    "ser expresso pela DSL de eventos — criptografar um valor antes de persistir localmente, " +
                    "aplicar uma transformação específica da plataforma, ou acionar uma capacidade nativa " +
                    "registrada sob um id (processWith). O evento em si não faz nada por conta própria: ele " +
                    "só entrega o incomingData para o processador certo encontrar e reagir."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("processWith", "String", "Obrigatório. ID do DataProcessor registrado no cliente que deve tratar o dado."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                ProcessData(
                    trigger = EventTriggers.onSuccess(),
                    processWith = "EVENT_RUNNER"
                )
                """
            )

            ShowroomSectionTitle("Por que não há demo interativa aqui")
            SimpleText(
                text = "ProcessData depende de um DataProcessor com o id passado em processWith estar " +
                    "registrado no cliente em tempo de execução — isso é configuração de app, feita no bootstrap " +
                    "do cliente Compose Multiplatform, não algo que este sample-server (que só descreve schemas) " +
                    "consiga demonstrar isoladamente. Sem um processador registrado sob o id usado, o evento é " +
                    "um no-op silencioso: nenhum onSuccess/onFailure dispara.",
                typography = typographyBodyMedium(),
                color = color(themeColorOnSurfaceVariant())
            )

            ShowroomNote(
                "incomingData nulo também resulta em no-op completo — nenhum trigger dispara. Se você " +
                    "registrar seu próprio DataProcessor no app cliente, o callback onSuccess dele não " +
                    "repassa dado algum: use UpdateData/SendData como canal lateral pra expor o resultado do " +
                    "processamento a outros eventos."
            )

            ShowroomRelated(
                names = listOf("TransformData", "EvaluateData", "GetData"),
                destination = "eventDetails"
            )
        }
    }
}
