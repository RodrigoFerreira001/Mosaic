package dev.catbit.mosaic.sample.server.dsl.tiles.showroom

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.sample.core.schemas.tiles.code.CodeViewerTileSchema
import dev.catbit.mosaic.sample.server.dsl.tiles.code.CodeViewer
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorOnTertiaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorOnPrimaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorPrimaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainer
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerLowest
import dev.catbit.mosaic.server.builder.color.themeColorTertiaryContainer
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.navigation.Navigate
import dev.catbit.mosaic.server.builder.event.builders.networking.SetIncomingDataToNetworkParamsHolderQueryParameters
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.chips.AssistChip
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Card
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyBodySmall
import dev.catbit.mosaic.server.builder.typography.typographyHeadlineSmall
import dev.catbit.mosaic.server.builder.typography.typographyLabelLarge
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleSmall

///**
// * Shared "showroom" building blocks used by every Tile/Event detail screen
// * (`screens/tile_details/builders/*.kt` and `screens/event_details/builders/*.kt`), so all ~100
// * detail pages share one visual language: hero → overview → params → code → live demo → notes —
// * the same pattern Material Design's own component docs follow.
// */

/** Root scaffold every detail builder wraps its content in. */
fun TileSchemaBuilderScope.ShowroomScaffold(content: TileSchemaBuilderScope.() -> Unit) {
    Column(
        id = "showroom_detail_root",
        style = {
            size(width = fillHorizontally(), height = fillVertically())
            background(color(themeColorSurfaceContainerLowest()))
            padding(horizontal = 24, top = 24, bottom = 40)
        },
        arrangement = arrangeVerticallySpacedBy(28),
        scrollable = true,
        tiles = content
    )
}

/** Hero block: category pill + name + one-sentence purpose. */
fun TileSchemaBuilderScope.ShowroomHero(
    category: String,
    description: String,
) {
    Column(
        style = { size(width = fillHorizontally(), height = wrapVertically()) },
        arrangement = arrangeVerticallySpacedBy(8)
    ) {
        Box(
            style = {
                size(width = wrapHorizontally(), height = wrapVertically())
                clip(roundedCornerShape(all = 100))
                background(color(themeColorPrimaryContainer()))
                padding(horizontal = 12, vertical = 4)
            }
        ) {
            SimpleText(
                text = category,
                typography = typographyLabelLarge(),
                color = color(themeColorOnPrimaryContainer())
            )
        }
        SimpleText(
            text = description,
            typography = typographyBodyMedium(),
            color = color(themeColorOnSurfaceVariant())
        )
    }
}

/** Section heading, e.g. "Visão geral", "Parâmetros", "Exemplo de código", "Demo interativa". */
fun TileSchemaBuilderScope.ShowroomSectionTitle(text: String) {
    SimpleText(
        text = text,
        typography = typographyHeadlineSmall()
    )
}

/** A body paragraph under a section title. */
fun TileSchemaBuilderScope.ShowroomParagraph(text: String) {
    SimpleText(
        text = text,
        typography = typographyBodyMedium(),
        color = color(themeColorOnSurfaceVariant())
    )
}

data class ShowroomParam(
    val name: String,
    val type: String,
    val notes: String,
)

/** Anatomy/parameters table — one row per builder field, mirroring the reference docs. */
fun TileSchemaBuilderScope.ShowroomParamsTable(params: List<ShowroomParam>) {
    Column(
        style = {
            size(width = fillHorizontally(), height = wrapVertically())
            clip(roundedCornerShape(all = 16))
            background(color(themeColorSurfaceContainer()))
        }
    ) {
        params.forEachIndexed { index, param ->
            Column(
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                    padding(horizontal = 16, vertical = 12)
                    if (index < params.lastIndex) {
                        border(color = color(themeColorSurfaceContainerLowest()), thickness = 1)
                    }
                },
                arrangement = arrangeVerticallySpacedBy(2)
            ) {
                Row(arrangement = arrangeHorizontallySpacedBy(8), alignment = alignVerticallyToCenter()) {
                    SimpleText(text = param.name, typography = typographyTitleSmall())
                    SimpleText(
                        text = param.type,
                        typography = typographyBodySmall(),
                        color = color(themeColorOnSurfaceVariant())
                    )
                }
                SimpleText(
                    text = param.notes,
                    typography = typographyBodySmall(),
                    color = color(themeColorOnSurfaceVariant())
                )
            }
        }
    }
}

/** Code sample block, dark syntax-highlighted viewer. */
fun TileSchemaBuilderScope.ShowroomCode(code: String, id: String = randomId()) {
    CodeViewer(
        id = id,
        code = code.trimIndent(),
        language = CodeViewerTileSchema.Language.KOTLIN,
        theme = CodeViewerTileSchema.Theme.ATOM_ONE,
        style = {
            size(width = fillHorizontally(), height = wrapVertically())
        }
    )
}

/**
 * Wraps the actual live, interactive tiles/events being demoed inside a distinct card, so it's
 * visually obvious the reader can tap/type and see the framework react for real — not a picture
 * of a demo, but the demo itself, rendered by the very framework it documents.
 */
fun TileSchemaBuilderScope.ShowroomDemoCard(
    title: String = "Demo interativa",
    id: String = randomId(),
    content: TileSchemaBuilderScope.() -> Unit,
) {
    Column(
        style = { size(width = fillHorizontally(), height = wrapVertically()) },
        arrangement = arrangeVerticallySpacedBy(12)
    ) {
        SimpleText(text = title, typography = typographyTitleMedium())
        Card(
            id = id,
            style = {
                size(width = fillHorizontally(), height = wrapVertically())
                clip(roundedCornerShape(all = 20))
            }
        ) {
            Column(
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                    padding(horizontal = 20, vertical = 20)
                },
                arrangement = arrangeVerticallySpacedBy(16),
                tiles = content
            )
        }
    }
}

/** Small callout for platform quirks, gotchas, or related-trigger notes. */
fun TileSchemaBuilderScope.ShowroomNote(text: String) {
    Row(
        style = {
            size(width = fillHorizontally(), height = wrapVertically())
            clip(roundedCornerShape(all = 12))
            background(color(themeColorTertiaryContainer()))
            padding(horizontal = 12, vertical = 12)
        },
        arrangement = arrangeHorizontallySpacedBy(10)
    ) {
        Icon(
            icon = icon(
                name = "info",
                size = 20,
                color = color(themeColorOnTertiaryContainer())
            )
        )
        SimpleText(
            text = text,
            typography = typographyBodySmall(),
            color = color(themeColorOnTertiaryContainer()),
            style = { size(width = fillHorizontally(), height = wrapVertically()) }
        )
    }
}

/** Row of clickable "related" chips linking to other catalog entries (tile or event names). */
fun TileSchemaBuilderScope.ShowroomRelated(title: String = "Relacionados", names: List<String>, destination: String) {
    if (names.isEmpty()) return
    Column(
        style = { size(width = fillHorizontally(), height = wrapVertically()) },
        arrangement = arrangeVerticallySpacedBy(12)
    ) {
        SimpleText(text = title, typography = typographyTitleMedium())
        Row(
            style = { size(width = fillHorizontally(), height = wrapVertically()) },
            arrangement = arrangeHorizontallySpacedBy(8)
        ) {
            names.forEach { relatedName ->
                ShowroomRelatedChip(name = relatedName, destination = destination)
            }
        }
    }
}

private fun TileSchemaBuilderScope.ShowroomRelatedChip(name: String, destination: String) {
    // Same navigation pattern as CatalogItem: stage the name as the next "event" query
    // param and navigate to the given details screen.
    AssistChip(
        text = name,
        events = {
            TransformData(
                trigger = EventTriggers.onClick(),
                template = mapOf("event" to name),
                events = {
                    SetIncomingDataToNetworkParamsHolderQueryParameters(
                        trigger = EventTriggers.onSuccess(),
                        events = {
                            Navigate(
                                trigger = EventTriggers.onSuccess(),
                                navigatorId = "root",
                                destination = destination
                            )
                        }
                    )
                }
            )
        }
    )
}
