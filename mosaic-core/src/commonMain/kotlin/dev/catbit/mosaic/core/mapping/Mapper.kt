package dev.catbit.mosaic.core.mapping

import kotlin.reflect.KClass

class Mapper(
    private val mappings: List<MappingHolder<*, *>>
) {
    @Suppress("UNCHECKED_CAST")
    fun <S : Any, T : Any> map(
        sourceInstance: S,
        target: KClass<T>
    ): T {
        val mappingHolder = mappings.firstOrNull { it.match(sourceInstance::class, target) }
            ?: throw RuntimeException("No mapping of ${sourceInstance::class} to $target found")

        return (mappingHolder as MappingHolder<S, T>).mapping(sourceInstance, this)
    }
}

inline fun <reified T : Any> Any.mapTo(mapper: Mapper) =
    mapper.map(this, T::class)

inline fun <reified T : Any> List<Any>.mapListTo(mapper: Mapper) =
    map { mapper.map(it, T::class) }