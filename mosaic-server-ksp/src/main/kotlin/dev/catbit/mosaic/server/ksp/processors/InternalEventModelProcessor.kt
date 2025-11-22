package dev.catbit.mosaic.server.ksp.processors

import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ksp.toClassName
import dev.catbit.mosaic.server.ksp.constants.buildForClassName
import dev.catbit.mosaic.server.ksp.constants.eventBuilderClassName
import dev.catbit.mosaic.server.ksp.constants.eventBuilderScopeClassName
import dev.catbit.mosaic.server.ksp.constants.eventModelClassName
import dev.catbit.mosaic.server.ksp.extensions.generateBuilder
import dev.catbit.mosaic.server.ksp.extensions.getBridge

class InternalEventModelProcessor(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {

        val bridge = resolver.getBridge() ?: return emptyList()

        if (bridge.containingFile?.packageName?.asString() != eventBuilderClassName.packageName) return emptyList()

        bridge
            .arguments
            .mapNotNull { it.value as? ArrayList<*> }
            .flatten()
            .mapNotNull { (it as KSType).declaration.closestClassDeclaration() }
            .forEach { declaration ->
                declaration.generateBuilder(
                    containingFile = bridge.containingFile,
                    modelClassName = eventModelClassName,
                    builderClassName = eventBuilderClassName,
                    builderScopeClassName = eventBuilderScopeClassName,
                    codeGenerator = environment.codeGenerator,
                )
            }

        return emptyList()
    }
}

class InternalEventModelProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ) = InternalEventModelProcessor(environment)
}