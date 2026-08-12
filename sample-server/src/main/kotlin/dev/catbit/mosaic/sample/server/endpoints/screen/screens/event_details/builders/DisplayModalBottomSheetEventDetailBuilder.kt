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
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.textButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.inputs.TextField
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleLarge

object DisplayModalBottomSheetEventDetailBuilder : EventDetailBuilder {

    private const val SHARE_SHEET_ID = "display_mbs_share"
    private const val FILL_SHEET_ID = "display_mbs_fill"
    private const val PARTIAL_SHEET_ID = "display_mbs_partial"
    private const val BLOCKING_SHEET_ID = "display_mbs_blocking"
    private const val SNACKBAR_SHEET_ID = "display_mbs_snackbar"
    private const val STACK_SHEET_1_ID = "display_mbs_stack_1"
    private const val STACK_SHEET_2_ID = "display_mbs_stack_2"
    private const val STACK_DIALOG_ID = "display_mbs_stack_dialog"

    override fun canBuild(eventName: String) = eventName == "DisplayModalBottomSheet"

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
                    "DismissModalBottomSheet em algum botão interno para fechá-lo."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("tiles", "TileSchemaBuilderScope.() -> Unit", "Obrigatório. Conteúdo do sheet."),
                    ShowroomParam("modalBottomSheetId", "String", "Padrão randomId(). Identifica este sheet para que um DismissModalBottomSheet o feche especificamente."),
                    ShowroomParam("isCancellable", "Boolean", "Padrão true. Arrastar para baixo ou tocar no scrim fecham quando true."),
                    ShowroomParam("fill", "Boolean", "Padrão false. Com true o sheet vai até o topo da tela; com false ele tem a altura do próprio conteúdo."),
                    ShowroomParam("allowsPartialExpansion", "Boolean", "Padrão false. Com true o sheet ganha um repouso na metade da tela e abre nele. Ignorado se o conteúdo for menor que meia tela."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                DisplayModalBottomSheet(
                    trigger = EventTriggers.onClick(),
                    modalBottomSheetId = "share_sheet",
                    isCancellable = true,
                    fill = false,
                    allowsPartialExpansion = false,
                    tiles = {
                        Column(id = "sheet_root") {
                            SimpleText(text = "Compartilhar via")
                        }
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")

            ShowroomDemoCard(title = "1. Compartilhar — sheet padrão (cancelável, sem fill)") {
                Button(
                    text = "Abrir sheet de compartilhamento",
                    buttonType = filledButton(),
                    events = {
                        DisplayModalBottomSheet(
                            trigger = EventTriggers.onClick(),
                            modalBottomSheetId = SHARE_SHEET_ID,
                            isCancellable = true,
                            fill = false,
                            tiles = {
                                SheetBody(title = "Compartilhar via") {
                                    listOf(
                                        "link" to "Copiar link",
                                        "chat" to "Mensagem",
                                        "print" to "Imprimir"
                                    ).forEach { (iconName, label) ->
                                        Row(
                                            style = { padding(vertical = 4) },
                                            arrangement = arrangeHorizontallySpacedBy(16),
                                            alignment = alignVerticallyToCenter(),
                                            events = {
                                                DismissModalBottomSheet(
                                                    trigger = EventTriggers.onClick(),
                                                    modalBottomSheetId = SHARE_SHEET_ID
                                                )
                                                DisplaySnackbar(
                                                    trigger = EventTriggers.onClick(),
                                                    message = "\"$label\" selecionado",
                                                    duration = snackbarShortDuration()
                                                )
                                            }
                                        ) {
                                            Icon(icon = icon(iconName))
                                            SimpleText(text = label, typography = typographyBodyMedium())
                                        }
                                    }
                                    CloseSheetButton(SHARE_SHEET_ID)
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "2. fill = true — sheet ocupando a tela inteira") {
                ShowroomParagraph(
                    "Com fill = true o sheet vai até o topo da tela e o formulário rola dentro dele. " +
                        "Não há estado parcial: arrastar para baixo fecha o sheet direto. Compare com a " +
                        "demo 1, onde fill = false deixa o sheet com a altura do próprio conteúdo."
                )
                Button(
                    text = "Abrir formulário em tela cheia",
                    buttonType = filledButton(),
                    events = {
                        DisplayModalBottomSheet(
                            trigger = EventTriggers.onClick(),
                            modalBottomSheetId = FILL_SHEET_ID,
                            isCancellable = true,
                            fill = true,
                            tiles = {
                                SheetBody(title = "Novo endereço") {
                                    listOf("CEP", "Rua", "Número", "Complemento", "Bairro", "Cidade").forEach { label ->
                                        SimpleText(
                                            text = label,
                                            typography = typographyBodyMedium(),
                                            color = color(themeColorOnSurfaceVariant())
                                        )
                                        TextField(style = { size(width = fillHorizontally()) })
                                    }
                                    Button(
                                        text = "Salvar",
                                        buttonType = filledButton(),
                                        events = {
                                            DismissModalBottomSheet(
                                                trigger = EventTriggers.onClick(),
                                                modalBottomSheetId = FILL_SHEET_ID
                                            )
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Endereço salvo",
                                                duration = snackbarShortDuration()
                                            )
                                        }
                                    )
                                    CloseSheetButton(FILL_SHEET_ID, text = "Descartar")
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "3. allowsPartialExpansion = true — abre pela metade") {
                ShowroomParagraph(
                    "O sheet ganha um repouso na metade da tela e abre nele; arraste para cima para " +
                        "chegar ao topo, e para baixo para fechar. Precisa vir junto com fill (ou com " +
                        "conteúdo comprovadamente longo): o Material só cria esse repouso para sheets " +
                        "mais altos que meia tela."
                )
                Button(
                    text = "Abrir lista em meia tela",
                    buttonType = filledButton(),
                    events = {
                        DisplayModalBottomSheet(
                            trigger = EventTriggers.onClick(),
                            modalBottomSheetId = PARTIAL_SHEET_ID,
                            isCancellable = true,
                            fill = true,
                            allowsPartialExpansion = true,
                            tiles = {
                                SheetBody(title = "Selecione um país") {
                                    listOf(
                                        "África do Sul", "Alemanha", "Argentina", "Austrália", "Brasil",
                                        "Canadá", "Chile", "China", "Colômbia", "Coreia do Sul",
                                        "Espanha", "Estados Unidos", "França", "Índia", "Itália",
                                        "Japão", "México", "Nigéria", "Portugal", "Uruguai"
                                    ).forEach { country ->
                                        Row(
                                            style = { padding(vertical = 8) },
                                            arrangement = arrangeHorizontallySpacedBy(16),
                                            alignment = alignVerticallyToCenter(),
                                            events = {
                                                DismissModalBottomSheet(
                                                    trigger = EventTriggers.onClick(),
                                                    modalBottomSheetId = PARTIAL_SHEET_ID
                                                )
                                                DisplaySnackbar(
                                                    trigger = EventTriggers.onClick(),
                                                    message = "$country selecionado",
                                                    duration = snackbarShortDuration()
                                                )
                                            }
                                        ) {
                                            Icon(icon = icon("public"))
                                            SimpleText(text = country, typography = typographyBodyMedium())
                                        }
                                    }
                                    CloseSheetButton(PARTIAL_SHEET_ID, text = "Cancelar")
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "4. isCancellable = false — só sai pelo botão") {
                ShowroomParagraph(
                    "Sem drag handle, sem arrastar para baixo, sem fechar tocando no scrim e sem back press. " +
                        "O único caminho de saída é um DismissModalBottomSheet dentro do próprio sheet."
                )
                Button(
                    text = "Abrir sheet obrigatório",
                    buttonType = filledTonalButton(),
                    events = {
                        DisplayModalBottomSheet(
                            trigger = EventTriggers.onClick(),
                            modalBottomSheetId = BLOCKING_SHEET_ID,
                            isCancellable = false,
                            fill = false,
                            tiles = {
                                SheetBody(title = "Aceite os termos") {
                                    SimpleText(
                                        text = "Tente arrastar para baixo ou tocar fora — nada acontece. " +
                                            "Use os botões abaixo.",
                                        typography = typographyBodyMedium(),
                                        color = color(themeColorOnSurfaceVariant())
                                    )
                                    Button(
                                        text = "Aceitar",
                                        buttonType = filledButton(),
                                        events = {
                                            DismissModalBottomSheet(
                                                trigger = EventTriggers.onClick(),
                                                modalBottomSheetId = BLOCKING_SHEET_ID
                                            )
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Termos aceitos",
                                                duration = snackbarShortDuration()
                                            )
                                        }
                                    )
                                    CloseSheetButton(BLOCKING_SHEET_ID, text = "Recusar")
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "5. Snackbar por cima do sheet") {
                ShowroomParagraph(
                    "O snackbar é renderizado num layer criado depois do sheet, então deve aparecer " +
                        "acima dele — inclusive com o sheet em fill."
                )
                Button(
                    text = "Abrir sheet e disparar snackbars de dentro",
                    buttonType = outlinedButton(),
                    events = {
                        DisplayModalBottomSheet(
                            trigger = EventTriggers.onClick(),
                            modalBottomSheetId = SNACKBAR_SHEET_ID,
                            isCancellable = true,
                            fill = true,
                            tiles = {
                                SheetBody(title = "Teste de z-order") {
                                    SimpleText(
                                        text = "O sheet está em fill (tela cheia). O snackbar disparado " +
                                            "abaixo precisa aparecer sobre ele, não atrás.",
                                        typography = typographyBodyMedium(),
                                        color = color(themeColorOnSurfaceVariant())
                                    )
                                    Button(
                                        text = "Snackbar curto",
                                        buttonType = outlinedButton(),
                                        events = {
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Snackbar disparado de dentro do sheet",
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
                                                message = "Este fica mais tempo — arraste o sheet enquanto ele aparece",
                                                duration = snackbarLongDuration(),
                                                actionLabel = "OK"
                                            )
                                        }
                                    )
                                    Button(
                                        text = "Dois snackbars em sequência",
                                        buttonType = outlinedButton(),
                                        events = {
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Primeiro — deve animar a saída antes do segundo",
                                                duration = snackbarShortDuration()
                                            )
                                            DisplaySnackbar(
                                                trigger = EventTriggers.onClick(),
                                                message = "Segundo — entrou depois que o primeiro saiu",
                                                duration = snackbarShortDuration()
                                            )
                                        }
                                    )
                                    CloseSheetButton(SNACKBAR_SHEET_ID)
                                }
                            }
                        )
                    }
                )
            }

            ShowroomDemoCard(title = "6. Empilhamento — sheet sobre sheet sobre dialog") {
                ShowroomParagraph(
                    "Cada overlay é uma camada independente com id próprio. O sheet do meio consegue " +
                        "fechar o de baixo sem se fechar, provando que a pilha não é um único slot."
                )
                Button(
                    text = "Abrir pilha de overlays",
                    buttonType = filledButton(),
                    events = {
                        DisplayModalBottomSheet(
                            trigger = EventTriggers.onClick(),
                            modalBottomSheetId = STACK_SHEET_1_ID,
                            isCancellable = true,
                            fill = false,
                            tiles = {
                                SheetBody(title = "Camada 1 — sheet base") {
                                    SimpleText(
                                        text = "id: $STACK_SHEET_1_ID",
                                        typography = typographyBodyMedium(),
                                        color = color(themeColorOnSurfaceVariant())
                                    )
                                    Button(
                                        text = "Abrir camada 2 (outro sheet)",
                                        buttonType = filledTonalButton(),
                                        events = {
                                            DisplayModalBottomSheet(
                                                trigger = EventTriggers.onClick(),
                                                modalBottomSheetId = STACK_SHEET_2_ID,
                                                isCancellable = true,
                                                fill = false,
                                                tiles = {
                                                    SheetBody(title = "Camada 2 — sheet sobre sheet") {
                                                        SimpleText(
                                                            text = "id: $STACK_SHEET_2_ID",
                                                            typography = typographyBodyMedium(),
                                                            color = color(themeColorOnSurfaceVariant())
                                                        )
                                                        Button(
                                                            text = "Abrir camada 3 (dialog)",
                                                            buttonType = filledTonalButton(),
                                                            events = {
                                                                DisplayDialog(
                                                                    trigger = EventTriggers.onClick(),
                                                                    dialogId = STACK_DIALOG_ID,
                                                                    isCancellable = true,
                                                                    usePlatformDefaultWidth = false,
                                                                    tiles = {
                                                                        DialogBody(title = "Camada 3 — dialog no topo") {
                                                                            SimpleText(
                                                                                text = "Três overlays vivos ao mesmo tempo. " +
                                                                                    "Feche na ordem que quiser.",
                                                                                typography = typographyBodyMedium(),
                                                                                color = color(themeColorOnSurfaceVariant())
                                                                            )
                                                                            Button(
                                                                                text = "Snackbar sobre os três",
                                                                                buttonType = outlinedButton(),
                                                                                events = {
                                                                                    DisplaySnackbar(
                                                                                        trigger = EventTriggers.onClick(),
                                                                                        message = "Snackbar acima de sheet + sheet + dialog",
                                                                                        duration = snackbarLongDuration()
                                                                                    )
                                                                                }
                                                                            )
                                                                            Button(
                                                                                text = "Fechar o sheet da camada 1",
                                                                                buttonType = outlinedButton(),
                                                                                events = {
                                                                                    DismissModalBottomSheet(
                                                                                        trigger = EventTriggers.onClick(),
                                                                                        modalBottomSheetId = STACK_SHEET_1_ID
                                                                                    )
                                                                                }
                                                                            )
                                                                            Button(
                                                                                text = "Fechar este dialog",
                                                                                buttonType = textButton(),
                                                                                events = {
                                                                                    DismissDialog(
                                                                                        trigger = EventTriggers.onClick(),
                                                                                        dialogId = STACK_DIALOG_ID
                                                                                    )
                                                                                }
                                                                            )
                                                                        }
                                                                    }
                                                                )
                                                            }
                                                        )
                                                        Button(
                                                            text = "Fechar só a camada 1 (a de baixo)",
                                                            buttonType = outlinedButton(),
                                                            events = {
                                                                DismissModalBottomSheet(
                                                                    trigger = EventTriggers.onClick(),
                                                                    modalBottomSheetId = STACK_SHEET_1_ID
                                                                )
                                                            }
                                                        )
                                                        CloseSheetButton(STACK_SHEET_2_ID, text = "Fechar esta camada")
                                                    }
                                                }
                                            )
                                        }
                                    )
                                    CloseSheetButton(STACK_SHEET_1_ID, text = "Fechar esta camada")
                                }
                            }
                        )
                    }
                )
            }

            ShowroomNote(
                "fill e allowsPartialExpansion são independentes: fill decide a altura do sheet, " +
                    "allowsPartialExpansion decide se existe um repouso na metade do caminho. " +
                    "A combinação clássica do Material (lista longa que abre pela metade) é os dois true."
            )

            ShowroomRelated(
                names = listOf("DismissModalBottomSheet", "DisplayDialog", "DisplayNavigationDrawer", "DisplaySnackbar"),
                destination = "eventDetails"
            )
        }
    }

    /**
     * Corpo padrão dos sheets desta página: título + conteúdo, com o respiro que o sheet não dá.
     * Scrollable porque o sheet limita a altura ao que cabe na tela — sem isso, conteúdo longo
     * (o formulário da demo de fill, por exemplo) fica cortado e inalcançável.
     */
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

    /** Mesmo corpo, para o dialog usado na demo de empilhamento. */
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

    private fun TileSchemaBuilderScope.CloseSheetButton(
        sheetId: String,
        text: String = "Fechar"
    ) {
        Button(
            text = text,
            buttonType = textButton(),
            events = {
                DismissModalBottomSheet(
                    trigger = EventTriggers.onClick(),
                    modalBottomSheetId = sheetId
                )
            }
        )
    }
}
