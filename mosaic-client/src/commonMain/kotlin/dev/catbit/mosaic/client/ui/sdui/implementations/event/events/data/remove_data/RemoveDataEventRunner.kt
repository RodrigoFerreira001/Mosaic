package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.remove_data

import dev.catbit.mosaic.client.domain.data.plain.RemovePlainDataByIdsUseCase
import dev.catbit.mosaic.client.domain.data.plain.RemovePlainDataByIdsUseCase.*
import dev.catbit.mosaic.client.domain.data.plain.RemovePlainDataUseCase
import dev.catbit.mosaic.client.domain.data.plain.RemovePlainDataUseCase.*
import dev.catbit.mosaic.client.domain.data.plain.WipePlainDataUseCase
import dev.catbit.mosaic.client.domain.data.segmented.RemoveSegmentedDataByIdsUseCase
import dev.catbit.mosaic.client.domain.data.segmented.RemoveSegmentedDataByIdsUseCase.*
import dev.catbit.mosaic.client.domain.data.segmented.RemoveSegmentedDataUseCase
import dev.catbit.mosaic.client.domain.data.segmented.RemoveSegmentedDataUseCase.*
import dev.catbit.mosaic.client.domain.data.segmented.WipeSegmentedDataUseCase
import dev.catbit.mosaic.client.domain.data.segmented.WipeSegmentedDataUseCase.*
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.data.AccessModeSchema
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.RemoveDataEventSchema
import dev.catbit.mosaic.core.domain.base.IO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object RemoveDataEventRunner : EventRunner<RemoveDataEventSchema> {
    override fun EventRunningScope.runEvent(event: RemoveDataEventSchema) {

        val removePlainDataUseCase = get<RemovePlainDataUseCase>()
        val removePlainDataByIdsUseCase = get<RemovePlainDataByIdsUseCase>()
        val wipePlainDataUseCase = get<WipePlainDataUseCase>()
        val removeSegmentedDataUseCase = get<RemoveSegmentedDataUseCase>()
        val removeSegmentedDataByIdsUseCase = get<RemoveSegmentedDataByIdsUseCase>()
        val wipeSegmentedDataUseCase = get<WipeSegmentedDataUseCase>()

        runSuspendOnScreenScope {
            withContext(Dispatchers.IO) {
                event.deletions.forEach { (dataSource, accessMode) ->
                    when (dataSource) {
                        DataSourceSchema.ScreenPlainData -> when (accessMode) {
                            AccessModeSchema.Full -> dataHolder.wipePlainData()
                            is AccessModeSchema.Single -> dataHolder.removePlainData(accessMode.dataId)
                            is AccessModeSchema.Batch -> accessMode.dataIds.forEach { dataHolder.removePlainData(it) }
                        }

                        is DataSourceSchema.ScreenSegmentedData -> when (accessMode) {
                            AccessModeSchema.Full -> dataHolder.wipeSegmentedData(dataSource.segmentId)
                            is AccessModeSchema.Single -> dataHolder.removeSegmentedData(
                                segmentId = dataSource.segmentId,
                                dataId = accessMode.dataId
                            )
                            is AccessModeSchema.Batch -> accessMode.dataIds.forEach {
                                dataHolder.removeSegmentedData(segmentId = dataSource.segmentId, dataId = it)
                            }
                        }

                        DataSourceSchema.PlainDataBase -> when (accessMode) {
                            AccessModeSchema.Full -> wipePlainDataUseCase(Unit)
                            is AccessModeSchema.Single -> removePlainDataUseCase(
                                RemovePlainDataUseCase.Params(dataKey = accessMode.dataId)
                            )
                            is AccessModeSchema.Batch -> removePlainDataByIdsUseCase(
                                RemovePlainDataByIdsUseCase.Params(dataKeys = accessMode.dataIds)
                            )
                        }

                        is DataSourceSchema.SegmentedDataBase -> when (accessMode) {
                            AccessModeSchema.Full -> wipeSegmentedDataUseCase(
                                WipeSegmentedDataUseCase.Params(segmentKey = dataSource.segmentId)
                            )
                            is AccessModeSchema.Single -> removeSegmentedDataUseCase(
                                RemoveSegmentedDataUseCase.Params(
                                    segmentKey = dataSource.segmentId,
                                    dataKey = accessMode.dataId
                                )
                            )
                            is AccessModeSchema.Batch -> removeSegmentedDataByIdsUseCase(
                                RemoveSegmentedDataByIdsUseCase.Params(
                                    segmentKey = dataSource.segmentId,
                                    dataKeys = accessMode.dataIds
                                )
                            )
                        }

                        DataSourceSchema.ScreenNavigationData -> Unit
                        is DataSourceSchema.Tile -> Unit
                    }
                }
            }
        }
    }
}
