package dev.catbit.mosaic.server.ksp.extensions

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

internal fun KSClassDeclaration.generateBuilder(
    containingFile: KSFile?,
    modelClassName: ClassName,
    builderClassName: ClassName,
    builderScopeClassName: ClassName,
    codeGenerator: CodeGenerator
) {
    val rawName = simpleName
        .asString()
        .removeSuffix(modelClassName.simpleName)

    val builderName = "$rawName${builderClassName.simpleName}"
    val properties = getAllProperties().toList()
    val parameterSpec = properties.toParameterSpecs()

    val builderTypeSpec = TypeSpec.classBuilder(builderName)
        .addSuperinterface(
            superinterface = builderClassName.parameterizedBy(toClassName())
        )
        .primaryConstructor(
            primaryConstructor = FunSpec.constructorBuilder()
                .addParameters(parameterSpec)
                .build()
        )
        .addProperties(properties.toPropertySpecs())
        .addFunction(properties.toBuildFunc(toClassName()))
        .build()

    val builderScopeFunSpec = FunSpec.builder(rawName)
        .addParameters(parameterSpec)
        .receiver(builderScopeClassName)
        .addCode(properties.toBuilderScopeFunBody(builderName))
        .build()


    val fileSpec = FileSpec
        .builder(packageName.asString(), builderName)
        .addImport(builderScopeClassName.packageName, builderScopeClassName.simpleName)
        .addType(builderTypeSpec)
        .indent("    ")
        .addFunction(builderScopeFunSpec)
        .build()

    runCatching {
        containingFile?.let {
            fileSpec.writeTo(
                codeGenerator = codeGenerator,
                dependencies = Dependencies(
                    aggregating = true,
                    sources = arrayOf(it)
                )
            )
        }
    }
}