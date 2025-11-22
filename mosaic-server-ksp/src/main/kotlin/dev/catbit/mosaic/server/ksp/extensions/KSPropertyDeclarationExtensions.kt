package dev.catbit.mosaic.server.ksp.extensions

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toTypeName
import dev.catbit.mosaic.server.ksp.constants.eventModelClassName
import dev.catbit.mosaic.server.ksp.constants.tileModelClassName

fun KSPropertyDeclaration.isListOf(
    className: ClassName
): Boolean = type.resolve().isListOf(className)

fun List<KSPropertyDeclaration>.toParameterSpecs() = map { property ->
    ParameterSpec.builder(
        property.simpleName.asString(),
        when {
            property.isListOf(eventModelClassName) -> TypeVariableName("EventBuilderScope.() -> Unit")
            property.isListOf(tileModelClassName) -> TypeVariableName("TileBuilderScope.() -> Unit")
            else -> property.type.toTypeName()
        }
    )
        .apply {
            if (property.isListOf(eventModelClassName) || property.isListOf(tileModelClassName)) {
                defaultValue("{}")
            }
        }
        .build()
}

fun List<KSPropertyDeclaration>.toPropertySpecs() = map { property ->
    PropertySpec.builder(
        property.simpleName.asString(),
        when {
            property.isListOf(eventModelClassName) -> TypeVariableName("EventBuilderScope.() -> Unit")
            property.isListOf(tileModelClassName) -> TypeVariableName("TileBuilderScope.() -> Unit")
            else -> property.type.toTypeName()
        }
    )
        .initializer(property.simpleName.asString())
        .addModifiers(KModifier.PRIVATE)
        .build()
}

fun List<KSPropertyDeclaration>.toBuildFunc(
    modelClassName: ClassName
) = FunSpec.builder("build")
    .addModifiers(KModifier.OVERRIDE)
    .returns(modelClassName)
    .addCode(
        CodeBlock.builder()
            .apply {
                add("return ${modelClassName.simpleName}(\n")
                indent()

                forEachIndexed { index, property ->
                    val simpleName = property.simpleName.asString()

                    when {
                        property.isListOf(eventModelClassName) ->
                            add("$simpleName = EventBuilderScope().apply($simpleName).build()${if (index != lastIndex) ",\n" else ""}")

                        property.isListOf(tileModelClassName) ->
                            add("$simpleName = TileBuilderScope().apply($simpleName).build()${if (index != lastIndex) ",\n" else ""}")

                        else -> add("$simpleName = $simpleName${if (index != lastIndex) ",\n" else ""}")
                    }
                }

                unindent()
                add("\n)")
            }
            .build()
    )
    .build()

fun List<KSPropertyDeclaration>.toBuilderScopeFunBody(
    builderName: String
) = CodeBlock.builder()
    .apply {
        add("addBuilder(\n")
        indent()
        add("$builderName(\n")
        indent()
        forEachIndexed { index, property ->
            val simpleName = property.simpleName.asString()
            add("$simpleName = $simpleName${if (index != lastIndex) ",\n" else ""}")
        }
        unindent()
        add("\n)")
        unindent()
        add("\n)")
    }
    .build()