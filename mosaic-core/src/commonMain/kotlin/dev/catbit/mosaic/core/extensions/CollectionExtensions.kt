package dev.catbit.mosaic.core.extensions

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

fun <T, R> Collection<T>.immutableMapTo(map: (T) -> R) =
    persistentListOf<R>().builder().apply {
        this@immutableMapTo.forEach { item -> add(map(item)) }
    }.build()

fun <K, V, R> Map<K, V>.immutableMapValuesTo(map: (V) -> R) =
    persistentMapOf<K, R>().builder().apply {
        this@immutableMapValuesTo.forEach { (key, value) -> put(key, map(value)) }
    }.build()