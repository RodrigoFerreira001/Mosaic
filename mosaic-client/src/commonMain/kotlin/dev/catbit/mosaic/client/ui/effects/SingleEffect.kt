package dev.catbit.mosaic.client.ui.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope

/**
 * `LaunchedEffect(Unit) { }` under a clearer name — runs [block] exactly once, the first time this
 * composable enters composition, and never again on recomposition (only if it leaves and re-enters
 * composition entirely, e.g. the call site itself is removed and re-added). Used wherever "once per
 * composition, no dependency to key on" needs to read as intent rather than as an easily-misread
 * `Unit` key — for example, `MosaicApplication`'s own root-navigator registration.
 *
 * @param block the one-shot suspend work to run.
 */
@Composable
fun SingleEffect(
    block: suspend CoroutineScope.() -> Unit
) {
    LaunchedEffect(Unit) {
        block()
    }
}