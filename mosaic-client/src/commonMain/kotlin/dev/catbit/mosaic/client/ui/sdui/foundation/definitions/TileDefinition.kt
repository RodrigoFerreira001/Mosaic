package dev.catbit.mosaic.client.ui.sdui.foundation.definitions

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlin.reflect.KClass

/**
 * Ties one [TileSchema] subtype to everything `mosaic-client` needs to render and hold it — the unit
 * passed in `MosaicDependencyInjectionConfig.tileDefinitions` to register a custom tile, and the
 * shape every built-in tile's own `XTileDefinition` object follows. `MosaicModules` uses
 * [tileSchemaClass] to build the framework's `MosaicSerializer` registration, [tileRenderer] to build
 * the `TileRendererManager` lookup table, and [tileHolderBuilder] to build the
 * `TileHolderBuilderManager` lookup table — one `Definition` feeds all three, so registering a custom
 * tile is exactly this one object, nothing more.
 */
interface TileDefinition <Schema: TileSchema> {
    /** The schema class this definition is for — the key everything else is looked up by. */
    val tileSchemaClass: KClass<Schema>
    /** Renders tiles of this schema type. */
    val tileRenderer: TileRenderer<Schema>
    /** Builds the live `TileHolder` for tiles of this schema type. */
    val tileHolderBuilder: TileHolderBuilder<Schema, out TileHolder<Schema>>
}