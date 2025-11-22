package dev.catbit.mosaic.server.builders

interface GenericBuilder<out T> {
    fun build(): T
}