package dev.catbit.mosaic.core.annotations

import dev.catbit.mosaic.core.trigger.Trigger
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Triggers(
    val triggers: Array<KClass<out Trigger>>
)
