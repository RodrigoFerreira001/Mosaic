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
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleLarge

object DismissDialogEventDetailBuilder : EventDetailBuilder {

    private const val DEMO_DIALOG_ID = "dismiss_dialog_demo"

    override fun canBuild(eventName: String) = eventName == "DismissDialog"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Overlays",
                description = "Fecha programaticamente um dialog aberto — o lado \"reação\" do " +
                    "par DisplayDialog / DismissDialog."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use dentro do próprio conteúdo do dialog (botão \"Cancelar\"/\"Fechar\") ou após uma " +
                    "ação assíncrona concluída dentro dele, como resposta de sucesso de uma " +
                    "SendNetworkRequest disparada por um botão do dialog. Informe o mesmo dialogId " +
                    "usado no DisplayDialog que abriu o dialog."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("dialogId", "String", "Obrigatório. O mesmo id informado no DisplayDialog que abriu o dialog."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Button(id = "confirm_btn", text = "Excluir") {
                    events = {
                        DismissDialog(
                            trigger = EventTriggers.onClick(),
                            dialogId = "delete_confirmation"
                        )
                    }
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Abra o dialog e feche-o com DismissDialog") {
                Button(
                    text = "Abrir dialog",
                    buttonType = filledButton(),
                    events = {
                        DisplayDialog(
                            trigger = EventTriggers.onClick(),
                            dialogId = DEMO_DIALOG_ID,
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
                                        text = "O botão abaixo é o próprio DismissDialog sendo demonstrado.",
                                        typography = typographyBodyMedium(),
                                        color = color(themeColorOnSurfaceVariant())
                                    )
                                    Button(
                                        text = "Fechar com DismissDialog",
                                        buttonType = filledTonalButton(),
                                        events = {
                                            DismissDialog(
                                                trigger = EventTriggers.onClick(),
                                                dialogId = DEMO_DIALOG_ID
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
                "Como os overlays são empilháveis, o dialogId é o que garante que o dialog certo seja " +
                    "fechado quando há mais de um aberto — guarde o mesmo id nos dois lados do par."
            )

            ShowroomRelated(
                names = listOf("DisplayDialog", "DismissModalBottomSheet", "DismissNavigationDrawer", "DismissSnackbar"),
                destination = "eventDetails"
            )
        }
    }
}
