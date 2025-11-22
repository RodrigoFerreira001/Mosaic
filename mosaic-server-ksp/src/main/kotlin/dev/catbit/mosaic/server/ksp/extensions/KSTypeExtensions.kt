package dev.catbit.mosaic.server.ksp.extensions

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toClassName

fun KSType.isListOf(
    className: ClassName
): Boolean {
    if (declaration.qualifiedName?.asString() != List::class.qualifiedName) return false
    if (arguments.size != 1) return false
    val argType = arguments.first().type?.resolve() ?: return false

    return argType.toClassName().simpleName.removeSuffix("?") == className.simpleName
}