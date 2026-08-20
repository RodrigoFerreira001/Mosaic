package dev.catbit.mosaic.client.extensions

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import dev.catbit.mosaic.core.data.schemas.animation.AnimationSpecSchema
import dev.catbit.mosaic.core.data.schemas.animation.ContentTransitionSchema
import dev.catbit.mosaic.core.data.schemas.animation.EasingType
import dev.catbit.mosaic.core.data.schemas.animation.EnterTransitionSchema
import dev.catbit.mosaic.core.data.schemas.animation.ExitTransitionSchema
import dev.catbit.mosaic.core.data.schemas.animation.OffsetType

/** Converts the wire-format [EasingType] into its Compose [Easing] counterpart, used inside
 * [AnimationSpecSchema.toTweenOrSpring] to build a `tween`'s curve. */
fun EasingType.toComposeEasing(): Easing = when (this) {
    EasingType.LINEAR -> LinearEasing
    EasingType.EASE_IN -> androidx.compose.animation.core.EaseIn
    EasingType.EASE_OUT -> androidx.compose.animation.core.EaseOut
    EasingType.EASE_IN_OUT -> androidx.compose.animation.core.EaseInOut
    EasingType.FAST_OUT_SLOW_IN -> FastOutSlowInEasing
    EasingType.FAST_OUT_LINEAR_IN -> FastOutLinearInEasing
    EasingType.LINEAR_OUT_SLOW_IN -> LinearOutSlowInEasing
}

/**
 * Resolves an [OffsetType] to a concrete pixel offset given the axis' [fullSize] — a slide
 * transition's `initialOffset`/`targetOffset` is one of these, since Compose's own
 * `slideInHorizontally`/`slideInVertically` only learn the real container size at draw time.
 *
 * @param fullSize the axis size (width or height, in px) this offset is relative to.
 * @return the resolved offset in px — [fullSize] for [OffsetType.Full], its negation for
 * [OffsetType.NegativeFull], a literal value for [OffsetType.Fixed], or a fraction of [fullSize] for
 * [OffsetType.Fraction].
 */
fun OffsetType.resolve(fullSize: Int): Int = when (this) {
    OffsetType.Full -> fullSize
    OffsetType.NegativeFull -> -fullSize
    is OffsetType.Fixed -> px
    is OffsetType.Fraction -> (fullSize * factor).toInt()
}

/** Converts an [EnterTransitionSchema] into its Compose [EnterTransition] counterpart. */
fun EnterTransitionSchema.toEnterTransition(): EnterTransition = when (this) {
    is EnterTransitionSchema.FadeIn -> fadeIn(
        animationSpec = animationSpec.toTweenOrSpring(),
        initialAlpha = initialAlpha
    )

    is EnterTransitionSchema.SlideInHorizontally -> slideInHorizontally(
        animationSpec = animationSpec.toTweenOrSpring(),
        initialOffsetX = { initialOffset.resolve(it) }
    )

    is EnterTransitionSchema.SlideInVertically -> slideInVertically(
        animationSpec = animationSpec.toTweenOrSpring(),
        initialOffsetY = { initialOffset.resolve(it) }
    )

    EnterTransitionSchema.None -> EnterTransition.None
}

/**
 * Converts an [ExitTransitionSchema] into its Compose [ExitTransition] counterpart.
 * [ExitTransitionSchema.KeepUntilTransitionsFinished] currently resolves to [ExitTransition.None]
 * as a placeholder, pending Compose's own `ExitTransition.KeepUntilTransitionsFinished` becoming
 * public API.
 */
fun ExitTransitionSchema.toExitTransition(): ExitTransition = when (this) {
    is ExitTransitionSchema.FadeOut -> fadeOut(
        animationSpec = animationSpec.toTweenOrSpring(),
        targetAlpha = targetAlpha
    )

    is ExitTransitionSchema.SlideOutHorizontally -> slideOutHorizontally(
        animationSpec = animationSpec.toTweenOrSpring(),
        targetOffsetX = { targetOffset.resolve(it) }
    )

    is ExitTransitionSchema.SlideOutVertically -> slideOutVertically(
        animationSpec = animationSpec.toTweenOrSpring(),
        targetOffsetY = { targetOffset.resolve(it) }
    )

    // TODO Replace with ExitTransition.KeepUntilTransitionsFinished when it becomes public API
    ExitTransitionSchema.KeepUntilTransitionsFinished -> ExitTransition.None
    ExitTransitionSchema.None -> ExitTransition.None
}

/** Combines every entry into a single Compose [EnterTransition] via repeated `+` composition — an
 * empty list resolves to [EnterTransition.None]. */
fun List<EnterTransitionSchema>.toCombinedEnterTransition(): EnterTransition =
    fold(EnterTransition.None) { acc, schema -> acc + schema.toEnterTransition() }

/** Combines every entry into a single Compose [ExitTransition] via repeated `+` composition — an
 * empty list resolves to [ExitTransition.None]. */
fun List<ExitTransitionSchema>.toCombinedExitTransition(): ExitTransition =
    fold(ExitTransition.None) { acc, schema -> acc + schema.toExitTransition() }

/** Converts a [ContentTransitionSchema] (a screen/entry's declared `transition`/`popTransition`/
 * `predictivePopTransition`) into the Compose [ContentTransform] `NavDisplay`'s `transitionSpec`
 * expects — the mechanism behind `MosaicApplication`'s screen transition wiring. */
fun ContentTransitionSchema.toContentTransform(): ContentTransform =
    enter.toCombinedEnterTransition() togetherWith exit.toCombinedExitTransition()

/** Converts an [AnimationSpecSchema] into the matching Compose `tween`/`spring`
 * [androidx.compose.animation.core.FiniteAnimationSpec]. */
private fun <T> AnimationSpecSchema.toTweenOrSpring(): androidx.compose.animation.core.FiniteAnimationSpec<T> =
    when (this) {
        is AnimationSpecSchema.Tween -> tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = easing.toComposeEasing()
        )

        is AnimationSpecSchema.Spring -> spring(
            dampingRatio = dampingRatio,
            stiffness = stiffness
        )
    }
