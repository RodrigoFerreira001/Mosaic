package dev.catbit.mosaic.sample.client.sdui.tiles.code.code_viewer

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.sample.core.schemas.tiles.code.CodeViewerTileSchema

object CodeViewerTileDefinition : TileDefinition<CodeViewerTileSchema> {
    override val tileSchemaClass = CodeViewerTileSchema::class
    override val tileRenderer = CodeViewerTileRenderer
    override val tileHolderBuilder = CodeViewerTileHolderBuilder
}
