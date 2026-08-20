package dev.catbit.mosaic.sample.server.dsl.tiles.showroom

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.sample.core.schemas.tiles.code.CodeViewerTileSchema
import dev.catbit.mosaic.sample.server.dsl.tiles.code.CodeViewer
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorOnTertiaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerHighest
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerLow
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
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyBodySmall
import dev.catbit.mosaic.server.builder.typography.typographyHeadlineSmall
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

///**
// * Shared "showroom" building blocks used by every Tile/Event detail screen
// * (`screens/tile_details/builders/*.kt` and `screens/event_details/builders/*.kt`), so all ~114
// * detail pages share one visual language: description → live demo → code sample → related —
// * a single trailing app-bar action (wired by the two detail screen shells, see `DokkaLinks.kt`)
// * links out to the matching Dokka page for the DSL builder function being demoed.
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

/**
 * The description block: a single accurate, user-friendly paragraph explaining what the tile/event
 * does. Rendered directly below the detail screen's app bar — no heading, no category chip.
 */
fun TileSchemaBuilderScope.ShowroomHero(description: String) {
    SimpleText(
        text = description,
        typography = typographyBodyMedium(),
        color = color(themeColorOnSurfaceVariant())
    )
}

/** Section heading, e.g. "Interactive demo", "Code sample". */
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
    title: String = "Interactive demo",
    id: String = randomId(),
    content: TileSchemaBuilderScope.() -> Unit,
) {
    Column(
        style = { size(width = fillHorizontally(), height = wrapVertically()) },
        arrangement = arrangeVerticallySpacedBy(12)
    ) {
        SimpleText(text = title, typography = typographyTitleMedium())
        Column(
            id = id,
            style = {
                size(width = fillHorizontally(), height = wrapVertically())
                clip(roundedCornerShape(all = 20))
                background(color(themeColorSurfaceContainerLow()))
                padding(horizontal = 20, vertical = 20)
            },
            arrangement = arrangeVerticallySpacedBy(16),
            tiles = content
        )
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
fun TileSchemaBuilderScope.ShowroomRelated(title: String = "Related", names: List<String>, destination: String) {
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

/**
 * A single skeleton placeholder shape for a [dev.catbit.mosaic.server.builder.tile.builders.grouping.Shimmer]
 * composition — a plain rounded (or circular) block, not a tile of its own. [width] fills the
 * available width when `null` (e.g. a full-width text line placeholder); [height] is always fixed.
 */
fun TileSchemaBuilderScope.SkeletonBlock(width: Int?, height: Int, circular: Boolean = false, id: String = randomId()) {
    Box(
        id = id,
        style = {
            size(
                width = width?.let { fixedHorizontally(it) } ?: fillHorizontally(),
                height = fixedVertically(height)
            )
            clip(if (circular) circleShape() else roundedCornerShape(all = 6))
            background(color(themeColorSurfaceContainerHighest()))
        }
    ) {}
}

/** A skeleton list row: a circular avatar block next to a title-line block, for shimmering lists. */
fun TileSchemaBuilderScope.SkeletonListEntry(id: String = randomId()) {
    Row(
        id = id,
        style = { size(width = fillHorizontally(), height = fixedVertically(40)) },
        arrangement = arrangeHorizontallySpacedBy(16),
        alignment = alignVerticallyToCenter()
    ) {
        SkeletonBlock(width = 40, height = 40, circular = true)
        SkeletonBlock(width = 160, height = 24)
    }
}

/**
 * A yellow warning pill for a hero header's colored strip — flags a showroom page whose content
 * is still actively evolving. Place as the sole child of the hero's colored [Box], with
 * `alignment = alignToTopEnd()` (or another corner), so this pill's own size determines its
 * position via that Box's `contentAlignment`.
 */
fun TileSchemaBuilderScope.UnderConstructionBadge(id: String = randomId()) {
    Row(
        id = id,
        style = {
            clip(roundedCornerShape(all = 50))
            background(color("#FFC107"))
            padding(horizontal = 12, vertical = 6)
            margin(horizontal = 12, vertical = 12)
        },
        arrangement = arrangeHorizontallySpacedBy(6),
        alignment = alignVerticallyToCenter()
    ) {
        Icon(icon = icon(name = "engineering", size = 16, color = color("#000000")))
        SimpleText(
            text = "Under construction: This may change at anytime",
            typography = typographyBodySmall(),
            color = color("#000000")
        )
    }
}
