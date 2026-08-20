package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.SelectionContainer
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyLabelLarge
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

object SelectionContainerTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "SelectionContainer"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Wraps child tiles in Compose's SelectionContainer, letting text be selected and " +
                    "copied across multiple tiles as one contiguous selection. It has no arrangement/alignment " +
                    "or layout of its own — it's a transparent wrapper. It doesn't fire onClick/onLongPress/" +
                    "onDisplay: long-press is left free for Compose's own text-selection gesture."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Press and drag to select text across the two SimpleText below") {
                SelectionContainer {
                    Column(arrangement = arrangeVerticallySpacedBy(4)) {
                        SimpleText(text = "Mosaic SDUI Framework", typography = typographyTitleMedium())
                        SimpleText(
                            text = "showroom@mosaic.dev — copy this text along with the title above",
                            typography = typographyLabelLarge(),
                            color = color(themeColorOnSurfaceVariant())
                        )
                    }
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                SelectionContainer(id = "profile_details") {
                    SimpleText(id = "profile_name", text = user.name)
                    SimpleText(id = "profile_email", text = user.email)
                }
                """
            )

            ShowroomRelated(
                names = listOf("Column", "SimpleText"),
                destination = "tileDetails"
            )
        }
    }
}
