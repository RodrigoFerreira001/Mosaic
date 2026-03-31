package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.evaluate_data

import dev.catbit.mosaic.client.domain.data.plain.GetAllPlainDataUseCase
import dev.catbit.mosaic.client.domain.data.plain.GetPlainDataByIdsUseCase
import dev.catbit.mosaic.client.domain.data.plain.GetPlainDataUseCase
import dev.catbit.mosaic.client.domain.data.segmented.GetAllSegmentedDataUseCase
import dev.catbit.mosaic.client.domain.data.segmented.GetSegmentedDataByIdsUseCase
import dev.catbit.mosaic.client.domain.data.segmented.GetSegmentedDataUseCase
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.data.AccessModeSchema
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.EvaluateDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.EvaluateDataEventSchema.Expression
import dev.catbit.mosaic.core.data.schemas.event.events.data.EvaluateDataEventSchema.Expression.DataExpression
import dev.catbit.mosaic.core.data.schemas.event.events.data.EvaluateDataEventSchema.Expression.DataExpression.Operation
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.domain.base.IO
import dev.catbit.mosaic.core.domain.base.invoke
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime

object EvaluateDataEventRunner : EventRunner<EvaluateDataEventSchema> {

    override fun EventRunningScope.runEvent(event: EvaluateDataEventSchema) {
        runSuspendOnScreenScope {
            withContext(Dispatchers.IO) {
                val result = runCatching { evaluate(event.expression) }
                    .getOrElse {
                        logError(it)
                        onTrigger(EventTriggers.onFailure(), data = it)
                        return@withContext
                    }

                if (result) {
                    onTrigger(EventTriggers.onSuccess(), data = incomingData)
                } else {
                    onTrigger(EventTriggers.onFailure(), data = incomingData)
                }
            }
        }
    }

    private suspend fun EventRunningScope.evaluate(expression: Expression): Boolean =
        when (expression) {
            is Expression.NotExpression -> !evaluate(expression.expression)
            is Expression.OrExpression -> evaluate(expression.leftExpression) || evaluate(expression.rightExpression)
            is Expression.AndExpression -> evaluate(expression.leftExpression) && evaluate(expression.rightExpression)
            is DataExpression -> {
                val value = resolveData(expression.data)
                applyOperation(value, expression.operation)
            }
        }

    private suspend fun EventRunningScope.resolveData(data: DataExpression.Data): Any? =
        when (data) {
            is DataExpression.Data.IncomingData -> incomingData
            is DataExpression.Data.DataSourceData -> fetchFromSource(data.reading)
        }

    private suspend fun EventRunningScope.fetchFromSource(
        reading: DataExpression.Data.DataSourceData.Reading
    ): Any? {
        val getPlainDataUseCase = get<GetPlainDataUseCase>()
        val getPlainDataByIdsUseCase = get<GetPlainDataByIdsUseCase>()
        val getAllPlainDataUseCase = get<GetAllPlainDataUseCase>()
        val getSegmentedDataUseCase = get<GetSegmentedDataUseCase>()
        val getSegmentedDataByIdsUseCase = get<GetSegmentedDataByIdsUseCase>()
        val getAllSegmentedDataUseCase = get<GetAllSegmentedDataUseCase>()

        return when (val accessMode = reading.accessMode) {
            is AccessModeSchema.Full -> when (val source = reading.dataSource) {
                DataSourceSchema.PlainDataBase ->
                    getAllPlainDataUseCase().getOrNull()

                is DataSourceSchema.SegmentedDataBase ->
                    getAllSegmentedDataUseCase(GetAllSegmentedDataUseCase.Params(source.segmentId)).getOrNull()

                DataSourceSchema.ScreenPlainData ->
                    dataHolder.getAllPlainData()

                is DataSourceSchema.ScreenSegmentedData ->
                    dataHolder.getAllSegmentedData(source.segmentId)

                DataSourceSchema.ScreenNavigationData ->
                    dataHolder.getAllNavigationData()

                is DataSourceSchema.Tile ->
                    tilesValueProducer.getValueWithKey(
                        tileId = source.tileId,
                        key = source.dataKey
                    )
            }

            is AccessModeSchema.Batch -> {
                val results: Map<String, Any?> = when (val source = reading.dataSource) {
                    DataSourceSchema.PlainDataBase ->
                        getPlainDataByIdsUseCase(GetPlainDataByIdsUseCase.Params(accessMode.dataIds)).getOrElse { emptyMap() }

                    is DataSourceSchema.SegmentedDataBase ->
                        getSegmentedDataByIdsUseCase(GetSegmentedDataByIdsUseCase.Params(source.segmentId, accessMode.dataIds)).getOrElse { emptyMap() }

                    DataSourceSchema.ScreenPlainData ->
                        accessMode.dataIds.associateWith { dataHolder.getPlainData(it) }

                    is DataSourceSchema.ScreenSegmentedData ->
                        accessMode.dataIds.associateWith { dataHolder.getSegmentedData(it, source.segmentId) }

                    DataSourceSchema.ScreenNavigationData ->
                        accessMode.dataIds.associateWith { dataHolder.getNavigationData(it) }

                    is DataSourceSchema.Tile ->
                        tilesValueProducer.getValueWithKey(
                            tileId = source.tileId,
                            key = source.dataKey
                        ) ?: emptyMap()
                }

                val found = results.filterValues { it != null }.mapValues { it.value!! }
                if (accessMode.unwrapValuesToList) found.values.toList() else found
            }

            is AccessModeSchema.Single -> when (val source = reading.dataSource) {
                DataSourceSchema.PlainDataBase ->
                    getPlainDataUseCase(GetPlainDataUseCase.Params(accessMode.dataId)).getOrNull()

                is DataSourceSchema.SegmentedDataBase ->
                    getSegmentedDataUseCase(GetSegmentedDataUseCase.Params(source.segmentId, accessMode.dataId)).getOrNull()

                DataSourceSchema.ScreenPlainData ->
                    dataHolder.getPlainData(accessMode.dataId)

                is DataSourceSchema.ScreenSegmentedData ->
                    dataHolder.getSegmentedData(accessMode.dataId, source.segmentId)

                DataSourceSchema.ScreenNavigationData ->
                    dataHolder.getNavigationData(accessMode.dataId)

                is DataSourceSchema.Tile ->
                    tilesValueProducer.getValueWithKey(
                        tileId = source.tileId,
                        key = source.dataKey
                    )
            }
        }
    }

    private fun applyOperation(value: Any?, operation: Operation): Boolean = when (operation) {

        // Null
        is Operation.NullOperation.IsNull -> value == null
        is Operation.NullOperation.IsNotNull -> value != null

        // String
        is Operation.StringOperation.IsEqualsTo -> (value as? String) == operation.target
        is Operation.StringOperation.IsLengthSmallerThan -> (value as? String)?.length?.let { it < operation.length } ?: false
        is Operation.StringOperation.IsLengthSmallerThanOrEquals -> (value as? String)?.length?.let { it <= operation.length } ?: false
        is Operation.StringOperation.IsLengthEqualsTo -> (value as? String)?.length?.let { it == operation.length } ?: false
        is Operation.StringOperation.IsLengthBiggerThan -> (value as? String)?.length?.let { it > operation.length } ?: false
        is Operation.StringOperation.IsLengthBiggerThanOrEquals -> (value as? String)?.length?.let { it >= operation.length } ?: false
        is Operation.StringOperation.MatchesRegex -> (value as? String)?.matches(Regex(operation.regex)) ?: false
        is Operation.StringOperation.Contains -> (value as? String)?.contains(operation.substring) ?: false
        is Operation.StringOperation.StartsWith -> (value as? String)?.startsWith(operation.prefix) ?: false
        is Operation.StringOperation.EndsWith -> (value as? String)?.endsWith(operation.suffix) ?: false
        is Operation.StringOperation.EqualsIgnoreCase -> (value as? String)?.equals(operation.target, ignoreCase = true) ?: false
        is Operation.StringOperation.IsBlank -> (value as? String)?.isBlank() ?: false
        is Operation.StringOperation.IsNotBlank -> (value as? String)?.isNotBlank() ?: false

        // Int
        is Operation.IntOperation.IsEven -> (value as? Int)?.let { it % 2 == 0 } ?: false
        is Operation.IntOperation.IsOdd -> (value as? Int)?.let { it % 2 != 0 } ?: false
        is Operation.IntOperation.IsSmallerThan -> (value as? Int)?.let { it < operation.target } ?: false
        is Operation.IntOperation.IsSmallerThanOrEquals -> (value as? Int)?.let { it <= operation.target } ?: false
        is Operation.IntOperation.IsEqualsTo -> (value as? Int)?.let { it == operation.target } ?: false
        is Operation.IntOperation.IsBiggerThan -> (value as? Int)?.let { it > operation.target } ?: false
        is Operation.IntOperation.IsBiggerThanOrEquals -> (value as? Int)?.let { it >= operation.target } ?: false

        // Long
        is Operation.LongOperation.IsSmallerThan -> (value as? Long)?.let { it < operation.target } ?: false
        is Operation.LongOperation.IsSmallerThanOrEquals -> (value as? Long)?.let { it <= operation.target } ?: false
        is Operation.LongOperation.IsEqualsTo -> (value as? Long)?.let { it == operation.target } ?: false
        is Operation.LongOperation.IsBiggerThan -> (value as? Long)?.let { it > operation.target } ?: false
        is Operation.LongOperation.IsBiggerThanOrEquals -> (value as? Long)?.let { it >= operation.target } ?: false

        // Float
        is Operation.FloatOperation.IsSmallerThan -> (value as? Float)?.let { it < operation.target } ?: false
        is Operation.FloatOperation.IsSmallerThanOrEquals -> (value as? Float)?.let { it <= operation.target } ?: false
        is Operation.FloatOperation.IsEqualsTo -> (value as? Float)?.let { it == operation.target } ?: false
        is Operation.FloatOperation.IsBiggerThan -> (value as? Float)?.let { it > operation.target } ?: false
        is Operation.FloatOperation.IsBiggerThanOrEquals -> (value as? Float)?.let { it >= operation.target } ?: false

        // Double
        is Operation.DoubleOperation.IsSmallerThan -> (value as? Double)?.let { it < operation.target } ?: false
        is Operation.DoubleOperation.IsSmallerThanOrEquals -> (value as? Double)?.let { it <= operation.target } ?: false
        is Operation.DoubleOperation.IsEqualsTo -> (value as? Double)?.let { it == operation.target } ?: false
        is Operation.DoubleOperation.IsBiggerThan -> (value as? Double)?.let { it > operation.target } ?: false
        is Operation.DoubleOperation.IsBiggerThanOrEquals -> (value as? Double)?.let { it >= operation.target } ?: false

        // Boolean
        is Operation.BooleanOperation.IsTrue -> (value as? Boolean) == true
        is Operation.BooleanOperation.IsFalse -> (value as? Boolean) == false

        // Map
        is Operation.MapOperation.ContainsKey -> (value as? Map<*, *>)?.containsKey(operation.key) ?: false
        is Operation.MapOperation.ContainsValue -> (value as? Map<*, *>)?.containsValue(operation.value) ?: false
        is Operation.MapOperation.IsEmpty -> (value as? Map<*, *>)?.isEmpty() ?: false
        is Operation.MapOperation.IsNotEmpty -> (value as? Map<*, *>)?.isNotEmpty() ?: false
        is Operation.MapOperation.IsSizeSmallerThan -> (value as? Map<*, *>)?.size?.let { it < operation.size } ?: false
        is Operation.MapOperation.IsSizeSmallerThanOrEquals -> (value as? Map<*, *>)?.size?.let { it <= operation.size } ?: false
        is Operation.MapOperation.IsSizeEqualsTo -> (value as? Map<*, *>)?.size?.let { it == operation.size } ?: false
        is Operation.MapOperation.IsSizeBiggerThan -> (value as? Map<*, *>)?.size?.let { it > operation.size } ?: false
        is Operation.MapOperation.IsSizeBiggerThanOrEquals -> (value as? Map<*, *>)?.size?.let { it >= operation.size } ?: false
        is Operation.MapOperation.ValueAtKeyEquals -> (value as? Map<*, *>)?.get(operation.key) == operation.value

        // List
        is Operation.ListOperation.Contains -> (value as? List<*>)?.contains(operation.value) ?: false
        is Operation.ListOperation.In -> operation.list.contains(value)
        is Operation.ListOperation.IsEmpty -> (value as? List<*>)?.isEmpty() ?: false
        is Operation.ListOperation.IsNotEmpty -> (value as? List<*>)?.isNotEmpty() ?: false
        is Operation.ListOperation.IsSizeSmallerThan -> (value as? List<*>)?.size?.let { it < operation.size } ?: false
        is Operation.ListOperation.IsSizeSmallerThanOrEquals -> (value as? List<*>)?.size?.let { it <= operation.size } ?: false
        is Operation.ListOperation.IsSizeEqualsTo -> (value as? List<*>)?.size?.let { it == operation.size } ?: false
        is Operation.ListOperation.IsSizeBiggerThan -> (value as? List<*>)?.size?.let { it > operation.size } ?: false
        is Operation.ListOperation.IsSizeBiggerThanOrEquals -> (value as? List<*>)?.size?.let { it >= operation.size } ?: false
        is Operation.ListOperation.ContainsAll -> (value as? List<*>)?.let { list -> operation.items.all { list.contains(it) } } ?: false
        is Operation.ListOperation.ContainsAny -> (value as? List<*>)?.let { list -> operation.items.any { list.contains(it) } } ?: false

        // LocalDateTime
        is Operation.LocalDateTimeOperation.IsEqualTo -> (value as? LocalDateTime) == operation.dateTime
        is Operation.LocalDateTimeOperation.IsBefore -> (value as? LocalDateTime)?.let { it < operation.dateTime } ?: false
        is Operation.LocalDateTimeOperation.IsAfter -> (value as? LocalDateTime)?.let { it > operation.dateTime } ?: false
        is Operation.LocalDateTimeOperation.IsWeekend -> (value as? LocalDateTime)?.let {
            it.dayOfWeek == DayOfWeek.SATURDAY || it.dayOfWeek == DayOfWeek.SUNDAY
        } ?: false
        is Operation.LocalDateTimeOperation.IsWeekday -> (value as? LocalDateTime)?.let {
            it.dayOfWeek != DayOfWeek.SATURDAY && it.dayOfWeek != DayOfWeek.SUNDAY
        } ?: false
    }
}
