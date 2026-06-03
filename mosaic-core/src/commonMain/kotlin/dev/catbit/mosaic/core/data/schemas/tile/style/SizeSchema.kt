package dev.catbit.mosaic.core.data.schemas.tile.style

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Defines the width and height constraints for a tile.
 *
 * **Width behaviors ([Behavior.Horizontal]):**
 * - [Behavior.Horizontal.Fill] — `fillMaxWidth()`. Optional [Fill.max] adds a `widthIn(max=)` cap (dp).
 * - [Behavior.Horizontal.Wrap] — `wrapContentWidth()`.
 * - [Behavior.Horizontal.Fixed] — `width(value.dp)`.
 * - [Behavior.Horizontal.Weight] — `Modifier.weight(value)` inside a `Row` parent. **Requires a
 *   `LocalRowScope` or `LocalFlowRowScope` to be active; has no effect outside a Row/FlowRow.**
 * - [Behavior.Horizontal.Span] — `gridItem(rowSpan = value)` inside a `Grid` parent. **Requires
 *   `LocalGridScope`; no effect outside a Grid.**
 * - [Behavior.Horizontal.Flex] — Flexbox-specific control (grow, shrink, basis, alignSelf, order).
 *   **Requires `LocalFlexBoxScope`; no effect outside a FlexBox.**
 *
 * **Height behaviors ([Behavior.Vertical]):**
 * - [Behavior.Vertical.Fill] — `fillMaxHeight()`. Optional [Behavior.Vertical.Fill.max] adds `heightIn(max=)` cap.
 * - [Behavior.Vertical.Wrap] — `wrapContentHeight()`.
 * - [Behavior.Vertical.Fixed] — `height(value.dp)`.
 * - [Behavior.Vertical.Weight] — `Modifier.weight(value)` inside a `Column`. **Requires `LocalColumnScope`.**
 * - [Behavior.Vertical.Span] — `gridItem(rowSpan = value)` inside a `Grid`. **Requires `LocalGridScope`.**
 * - [Behavior.Vertical.FillRow] — `fillMaxRowHeight(fraction)` inside a `FlowRow`. **Requires
 *   `LocalFlowRowScope`; defaults to `fraction = 1f` (full row height).**
 *
 * **DSL helpers (mosaic-server):**
 * ```kotlin
 * size(width = fillHorizontally())                // Fill, no cap
 * size(width = fillHorizontally(max = 400))       // Fill with 400dp cap
 * size(width = wrapHorizontally())
 * size(width = fixedHorizontally(200))
 * size(width = weightHorizontally(1f))            // only effective inside Row
 * size(width = spanHorizontally(2))               // only effective inside Grid
 * size(width = flexHorizontally(...))             // only effective inside FlexBox
 * size(height = fillVertically())
 * size(height = fillVertically(max = 300))
 * size(height = wrapVertically())
 * size(height = fixedVertically(56))
 * size(height = weightVertically(1f))             // only effective inside Column
 * size(height = fillRowHeight())                  // only effective inside FlowRow
 * size(height = fillRowHeight(0.5f))              // 50% of row height
 * ```
 */
@Serializable
data class SizeSchema(
    @SerialName("width") val width: Behavior.Horizontal,
    @SerialName("height") val height: Behavior.Vertical
) {
    @Serializable
    @SerialName("behavior")
    sealed interface Behavior {

        @Serializable
        @SerialName("horizontal")
        sealed interface Horizontal : Behavior {

            @Serializable
            @SerialName("fill")
            data class Fill(val max: Int? = null) : Horizontal

            @Serializable
            @SerialName("wrap")
            data object Wrap : Horizontal

            @Serializable
            @SerialName("fixed")
            data class Fixed(@SerialName("value") val value: Int) : Horizontal

            @Serializable
            @SerialName("weight")
            data class Weight(
                @SerialName("value") val value: Float
            ) : Horizontal

            @Serializable
            @SerialName("span")
            data class Span(
                @SerialName("value") val value: Int
            ) : Horizontal

            @Serializable
            @SerialName("flex")
            data class Flex(
                @SerialName("grow") val grow: Float?,
                @SerialName("shrink") val shrink: Float?,
                @SerialName("basis") val basis: FlexBasis?,
                @SerialName("alignSelf") val alignSelf: FlexAlignSelf?,
                @SerialName("order") val order: Int?
            ) : Horizontal {

                @Serializable
                sealed interface FlexBasis {
                    @Serializable
                    @SerialName("auto")
                    data object Auto : FlexBasis

                    @Serializable
                    @SerialName("fixed")
                    data class Fixed(
                        @SerialName("value") val value: Int
                    ) : FlexBasis

                    @Serializable
                    @SerialName("fraction")
                    data class Fraction(
                        @SerialName("value") val value: Float
                    ) : FlexBasis
                }

                @Serializable
                enum class FlexAlignSelf {
                    Auto,
                    Start,
                    Center,
                    End,
                    Stretch,
                    Baseline
                }
            }
        }

        @Serializable
        @SerialName("vertical")
        sealed interface Vertical : Behavior {

            @Serializable
            @SerialName("fill")
            data class Fill(val max: Int? = null) : Vertical

            @Serializable
            @SerialName("wrap")
            data object Wrap : Vertical

            @Serializable
            @SerialName("fixed")
            data class Fixed(
                @SerialName("value") val value: Int
            ) : Vertical

            @Serializable
            @SerialName("weight")
            data class Weight(
                @SerialName("value") val value: Float
            ) : Vertical

            @Serializable
            @SerialName("span")
            data class Span(
                @SerialName("value") val value: Int
            ) : Vertical

            @Serializable
            @SerialName("fillRow")
            data class FillRow(
                @SerialName("fraction") val fraction: Float = 1f
            ) : Vertical
        }
    }
}
