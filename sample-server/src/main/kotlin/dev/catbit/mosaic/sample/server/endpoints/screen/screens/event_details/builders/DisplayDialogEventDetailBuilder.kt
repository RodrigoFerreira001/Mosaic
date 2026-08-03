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
import dev.catbit.mosaic.server.builder.event.builders.overlays.dialog.DismissDialog
import dev.catbit.mosaic.server.builder.event.builders.overlays.dialog.DisplayDialog
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.textButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleLarge

object DisplayDialogEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DisplayDialog"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Overlays",
                description = "Exibe um dialog modal com uma árvore de tiles definida pelo servidor. Aparece " +
                    "imediatamente — sem chamada de rede envolvida."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use para confirmações, alertas customizados ou pequenos formulários que precisam " +
                    "interromper o fluxo atual. O conteúdo do dialog é uma árvore de tiles completa: " +
                    "qualquer tile e qualquer evento pode viver dentro dele, incluindo um DismissDialog " +
                    "para fechá-lo a partir de um botão interno."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("tiles", "TileSchemaBuilderScope.() -> Unit", "Obrigatório. Conteúdo do dialog."),
                    ShowroomParam("isCancellable", "Boolean", "Padrão true. Toque fora ou botão voltar fecham o dialog quando true."),
                    ShowroomParam("usePlatformDefaultWidth", "Boolean", "Padrão false. Quando false, a árvore de tiles controla a largura."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                DisplayDialog(
                    trigger = EventTriggers.onClick(),
                    isCancellable = true,
                    usePlatformDefaultWidth = false,
                    tiles = {
                        Column(id = "dialog_root") {
                            SimpleText(id = "msg", text = "Excluir este item?")
                            Button(id = "confirm_btn", text = "Excluir") {
                                DismissDialog(trigger = EventTriggers.onClick())
                            }
                        }
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Abra um dialog real, com conteúdo interativo de verdade") {
                Button(
                    text = "Abrir dialog",
                    buttonType = filledButton(),
                    events = {
                        DisplayDialog(
                            trigger = EventTriggers.onClick(),
                            isCancellable = true,
                            usePlatformDefaultWidth = false,
                            tiles = {
                                Column(
                                    style = { padding(horizontal = 24, vertical = 24) },
                                    arrangement = arrangeVerticallySpacedBy(16)
                                ) {
                                    SimpleText(
                                        text = "Dialog de exemplo",
                                        typography = typographyTitleLarge()
                                    )
                                    SimpleText(
                                        text = "Este conteúdo foi montado pelo próprio evento DisplayDialog. " +
                                            "O botão abaixo dispara DismissDialog para fechar.",
                                        typography = typographyBodyMedium(),
                                        color = color(themeColorOnSurfaceVariant())
                                    )
                                    Button(
                                        text = "Fechar",
                                        buttonType = textButton(),
                                        events = {
                                            DismissDialog(trigger = EventTriggers.onClick())
                                        }
                                    )
                                }
                            }
                        )
                    }
                )
            }

            ShowroomNote(
                "O par ação/reação DisplayDialog → DismissDialog é o padrão mais comum: o próprio " +
                    "conteúdo do dialog carrega o botão que o fecha."
            )

            ShowroomRelated(
                names = listOf("DismissDialog", "DisplayBottomSheet", "DisplayNavigationDrawer", "DisplaySnackbar"),
                destination = "eventDetails"
            )
        }
    }
}
