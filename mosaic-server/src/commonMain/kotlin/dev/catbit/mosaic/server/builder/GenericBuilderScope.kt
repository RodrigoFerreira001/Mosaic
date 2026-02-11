package dev.catbit.mosaic.server.builder

// TODO Definir aqui o esquema de BuildingScope (CompositionScope)
abstract class GenericBuilderScope <Model, Builder: GenericBuilder<Model>>  {
    private val builders = mutableListOf<Builder>()

    internal fun addBuilder(builder: Builder) {
        builders.add(builder)
    }

    internal fun build() = builders.map { it.build() }
}