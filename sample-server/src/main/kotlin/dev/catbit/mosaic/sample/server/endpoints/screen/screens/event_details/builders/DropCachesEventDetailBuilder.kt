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
import dev.catbit.mosaic.server.builder.event.builders.system.DropCaches
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.FlowRow
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object DropCachesEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DropCaches"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "System",
                description = "Descarta caches locais persistidos pelo Mosaic — telas, grafo inicial de navegação e/ou versão de cache — cada um controlado por um flag booleano independente."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Útil em fluxos de logout, troca de ambiente/tenant, ou botões de \"forçar " +
                    "atualização\" em telas de configuração/debug. Cada flag é independente: você pode " +
                    "descartar só o cache de telas sem mexer no grafo inicial, por exemplo."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("dropScreensCache", "Boolean", "Descarta todo ScreenResponse cacheado localmente."),
                    ShowroomParam("dropInitialGraphCache", "Boolean", "Descarta o grafo de navegação inicial cacheado."),
                    ShowroomParam("dropVersionCache", "Boolean", "Descarta a versão de cache-busting armazenada localmente."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                DropCaches(
                    trigger = EventTriggers.onClick(),
                    dropScreensCache = true,
                    dropInitialGraphCache = true,
                    dropVersionCache = true,
                    events = {
                        RefreshScreen(trigger = EventTriggers.onSuccess())
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Cada botão dispara DropCaches com uma combinação de flags") {
                SimpleText(
                    id = "drop_caches_status",
                    text = "Status: nenhum cache descartado ainda"
                )
                FlowRow(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    horizontalArrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Só dropScreensCache",
                        buttonType = outlinedButton(),
                        events = { dropCachesDemo("dropScreensCache = true", dropScreensCache = true, dropInitialGraphCache = false, dropVersionCache = false) }
                    )
                    Button(
                        text = "Só dropInitialGraphCache",
                        buttonType = outlinedButton(),
                        events = { dropCachesDemo("dropInitialGraphCache = true", dropScreensCache = false, dropInitialGraphCache = true, dropVersionCache = false) }
                    )
                    Button(
                        text = "Só dropVersionCache",
                        buttonType = outlinedButton(),
                        events = { dropCachesDemo("dropVersionCache = true", dropScreensCache = false, dropInitialGraphCache = false, dropVersionCache = true) }
                    )
                    Button(
                        text = "Limpar tudo",
                        events = { dropCachesDemo("os 3 flags = true", dropScreensCache = true, dropInitialGraphCache = true, dropVersionCache = true) }
                    )
                }
            }

            ShowroomNote(
                "DropCaches é destrutivo: force o reload das telas afetadas (RefreshScreen/GetScreen) " +
                    "depois de descartar, senão a UI pode continuar mostrando dados obsoletos até a " +
                    "próxima navegação."
            )

            ShowroomRelated(
                names = listOf("RefreshScreen", "GetScreen", "BroadcastToSystem"),
                destination = "eventDetails"
            )
        }
    }
}

private fun dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope.dropCachesDemo(
    label: String,
    dropScreensCache: Boolean,
    dropInitialGraphCache: Boolean,
    dropVersionCache: Boolean,
) {
    DropCaches(
        trigger = EventTriggers.onClick(),
        dropScreensCache = dropScreensCache,
        dropInitialGraphCache = dropInitialGraphCache,
        dropVersionCache = dropVersionCache,
        events = {
            UpdateTiles(
                trigger = EventTriggers.onSuccess(),
                updates = {
                    update(
                        tileId = "drop_caches_status",
                        updateData = inlineTileUpdateData("text" to "Status: caches descartados ($label)")
                    )
                }
            )
        }
    )
}
