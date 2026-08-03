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
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.textButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.inputs.TextField
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleLarge

object DisplayBottomSheetEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DisplayBottomSheet"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Overlays",
                description = "Exibe um bottom sheet modal com uma árvore de tiles definida pelo servidor. " +
                    "Aparece imediatamente — sem chamada de rede envolvida."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use para menus contextuais, action sheets, formulários curtos ou painéis de filtro. " +
                    "Assim como o dialog, o conteúdo é uma árvore de tiles completa — inclua um " +
                    "DismissBottomSheet em algum botão interno para fechá-lo."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("tiles", "TileSchemaBuilderScope.() -> Unit", "Obrigatório. Conteúdo do sheet."),
                    ShowroomParam("isCancellable", "Boolean", "Padrão true. Arrastar para baixo ou tocar no scrim fecham quando true."),
                    ShowroomParam("fill", "Boolean", "Padrão false. Quando true, expande até a altura total da tela."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                DisplayBottomSheet(
                    trigger = EventTriggers.onClick(),
                    isCancellable = true,
                    fill = false,
                    tiles = {
                        Column(id = "sheet_root") {
                            SimpleText(text = "Compartilhar via")
                        }
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Abra um bottom sheet real com opções de ação") {
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
                                        text = "Compartilhar via",
                                        typography = typographyTitleLarge()
                                    )
                                    TextField(
                                        style = {
                                            size(
                                                width = fillHorizontally()
                                            )
                                        }
                                    )
                                    SimpleText(
                                        text = "Conteúdo montado pelo evento DisplayBottomSheet, com o " +
                                            "próprio DismissBottomSheet fechando-o a seguir.",
                                        typography = typographyBodyMedium(),
                                        color = color(themeColorOnSurfaceVariant())
                                    )
                                    listOf("link" to "E-mail", "chat" to "Mensagem", "print" to "Imprimir").forEach { (iconName, label) ->
                                        Row(
                                            style = { padding(vertical = 4) },
                                            arrangement = arrangeHorizontallySpacedBy(16),
                                            alignment = alignVerticallyToCenter(),
                                            events = {
                                                DismissBottomSheet(trigger = EventTriggers.onClick())
                                            }
                                        ) {
                                            Icon(icon = icon(iconName))
                                            SimpleText(text = label, typography = typographyBodyMedium())
                                        }
                                    }
                                    Button(
                                        text = "Fechar",
                                        buttonType = textButton(),
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
                "Use fill = true para sheets que precisam de bastante espaço, como formulários com " +
                    "vários campos — o comportamento de arrastar/fechar continua o mesmo."
            )

            ShowroomRelated(
                names = listOf("DismissBottomSheet", "DisplayDialog", "DisplayNavigationDrawer", "DisplaySnackbar"),
                destination = "eventDetails"
            )
        }
    }
}
