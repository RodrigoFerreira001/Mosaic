package dev.catbit.mosaic.server.builder

interface GenericBuilder<out T> {
    fun build(): T
}