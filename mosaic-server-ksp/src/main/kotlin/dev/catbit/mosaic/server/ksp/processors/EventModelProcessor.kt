package dev.catbit.mosaic.server.ksp.processors

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import dev.catbit.mosaic.server.ksp.constants.eventBuilderClassName
import dev.catbit.mosaic.server.ksp.constants.eventBuilderScopeClassName
import dev.catbit.mosaic.server.ksp.constants.eventModelClassName
import dev.catbit.mosaic.server.ksp.extensions.generateBuilder
import dev.catbit.mosaic.server.ksp.extensions.getAllModelsOf

class EventModelProcessor(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {

        resolver
            .getAllModelsOf(eventModelClassName.canonicalName)
            .forEach { declaration ->
                declaration.generateBuilder(
                    containingFile = declaration.containingFile,
                    modelClassName = eventModelClassName,
                    builderClassName = eventBuilderClassName,
                    builderScopeClassName = eventBuilderScopeClassName,
                    codeGenerator = environment.codeGenerator,
                )
            }

        return emptyList()
    }
}

class EventModelProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ) = EventModelProcessor(environment)
}