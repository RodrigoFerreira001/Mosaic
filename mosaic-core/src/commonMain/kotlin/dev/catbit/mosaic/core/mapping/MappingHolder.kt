package dev.catbit.mosaic.core.mapping

import kotlin.reflect.KClass

data class MappingHolder<out S : Any, out T : Any>(
    val source: KClass<@UnsafeVariance S>,
    val target: KClass<@UnsafeVariance T>,
    val mapping: Mapper.(@UnsafeVariance S) -> T
) {
    fun match(
        source: KClass<*>,
        target: KClass<*>
    ) = this.source == source && this.target == target
}

inline fun <reified S: Any, reified T: Any> mapping(
    noinline mappingFunc: Mapper.(S) -> T
) = MappingHolder(S::class, T::class, mappingFunc)