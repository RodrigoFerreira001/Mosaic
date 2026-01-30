package dev.catbit.mosaic.core.data.schemas.tile

interface GroupingTileSchema : TileSchema {
    val tiles: List<TileSchema>
}