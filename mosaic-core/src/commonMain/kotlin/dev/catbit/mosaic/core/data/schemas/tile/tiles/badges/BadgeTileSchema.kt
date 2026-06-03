package dev.catbit.mosaic.core.data.schemas.tile.tiles.badges

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders a Material 3 [Badge] — a small colored dot or pill typically used to indicate a
 * notification count or status. When [content] is non-null, the text is displayed inside the
 * badge pill. When [content] is `null`, only a plain dot badge is rendered.
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `content: String?`
 *
 * **Triggers dispatched:** None. This tile does not dispatch any event triggers.
 *
 * **Notes:** The badge is typically used as a child inside a [BadgedBox] composable provided
 * by the parent tile. It is not interactive by itself. To remove the badge, either hide it
 * via `visibility` or clear [content] and rely on a parent container to control its placement.
 */
@Serializable
@SerialName("Badge")
data class BadgeTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("content") val content: String? = null
) : TileSchema
