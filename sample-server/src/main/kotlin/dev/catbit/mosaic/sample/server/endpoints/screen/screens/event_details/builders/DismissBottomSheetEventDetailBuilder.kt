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
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.snackbarShortDuration
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleLarge

object DismissBottomSheetEventDetailBuilder : EventDetailBuilder {

    private const val DEMO_SHEET_ID = "dismiss_bs_demo"
    private const val OUTSIDE_SHEET_ID = "dismiss_bs_outside"

    override fun canBuild(eventName: String) = eventName == "DismissBottomSheet"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Overlays",
                description = "Fecha um bottom sheet não-modal aberto — o lado \"reação\" do par " +
                    "DisplayBottomSheet / DismissBottomSheet."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use dentro do próprio conteúdo do sheet (botão \"Fechar\") ou a partir de qualquer " +
                    "ponto da tela — como o sheet não-modal não bloqueia o que está atrás, é comum o " +
                    "botão que o fecha viver fora dele. Informe o mesmo bottomSheetId usado no " +
                    "DisplayBottomSheet que o abriu."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("bottomSheetId", "String", "Obrigatório. O mesmo id informado no DisplayBottomSheet que abriu o sheet."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Button(id = "close_btn", text = "Fechar") {
                    events = {
                        DismissBottomSheet(
                            trigger = EventTriggers.onClick(),
                            bottomSheetId = "now_playing"
                        )
                    }
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")

            ShowroomDemoCard(title = "1. Fechando de dentro do sheet") {
                Button(
                    text = "Abrir sheet",
                    buttonType = filledButton(),
                    events = {
                        DisplayBottomSheet(
                            trigger = EventTriggers.onClick(),
                            bottomSheetId = DEMO_SHEET_ID,
                            isCancellable = true,
                            fill = false,
                            tiles = {
                                SheetBody(title = "Sheet de exemplo") {
                                    Paragraph("O botão abaixo é o próprio DismissBottomSheet sendo demonstrado.")
                                    Button(
                                        text = "Fechar com DismissBottomSheet",
                                        buttonType = filledTonalButton(),
                                        events = {
                                            DismissBottomSheet(
                                                trigger = EventTriggers.onClick(),
                                                bottomSheetId = DEMO_SHEET_ID
                                            )
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Sheet fechado programaticamente",
                                                duration = snackbarShortDuration()
                                            )
                                        }
                                    )
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "2. Fechando de fora do sheet") {
                ShowroomParagraph(
                    "Abra o sheet e feche-o pelo botão desta página, sem tocar no sheet. Isso só é " +
                        "possível porque o não-modal não bloqueia a tela por trás — com um modal, " +
                        "este botão estaria inacessível."
                )
                Button(
                    text = "Abrir sheet",
                    buttonType = filledButton(),
                    events = {
                        DisplayBottomSheet(
                            trigger = EventTriggers.onClick(),
                            bottomSheetId = OUTSIDE_SHEET_ID,
                            isCancellable = true,
                            fill = false,
                            tiles = {
                                SheetBody(title = "Feche-me daqui de fora") {
                                    Paragraph(
                                        "Não há botão de fechar aqui dentro. Use o botão da página, " +
                                            "logo abaixo deste sheet."
                                    )
                                }
                            }
                        )
                    }
                )
                Button(
                    text = "Fechar o sheet aberto",
                    buttonType = outlinedButton(),
                    events = {
                        DismissBottomSheet(
                            trigger = EventTriggers.onClick(),
                            bottomSheetId = OUTSIDE_SHEET_ID
                        )
                    }
                )
            }

            ShowroomNote(
                "Fechar um id que não está na pilha dispara onFailure() em vez de ser ignorado — o " +
                    "que também cobre o caso de fechar o mesmo sheet duas vezes."
            )

            ShowroomRelated(
                names = listOf("DisplayBottomSheet", "DismissModalBottomSheet", "DismissDialog", "DismissSnackbar"),
                destination = "eventDetails"
            )
        }
    }

    private fun TileSchemaBuilderScope.SheetBody(
        title: String,
        content: TileSchemaBuilderScope.() -> Unit
    ) {
        Column(
            style = { padding(horizontal = 24, top = 8, bottom = 32) },
            arrangement = arrangeVerticallySpacedBy(16),
            scrollable = true
        ) {
            SimpleText(text = title, typography = typographyTitleLarge())
            content()
        }
    }

    private fun TileSchemaBuilderScope.Paragraph(text: String) {
        SimpleText(
            text = text,
            typography = typographyBodyMedium(),
            color = color(themeColorOnSurfaceVariant())
        )
    }
}
