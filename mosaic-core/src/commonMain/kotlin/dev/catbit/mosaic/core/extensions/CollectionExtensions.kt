package dev.catbit.mosaic.core.extensions

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

/** Maps this collection with [map] directly into a `kotlinx.collections.immutable`
 * `PersistentList`, avoiding an intermediate mutable `List` — the usual way a `TileHolder`/
 * `EventHolder`'s `getTileSchema()`/`getEventSchema()` converts its child holders (`events.map { it.get() }`)
 * back into the immutable list shape a schema's own `events`/`tiles` field expects. */
fun <T, R> Collection<T>.immutableMapTo(map: (T) -> R) =
    persistentListOf<R>().builder().apply {
        this@immutableMapTo.forEach { item -> add(map(item)) }
    }.build()

/** Maps this map's values with [map] directly into a `kotlinx.collections.immutable`
 * `PersistentMap`, keys unchanged, avoiding an intermediate mutable `Map`. */
fun <K, V, R> Map<K, V>.immutableMapValuesTo(map: (V) -> R) =
    persistentMapOf<K, R>().builder().apply {
        this@immutableMapValuesTo.forEach { (key, value) -> put(key, map(value)) }
    }.build()