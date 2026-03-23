package dev.catbit.mosaic.endpoints.screen.screens

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.data.schemas.tile.style.ShapeSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.IconButtonTileSchema
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorBackground
import dev.catbit.mosaic.server.builder.color.themeColorError
import dev.catbit.mosaic.server.builder.color.themeColorOnSecondary
import dev.catbit.mosaic.server.builder.color.themeColorOutline
import dev.catbit.mosaic.server.builder.color.themeColorPrimary
import dev.catbit.mosaic.server.builder.color.themeColorSecondary
import dev.catbit.mosaic.server.builder.color.themeColorSurface
import dev.catbit.mosaic.server.builder.event.builders.networking.SendNetworkRequest
import dev.catbit.mosaic.server.builder.event.builders.screen.RefreshScreen
import dev.catbit.mosaic.server.builder.icon
import dev.catbit.mosaic.server.builder.placement.alignHorizontallyToCenter
import dev.catbit.mosaic.server.builder.placement.alignToCenter
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallyToTop
import dev.catbit.mosaic.server.builder.roundedIcon
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Grid
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.AsyncImage
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.image.fillWidthContentScale
import dev.catbit.mosaic.server.builder.tile.builders.image.insideContentScale
import dev.catbit.mosaic.server.builder.tile.builders.inputs.Checkbox
import dev.catbit.mosaic.server.builder.tile.builders.inputs.RadioButton
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall

suspend fun RoutingCall.respondHome() {
    respond(
        Screen(
            id = "home",
            tiles = {
                Column(
                    arrangement = arrangeVerticallyToTop(),
                    alignment = alignHorizontallyToCenter(),
                    tiles = {
                        Button(
                            text = "Reload",
                            events = {
                                RefreshScreen(
                                    trigger = EventTriggers.onClick()
                                )
                            },
                            style = {
                                margin(horizontal = 24)
                            }
                        )
                        Button(
                            text = "Make network call",
                            events = {
                                SendNetworkRequest(
                                    trigger = EventTriggers.onClick(),
                                    url = "https://naas.isalman.dev/no",
                                    method = HttpMethod.GET
                                )
                            },
                            style = {
                                margin(horizontal = 24)
                            }
                        )
                        IconButton(
                            icon = icon(
                                name = "add_circle",
                                style = IconSchema.Style.ROUNDED
                            ),
                            buttonType = IconButtonTileSchema.Type.FILLED
                        )
                        Grid(
                            columns = 2,
                            gutter = 2,
                            style = {
                                margin(horizontal = 24)
                            },
                            tiles = {
                                Box(
                                    style = {
                                        size(
                                            width = spanHorizontally(1),
                                            height = fixedVertically(56)
                                        )
                                        background(
                                            color = color(themeColorPrimary())
                                        )
                                    },
                                    tiles = {}
                                )
                                Box(
                                    style = {
                                        size(
                                            width = spanHorizontally(1),
                                            height = fixedVertically(56)
                                        )
                                        background(
                                            color = color(themeColorSecondary())
                                        )
                                        clip(circleShape())
                                    },
                                    alignment = alignToCenter(),
                                    tiles = {
                                        SimpleText(
                                            text = "Recarregar",
                                            color = color(themeColorOnSecondary()),
                                            style = {
                                                size(
                                                    width = wrapHorizontally()
                                                )
                                            }
                                        )
                                    },
                                    events = {
                                        RefreshScreen(
                                            trigger = EventTriggers.onClick()
                                        )
                                    }
                                )
                            }
                        )
                        AsyncImage(
                            url = "https://content.imageresizer.com/images/memes/Sad-cat-tears-meme-7.jpg",
                            style = {
                                size(
                                    height = fixedVertically(100)
                                )
                            },
                            contentScale = insideContentScale()
                        )
                        Icon(
                            icon = icon(
                                name = "home",
                                size = 64,
                                color = color(themeColorError()),
                                style = roundedIcon()
                            )
                        )
                        SimpleText(text = "Do que você mais gosta?")
                        Row(
                            alignment = alignVerticallyToCenter(),
                            tiles = {
                                Checkbox()
                                SimpleText(text = "Estudar")
                            }
                        )
                        Row(
                            alignment = alignVerticallyToCenter(),
                            tiles = {
                                Checkbox()
                                SimpleText(text = "Batata")
                            }
                        )
                        SimpleText(text = "Como deseja receber seu salário")
                        Row(
                            alignment = alignVerticallyToCenter(),
                            tiles = {
                                RadioButton(
                                    groupId = "salario",
                                    selected = true
                                )
                                SimpleText(text = "Em euros")
                            }
                        )
                        Row(
                            alignment = alignVerticallyToCenter(),
                            tiles = {
                                RadioButton(
                                    groupId = "salario"
                                )
                                SimpleText(text = "Em reais")
                            }
                        )
                    },
                    isScrollable = true,
                    style = {
                        windowInsets(windowInsetsSystemBars())
                        size(
                            width = fillHorizontally(),
                            height = fillVertically()
                        )
                    }
                )
            }
        )
    )
}