package dev.catbit.mosaic.core.data.schemas.animation

import kotlinx.serialization.Serializable

/**
 * Interpolation curve for [AnimationSpecSchema.Tween] animations.
 *
 * **Compose mapping:**
 * - [LINEAR]             → `LinearEasing`
 * - [EASE_IN]            → `EaseIn`
 * - [EASE_OUT]           → `EaseOut`
 * - [EASE_IN_OUT]        → `EaseInOut`
 * - [FAST_OUT_SLOW_IN]   → `FastOutSlowInEasing` (Material default, good for most transitions)
 * - [FAST_OUT_LINEAR_IN] → `FastOutLinearInEasing` (good for elements leaving the screen)
 * - [LINEAR_OUT_SLOW_IN] → `LinearOutSlowInEasing` (good for elements entering the screen)
 */
@Serializable
enum class EasingType {
    LINEAR,
    EASE_IN,
    EASE_OUT,
    EASE_IN_OUT,
    FAST_OUT_SLOW_IN,
    FAST_OUT_LINEAR_IN,
    LINEAR_OUT_SLOW_IN
}
