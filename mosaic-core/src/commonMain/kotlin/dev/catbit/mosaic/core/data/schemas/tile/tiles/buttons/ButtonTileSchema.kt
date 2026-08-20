package dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 button. The concrete composable is picked by [buttonType]:
 * [Type.FILLED] → `Button`, [Type.ELEVATED] → `ElevatedButton`, [Type.FILLED_TONAL] →
 * `FilledTonalButton`, [Type.OUTLINED] → `OutlinedButton`, [Type.TEXT] → `TextButton`.
 *
 * **Content:** when [loading] is `true` the button shows a 24dp `CircularProgressIndicator`
 * (2dp stroke, round cap, tinted with `LocalContentColor`) instead of its normal content.
 * Otherwise it renders [text] together with the optional [icon], ordered by [iconPosition]
 * ([IconPosition.START] → icon, 8dp spacer, text; [IconPosition.END] → text, 8dp spacer, icon).
 *
 * **Shape:** [Shape.SQUARE] maps to `MaterialTheme.shapes.medium`, [Shape.ROUNDED] to
 * `CircleShape`.
 *
 * **Enabled state:** the button is interactive only when [enabled] is `true` **and** [loading]
 * is `false` — the two are combined into the single `enabled` the Material button receives, so
 * a loading button is disabled for real: it takes Material's disabled colors and is reported as
 * disabled to accessibility services. A slow action therefore cannot be submitted twice.
 *
 * **Triggers dispatched:**
 * - `OnClickEventTrigger` — fired when the button is tapped while interactive.
 */
@Immutable
@Triggers([OnClickEventTrigger::class])
@Serializable
@SerialName("Button")
data class ButtonTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
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