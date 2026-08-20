package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.events.security.RequestPermissionEventSchema.Permissions
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
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
                description = "Requests one or more runtime permissions using each platform's native " +
                    "mechanism (ActivityResultRegistry on Android, AVFoundation/CoreLocation on iOS, the " +
                    "Permissions API on the web). Use it before accessing the camera, microphone, location, " +
                    "notifications, contacts, or gallery/storage. The consuming app must declare the " +
                    "permission in AndroidManifest.xml (Android) and in the usage description keys of " +
                    "Info.plist (iOS) — a missing key on iOS crashes the app at runtime."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Request the real notification permission") {
                SimpleText(
                    id = "request_permission_status",
                    text = "Status: waiting"
                )
                Button(
                    text = "Request NOTIFICATION permission",
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
                                            updateData = inlineTileUpdateData("text" to "Status: granted ✅")
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onPermissionRationale(),
                                    updates = {
                                        update(
                                            tileId = "request_permission_status",
                                            updateData = inlineTileUpdateData("text" to "Status: show a rationale and ask again")
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onPermissionsDenied(),
                                    updates = {
                                        update(
                                            tileId = "request_permission_status",
                                            updateData = inlineTileUpdateData("text" to "Status: denied ❌")
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
            }

            ShowroomSectionTitle("Code sample")
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

            ShowroomRelated(
                names = listOf("TakePicture", "GetImageFromGallery", "OpenExternalLink"),
                destination = "eventDetails"
            )
        }
    }
}
