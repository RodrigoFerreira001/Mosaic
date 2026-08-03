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
import dev.catbit.mosaic.server.builder.event.builders.theme.ResetTheme
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton

object ResetThemeEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "ResetTheme"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Theme",
                description = "Reverte um SetTheme anterior, restaurando o color scheme padrão do app — " +
                    "claro e escuro."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use em ações de \"restaurar tema padrão\", ou ao encerrar uma sessão white-labeled que tinha " +
                    "aplicado cores customizadas com SetTheme. Não recebe parâmetros: sempre volta pro esquema " +
                    "original do app, o mesmo de antes de qualquer SetTheme ter sido disparado."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("—", "—", "ResetTheme não recebe parâmetros além de trigger/events."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                ResetTheme(
                    trigger = EventTriggers.onClick()
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Restaure o tema padrão") {
                ShowroomNote(
                    "Vá até o evento SetTheme e aplique o tema \"oceano\" primeiro — depois volte aqui e " +
                        "toque no botão abaixo pra ver as cores do app voltarem ao normal."
                )
                Button(
                    text = "Restaurar com ResetTheme",
                    buttonType = outlinedButton(),
                    events = {
                        ResetTheme(trigger = EventTriggers.onClick())
                    }
                )
            }

            ShowroomRelated(
                names = listOf("SetTheme"),
                destination = "eventDetails"
            )
        }
    }
}
