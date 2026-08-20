package dev.catbit.mosaic.server.builder.animation

import dev.catbit.mosaic.core.data.schemas.animation.AnimationSpecSchema
import dev.catbit.mosaic.core.data.schemas.animation.ContentTransitionSchema
import dev.catbit.mosaic.core.data.schemas.animation.EnterTransitionSchema
import dev.catbit.mosaic.core.data.schemas.animation.ExitTransitionSchema
import dev.catbit.mosaic.core.data.schemas.animation.OffsetType
import kotlinx.collections.immutable.persistentListOf

/**
 * New screen slides in from off-screen left; the outgoing screen doesn't animate out. Used as a
 * `Graph`/`NestedNavigationGraph`/entry `transition`, `popTransition` or `predictivePopTransition`.
 *
 * @param animationSpec Timing/easing curve driving the slide. Defaults to a standard tween.
 */
fun slideInFromLeftTransition(
    animationSpec: AnimationSpecSchema = AnimationSpecSchema.Tween()
) = ContentTransitionSchema(
    enter = persistentListOf(
        EnterTransitionSchema.SlideInHorizontally(
            animationSpec = animationSpec,
            initialOffset = OffsetType.NegativeFull
        )
    ),
    exit = persistentListOf(ExitTransitionSchema.None)
)

/**
 * New screen slides in from off-screen right; the outgoing screen doesn't animate out. Used as a
 * `Graph`/`NestedNavigationGraph`/entry `transition`, `popTransition` or `predictivePopTransition`.
 *
 * @param animationSpec Timing/easing curve driving the slide. Defaults to a standard tween.
 */
fun slideInFromRightTransition(
    animationSpec: AnimationSpecSchema = AnimationSpecSchema.Tween()
) = ContentTransitionSchema(
    enter = persistentListOf(
        EnterTransitionSchema.SlideInHorizontally(
            animationSpec = animationSpec,
            initialOffset = OffsetType.Full
        )
    ),
    exit = persistentListOf(ExitTransitionSchema.None)
)

/**
 * New screen slides in from off-screen bottom; the outgoing screen doesn't animate out. Used as
 * a `Graph`/`NestedNavigationGraph`/entry `transition`, `popTransition` or `predictivePopTransition`.
 *
 * @param animationSpec Timing/easing curve driving the slide. Defaults to a standard tween.
 */
fun slideInFromBottomTransition(
    animationSpec: AnimationSpecSchema = AnimationSpecSchema.Tween()
) = ContentTransitionSchema(
    enter = persistentListOf(
        EnterTransitionSchema.SlideInVertically(
            animationSpec = animationSpec,
            initialOffset = OffsetType.Full
        )
    ),
    exit = persistentListOf(ExitTransitionSchema.None)
)

/**
 * New screen slides in from off-screen top; the outgoing screen doesn't animate out. Used as a
 * `Graph`/`NestedNavigationGraph`/entry `transition`, `popTransition` or `predictivePopTransition`.
 *
 * @param animationSpec Timing/easing curve driving the slide. Defaults to a standard tween.
 */
fun slideInFromTopTransition(
    animationSpec: AnimationSpecSchema = AnimationSpecSchema.Tween()
) = ContentTransitionSchema(
    enter = persistentListOf(
        EnterTransitionSchema.SlideInVertically(
            animationSpec = animationSpec,
            initialOffset = OffsetType.NegativeFull
        )
    ),
    exit = persistentListOf(ExitTransitionSchema.None)
)

/**
 * Outgoing screen slides out toward off-screen left; the incoming screen doesn't animate in
 * (appears immediately underneath). Used as a `Graph`/`NestedNavigationGraph`/entry `transition`,
 * `popTransition` or `predictivePopTransition`.
 *
 * @param animationSpec Timing/easing curve driving the slide. Defaults to a standard tween.
 */
fun slideOutToLeftTransition(
    animationSpec: AnimationSpecSchema = AnimationSpecSchema.Tween()
) = ContentTransitionSchema(
    enter = persistentListOf(EnterTransitionSchema.None),
    exit = persistentListOf(
        ExitTransitionSchema.SlideOutHorizontally(
            animationSpec = animationSpec,
            targetOffset = OffsetType.NegativeFull
        )
    )
)

/**
 * Outgoing screen slides out toward off-screen right; the incoming screen doesn't animate in
 * (appears immediately underneath). Used as a `Graph`/`NestedNavigationGraph`/entry `transition`,
 * `popTransition` or `predictivePopTransition`.
 *
 * @param animationSpec Timing/easing curve driving the slide. Defaults to a standard tween.
 */
fun slideOutToRightTransition(
    animationSpec: AnimationSpecSchema = AnimationSpecSchema.Tween()
) = ContentTransitionSchema(
    enter = persistentListOf(EnterTransitionSchema.None),
    exit = persistentListOf(
        ExitTransitionSchema.SlideOutHorizontally(
            animationSpec = animationSpec,
            targetOffset = OffsetType.Full
        )
    )
)

/**
 * Outgoing screen slides out toward off-screen top; the incoming screen doesn't animate in
 * (appears immediately underneath). Used as a `Graph`/`NestedNavigationGraph`/entry `transition`,
 * `popTransition` or `predictivePopTransition`.
 *
 * @param animationSpec Timing/easing curve driving the slide. Defaults to a standard tween.
 */
fun slideOutToTopTransition(
    animationSpec: AnimationSpecSchema = AnimationSpecSchema.Tween()
) = ContentTransitionSchema(
    enter = persistentListOf(EnterTransitionSchema.None),
    exit = persistentListOf(
        ExitTransitionSchema.SlideOutVertically(
            animationSpec = animationSpec,
            targetOffset = OffsetType.NegativeFull
        )
    )
)

/**
 * Outgoing screen slides out toward off-screen bottom; the incoming screen doesn't animate in
 * (appears immediately underneath). Used as a `Graph`/`NestedNavigationGraph`/entry `transition`,
 * `popTransition` or `predictivePopTransition`.
 *
 * @param animationSpec Timing/easing curve driving the slide. Defaults to a standard tween.
 */
fun slideOutToBottomTransition(
    animationSpec: AnimationSpecSchema = AnimationSpecSchema.Tween()
) = ContentTransitionSchema(
    enter = persistentListOf(EnterTransitionSchema.None),
    exit = persistentListOf(
        ExitTransitionSchema.SlideOutVertically(
            animationSpec = animationSpec,
            targetOffset = OffsetType.Full
        )
    )
)

/**
 * Standard push/pop pair: the new screen slides in from the right while the outgoing one slides
 * out to the left. Used as a `Graph`/`NestedNavigationGraph`/entry `transition`, `popTransition`
 * or `predictivePopTransition`.
 *
 * @param animationSpec Timing/easing curve driving the slide. Defaults to a standard tween.
 */
fun slideHorizontalTransition(
    animationSpec: AnimationSpecSchema = AnimationSpecSchema.Tween()
) = ContentTransitionSchema(
    enter = persistentListOf(
        EnterTransitionSchema.SlideInHorizontally(
            animationSpec = animationSpec,
            initialOffset = OffsetType.Full
        )
    ),
    exit = persistentListOf(
        ExitTransitionSchema.SlideOutHorizontally(
            animationSpec = animationSpec,
            targetOffset = OffsetType.NegativeFull
        )
    )
)

/**
 * Vertical push/pop pair: the new screen slides in from the bottom while the outgoing one slides
 * out to the top. Used as a `Graph`/`NestedNavigationGraph`/entry `transition`, `popTransition`
 * or `predictivePopTransition`.
 *
 * @param animationSpec Timing/easing curve driving the slide. Defaults to a standard tween.
 */
fun slideVerticalTransition(
    animationSpec: AnimationSpecSchema = AnimationSpecSchema.Tween()
) = ContentTransitionSchema(
    enter = persistentListOf(
        EnterTransitionSchema.SlideInVertically(
            animationSpec = animationSpec,
            initialOffset = OffsetType.Full
        )
    ),
    exit = persistentListOf(
        ExitTransitionSchema.SlideOutVertically(
            animationSpec = animationSpec,
            targetOffset = OffsetType.NegativeFull
        )
    )
)

/**
 * Reverse of [slideHorizontalTransition] — the previous screen slides back in from the left while
 * the current one slides out to the right. The natural `popTransition` to pair with
 * [slideHorizontalTransition] as `transition`, so pushing and popping visually mirror each other
 * instead of both sliding in the same direction. Used as a `Graph`/`NestedNavigationGraph`/entry
 * `transition`, `popTransition` or `predictivePopTransition`.
 *
 * @param animationSpec Timing/easing curve driving the slide. Defaults to a standard tween.
 */
fun slideHorizontalReverseTransition(
    animationSpec: AnimationSpecSchema = AnimationSpecSchema.Tween()
) = ContentTransitionSchema(
    enter = persistentListOf(
        EnterTransitionSchema.SlideInHorizontally(
            animationSpec = animationSpec,
            initialOffset = OffsetType.NegativeFull
        )
    ),
    exit = persistentListOf(
        ExitTransitionSchema.SlideOutHorizontally(
            animationSpec = animationSpec,
            targetOffset = OffsetType.Full
        )
    )
)

/**
 * Cross-fade pair: the new screen fades in while the outgoing one fades out, both in place.
 * Used as a `Graph`/`NestedNavigationGraph`/entry `transition`, `popTransition` or
 * `predictivePopTransition`.
 *
 * @param animationSpec Timing/easing curve driving the fade. Defaults to a standard tween.
 */
fun fadeTransition(
    animationSpec: AnimationSpecSchema = AnimationSpecSchema.Tween()
) = ContentTransitionSchema(
    enter = persistentListOf(
        EnterTransitionSchema.FadeIn(animationSpec = animationSpec)
    ),
    exit = persistentListOf(
        ExitTransitionSchema.FadeOut(animationSpec = animationSpec)
    )
)

/**
 * Combined fade + horizontal push/pop pair: the new screen fades and slides in from the right
 * while the outgoing one fades and slides out to the left. Used as a
 * `Graph`/`NestedNavigationGraph`/entry `transition`, `popTransition` or `predictivePopTransition`.
 *
 * @param animationSpec Timing/easing curve driving both the fade and the slide. Defaults to a standard tween.
 */
fun fadeAndSlideHorizontalTransition(
    animationSpec: AnimationSpecSchema = AnimationSpecSchema.Tween()
) = ContentTransitionSchema(
    enter = persistentListOf(
        EnterTransitionSchema.FadeIn(animationSpec = animationSpec),
        EnterTransitionSchema.SlideInHorizontally(
            animationSpec = animationSpec,
            initialOffset = OffsetType.Full
        )
    ),
    exit = persistentListOf(
        ExitTransitionSchema.FadeOut(animationSpec = animationSpec),
        ExitTransitionSchema.SlideOutHorizontally(
            animationSpec = animationSpec,
            targetOffset = OffsetType.NegativeFull
        )
    )
)

/**
 * The new screen slides in from the right and settles on top; the outgoing screen stays put
 * underneath, unanimated, until the transition finishes (rather than sliding out) — the classic
 * "new screen slides over the previous one" effect. Used as a `Graph`/`NestedNavigationGraph`/entry
 * `transition`, `popTransition` or `predictivePopTransition`.
 *
 * @param animationSpec Timing/easing curve driving the slide. Defaults to a standard tween.
 */
fun slideOverTransition(
    animationSpec: AnimationSpecSchema = AnimationSpecSchema.Tween()
) = ContentTransitionSchema(
    enter = persistentListOf(
        EnterTransitionSchema.SlideInHorizontally(
            animationSpec = animationSpec,
            initialOffset = OffsetType.Full
        )
    ),
    exit = persistentListOf(
        ExitTransitionSchema.KeepUntilTransitionsFinished
    )
)
