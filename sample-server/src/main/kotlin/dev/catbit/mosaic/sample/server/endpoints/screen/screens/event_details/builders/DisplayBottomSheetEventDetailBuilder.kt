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
import dev.catbit.mosaic.server.builder.event.builders.overlays.modal_bottom_sheet.DismissModalBottomSheet
import dev.catbit.mosaic.server.builder.event.builders.overlays.modal_bottom_sheet.DisplayModalBottomSheet
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.snackbarShortDuration
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.textButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleLarge

object DisplayBottomSheetEventDetailBuilder : EventDetailBuilder {

    private const val PLAYER_SHEET_ID = "display_bs_player"
    private const val PARTIAL_SHEET_ID = "display_bs_partial"
    private const val BLOCKING_SHEET_ID = "display_bs_blocking"
    private const val STACK_SHEET_ID = "display_bs_stack"
    private const val STACK_MODAL_ID = "display_bs_stack_modal"

    override fun canBuild(eventName: String) = eventName == "DisplayBottomSheet"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Overlays",
                description = "Exibe um bottom sheet não-modal com uma árvore de tiles definida pelo " +
                    "servidor. Sem scrim: a tela por trás continua visível e interativa."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "A diferença para o DisplayModalBottomSheet não é visual, é de intenção. O modal abre " +
                    "numa janela própria e interrompe: enquanto ele estiver aberto, o usuário só " +
                    "interage com ele. O não-modal é renderizado dentro do layout da tela e convive " +
                    "com ela — serve para painéis persistentes como player de mídia, filtros ao vivo " +
                    "ou um resumo que acompanha a navegação. Se o usuário precisa resolver o sheet " +
                    "antes de continuar, use o modal."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("tiles", "TileSchemaBuilderScope.() -> Unit", "Obrigatório. Conteúdo do sheet."),
                    ShowroomParam("bottomSheetId", "String", "Padrão randomId(). Identifica este sheet para que um DismissBottomSheet o feche especificamente."),
                    ShowroomParam("isCancellable", "Boolean", "Padrão true. Arrastar para baixo ou back fecham quando true. Não há scrim para tocar."),
                    ShowroomParam("fill", "Boolean", "Padrão false. Com true o sheet vai até o topo da tela; com false ele tem a altura do próprio conteúdo."),
                    ShowroomParam("allowsPartialExpansion", "Boolean", "Padrão false. Com true o sheet ganha um repouso na metade da tela e abre nele. Ignorado se o conteúdo for menor que meia tela."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                DisplayBottomSheet(
                    trigger = EventTriggers.onClick(),
                    bottomSheetId = "now_playing",
                    isCancellable = true,
                    fill = false,
                    tiles = {
                        Column(id = "player_root") {
                            SimpleText(text = "Tocando agora")
                        }
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")

            ShowroomDemoCard(title = "1. Player — sheet com a altura do conteúdo") {
                ShowroomParagraph(
                    "Abra o sheet e continue rolando esta página por trás dele: sem scrim, nada fica " +
                        "bloqueado. É esse o comportamento que distingue o não-modal."
                )
                Button(
                    text = "Abrir player",
                    buttonType = filledButton(),
                    events = {
                        DisplayBottomSheet(
                            trigger = EventTriggers.onClick(),
                            bottomSheetId = PLAYER_SHEET_ID,
                            isCancellable = true,
                            fill = false,
                            tiles = {
                                SheetBody(title = "Tocando agora") {
                                    Paragraph(
                                        "Role a página por trás deste sheet — ela continua respondendo, " +
                                            "e os botões da tela seguem clicáveis."
                                    )
                                    Button(
                                        text = "Snackbar de dentro do sheet",
                                        buttonType = outlinedButton(),
                                        events = {
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Snackbar sobre o bottom sheet não-modal",
                                                duration = snackbarShortDuration()
                                            )
                                        }
                                    )
                                    CloseSheetButton(PLAYER_SHEET_ID)
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "2. fill + allowsPartialExpansion — abre pela metade") {
                ShowroomParagraph(
                    "Mesma mecânica de altura do modal: fill leva o sheet ao topo, e " +
                        "allowsPartialExpansion cria o repouso na metade, onde ele abre."
                )
                Button(
                    text = "Abrir lista em meia tela",
                    buttonType = filledTonalButton(),
                    events = {
                        DisplayBottomSheet(
                            trigger = EventTriggers.onClick(),
                            bottomSheetId = PARTIAL_SHEET_ID,
                            isCancellable = true,
                            fill = true,
                            allowsPartialExpansion = true,
                            tiles = {
                                SheetBody(title = "Faixas do álbum") {
                                    (1..20).forEach { index ->
                                        SimpleText(
                                            text = "$index. Faixa de exemplo",
                                            typography = typographyBodyMedium()
                                        )
                                    }
                                    CloseSheetButton(PARTIAL_SHEET_ID)
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "3. isCancellable = false — só sai pelo botão") {
                ShowroomParagraph(
                    "Sem arrastar para baixo e sem back press. Como o não-modal não tem scrim, essas " +
                        "eram as únicas saídas por gesto — resta o DismissBottomSheet. Note que a " +
                        "página atrás continua rolando normalmente: travar o sheet não trava a tela."
                )
                Button(
                    text = "Abrir sheet travado",
                    buttonType = filledTonalButton(),
                    events = {
                        DisplayBottomSheet(
                            trigger = EventTriggers.onClick(),
                            bottomSheetId = BLOCKING_SHEET_ID,
                            isCancellable = false,
                            fill = false,
                            tiles = {
                                SheetBody(title = "Upload em andamento") {
                                    Paragraph(
                                        "Tente arrastar este sheet para baixo ou apertar voltar — ele " +
                                            "não se move. Só o botão abaixo o fecha."
                                    )
                                    Button(
                                        text = "Cancelar upload",
                                        buttonType = filledTonalButton(),
                                        events = {
                                            DismissBottomSheet(
                                                trigger = EventTriggers.onClick(),
                                                bottomSheetId = BLOCKING_SHEET_ID
                                            )
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Upload cancelado",
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

            ShowroomDemoCard(title = "4. Convivendo com um modal") {
                ShowroomParagraph(
                    "Abra o não-modal e, com ele aberto, abra um modal por cima. O modal ganha a " +
                        "janela e o scrim; o não-modal continua na pilha, embaixo, e reaparece " +
                        "intacto quando o modal fecha."
                )
                Button(
                    text = "Abrir não-modal",
                    buttonType = filledButton(),
                    events = {
                        DisplayBottomSheet(
                            trigger = EventTriggers.onClick(),
                            bottomSheetId = STACK_SHEET_ID,
                            isCancellable = true,
                            fill = false,
                            tiles = {
                                SheetBody(title = "Camada não-modal") {
                                    Paragraph("id: $STACK_SHEET_ID")
                                    Button(
                                        text = "Abrir um modal por cima",
                                        buttonType = filledTonalButton(),
                                        events = {
                                            DisplayModalBottomSheet(
                                                trigger = EventTriggers.onClick(),
                                                modalBottomSheetId = STACK_MODAL_ID,
                                                isCancellable = true,
                                                fill = false,
                                                tiles = {
                                                    SheetBody(title = "Camada modal") {
                                                        Paragraph(
                                                            "Este tem scrim e janela própria. Feche-o e " +
                                                                "o não-modal continua onde estava."
                                                        )
                                                        Button(
                                                            text = "Fechar este modal",
                                                            buttonType = textButton(),
                                                            events = {
                                                                DismissModalBottomSheet(
                                                                    trigger = EventTriggers.onClick(),
                                                                    modalBottomSheetId = STACK_MODAL_ID
                                                                )
                                                            }
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                    )
                                    CloseSheetButton(STACK_SHEET_ID, text = "Fechar este sheet")
                                }
                            }
                        )
                    }
                )
            }

            ShowroomNote(
                "Sem scrim, o sheet não-modal não intercepta toques fora dele — inclusive os do " +
                    "próprio conteúdo da tela, que pode disparar eventos enquanto o sheet está aberto. " +
                    "Leve isso em conta ao decidir entre os dois."
            )

            ShowroomRelated(
                names = listOf("DismissBottomSheet", "DisplayModalBottomSheet", "DisplayDialog", "DisplaySnackbar"),
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

    private fun TileSchemaBuilderScope.CloseSheetButton(
        sheetId: String,
        text: String = "Fechar"
    ) {
        Button(
            text = text,
            buttonType = textButton(),
            events = {
                DismissBottomSheet(
                    trigger = EventTriggers.onClick(),
                    bottomSheetId = sheetId
                )
            }
        )
    }
}
