package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.CardTileSchema
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Card
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodySmall
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

object CardTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Card"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A Material 3 container with elevation, shape, and theme color — groups related " +
                    "content with visual emphasis. The click is always \"on\" in a Card, even without registered " +
                    "events — nothing visible just happens if no event is listening to onClick. Internally it " +
                    "uses ColumnScope, so children can use Column modifiers."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "The 3 kinds side by side — all really clickable") {
                Row(arrangement = arrangeHorizontallySpacedBy(12)) {
                    listOf(
                        CardTileSchema.Kind.DEFAULT to "Default",
                        CardTileSchema.Kind.ELEVATED to "Elevated",
                        CardTileSchema.Kind.OUTLINED to "Outlined",
                    ).forEach { (kind, label) ->
                        Card(
                            kind = kind,
                            style = { size(width = weightHorizontally(1f), height = wrapVertically()) },
                            events = {
                                DisplaySnackbar(trigger = EventTriggers.onClick(), message = "Card $label clicked")
                            }
                        ) {
                            SimpleText(
                                text = label,
                                typography = typographyTitleMedium(),
                                style = { size(width = fillHorizontally(), height = wrapVertically()); padding(horizontal = 16, vertical = 16) }
                            )
                            SimpleText(
                                text = "kind = CardTileSchema.Kind.${kind.name}",
                                typography = typographyBodySmall(),
                                color = color(themeColorOnSurfaceVariant()),
                                style = { size(width = fillHorizontally(), height = wrapVertically()); padding(horizontal = 16, bottom = 16) }
                            )
                        }
                    }
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Card(
                    id = "env_card_${'$'}{env.id}",
                    kind = CardTileSchema.Kind.OUTLINED,
                    events = {
                        Navigate(trigger = EventTriggers.onClick(), navigatorId = "root", destination = "env_detail")
                    }
                ) {
                    SimpleText(text = env.name, typography = typographyTitleMedium())
                    SimpleText(text = env.url, color = color(themeColorOnSurfaceVariant()))
                }
                """
            )

            ShowroomRelated(
                names = listOf("Column", "Box", "Carousel"),
                destination = "tileDetails"
            )
        }
    }
}
