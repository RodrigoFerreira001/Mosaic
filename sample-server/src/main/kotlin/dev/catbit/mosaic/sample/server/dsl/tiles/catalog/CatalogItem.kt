package dev.catbit.mosaic.sample.server.dsl.tiles.catalog

import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurface
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainer
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Card
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodySmall
import dev.catbit.mosaic.server.builder.typography.typographyTitleSmall

/**
 * A single card in the tiles/events catalog: an icon representing the item, its name, and an
 * optional category caption, wrapped in a rounded (16dp) [Card] that carries [searchableTerms]
 * so the parent list can be filtered by the screen's [dev.catbit.mosaic.server.builder.tile.builders.search.SearchBar].
 */
fun TileSchemaBuilderScope.CatalogItem(
    name: String,
    iconName: String,
    category: String? = null,
    id: String = randomId(),
) {
    Card(
        id = id,
        style = {
            size(width = fillHorizontally(), height = wrapVertically())
            clip(roundedCornerShape(all = 16))
            background(color(themeColorSurfaceContainer()))
        },
        searchableTerms = listOfNotNull(name, category),
    ) {
        Row(
            style = {
                size(width = fillHorizontally(), height = wrapVertically())
                padding(horizontal = 16, vertical = 12)
            },
            arrangement = arrangeHorizontallySpacedBy(16),
            alignment = alignVerticallyToCenter()
        ) {
            Icon(
                icon = icon(
                    name = iconName,
                    size = 24,
                    color = color(themeColorOnSurface())
                )
            )
            Column(
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                }
            ) {
                SimpleText(
                    text = name,
                    typography = typographyTitleSmall()
                )
                if (category != null) {
                    SimpleText(
                        text = category,
                        typography = typographyBodySmall(),
                        style = {
                            size(width = wrapHorizontally(), height = wrapVertically())
                            margin(top = 2)
                        }
                    )
                }
            }
        }
    }
}
