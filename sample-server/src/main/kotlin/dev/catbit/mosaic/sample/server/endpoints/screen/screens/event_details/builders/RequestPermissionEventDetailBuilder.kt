package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.events.security.RequestPermissionEventSchema.Permissions
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParagraph
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParam
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParamsTable
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.security.RequestPermission
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object RequestPermissionEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "RequestPermission"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Security",
                description = "Pede uma ou mais permissões de runtime usando o mecanismo nativo de cada plataforma (ActivityResultRegistry no Android, AVFoundation/CoreLocation no iOS, Permissions API na web)."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use antes de acessar câmera, microfone, localização, notificações, contatos ou " +
                    "galeria/armazenamento. O app consumidor precisa declarar a permissão no " +
                    "AndroidManifest.xml (Android) e nas usage description keys do Info.plist (iOS) — " +
                    "a ausência de uma key no iOS derruba o app em runtime."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam(
                        "permissions",
                        "List<Permissions>",
                        "Obrigatório. CAMERA, GALLERY, STORAGE, MICROPHONE, LOCATION, NOTIFICATION, CONTACTS."
                    ),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                RequestPermission(
                    trigger = EventTriggers.onClick(),
                    permissions = listOf(Permissions.NOTIFICATION),
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onPermissionsAcquired(),
                            updates = { /* ... */ }
                        )
                        UpdateTiles(
                            trigger = EventTriggers.onPermissionsDenied(),
                            updates = { /* ... */ }
                        )
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Pede a permissão de notificação de verdade") {
                SimpleText(
                    id = "request_permission_status",
                    text = "Status: aguardando"
                )
                Button(
                    text = "Pedir permissão NOTIFICATION",
                    events = {
                        RequestPermission(
                            trigger = EventTriggers.onClick(),
                            permissions = listOf(Permissions.NOTIFICATION),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onPermissionsAcquired(),
                                    updates = {
                                        update(
                                            tileId = "request_permission_status",
                                            updateData = inlineTileUpdateData("text" to "Status: concedida ✅")
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onPermissionRationale(),
                                    updates = {
                                        update(
                                            tileId = "request_permission_status",
                                            updateData = inlineTileUpdateData("text" to "Status: mostre uma explicação e peça de novo")
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onPermissionsDenied(),
                                    updates = {
                                        update(
                                            tileId = "request_permission_status",
                                            updateData = inlineTileUpdateData("text" to "Status: negada ❌")
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
            }

            ShowroomRelated(
                names = listOf("TakePicture", "GetImageFromGallery", "OpenExternalLink"),
                destination = "eventDetails"
            )
        }
    }
}
