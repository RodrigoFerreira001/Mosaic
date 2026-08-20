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
 * Renders a Material 3 icon button. The concrete composable is picked by [buttonType]:
 * [Type.DEFAULT] → `IconButton`, [Type.FILLED] → `FilledIconButton`, [Type.FILLED_TONAL] →
 * `FilledTonalIconButton`, [Type.OUTLINED] → `OutlinedIconButton`.
 *
 * **Content:** when [loading] is `true` the button shows a 24dp `CircularProgressIndicator`
 * (2dp stroke, round cap, tinted with `LocalContentColor`); otherwise it renders [icon] with
 * its color, size and style applied.
 *
 * **Enabled state:** the button is interactive only when [enabled] is `true` **and** [loading]
 * is `false` — the two are combined into the single `enabled` the Material composable receives,
 * so a loading button is disabled for real: it takes Material's disabled colors and is reported
 * as disabled to accessibility services. A slow action therefore cannot be submitted twice.
 *
 * **Triggers dispatched:**
 * - `OnClickEventTrigger` — fired when the button is tapped while interactive.
 */
@Immutable
@Triggers([OnClickEventTrigger::class])
@Serializable
@SerialName("IconButton")
data class IconButtonTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("icon") val icon: IconSchema,
    @SerialName("buttonType") val buttonType: Type = Type.DEFAULT,
    @SerialName("loading") val loading: Boolean = false,
    @SerialName("enabled") val enabled: Boolean,
) : TileSchema {

    enum class Type {
        DEFAULT,
        FILLED,
        FILLED_TONAL,
        OUTLINED,
    }
}
