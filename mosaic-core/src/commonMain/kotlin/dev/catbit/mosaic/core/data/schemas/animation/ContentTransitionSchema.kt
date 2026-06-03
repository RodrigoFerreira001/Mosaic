package dev.catbit.mosaic.core.data.schemas.animation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Combined enter + exit transition specification used for navigation screen transitions.
 *
 * - [enter] — list of [EnterTransitionSchema] variants combined with `+` operator at runtime.
 *   An empty list produces `EnterTransition.None` (no animation).
 * - [exit] — list of [ExitTransitionSchema] variants combined with `+` operator at runtime.
 *   An empty list produces `ExitTransition.None` (no animation).
 *
 * Multiple transitions in the same list are layered (e.g. fade + slide simultaneously).
 *
 * **DSL usage (via transition preset builders in mosaic-server):**
 * ```kotlin
 * slideHorizontal()         // SlideInHorizontally(Full) + SlideOutHorizontally(NegativeFull)
 * slideVertical()
 * fade()
 * fadeAndSlideHorizontal()
 * slideOver()
 * slideInFromLeft()   slideInFromRight()   slideInFromTop()   slideInFromBottom()
 * slideOutToLeft()    slideOutToRight()    slideOutToTop()    slideOutToBottom()
 * ```
 *
 * This type is used as `transition` and `popTransition` in `GraphEntryBuilder.entry(...)`.
 */
@Serializable
data class ContentTransitionSchema(
    @SerialName("enter") val enter: List<EnterTransitionSchema> = emptyList(),
    @SerialName("exit") val exit: List<ExitTransitionSchema> = emptyList()
)
