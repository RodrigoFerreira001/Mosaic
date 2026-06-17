package dev.catbit.mosaic.core.extensions

import kotlinx.collections.immutable.persistentListOf

fun <T, R> Collection<T>.immutableMapTo(map: (T) -> R) =
    persistentListOf<R>().builder().apply {
        this@immutableMapTo.forEach { item -> add(map(item)) }
    }.build()