package dev.catbit.mosaic.server.builder

abstract class GenericBuilderScope <Model, Builder: GenericBuilder<Model>>  {
    private val builders = mutableListOf<Builder>()

    internal fun addBuilder(builder: Builder) {
        builders.add(builder)
    }

    fun build() = builders.map { it.build() }
}