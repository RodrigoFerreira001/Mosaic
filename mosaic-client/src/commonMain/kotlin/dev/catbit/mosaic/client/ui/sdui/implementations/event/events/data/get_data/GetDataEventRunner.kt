package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.get_data

import dev.catbit.mosaic.client.domain.data.plain.GetAllPlainDataUseCase
import dev.catbit.mosaic.client.domain.data.plain.GetPlainDataByIdsUseCase
import dev.catbit.mosaic.client.domain.data.plain.GetPlainDataUseCase
import dev.catbit.mosaic.client.domain.data.segmented.GetAllSegmentedDataUseCase
import dev.catbit.mosaic.client.domain.data.segmented.GetSegmentedDataByIdsUseCase
import dev.catbit.mosaic.client.domain.data.segmented.GetSegmentedDataUseCase
import dev.catbit.mosaic.client.ui.sdui.foundation.data_holder.ApplicationDataHolder
import dev.catbit.mosaic.client.exceptions.DataNotFoundException
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.data.AccessModeSchema
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.GetDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.domain.base.IO
import dev.catbit.mosaic.core.domain.base.invoke
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private enum class ResultLevel { ANY, LIST, MAP }

object GetDataEventRunner : EventRunner<GetDataEventSchema> {

    override suspend fun EventRunningScope.runEvent(event: GetDataEventSchema) {
        withContext(Dispatchers.IO) {
            onTrigger(EventTriggers.onStart())

            val getPlainDataUseCase = get<GetPlainDataUseCase>()
            val getPlainDataByIdsUseCase = get<GetPlainDataByIdsUseCase>()
            val getAllPlainDataUseCase = get<GetAllPlainDataUseCase>()
            val getSegmentedDataUseCase = get<GetSegmentedDataUseCase>()
            val getSegmentedDataByIdsUseCase = get<GetSegmentedDataByIdsUseCase>()
            val getAllSegmentedDataUseCase = get<GetAllSegmentedDataUseCase>()
            val applicationDataHolder = get<ApplicationDataHolder>()

            // Accumulates all key-value results across readings.
            // Later readings overwrite earlier ones on key collision.
            val accumulator = mutableMapOf<String, Any>()

            // Tracks the final output type by precedence: MAP > LIST > ANY
            var resultLevel = ResultLevel.ANY

            for (reading in event.readings) {
                when (val accessMode = reading.accessMode) {

                    is AccessModeSchema.Full -> {
                        val data = when (val source = reading.dataSource) {
                            DataSourceSchema.PlainDataBase ->
                                getAllPlainDataUseCase()
                                    .getOrElse {
                                        onTrigger(EventTriggers.onFailure(), data = it)
                                        return@withContext
                                    }

                            is DataSourceSchema.SegmentedDataBase ->
                                getAllSegmentedDataUseCase(GetAllSegmentedDataUseCase.Params(source.segmentId))
                                    .getOrElse {
                                        onTrigger(EventTriggers.onFailure(), data = it)
                                        return@withContext
                                    }

                            DataSourceSchema.ApplicationPlainData ->
                                applicationDataHolder.getAllPlainData()

                            is DataSourceSchema.ApplicationSegmentedData ->
                                applicationDataHolder.getAllSegmentedData(source.segmentId)

                            DataSourceSchema.ScreenPlainData ->
                                screenDataHolder.getAllPlainData()

                            is DataSourceSchema.ScreenSegmentedData ->
                                screenDataHolder.getAllSegmentedData(source.segmentId)

                            DataSourceSchema.ScreenNavigationData ->
                                screenDataHolder.getAllNavigationData()

                            is DataSourceSchema.Tile ->
                                tilesValueProducer.getValueWithKey(
                                    tileId = source.tileId,
                                    key = source.dataKey
                                ) ?: emptyMap()
                        }

                        accumulator.putAll(data)
                        resultLevel = ResultLevel.MAP
                    }

                    is AccessModeSchema.Batch -> {
                        val batchResults: Map<String, Any?> = when (val source = reading.dataSource) {
                            DataSourceSchema.PlainDataBase ->
                                getPlainDataByIdsUseCase(GetPlainDataByIdsUseCase.Params(accessMode.dataIds)).getOrElse { emptyMap() }

                            is DataSourceSchema.SegmentedDataBase ->
                                getSegmentedDataByIdsUseCase(GetSegmentedDataByIdsUseCase.Params(source.segmentId, accessMode.dataIds)).getOrElse { emptyMap() }

                            DataSourceSchema.ApplicationPlainData ->
                                accessMode.dataIds.associateWith { applicationDataHolder.getPlainData(it) }

                            is DataSourceSchema.ApplicationSegmentedData ->
                                accessMode.dataIds.associateWith { applicationDataHolder.getSegmentedData(it, source.segmentId) }

                            DataSourceSchema.ScreenPlainData ->
                                accessMode.dataIds.associateWith { screenDataHolder.getPlainData(it) }

                            is DataSourceSchema.ScreenSegmentedData ->
                                accessMode.dataIds.associateWith { screenDataHolder.getSegmentedData(it, source.segmentId) }

                            DataSourceSchema.ScreenNavigationData ->
                                accessMode.dataIds.associateWith { screenDataHolder.getNavigationData(it) }

                            is DataSourceSchema.Tile ->
                                tilesValueProducer.getValueWithKey(
                                    tileId = source.tileId,
                                    key = source.dataKey
                                ) ?: emptyMap()
                        }

                        for (dataId in accessMode.dataIds) {
                            val value = batchResults[dataId]
                            if (value == null) {
                                if (!accessMode.allowMissingData) {
                                    onTrigger(
                                        eventTrigger = EventTriggers.onFailure(),
                                        data = DataNotFoundException("Data with key '$dataId' not found.")
                                    )
                                    return@withContext
                                }
                            } else {
                                accumulator[dataId] = value
                            }
                        }

                        if (!accessMode.unwrapValuesToList) {
                            resultLevel = ResultLevel.MAP
                        } else if (resultLevel != ResultLevel.MAP) {
                            resultLevel = ResultLevel.LIST
                        }
                    }

                    is AccessModeSchema.Single -> {
                        val value = when (val source = reading.dataSource) {
                            DataSourceSchema.PlainDataBase ->
                                getPlainDataUseCase(GetPlainDataUseCase.Params(accessMode.dataId)).getOrNull()

                            is DataSourceSchema.SegmentedDataBase ->
                                getSegmentedDataUseCase(GetSegmentedDataUseCase.Params(source.segmentId, accessMode.dataId)).getOrNull()

                            DataSourceSchema.ApplicationPlainData ->
                                applicationDataHolder.getPlainData(accessMode.dataId)

                            is DataSourceSchema.ApplicationSegmentedData ->
                                applicationDataHolder.getSegmentedData(accessMode.dataId, source.segmentId)

                            DataSourceSchema.ScreenPlainData ->
                                screenDataHolder.getPlainData(accessMode.dataId)

                            is DataSourceSchema.ScreenSegmentedData ->
                                screenDataHolder.getSegmentedData(accessMode.dataId, source.segmentId)

                            DataSourceSchema.ScreenNavigationData ->
                                screenDataHolder.getNavigationData(accessMode.dataId)

                            is DataSourceSchema.Tile ->
                                tilesValueProducer.getValueWithKey(
                                    tileId = source.tileId,
                                    key = source.dataKey
                                )
                        }

                        if (value == null) {
                            onTrigger(
                                eventTrigger = EventTriggers.onFailure(),
                                data = DataNotFoundException("Data with key '${accessMode.dataId}' not found.")
                            )
                            return@withContext
                        }

                        accumulator[accessMode.dataId] = value
                    }
                }
            }

            val finalData: Any = when (resultLevel) {
                ResultLevel.MAP -> accumulator
                ResultLevel.LIST -> accumulator.values.toList()
                ResultLevel.ANY -> if (event.readings.size == 1) accumulator.values.first() else accumulator.values.toList()
            }

            onTrigger(EventTriggers.onSuccess(), data = finalData)
        }
    }
}
