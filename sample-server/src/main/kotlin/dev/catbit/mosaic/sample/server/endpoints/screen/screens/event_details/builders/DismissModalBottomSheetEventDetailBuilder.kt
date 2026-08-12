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
import dev.catbit.mosaic.server.builder.event.builders.overlays.modal_bottom_sheet.DismissModalBottomSheet
import dev.catbit.mosaic.server.builder.event.builders.overlays.modal_bottom_sheet.DisplayModalBottomSheet
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleLarge

object DismissModalBottomSheetEventDetailBuilder : EventDetailBuilder {

    private const val DEMO_SHEET_ID = "dismiss_modal_bottom_sheet_demo"

    override fun canBuild(eventName: String) = eventName == "DismissModalBottomSheet"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Overlays",
                description = "Fecha programaticamente um bottom sheet aberto — o lado \"reação\" " +
                    "do par DisplayModalBottomSheet / DismissModalBottomSheet."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use em qualquer botão/linha dentro do sheet que representa uma seleção ou cancelamento, " +
                    "ou como filho de onSuccess de um evento assíncrono disparado a partir dele. Informe o " +
                    "mesmo modalBottomSheetId usado no DisplayModalBottomSheet que abriu o sheet."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("modalBottomSheetId", "String", "Obrigatório. O mesmo id informado no DisplayModalBottomSheet que abriu o sheet."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Row(events = {
                    DismissModalBottomSheet(
                        trigger = EventTriggers.onClick(),
                        modalBottomSheetId = "share_sheet"
                    )
                }) {
                    SimpleText(text = "Cancelar")
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Abra o sheet e feche-o com DismissModalBottomSheet") {
                Button(
                    text = "Abrir bottom sheet",
                    buttonType = filledButton(),
                    events = {
                        DisplayModalBottomSheet(
                            trigger = EventTriggers.onClick(),
                            modalBottomSheetId = DEMO_SHEET_ID,
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
                                        text = "O botão abaixo é o próprio DismissModalBottomSheet sendo demonstrado.",
                                        typography = typographyBodyMedium(),
                                        color = color(themeColorOnSurfaceVariant())
                                    )
                                    Button(
                                        text = "Fechar com DismissModalBottomSheet",
                                        buttonType = filledTonalButton(),
                                        events = {
                                            DismissModalBottomSheet(
                                                trigger = EventTriggers.onClick(),
                                                modalBottomSheetId = DEMO_SHEET_ID
                                            )
                                        }
                                    )
                                }
                            }
                        )
                    }
                )
            }

            ShowroomNote(
                "DismissModalBottomSheet dispara onSuccess() assim que marca o sheet para fechar, e " +
                    "onFailure() se nenhum sheet com aquele id estiver na pilha — o que também cobre " +
                    "o caso de fechar o mesmo sheet duas vezes."
            )

            ShowroomRelated(
                names = listOf("DisplayModalBottomSheet", "DismissDialog", "DismissNavigationDrawer", "DismissSnackbar"),
                destination = "eventDetails"
            )
        }
    }
}
