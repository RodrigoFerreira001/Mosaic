package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignHorizontallyToCenter
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyHeadlineSmall

/** Shared "not implemented yet" placeholder rendered by [EventDetailBuilder]s without a real demo. */
internal fun TileSchemaBuilderScope.NotImplementedYetDetail(eventName: String) {
    Column(
        id = "events_catalog_detail_root",
        style = {
            size(width = fillHorizontally(), height = fillVertically())
            padding(horizontal = 24)
        },
        alignment = alignHorizontallyToCenter(),
        arrangement = arrangeVerticallySpacedBy(
            space = 8,
            alignment = alignVerticallyToCenter()
        )
    ) {
        Icon(
            icon = icon(
                name = "construction",
                size = 48,
                color = color(themeColorOnSurfaceVariant())
            )
        )
        SimpleText(
            text = eventName,
            typography = typographyHeadlineSmall()
        )
        SimpleText(
            text = "Demo ainda não implementada para este item.",
            typography = typographyBodyMedium()
        )
    }
}
