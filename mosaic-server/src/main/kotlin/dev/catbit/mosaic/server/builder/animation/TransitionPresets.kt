package dev.catbit.mosaic.server.builder.animation

import dev.catbit.mosaic.core.data.schemas.animation.AnimationSpecSchema
import dev.catbit.mosaic.core.data.schemas.animation.ContentTransitionSchema
import dev.catbit.mosaic.core.data.schemas.animation.EnterTransitionSchema
import dev.catbit.mosaic.core.data.schemas.animation.ExitTransitionSchema
import dev.catbit.mosaic.core.data.schemas.animation.OffsetType
import kotlinx.collections.immutable.persistentListOf

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
