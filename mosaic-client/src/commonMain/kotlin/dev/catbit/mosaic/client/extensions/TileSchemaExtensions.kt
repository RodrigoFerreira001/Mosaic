package dev.catbit.mosaic.client.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * Filters the children a container tile should render according to its `filterChildrenByTerm`.
 *
 * When [term] is `null` or blank every child is kept. Otherwise only children whose
 * `searchableTerms` contain [term] as a case-insensitive substring survive — children without
 * searchable terms are dropped.
 */
@Composable
fun ImmutableList<TileSchema>.filteredBy(
    term: String?
): ImmutableList<TileSchema> = remember(this, term) {
    val filterTerm = term?.takeIf { it.isNotEmpty() } ?: return@remember this

    filter { tile ->
        tile.searchableTerms?.any { it.contains(filterTerm, ignoreCase = true) } == true
    }.toImmutableList()
}
