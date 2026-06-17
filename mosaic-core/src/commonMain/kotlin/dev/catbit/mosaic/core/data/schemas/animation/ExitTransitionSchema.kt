package dev.catbit.mosaic.core.data.schemas.animation

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Exit transition applied when a screen or animated content leaves the composition.
 *
 * Multiple variants can coexist in [ContentTransitionSchema.exit] — they are combined with
 * the `+` operator to run simultaneously (e.g. fade + slide).
 *
 * **Variants:**
 * - [FadeOut] — `fadeOut(animationSpec, targetAlpha)`. Default: alpha `1f → 0f` over 300ms.
 * - [SlideOutHorizontally] — `slideOutHorizontally(animationSpec, targetOffsetX)`.
 *   Default offset: [OffsetType.NegativeFull] (slides out to the left).
 * - [SlideOutVertically] — `slideOutVertically(animationSpec, targetOffsetY)`.
 *   Default offset: [OffsetType.NegativeFull] (slides out upward).
 * - [KeepUntilTransitionsFinished] — **currently maps to `ExitTransition.None`** in the client
 *   (`AnimationSchemaExtensions`). The TODO comment indicates this should use
 *   `ExitTransition.KeepUntilTransitionsFinished` once it becomes stable public API.
 * - [None] — `ExitTransition.None` (no animation).
 *
 * **How [OffsetType] maps to pixel offset (lambda `{ fullSize }`)**:
 * - [OffsetType.Full] → `+fullSize` (exits to right/bottom)
 * - [OffsetType.NegativeFull] → `-fullSize` (exits to left/top — default for slide-out)
 * - [OffsetType.Fixed] → exact pixel value
 * - [OffsetType.Fraction] → `(fullSize * factor).toInt()`
 */
@Immutable
@Serializable
sealed interface ExitTransitionSchema {

    @Serializable
    @SerialName("fadeOut")
    data class FadeOut(
        @SerialName("animationSpec") val animationSpec: AnimationSpecSchema = AnimationSpecSchema.Tween(),
        @SerialName("targetAlpha") val targetAlpha: Float = 0f
    ) : ExitTransitionSchema

    @Serializable
    @SerialName("slideOutHorizontally")
    data class SlideOutHorizontally(
        @SerialName("animationSpec") val animationSpec: AnimationSpecSchema = AnimationSpecSchema.Tween(),
        @SerialName("targetOffset") val targetOffset: OffsetType = OffsetType.NegativeFull
    ) : ExitTransitionSchema

    @Serializable
    @SerialName("slideOutVertically")
    data class SlideOutVertically(
        @SerialName("animationSpec") val animationSpec: AnimationSpecSchema = AnimationSpecSchema.Tween(),
        @SerialName("targetOffset") val targetOffset: OffsetType = OffsetType.NegativeFull
    ) : ExitTransitionSchema

    @Serializable
    @SerialName("keepUntilTransitionsFinished")
    data object KeepUntilTransitionsFinished : ExitTransitionSchema

    @Serializable
    @SerialName("noneExit")
    data object None : ExitTransitionSchema
}
