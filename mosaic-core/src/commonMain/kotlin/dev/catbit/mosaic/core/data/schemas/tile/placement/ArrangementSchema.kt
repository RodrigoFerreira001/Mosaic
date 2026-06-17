package dev.catbit.mosaic.core.data.schemas.tile.placement

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Arrangement value controlling how children are distributed along the main axis of a container.
 *
 * Three sub-interfaces correspond to layout direction:
 * - [Horizontal] — for `RowTile` main axis. Includes [Horizontal.Start], [Horizontal.End],
 *   [Horizontal.SpacedBy], plus any [HorizontalOrVertical] variant.
 * - [Vertical] — for `ColumnTile` main axis. Includes [Vertical.Top], [Vertical.Bottom],
 *   [Vertical.SpacedBy], plus any [HorizontalOrVertical] variant.
 * - [HorizontalOrVertical] — valid for both Row and Column: [HorizontalOrVertical.Center],
 *   [HorizontalOrVertical.SpaceEvenly], [HorizontalOrVertical.SpaceBetween],
 *   [HorizontalOrVertical.SpaceAround].
 *
 * `SpacedBy` takes a `space` (dp) and an optional cross-axis `alignment` to align children
 * along the cross axis within each slot.
 *
 * **DSL helpers (mosaic-server):**
 * ```kotlin
 * // For Column:
 * arrangeVerticallyToTop()
 * arrangeVerticallyToBottom()
 * arrangeVerticallySpacedBy(space = 16, alignment = alignVerticallyToCenter())
 * arrangeToCenter()        // HorizontalOrVertical.Center
 * arrangeToSpaceEvenly()
 * arrangeToSpaceBetween()
 * arrangeToSpaceAround()
 *
 * // For Row:
 * arrangeHorizontallyToStart()
 * arrangeHorizontallyToEnd()
 * arrangeHorizontallySpacedBy(space = 8)
 * arrangeToCenter()        // also valid for Row
 * ```
 *
 * **Notes:** `Horizontal.Start` and `Vertical.Top` are the fallback when the `when` branch
 * hits an `else` in the client extensions — unknown arrangements default to Start/Top.
 */
@Serializable
@Immutable
sealed interface ArrangementSchema {

    @Serializable
    @SerialName("Horizontal")
    sealed interface Horizontal : ArrangementSchema {

        @Serializable
        @SerialName("Start")
        data object Start : Horizontal

        @Serializable
        @SerialName("End")
        data object End : Horizontal

        @Serializable
        @SerialName("SpacedBy")
        data class SpacedBy(
            val space: Int,
            val alignment: AlignmentSchema.Horizontal
        ) : Horizontal
    }

    @Serializable
    @SerialName("Vertical")
    sealed interface Vertical : ArrangementSchema {

        @Serializable
        @SerialName("Top")
        data object Top : Vertical

        @Serializable
        @SerialName("Bottom")
        data object Bottom : Vertical

        @Serializable
        @SerialName("SpacedBy")
        data class SpacedBy(
            val space: Int,
            val alignment: AlignmentSchema.Vertical
        ) : Vertical
    }

    @Serializable
    @SerialName("HorizontalOrVertical")
    sealed interface HorizontalOrVertical : Horizontal, Vertical {

        @Serializable
        @SerialName("Center")
        data object Center : HorizontalOrVertical

        @Serializable
        @SerialName("SpaceEvenly")
        data object SpaceEvenly : HorizontalOrVertical

        @Serializable
        @SerialName("SpaceBetween")
        data object SpaceBetween : HorizontalOrVertical

        @Serializable
        @SerialName("SpaceAround")
        data object SpaceAround : HorizontalOrVertical
    }
}