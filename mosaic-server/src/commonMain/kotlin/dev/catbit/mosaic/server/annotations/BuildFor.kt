package dev.catbit.mosaic.server.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
internal annotation class BuildFor(
    val models: Array<KClass<*>>
)
