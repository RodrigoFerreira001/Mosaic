package dev.catbit.mosaic.core.data.schemas.animation

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Enter transition applied when a screen or animated content becomes visible.
 *
 * Multiple variants can coexist in [ContentTransitionSchema.enter] — they are combined with
 * the `+` operator to run simultaneously (e.g. fade + slide).
 *
 * **Variants:**
 * - [FadeIn] — `fadeIn(animationSpec, initialAlpha)`. Default: alpha `0f → 1f` over 300ms.
 * - [SlideInHorizontally] — `slideInHorizontally(animationSpec, initialOffsetX)`.
 *   Default offset: [OffsetType.Full] (slides in from fully off-screen right).
 * - [SlideInVertically] — `slideInVertically(animationSpec, initialOffsetY)`.
 *   Default offset: [OffsetType.Full] (slides in from below).
 * - [None] — `EnterTransition.None` (no animation; use to disable enter on one side of a pair).
 *
 * **How [OffsetType] maps to pixel offset (lambda `{ fullSize }`)**:
 * - [OffsetType.Full] → `+fullSize` (from right/bottom)
 * - [OffsetType.NegativeFull] → `-fullSize` (from left/top)
 * - [OffsetType.Fixed] → exact pixel value
 * - [OffsetType.Fraction] → `(fullSize * factor).toInt()`
 */
@Immutable
@Serializable
sealed interface EnterTransitionSchema {

    @Serializable
    @SerialName("fadeIn")
    data class FadeIn(
        @SerialName("animationSpec") val animationSpec: AnimationSpecSchema = AnimationSpecSchema.Tween(),
        @SerialName("initialAlpha") val initialAlpha: Float = 0f
    ) : EnterTransitionSchema

    @Serializable
    @SerialName("slideInHorizontally")
    data class SlideInHorizontally(
        @SerialName("animationSpec") val animationSpec: AnimationSpecSchema = AnimationSpecSchema.Tween(),
        @SerialName("initialOffset") val initialOffset: OffsetType = OffsetType.Full
    ) : EnterTransitionSchema

    @Serializable
    @SerialName("slideInVertically")
    data class SlideInVertically(
        @SerialName("animationSpec") val animationSpec: AnimationSpecSchema = AnimationSpecSchema.Tween(),
        @SerialName("initialOffset") val initialOffset: OffsetType = OffsetType.Full
    ) : EnterTransitionSchema

    @Serializable
    @SerialName("noneEnter")
    data object None : EnterTransitionSchema
}
