package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
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
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

object StartCountdownTimerEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "StartCountdownTimer"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Time",
                description = "Inicia uma contagem regressiva no cliente, de timerData.initial até zero, em " +
                    "decrementos de timerData.step."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Pensado para contagens regressivas de expiração de OTP, avisos de timeout de sessão, ou " +
                    "ações com prazo — onTimeTick() dispararia a cada step decorrido, com o tempo restante " +
                    "como incomingData, e onTimeFinish() dispararia uma única vez ao chegar a zero."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("timerData", "TimerData", "Obrigatório. Construído com milliseconds(initial, step) ou seconds(initial, step) — step precisa ser menor que initial."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                StartCountdownTimer(
                    trigger = EventTriggers.onDisplay(),
                    timerData = seconds(initial = 120, step = 1),
                    events = {
                        UpdateTiles(trigger = EventTriggers.onTimeTick(), updates = {
                            update("timer_label", incomingTileUpdateData())
                        })
                        DisplaySnackbar(trigger = EventTriggers.onTimeFinish(), message = "Session expired")
                    }
                )
                """
            )

            ShowroomSectionTitle("Por que não há demo interativa aqui")
            ShowroomParagraph(
                "O próprio schema deste evento documenta que seu runner no cliente é atualmente um " +
                    "placeholder — a lógica de contagem regressiva e disparo de onTimeTick()/onTimeFinish() " +
                    "ainda não foi implementada (StartCountdownTimerEventSchema, mosaic-core). Diferente do " +
                    "StartTimeLoop (que já funciona de verdade), montar uma demo \"ao vivo\" aqui simularia um " +
                    "comportamento que o framework ainda não entrega."
            )

            ShowroomNote(
                "Use StartTimeLoop para laços de tempo recorrentes que já funcionam hoje — ele é a base sobre " +
                    "a qual a contagem regressiva provavelmente será implementada."
            )

            ShowroomRelated(
                names = listOf("StartTimeLoop", "DisplaySnackbar", "UpdateTiles"),
                destination = "eventDetails"
            )
        }
    }
}
