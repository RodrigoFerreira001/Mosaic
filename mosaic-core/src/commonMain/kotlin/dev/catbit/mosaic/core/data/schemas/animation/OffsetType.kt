package dev.catbit.mosaic.core.data.schemas.animation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Slide offset for [EnterTransitionSchema] and [ExitTransitionSchema] variants.
 *
 * Passed as a lambda `{ fullSize: Int -> resolve(fullSize) }` to Compose slide functions,
 * where `fullSize` is the composable's measured size in pixels along the slide axis.
 *
 * **Variants:**
 * - [Full] → `+fullSize` — starts/ends fully off-screen in the positive direction (right/bottom).
 * - [NegativeFull] → `-fullSize` — starts/ends fully off-screen in the negative direction (left/top).
 *   This is the default for slide-out transitions.
 * - [Fixed] → exact pixel offset (positive = right/down, negative = left/up).
 * - [Fraction] → `(fullSize * factor).toInt()`. Use values in `0f..1f` for partial slides or
 *   negative values for reverse direction.
 *
 * **Examples:**
 * - Enter from left: `SlideInHorizontally(initialOffset = NegativeFull)` (offset starts at -fullSize → 0)
 * - Enter from right: `SlideInHorizontally(initialOffset = Full)` (offset starts at +fullSize → 0)
 * - Partial slide from below: `SlideInVertically(initialOffset = Fraction(0.3f))`
 */
@Serializable
sealed interface OffsetType {

    @Serializable
    @SerialName("full")
    data object Full : OffsetType

    @Serializable
    @SerialName("negativeFull")
    data object NegativeFull : OffsetType

    @Serializable
    @SerialName("fixed")
    data class Fixed(
        @SerialName("px") val px: Int
    ) : OffsetType

    @Serializable
    @SerialName("fraction")
    data class Fraction(
        @SerialName("factor") val factor: Float
    ) : OffsetType
}
