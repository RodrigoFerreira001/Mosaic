package dev.catbit.mosaic.sample.server.endpoints.lazy_tiles

import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.CardTileSchema
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Card
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodySmall
import dev.catbit.mosaic.server.builder.typography.typographyTitleSmall
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.coroutines.delay

/**
 * Backs the LazyTiles showroom demo: a real endpoint returning a JSON array of tile schemas, the
 * exact shape a `LazyTiles` tile's `url` is expected to resolve to. A short artificial delay
 * keeps the demo's placeholder shimmer actually visible; `?fail=true` returns a server error so
 * the demo's failureTiles branch can be exercised too.
 */
fun Route.lazyTiles() {
    get("lazy-tiles/recommendations") {
        delay(900)

        if (call.request.queryParameters["fail"] == "true") {
            call.respond(HttpStatusCode.InternalServerError)
            return@get
        }

        val recommendations = listOf(
            "Mountain trail" to "12 km · Moderate",
            "Coastal walk" to "6 km · Easy",
            "Forest loop" to "18 km · Hard",
            "River path" to "9 km · Easy"
        )

        val tiles = TileSchemaBuilderScope().apply {
            recommendations.forEach { (title, subtitle) ->
                Card(
                    kind = CardTileSchema.Kind.OUTLINED,
                    style = { size(width = fillHorizontally(), height = wrapVertically()) }
                ) {
                    Row(
                        style = { size(width = fillHorizontally(), height = wrapVertically()); padding(horizontal = 16, vertical = 16) },
                        arrangement = arrangeHorizontallySpacedBy(12),
                        alignment = alignVerticallyToCenter()
                    ) {
                        Icon(icon = icon("hiking"))
                        Column(arrangement = arrangeVerticallySpacedBy(2)) {
                            SimpleText(text = title, typography = typographyTitleSmall())
                            SimpleText(text = subtitle, typography = typographyBodySmall(), color = color(themeColorOnSurfaceVariant()))
                        }
                    }
                }
            }
        }.build()

        call.respond(tiles)
    }
}
