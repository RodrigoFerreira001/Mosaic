package dev.catbit.mosaic.core.data.schemas.animation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Timing specification for Mosaic animations ([EnterTransitionSchema], [ExitTransitionSchema]).
 *
 * **Variants:**
 * - [Tween] — fixed-duration interpolation. Converts to `tween(durationMillis, delayMillis, easing)`.
 *   Default: 300ms, 0ms delay, `FAST_OUT_SLOW_IN` easing.
 * - [Spring] — physics-based, duration determined by [dampingRatio] and [stiffness].
 *   Default: `dampingRatio = 1f` (critically damped, no overshoot), `stiffness = 1500f` (StiffnessMediumLow).
 *
 * **DSL usage (typically inside transition builders):**
 * ```kotlin
 * AnimationSpecSchema.Tween(durationMillis = 400, easing = EasingType.EASE_IN_OUT)
 * AnimationSpecSchema.Spring(dampingRatio = 0.7f, stiffness = 400f)
 * ```
 *
 * **Notes:** Both variants produce a `FiniteAnimationSpec<T>` in the client, so they are
 * interchangeable wherever `AnimationSpecSchema` is accepted. Spring ignores [delayMillis]
 * (there is no delay parameter in `spring()`).
 */
@Serializable
sealed interface AnimationSpecSchema {

    @Serializable
    @SerialName("tween")
    data class Tween(
        @SerialName("durationMillis") val durationMillis: Int = 300,
        @SerialName("delayMillis") val delayMillis: Int = 0,
        @SerialName("easing") val easing: EasingType = EasingType.FAST_OUT_SLOW_IN
    ) : AnimationSpecSchema

    @Serializable
    @SerialName("spring")
    data class Spring(
        @SerialName("dampingRatio") val dampingRatio: Float = 1f,
        @SerialName("stiffness") val stiffness: Float = 1500f
    ) : AnimationSpecSchema
}
