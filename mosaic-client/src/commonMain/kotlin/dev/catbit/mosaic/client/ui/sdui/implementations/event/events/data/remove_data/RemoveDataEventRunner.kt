package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.remove_data

import dev.catbit.mosaic.client.domain.data.plain.RemovePlainDataByIdsUseCase
import dev.catbit.mosaic.client.domain.data.plain.RemovePlainDataUseCase
import dev.catbit.mosaic.client.domain.data.plain.WipePlainDataUseCase
import dev.catbit.mosaic.client.domain.data.segmented.RemoveSegmentedDataByIdsUseCase
import dev.catbit.mosaic.client.domain.data.segmented.RemoveSegmentedDataUseCase
import dev.catbit.mosaic.client.domain.data.segmented.WipeSegmentedDataUseCase
import dev.catbit.mosaic.client.ui.sdui.foundation.data_holder.ApplicationDataHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.data.AccessModeSchema
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.RemoveDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.domain.base.IO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object RemoveDataEventRunner : EventRunner<RemoveDataEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: RemoveDataEventSchema) {

        val removePlainDataUseCase = get<RemovePlainDataUseCase>()
        val removePlainDataByIdsUseCase = get<RemovePlainDataByIdsUseCase>()
        val wipePlainDataUseCase = get<WipePlainDataUseCase>()
        val removeSegmentedDataUseCase = get<RemoveSegmentedDataUseCase>()
        val removeSegmentedDataByIdsUseCase = get<RemoveSegmentedDataByIdsUseCase>()
        val wipeSegmentedDataUseCase = get<WipeSegmentedDataUseCase>()
        val applicationDataHolder = get<ApplicationDataHolder>()

        var anyErrorOccurred = false

        withContext(Dispatchers.IO) {
            event.deletions.forEach { (dataSource, accessMode) ->
                when (dataSource) {
                    DataSourceSchema.ApplicationPlainData -> when (accessMode) {
                        AccessModeSchema.Full -> applicationDataHolder.wipePlainData()
                        is AccessModeSchema.Single -> applicationDataHolder.removePlainData(accessMode.dataId)
                        is AccessModeSchema.Batch -> accessMode.dataIds.forEach { applicationDataHolder.removePlainData(it) }
                    }

                    is DataSourceSchema.ApplicationSegmentedData -> when (accessMode) {
                        AccessModeSchema.Full -> applicationDataHolder.wipeSegmentedData(dataSource.segmentId)
                        is AccessModeSchema.Single -> applicationDataHolder.removeSegmentedData(
                            segmentId = dataSource.segmentId,
                            dataId = accessMode.dataId
                        )
                        is AccessModeSchema.Batch -> accessMode.dataIds.forEach {
                            applicationDataHolder.removeSegmentedData(segmentId = dataSource.segmentId, dataId = it)
                        }
                    }

                    DataSourceSchema.ScreenPlainData -> when (accessMode) {
                        AccessModeSchema.Full -> screenDataHolder.wipePlainData()
                        is AccessModeSchema.Single -> screenDataHolder.removePlainData(accessMode.dataId)
                        is AccessModeSchema.Batch -> accessMode.dataIds.forEach { screenDataHolder.removePlainData(it) }
                    }

                    is DataSourceSchema.ScreenSegmentedData -> when (accessMode) {
                        AccessModeSchema.Full -> screenDataHolder.wipeSegmentedData(dataSource.segmentId)
                        is AccessModeSchema.Single -> screenDataHolder.removeSegmentedData(
                            segmentId = dataSource.segmentId,
                            dataId = accessMode.dataId
                        )
                        is AccessModeSchema.Batch -> accessMode.dataIds.forEach {
                            screenDataHolder.removeSegmentedData(segmentId = dataSource.segmentId, dataId = it)
                        }
                    }

                    DataSourceSchema.PlainDataBase -> when (accessMode) {
                        AccessModeSchema.Full -> wipePlainDataUseCase(Unit)
                            .onFailure { anyErrorOccurred = true }
                        is AccessModeSchema.Single -> removePlainDataUseCase(
                            RemovePlainDataUseCase.Params(dataKey = accessMode.dataId)
                        ).onFailure { anyErrorOccurred = true }
                        is AccessModeSchema.Batch -> removePlainDataByIdsUseCase(
                            RemovePlainDataByIdsUseCase.Params(dataKeys = accessMode.dataIds)
                        ).onFailure { anyErrorOccurred = true }
                    }

                    is DataSourceSchema.SegmentedDataBase -> when (accessMode) {
                        AccessModeSchema.Full -> wipeSegmentedDataUseCase(
                            WipeSegmentedDataUseCase.Params(segmentKey = dataSource.segmentId)
                        ).onFailure { anyErrorOccurred = true }
                        is AccessModeSchema.Single -> removeSegmentedDataUseCase(
                            RemoveSegmentedDataUseCase.Params(
                                segmentKey = dataSource.segmentId,
                                dataKey = accessMode.dataId
                            )
                        ).onFailure { anyErrorOccurred = true }
                        is AccessModeSchema.Batch -> removeSegmentedDataByIdsUseCase(
                            RemoveSegmentedDataByIdsUseCase.Params(
                                segmentKey = dataSource.segmentId,
                                dataKeys = accessMode.dataIds
                            )
                        ).onFailure { anyErrorOccurred = true }
                    }

                    DataSourceSchema.ScreenNavigationData -> Unit
                    is DataSourceSchema.Tile -> Unit
                }
            }
        }

        if (anyErrorOccurred) {
            onTrigger(EventTriggers.onFailure())
        } else {
            onTrigger(EventTriggers.onSuccess())
        }
    }
}
