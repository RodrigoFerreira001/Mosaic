package dev.catbit.mosaic.server.builders

abstract class GenericBuilderScope <Model, Builder: GenericBuilder<Model>>  {
    private val builders = mutableListOf<Builder>()

    fun addBuilder(builder: Builder) {
        builders.add(builder)
    }

    fun build() = builders.map { it.build() }
}