package dev.catbit.mosaic.client.ui.sdui.foundation.resources

import org.jetbrains.compose.resources.DrawableResource

/**
 * Resolves the plain string name the `Image` tile's `resourceName` field references to an actual
 * app-bundled [DrawableResource] — the mechanism behind `Image` (as opposed to `AsyncImage`, which
 * loads from a URL/bytes and doesn't need this holder). A single Koin `single`, built once at
 * startup from `MosaicDependencyInjectionConfig.drawableResources`.
 *
 * @param resources every registered name → drawable mapping, supplied by the app host.
 */
class DrawableResourcesHolder(
    private val resources: Map<String, DrawableResource>
) {
    /**
     * Looks up the drawable registered under [name].
     *
     * @return the matching [DrawableResource], or `null` if nothing is registered under [name] — in
     * which case `ImageTileRenderer` renders nothing at all, no fallback, no error trigger.
     */
    operator fun get(name: String): DrawableResource? = resources[name]
}
