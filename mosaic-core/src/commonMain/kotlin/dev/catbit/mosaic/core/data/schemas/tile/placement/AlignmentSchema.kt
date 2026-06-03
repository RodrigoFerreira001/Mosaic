package dev.catbit.mosaic.core.data.schemas.tile.placement

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Alignment value for positioning children inside a container tile.
 *
 * Three sub-interfaces exist for different container types:
 * - [Vertical] — used as the `alignment` parameter of a `ColumnTile` (cross-axis, horizontal alignment).
 *   Maps to `Alignment.Vertical`: [Vertical.Top] → `Alignment.Top`, [Vertical.Center] → `Alignment.CenterVertically`,
 *   [Vertical.Bottom] → `Alignment.Bottom`.
 * - [Horizontal] — used as the `alignment` parameter of a `RowTile` (cross-axis, vertical alignment).
 *   Maps to `Alignment.Horizontal`: [Horizontal.Start] → `Alignment.Start`, [Horizontal.Center] → `Alignment.CenterHorizontally`,
 *   [Horizontal.End] → `Alignment.End`.
 * - [TwoDimensional] — used as the `contentAlignment` of a `BoxTile`. Maps to `Alignment` (e.g.
 *   [TwoDimensional.Center] → `Alignment.Center`, [TwoDimensional.TopStart] → `Alignment.TopStart`).
 *
 * **DSL helpers (mosaic-server):**
 * ```kotlin
 * // For Column (horizontal cross-axis):
 * alignHorizontallyToStart()
 * alignHorizontallyToCenter()
 * alignHorizontallyToEnd()
 *
 * // For Row (vertical cross-axis):
 * alignVerticallyToTop()
 * alignVerticallyToCenter()
 * alignVerticallyToBottom()
 *
 * // For Box (two-dimensional):
 * alignToTopStart()      alignToTopCenter()      alignToTopEnd()
 * alignToCenterStart()   alignToCenter()         alignToCenterEnd()
 * alignToBottomStart()   alignToBottomCenter()   alignToBottomEnd()
 * ```
 */
@Serializable
sealed interface AlignmentSchema {

    @Serializable
    @SerialName("Vertical")
    sealed interface Vertical : AlignmentSchema {

        @Serializable
        @SerialName("top")
        data object Top : Vertical

        @Serializable
        @SerialName("center")
        data object Center : Vertical

        @Serializable
        @SerialName("bottom")
        data object Bottom : Vertical
    }

    @Serializable
    @SerialName("Horizontal")
    sealed interface Horizontal : AlignmentSchema {

        @Serializable
        @SerialName("start")
        data object Start : Horizontal

        @Serializable
        @SerialName("center")
        data object Center : Horizontal

        @Serializable
        @SerialName("end")
        data object End : Horizontal
    }

    @Serializable
    @SerialName("TwoDimensional")
    sealed interface TwoDimensional : AlignmentSchema {

        @Serializable
        @SerialName("top_start")
        data object TopStart: TwoDimensional

        @Serializable
        @SerialName("top_center")
        data object TopCenter: TwoDimensional

        @Serializable
        @SerialName("top_end")
        data object TopEnd: TwoDimensional

        @Serializable
        @SerialName("center_start")
        data object CenterStart: TwoDimensional

        @Serializable
        @SerialName("center")
        data object Center: TwoDimensional

        @Serializable
        @SerialName("center_end")
        data object CenterEnd: TwoDimensional

        @Serializable
        @SerialName("bottom_start")
        data object BottomStart: TwoDimensional

        @Serializable
        @SerialName("bottom_center")
        data object BottomCenter: TwoDimensional

        @Serializable
        @SerialName("bottom_end")
        data object BottomEnd: TwoDimensional
    }
}