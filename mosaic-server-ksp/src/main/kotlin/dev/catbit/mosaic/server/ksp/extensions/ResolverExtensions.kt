package dev.catbit.mosaic.server.ksp.extensions

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.toClassName
import dev.catbit.mosaic.server.ksp.constants.buildForClassName

fun Resolver.getAllModelsOf(
    qualifiedName: String
): Sequence<KSClassDeclaration> =
    getAllFiles()
        .flatMap { it.declarations }
        .filterIsInstance<KSClassDeclaration>()
        .filter { it.classKind == ClassKind.CLASS }
        .filter { classDeclaration ->
            classDeclaration.superTypes.any { superType ->
                superType.resolve().declaration.qualifiedName?.asString() == qualifiedName
            }
        }

internal fun Resolver.getBridge() =
    getSymbolsWithAnnotation(buildForClassName.canonicalName)
        .flatMap { it.annotations }
        .filter { it.annotationType.resolve().toClassName() == buildForClassName }
        .firstOrNull()