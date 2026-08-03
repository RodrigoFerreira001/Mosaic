package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.snackbar.SnackbarDurationSchema
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
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DismissSnackbar
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton

object DismissSnackbarEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DismissSnackbar"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Overlays",
                description = "Fecha programaticamente o snackbar exibido no momento — útil pra encerrar um " +
                    "snackbar Indefinite depois que uma operação assíncrona termina."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use principalmente com snackbars de duration = Indefinite, que não somem sozinhos — por " +
                    "exemplo, um \"Enviando...\" que precisa ser fechado manualmente assim que a requisição " +
                    "terminar (sucesso ou falha). Importante: DismissSnackbar NÃO dispara " +
                    "onSnackbarDismissed() — esse trigger é reservado pro fechamento natural (timeout, swipe " +
                    "ou toque fora), não pro fechamento programático."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("—", "—", "DismissSnackbar não recebe parâmetros além de trigger/events."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                SendNetworkRequest(
                    trigger = EventTriggers.onClick(),
                    url = "/api/upload",
                    method = HttpMethod.POST,
                    events = {
                        DisplaySnackbar(trigger = EventTriggers.onStart(), message = "Enviando...", duration = SnackbarDurationSchema.Indefinite)
                        DismissSnackbar(trigger = EventTriggers.onSuccess())
                        DismissSnackbar(trigger = EventTriggers.onFailure())
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Abra um snackbar indefinido e feche-o manualmente") {
                Button(
                    text = "Abrir snackbar Indefinite",
                    events = {
                        DisplaySnackbar(
                            trigger = EventTriggers.onClick(),
                            message = "Este snackbar não some sozinho — feche-o abaixo",
                            duration = SnackbarDurationSchema.Indefinite
                        )
                    }
                )
                Button(
                    text = "Fechar com DismissSnackbar",
                    buttonType = outlinedButton(),
                    events = {
                        DismissSnackbar(trigger = EventTriggers.onClick())
                    }
                )
                ShowroomNote(
                    "Abra o snackbar indefinido primeiro; ele fica na tela até você tocar em \"Fechar com " +
                        "DismissSnackbar\" — repare que fechar assim não é o mesmo que deixá-lo sumir " +
                        "naturalmente, já que onSnackbarDismissed() só dispara nesse segundo caso."
                )
            }

            ShowroomRelated(
                names = listOf("DisplaySnackbar", "DismissDialog", "DismissBottomSheet"),
                destination = "eventDetails"
            )
        }
    }
}
