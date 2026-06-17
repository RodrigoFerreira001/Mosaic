package dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLongPressEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 button in one of five visual styles controlled by [buttonType].
 *
 * **Updatable fields (via UpdateTiles):** `text`, `icon`, `buttonType`, `shape`, `loading`,
 * `enabled`, `visibility`, `style`.
 *
 * **Triggers dispatched:**
 * - [OnClickEventTrigger] — fired when the user taps the button (only when [loading] is false,
 *   because the content slot shows a spinner and the user cannot distinguish a click target).
 * - [OnLongPressEventTrigger] — declared via `@Triggers` but not explicitly wired in the
 *   current renderer; available for future use or custom gesture handling.
 *
 * **Notes:** When [loading] is `true` the text and icon are replaced by a [CircularProgressIndicator];
 * the button itself remains pressable (the `enabled` flag is the sole interactivity gate).
 * [icon] position relative to [text] is controlled by [iconPosition]: `START` renders the icon
 * before the text, `END` renders it after. [shape] maps `SQUARE` to [MaterialTheme.shapes.medium]
 * and `ROUNDED` to [CircleShape]. The renderer always fires [OnClickEventTrigger] via
 * `triggerEvent(EventTriggers.onClick())` regardless of the [buttonType] variant.
 */
@Immutable
@Triggers(
    [
        OnClickEventTrigger::class,
        OnLongPressEventTrigger::class
    ]
)
@Serializable
@SerialName("Button")
data class ButtonTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("text") val text: String,
    @SerialName("icon") val icon: IconSchema? = null,
    @SerialName("buttonType") val buttonType: Type = Type.FILLED,
    @SerialName("shape") val shape: Shape = Shape.ROUNDED,
    @SerialName("loading") val loading: Boolean = false,
    @SerialName("enabled") val enabled: Boolean = true,
    @SerialName("iconPosition") val iconPosition: IconPosition = IconPosition.START
) : TileSchema {

    enum class Type {
        FILLED,
        ELEVATED,
        FILLED_TONAL,
        OUTLINED,
        TEXT
    }

    enum class Shape {
        SQUARE, ROUNDED
    }

    enum class IconPosition {
        START, END
    }
}