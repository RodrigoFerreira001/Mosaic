package dev.catbit.mosaic.server.builder.data

import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema

fun applicationPlainData() = DataSourceSchema.ApplicationPlainData

fun applicationSegmentedData(segmentId: String) = DataSourceSchema.ApplicationSegmentedData(segmentId)

fun segmentedDataBase(segmentId: String) = DataSourceSchema.SegmentedDataBase(segmentId)

fun plainDataBase() = DataSourceSchema.PlainDataBase

fun screenNavigationData() = DataSourceSchema.ScreenNavigationData

fun screenPlainData() = DataSourceSchema.ScreenPlainData

fun screenSegmentedData(segmentId: String) = DataSourceSchema.ScreenSegmentedData(segmentId)

fun tile(tileId: String, dataKey: String) = DataSourceSchema.Tile(tileId, dataKey)
