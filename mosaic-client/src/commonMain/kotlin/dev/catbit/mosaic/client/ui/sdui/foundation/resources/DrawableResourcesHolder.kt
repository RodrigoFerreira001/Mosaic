package dev.catbit.mosaic.client.ui.sdui.foundation.resources

import org.jetbrains.compose.resources.DrawableResource

class DrawableResourcesHolder(
    private val resources: Map<String, DrawableResource>
) {
    operator fun get(name: String): DrawableResource? = resources[name]
}
