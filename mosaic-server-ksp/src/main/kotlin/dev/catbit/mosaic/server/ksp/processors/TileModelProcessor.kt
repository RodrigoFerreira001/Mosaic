package dev.catbit.mosaic.server.ksp.processors

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import dev.catbit.mosaic.server.ksp.constants.tileBuilderClassName
import dev.catbit.mosaic.server.ksp.constants.tileBuilderScopeClassName
import dev.catbit.mosaic.server.ksp.constants.tileModelClassName
import dev.catbit.mosaic.server.ksp.extensions.generateBuilder
import dev.catbit.mosaic.server.ksp.extensions.getAllModelsOf

class TileModelProcessor(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {

        resolver
            .getAllModelsOf(tileModelClassName.canonicalName)
            .forEach { declaration ->
                declaration.generateBuilder(
                    containingFile = declaration.containingFile,
                    modelClassName = tileModelClassName,
                    builderClassName = tileBuilderClassName,
                    builderScopeClassName = tileBuilderScopeClassName,
                    codeGenerator = environment.codeGenerator,
                )
            }

        return emptyList()
    }
}

class TileModelProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ) = TileModelProcessor(environment)
}