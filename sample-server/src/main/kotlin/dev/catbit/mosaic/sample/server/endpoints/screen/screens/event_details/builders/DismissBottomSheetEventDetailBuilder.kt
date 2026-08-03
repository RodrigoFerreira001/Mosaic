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
import dev.catbit.mosaic.server.builder.event.builders.overlays.bottom_sheet.DismissBottomSheet
import dev.catbit.mosaic.server.builder.event.builders.overlays.bottom_sheet.DisplayBottomSheet
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleLarge

object DismissBottomSheetEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DismissBottomSheet"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Overlays",
                description = "Fecha programaticamente o bottom sheet exibido no momento — o lado \"reação\" " +
                    "do par DisplayBottomSheet / DismissBottomSheet."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use em qualquer botão/linha dentro do sheet que representa uma seleção ou cancelamento, " +
                    "ou como filho de onSuccess de um evento assíncrono disparado a partir dele. Fecha o " +
                    "sheet aberto no momento, sem precisar de nenhum identificador."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("—", "—", "DismissBottomSheet não recebe parâmetros além de trigger/events."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Row(events = {
                    DismissBottomSheet(trigger = EventTriggers.onClick())
                }) {
                    SimpleText(text = "Cancelar")
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Abra o sheet e feche-o com DismissBottomSheet") {
                Button(
                    text = "Abrir bottom sheet",
                    buttonType = filledButton(),
                    events = {
                        DisplayBottomSheet(
                            trigger = EventTriggers.onClick(),
                            isCancellable = true,
                            fill = false,
                            tiles = {
                                Column(
                                    style = { padding(horizontal = 24, top = 24, bottom = 32) },
                                    arrangement = arrangeVerticallySpacedBy(16)
                                ) {
                                    SimpleText(
                                        text = "Bottom sheet de exemplo",
                                        typography = typographyTitleLarge()
                                    )
                                    SimpleText(
                                        text = "O botão abaixo é o próprio DismissBottomSheet sendo demonstrado.",
                                        typography = typographyBodyMedium(),
                                        color = color(themeColorOnSurfaceVariant())
                                    )
                                    Button(
                                        text = "Fechar com DismissBottomSheet",
                                        buttonType = filledTonalButton(),
                                        events = {
                                            DismissBottomSheet(trigger = EventTriggers.onClick())
                                        }
                                    )
                                }
                            }
                        )
                    }
                )
            }

            ShowroomNote(
                "DismissBottomSheet dispara onBottomSheetDismissed() como trigger filho depois que o " +
                    "sheet termina de fechar — útil para limpar estado assim que a UI sai de cena."
            )

            ShowroomRelated(
                names = listOf("DisplayBottomSheet", "DismissDialog", "DismissNavigationDrawer", "DismissSnackbar"),
                destination = "eventDetails"
            )
        }
    }
}
