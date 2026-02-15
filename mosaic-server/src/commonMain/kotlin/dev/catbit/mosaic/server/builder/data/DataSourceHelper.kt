package dev.catbit.mosaic.server.builder.data

import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema

fun segmentedDataBase(segmentId: String) = DataSourceSchema.SegmentedDataBase(segmentId)

fun plainDataBase() = DataSourceSchema.PlainDataBase

fun screenNavigationData() = DataSourceSchema.ScreenNavigationData

fun screenPlainData() = DataSourceSchema.ScreenPlainData

fun screenSegmentedData(segmentId: String) = DataSourceSchema.ScreenSegmentedData(segmentId)
