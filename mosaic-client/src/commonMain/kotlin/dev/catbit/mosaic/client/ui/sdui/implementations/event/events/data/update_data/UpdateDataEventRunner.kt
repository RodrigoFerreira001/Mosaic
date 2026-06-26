package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.update_data

import dev.catbit.mosaic.client.domain.data.plain.UpdatePlainDataUseCase
import dev.catbit.mosaic.client.domain.data.segmented.UpdateSegmentedDataUseCase
import dev.catbit.mosaic.client.ui.sdui.foundation.data_holder.ApplicationDataHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.UpdateDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.domain.base.IO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UpdateDataEventRunner : EventRunner<UpdateDataEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: UpdateDataEventSchema) {

        val updatePlainDataUseCase = get<UpdatePlainDataUseCase>()
        val updateSegmentedDataUseCase = get<UpdateSegmentedDataUseCase>()
        val applicationDataHolder = get<ApplicationDataHolder>()

        var anyErrorOccurred = false

        withContext(Dispatchers.IO) {
            with(event) {
                updates
                    .groupBy(
                        keySelector = { it.dataSource },
                        valueTransform = { it.updateData }
                    )
                    .forEach { (dataSource, updates) ->
                        val entries = updates.flatMap { update ->
                            resolveUpdateData(update, incomingData)?.entries.orEmpty()
                        }

                        when (dataSource) {
                            DataSourceSchema.ApplicationPlainData -> {
                                entries.forEach { (dataKey, data) ->
                                    applicationDataHolder.addPlainData(dataId = dataKey, data = data)
                                }
                            }

                            is DataSourceSchema.ApplicationSegmentedData -> {
                                entries.forEach { (dataKey, data) ->
                                    applicationDataHolder.addSegmentedData(
                                        segmentId = dataSource.segmentId,
                                        dataId = dataKey,
                                        data = data
                                    )
                                }
                            }

                            DataSourceSchema.ScreenPlainData -> {
                                entries.forEach { (dataKey, data) ->
                                    screenDataHolder.addPlainData(dataId = dataKey, data = data)
                                }
                            }

                            is DataSourceSchema.ScreenSegmentedData -> {
                                entries.forEach { (dataKey, data) ->
                                    screenDataHolder.addSegmentedData(
                                        segmentId = dataSource.segmentId,
                                        dataId = dataKey,
                                        data = data
                                    )
                                }
                            }

                            DataSourceSchema.PlainDataBase -> {
                                entries.forEach { (dataKey, data) ->
                                    updatePlainDataUseCase(
                                        UpdatePlainDataUseCase.Params(dataKey = dataKey, data = data)
                                    ).onFailure {
                                        logError(
                                            throwable = it,
                                            tag = "UpdateDataEventRunner"
                                        )
                                        anyErrorOccurred = true
                                    }
                                }
                            }

                            is DataSourceSchema.SegmentedDataBase -> {
                                entries.forEach { (dataKey, data) ->
                                    updateSegmentedDataUseCase(
                                        UpdateSegmentedDataUseCase.Params(
                                            segmentKey = dataSource.segmentId,
                                            dataKey = dataKey,
                                            data = data
                                        )
                                    ).onFailure {
                                        logError(
                                            throwable = it,
                                            tag = "UpdateDataEventRunner"
                                        )
                                        anyErrorOccurred = true
                                    }
                                }
                            }

                            DataSourceSchema.ScreenNavigationData -> Unit
                            is DataSourceSchema.Tile -> Unit
                        }
                    }
            }
        }

        if (anyErrorOccurred) {
            onTrigger(EventTriggers.onFailure())
        } else {
            onTrigger(EventTriggers.onSuccess())
        }
    }

    private fun EventRunningScope.resolveUpdateData(
        update: UpdateDataEventSchema.Update.UpdateDate,
        incomingData: Any?
    ) = when (update) {
        UpdateDataEventSchema.Update.UpdateDate.Incoming -> incomingData.asMapAny()
        is UpdateDataEventSchema.Update.UpdateDate.Inline -> update.data
    }
}
