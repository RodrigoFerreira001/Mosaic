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
import dev.catbit.mosaic.server.builder.event.builders.overlays.modal_bottom_sheet.DismissModalBottomSheet
import dev.catbit.mosaic.server.builder.event.builders.overlays.modal_bottom_sheet.DisplayModalBottomSheet
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.snackbarLongDuration
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

object DisplayDialogEventDetailBuilder : EventDetailBuilder {

    private const val CONFIRM_DIALOG_ID = "display_dialog_confirm"
    private const val BLOCKING_DIALOG_ID = "display_dialog_blocking"
    private const val PLATFORM_WIDTH_DIALOG_ID = "display_dialog_platform_width"
    private const val WIDE_DIALOG_ID = "display_dialog_wide"
    private const val SNACKBAR_DIALOG_ID = "display_dialog_snackbar"
    private const val STACK_DIALOG_1_ID = "display_dialog_stack_1"
    private const val STACK_DIALOG_2_ID = "display_dialog_stack_2"
    private const val STACK_DIALOG_3_ID = "display_dialog_stack_3"
    private const val STACK_SHEET_ID = "display_dialog_stack_sheet"

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
                    ShowroomParam("dialogId", "String", "Padrão randomId(). Identifica este dialog para que um DismissDialog o feche especificamente."),
                    ShowroomParam("isCancellable", "Boolean", "Padrão true. Toque fora ou botão voltar fecham o dialog quando true."),
                    ShowroomParam("usePlatformDefaultWidth", "Boolean", "Padrão false. Quando false, a árvore de tiles controla a largura."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                DisplayDialog(
                    trigger = EventTriggers.onClick(),
                    dialogId = "delete_confirmation",
                    isCancellable = true,
                    usePlatformDefaultWidth = false,
                    tiles = {
                        Column(id = "dialog_root") {
                            SimpleText(id = "msg", text = "Excluir este item?")
                            Button(id = "confirm_btn", text = "Excluir") {
                                DismissDialog(
                                    trigger = EventTriggers.onClick(),
                                    dialogId = "delete_confirmation"
                                )
                            }
                        }
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")

            ShowroomDemoCard(title = "1. Confirmação — dialog padrão, cancelável") {
                Button(
                    text = "Excluir item",
                    buttonType = filledButton(),
                    events = {
                        DisplayDialog(
                            trigger = EventTriggers.onClick(),
                            dialogId = CONFIRM_DIALOG_ID,
                            isCancellable = true,
                            usePlatformDefaultWidth = false,
                            tiles = {
                                DialogBody(title = "Excluir este item?") {
                                    Paragraph("Toque fora ou use o back para cancelar — isCancellable = true.")
                                    Button(
                                        text = "Excluir",
                                        buttonType = filledButton(),
                                        events = {
                                            DismissDialog(
                                                trigger = EventTriggers.onClick(),
                                                dialogId = CONFIRM_DIALOG_ID
                                            )
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Item excluído",
                                                duration = snackbarLongDuration(),
                                                actionLabel = "Desfazer"
                                            )
                                        }
                                    )
                                    CloseDialogButton(CONFIRM_DIALOG_ID, text = "Cancelar")
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "2. isCancellable = false — decisão obrigatória") {
                ShowroomParagraph(
                    "Toque fora e back press ficam inertes. O dialog só sai por um DismissDialog interno."
                )
                Button(
                    text = "Abrir dialog bloqueante",
                    buttonType = filledTonalButton(),
                    events = {
                        DisplayDialog(
                            trigger = EventTriggers.onClick(),
                            dialogId = BLOCKING_DIALOG_ID,
                            isCancellable = false,
                            usePlatformDefaultWidth = false,
                            tiles = {
                                DialogBody(title = "Sessão expirada") {
                                    Paragraph(
                                        "Tente tocar fora ou apertar voltar — o dialog não fecha. " +
                                            "Escolha uma das opções."
                                    )
                                    Button(
                                        text = "Entrar novamente",
                                        buttonType = filledButton(),
                                        events = {
                                            DismissDialog(
                                                trigger = EventTriggers.onClick(),
                                                dialogId = BLOCKING_DIALOG_ID
                                            )
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Redirecionando para o login...",
                                                duration = snackbarShortDuration()
                                            )
                                        }
                                    )
                                    CloseDialogButton(BLOCKING_DIALOG_ID, text = "Sair")
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "3. usePlatformDefaultWidth — largura da plataforma vs. do conteúdo") {
                ShowroomParagraph(
                    "Abra os dois em sequência e compare: com true, a plataforma decide a largura e o " +
                        "conteúdo se adapta; com false, a árvore de tiles manda."
                )
                Button(
                    text = "usePlatformDefaultWidth = true",
                    buttonType = outlinedButton(),
                    events = {
                        DisplayDialog(
                            trigger = EventTriggers.onClick(),
                            dialogId = PLATFORM_WIDTH_DIALOG_ID,
                            isCancellable = true,
                            usePlatformDefaultWidth = true,
                            tiles = {
                                DialogBody(title = "Largura da plataforma") {
                                    Paragraph(
                                        "A largura deste dialog veio do sistema. Textos longos quebram " +
                                            "dentro do limite imposto por ele, e o conteúdo não consegue esticar."
                                    )
                                    CloseDialogButton(PLATFORM_WIDTH_DIALOG_ID)
                                }
                            }
                        )
                    }
                )
                Button(
                    text = "usePlatformDefaultWidth = false",
                    buttonType = outlinedButton(),
                    events = {
                        DisplayDialog(
                            trigger = EventTriggers.onClick(),
                            dialogId = WIDE_DIALOG_ID,
                            isCancellable = true,
                            usePlatformDefaultWidth = false,
                            tiles = {
                                DialogBody(title = "Largura do conteúdo") {
                                    Paragraph(
                                        "Aqui a árvore de tiles controla a largura — o Surface do dialog " +
                                            "acompanha o que o conteúdo pedir."
                                    )
                                    CloseDialogButton(WIDE_DIALOG_ID)
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "4. Snackbar por cima do dialog") {
                ShowroomParagraph(
                    "O dialog abre uma janela própria; o snackbar precisa vir num layer criado depois " +
                        "dele para aparecer por cima."
                )
                Button(
                    text = "Abrir dialog e disparar snackbars de dentro",
                    buttonType = outlinedButton(),
                    events = {
                        DisplayDialog(
                            trigger = EventTriggers.onClick(),
                            dialogId = SNACKBAR_DIALOG_ID,
                            isCancellable = true,
                            usePlatformDefaultWidth = false,
                            tiles = {
                                DialogBody(title = "Teste de z-order") {
                                    Button(
                                        text = "Snackbar curto",
                                        buttonType = outlinedButton(),
                                        events = {
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Snackbar disparado de dentro do dialog",
                                                duration = snackbarShortDuration()
                                            )
                                        }
                                    )
                                    Button(
                                        text = "Snackbar longo com ação",
                                        buttonType = outlinedButton(),
                                        events = {
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Toque na ação com o dialog ainda aberto",
                                                duration = snackbarLongDuration(),
                                                actionLabel = "OK"
                                            )
                                        }
                                    )
                                    Button(
                                        text = "Snackbar e fechar o dialog junto",
                                        buttonType = outlinedButton(),
                                        events = {
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "O dialog sumiu, o snackbar continua",
                                                duration = snackbarLongDuration()
                                            )
                                            DismissDialog(
                                                trigger = EventTriggers.onClick(),
                                                dialogId = SNACKBAR_DIALOG_ID
                                            )
                                        }
                                    )
                                    CloseDialogButton(SNACKBAR_DIALOG_ID)
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "5. Empilhamento — três dialogs e um sheet") {
                ShowroomParagraph(
                    "Cada camada tem id próprio, então dá para fechar a do meio sem tocar nas outras. " +
                        "A camada 3 abre um bottom sheet por cima de tudo."
                )
                Button(
                    text = "Abrir pilha de dialogs",
                    buttonType = filledButton(),
                    events = {
                        DisplayDialog(
                            trigger = EventTriggers.onClick(),
                            dialogId = STACK_DIALOG_1_ID,
                            isCancellable = true,
                            usePlatformDefaultWidth = false,
                            tiles = {
                                DialogBody(title = "Camada 1") {
                                    Paragraph("id: $STACK_DIALOG_1_ID")
                                    Button(
                                        text = "Abrir camada 2",
                                        buttonType = filledTonalButton(),
                                        events = {
                                            DisplayDialog(
                                                trigger = EventTriggers.onClick(),
                                                dialogId = STACK_DIALOG_2_ID,
                                                isCancellable = true,
                                                usePlatformDefaultWidth = false,
                                                tiles = {
                                                    DialogBody(title = "Camada 2") {
                                                        Paragraph("id: $STACK_DIALOG_2_ID")
                                                        Button(
                                                            text = "Abrir camada 3",
                                                            buttonType = filledTonalButton(),
                                                            events = {
                                                                DisplayDialog(
                                                                    trigger = EventTriggers.onClick(),
                                                                    dialogId = STACK_DIALOG_3_ID,
                                                                    isCancellable = true,
                                                                    usePlatformDefaultWidth = false,
                                                                    tiles = {
                                                                        DialogBody(title = "Camada 3") {
                                                                            Paragraph(
                                                                                "Três dialogs empilhados. " +
                                                                                    "Feche o do meio e veja os outros dois seguirem vivos."
                                                                            )
                                                                            Button(
                                                                                text = "Abrir bottom sheet sobre os três",
                                                                                buttonType = filledTonalButton(),
                                                                                events = {
                                                                                    DisplayModalBottomSheet(
                                                                                        trigger = EventTriggers.onClick(),
                                                                                        modalBottomSheetId = STACK_SHEET_ID,
                                                                                        isCancellable = true,
                                                                                        fill = false,
                                                                                        tiles = {
                                                                                            Column(
                                                                                                style = { padding(horizontal = 24, top = 8, bottom = 32) },
                                                                                                arrangement = arrangeVerticallySpacedBy(16)
                                                                                            ) {
                                                                                                SimpleText(
                                                                                                    text = "Sheet sobre três dialogs",
                                                                                                    typography = typographyTitleLarge()
                                                                                                )
                                                                                                Paragraph(
                                                                                                    "Quatro overlays simultâneos. O snackbar abaixo " +
                                                                                                        "precisa aparecer sobre todos eles."
                                                                                                )
                                                                                                Button(
                                                                                                    text = "Disparar snackbar",
                                                                                                    buttonType = outlinedButton(),
                                                                                                    events = {
                                                                                                        DisplaySnackbar(
                                                                                                            trigger = EventTriggers.onClick(),
                                                                                                            message = "Snackbar sobre 3 dialogs + 1 sheet",
                                                                                                            duration = snackbarLongDuration()
                                                                                                        )
                                                                                                    }
                                                                                                )
                                                                                                Button(
                                                                                                    text = "Fechar este sheet",
                                                                                                    buttonType = textButton(),
                                                                                                    events = {
                                                                                                        DismissModalBottomSheet(
                                                                                                            trigger = EventTriggers.onClick(),
                                                                                                            modalBottomSheetId = STACK_SHEET_ID
                                                                                                        )
                                                                                                    }
                                                                                                )
                                                                                            }
                                                                                        }
                                                                                    )
                                                                                }
                                                                            )
                                                                            Button(
                                                                                text = "Fechar a camada 2 (a do meio)",
                                                                                buttonType = outlinedButton(),
                                                                                events = {
                                                                                    DismissDialog(
                                                                                        trigger = EventTriggers.onClick(),
                                                                                        dialogId = STACK_DIALOG_2_ID
                                                                                    )
                                                                                }
                                                                            )
                                                                            CloseDialogButton(STACK_DIALOG_3_ID, text = "Fechar esta camada")
                                                                        }
                                                                    }
                                                                )
                                                            }
                                                        )
                                                        CloseDialogButton(STACK_DIALOG_2_ID, text = "Fechar esta camada")
                                                    }
                                                }
                                            )
                                        }
                                    )
                                    CloseDialogButton(STACK_DIALOG_1_ID, text = "Fechar esta camada")
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
                names = listOf("DismissDialog", "DisplayModalBottomSheet", "DisplayNavigationDrawer", "DisplaySnackbar"),
                destination = "eventDetails"
            )
        }
    }

    /** Corpo padrão dos dialogs desta página: título + conteúdo espaçado. */
    private fun TileSchemaBuilderScope.DialogBody(
        title: String,
        content: TileSchemaBuilderScope.() -> Unit
    ) {
        Column(
            arrangement = arrangeVerticallySpacedBy(16)
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

    private fun TileSchemaBuilderScope.CloseDialogButton(
        dialogId: String,
        text: String = "Fechar"
    ) {
        Button(
            text = text,
            buttonType = textButton(),
            events = {
                DismissDialog(
                    trigger = EventTriggers.onClick(),
                    dialogId = dialogId
                )
            }
        )
    }
}
