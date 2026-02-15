package dev.catbit.mosaic.server.builder.event.builders.data

import dev.catbit.mosaic.core.data.schemas.event.data.AccessModeSchema
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.EvaluateDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.EvaluateDataEventSchema.Expression.DataExpression.Data
import dev.catbit.mosaic.core.data.schemas.event.events.data.EvaluateDataEventSchema.Expression.DataExpression.Operation
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import kotlinx.datetime.LocalDateTime

class EvaluateDataEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val expression: EvaluateDataEventSchema.Expression
) : EventSchemaBuilder<EvaluateDataEventSchema> {

    override fun build() = EvaluateDataEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        expression = expression
    )
}

fun EventSchemaBuilderScope.EvaluateData(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    expression: EvaluateDataEventSchema.Expression
) {
    addBuilder(
        EvaluateDataEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            expression = expression
        )
    )
}

fun not(
    expression: EvaluateDataEventSchema.Expression
) = EvaluateDataEventSchema.Expression.NotExpression(expression)

private fun and(
    leftExpression: EvaluateDataEventSchema.Expression,
    rightExpression: EvaluateDataEventSchema.Expression
) = EvaluateDataEventSchema.Expression.AndExpression(leftExpression, rightExpression)

infix fun EvaluateDataEventSchema.Expression.and(
    otherExpression: EvaluateDataEventSchema.Expression
) = and(
    leftExpression = this,
    rightExpression = otherExpression
)

private fun or(
    leftExpression: EvaluateDataEventSchema.Expression,
    rightExpression: EvaluateDataEventSchema.Expression
) = EvaluateDataEventSchema.Expression.OrExpression(leftExpression, rightExpression)

infix fun EvaluateDataEventSchema.Expression.or(
    otherExpression: EvaluateDataEventSchema.Expression
) = or(
    leftExpression = this,
    rightExpression = otherExpression
)

private fun data(
    data: Data,
    operation: Operation
) = EvaluateDataEventSchema.Expression.DataExpression(data, operation)

fun incomingData() = Data.IncomingData

fun dataSourceData(
    dataSource: DataSourceSchema,
    accessMode: AccessModeSchema
) = Data.DataSourceData(Data.DataSourceData.Reading(dataSource, accessMode))

fun Data.isNull() = data(
    data = this,
    operation = Operation.NullOperation.IsNull
)

fun Data.isNotNull() = data(
    data = this,
    operation = Operation.NullOperation.IsNotNull
)

fun Data.isEqualsTo(target: String) = data(
    data = this,
    operation = Operation.StringOperation.IsEqualsTo(target)
)

fun Data.isLengthSmallerThan(length: Int) = data(
    data = this,
    operation = Operation.StringOperation.IsLengthSmallerThan(length)
)

fun Data.isLengthSmallerThanOrEquals(length: Int) = data(
    data = this,
    operation = Operation.StringOperation.IsLengthSmallerThanOrEquals(length)
)

fun Data.isLengthEqualsTo(length: Int) = data(
    data = this,
    operation = Operation.StringOperation.IsLengthEqualsTo(length)
)

fun Data.isLengthBiggerThan(length: Int) = data(
    data = this,
    operation = Operation.StringOperation.IsLengthBiggerThan(length)
)

fun Data.isLengthBiggerThanOrEquals(length: Int) = data(
    data = this,
    operation = Operation.StringOperation.IsLengthBiggerThanOrEquals(length)
)

fun Data.matchesRegex(regex: String) = data(
    data = this,
    operation = Operation.StringOperation.MatchesRegex(regex)
)

fun Data.containsSubstring(substring: String) = data(
    data = this,
    operation = Operation.StringOperation.Contains(substring)
)

fun Data.startsWith(prefix: String) = data(
    data = this,
    operation = Operation.StringOperation.StartsWith(prefix)
)

fun Data.endsWith(suffix: String) = data(
    data = this,
    operation = Operation.StringOperation.EndsWith(suffix)
)

fun Data.equalsIgnoreCase(target: String) = data(
    data = this,
    operation = Operation.StringOperation.EqualsIgnoreCase(target)
)

fun Data.isBlank() = data(
    data = this,
    operation = Operation.StringOperation.IsBlank
)

fun Data.isNotBlank() = data(
    data = this,
    operation = Operation.StringOperation.IsNotBlank
)

fun Data.isIntEven() = data(
    data = this,
    operation = Operation.IntOperation.IsEven
)

fun Data.isIntOdd() = data(
    data = this,
    operation = Operation.IntOperation.IsOdd
)

fun Data.isSmallerThan(target: Int) = data(
    data = this,
    operation = Operation.IntOperation.IsSmallerThan(target)
)

fun Data.isSmallerThanOrEquals(target: Int) = data(
    data = this,
    operation = Operation.IntOperation.IsSmallerThanOrEquals(target)
)

fun Data.isEqualsTo(target: Int) = data(
    data = this,
    operation = Operation.IntOperation.IsEqualsTo(target)
)

fun Data.isBiggerThan(target: Int) = data(
    data = this,
    operation = Operation.IntOperation.IsBiggerThan(target)
)

fun Data.isBiggerThanOrEquals(target: Int) = data(
    data = this,
    operation = Operation.IntOperation.IsBiggerThanOrEquals(target)
)

fun Data.isSmallerThan(target: Long) = data(
    data = this,
    operation = Operation.LongOperation.IsSmallerThan(target)
)

fun Data.isSmallerThanOrEquals(target: Long) = data(
    data = this,
    operation = Operation.LongOperation.IsSmallerThanOrEquals(target)
)

fun Data.isEqualsTo(target: Long) = data(
    data = this,
    operation = Operation.LongOperation.IsEqualsTo(target)
)

fun Data.isBiggerThan(target: Long) = data(
    data = this,
    operation = Operation.LongOperation.IsBiggerThan(target)
)

fun Data.isBiggerThanOrEquals(target: Long) = data(
    data = this,
    operation = Operation.LongOperation.IsBiggerThanOrEquals(target)
)

fun Data.isSmallerThan(target: Float) = data(
    data = this,
    operation = Operation.FloatOperation.IsSmallerThan(target)
)

fun Data.isSmallerThanOrEquals(target: Float) = data(
    data = this,
    operation = Operation.FloatOperation.IsSmallerThanOrEquals(target)
)

fun Data.isEqualsTo(target: Float) = data(
    data = this,
    operation = Operation.FloatOperation.IsEqualsTo(target)
)

fun Data.isBiggerThan(target: Float) = data(
    data = this,
    operation = Operation.FloatOperation.IsBiggerThan(target)
)

fun Data.isBiggerThanOrEquals(target: Float) = data(
    data = this,
    operation = Operation.FloatOperation.IsBiggerThanOrEquals(target)
)

fun Data.isSmallerThan(target: Double) = data(
    data = this,
    operation = Operation.DoubleOperation.IsSmallerThan(target)
)

fun Data.isSmallerThanOrEquals(target: Double) = data(
    data = this,
    operation = Operation.DoubleOperation.IsSmallerThanOrEquals(target)
)

fun Data.isEqualsTo(target: Double) = data(
    data = this,
    operation = Operation.DoubleOperation.IsEqualsTo(target)
)

fun Data.isBiggerThan(target: Double) = data(
    data = this,
    operation = Operation.DoubleOperation.IsBiggerThan(target)
)

fun Data.isBiggerThanOrEquals(target: Double) = data(
    data = this,
    operation = Operation.DoubleOperation.IsBiggerThanOrEquals(target)
)

fun Data.isFalse() = data(
    data = this,
    operation = Operation.BooleanOperation.IsFalse
)

fun Data.isTrue() = data(
    data = this,
    operation = Operation.BooleanOperation.IsTrue
)

fun Data.containsKey(key: String) = data(
    data = this,
    operation = Operation.MapOperation.ContainsKey(key)
)

fun Data.containsValue(value: AnySerializable) = data(
    data = this,
    operation = Operation.MapOperation.ContainsValue(value)
)

fun Data.isMapEmpty() = data(
    data = this,
    operation = Operation.MapOperation.IsEmpty
)

fun Data.isMapNotEmpty() = data(
    data = this,
    operation = Operation.MapOperation.IsNotEmpty
)

fun Data.isMapSizeSmallerThan(size: Int) = data(
    data = this,
    operation = Operation.MapOperation.IsSizeSmallerThan(size)
)

fun Data.isMapSizeSmallerThanOrEquals(size: Int) = data(
    data = this,
    operation = Operation.MapOperation.IsSizeSmallerThanOrEquals(size)
)

fun Data.isMapSizeEqualsTo(size: Int) = data(
    data = this,
    operation = Operation.MapOperation.IsSizeEqualsTo(size)
)

fun Data.isMapSizeBiggerThan(size: Int) = data(
    data = this,
    operation = Operation.MapOperation.IsSizeBiggerThan(size)
)

fun Data.isMapSizeBiggerThanOrEquals(size: Int) = data(
    data = this,
    operation = Operation.MapOperation.IsSizeBiggerThanOrEquals(size)
)

fun Data.valueAtKeyEquals(key: String, value: AnySerializable) = data(
    data = this,
    operation = Operation.MapOperation.ValueAtKeyEquals(key, value)
)

fun Data.listContains(value: AnySerializable) = data(
    data = this,
    operation = Operation.ListOperation.Contains(value)
)

fun Data.inList(list: List<AnySerializable>) = data(
    data = this,
    operation = Operation.ListOperation.In(list)
)

fun Data.isListEmpty() = data(
    data = this,
    operation = Operation.ListOperation.IsEmpty
)

fun Data.isListNotEmpty() = data(
    data = this,
    operation = Operation.ListOperation.IsNotEmpty
)

fun Data.isListSizeSmallerThan(size: Int) = data(
    data = this,
    operation = Operation.ListOperation.IsSizeSmallerThan(size)
)

fun Data.isListSizeSmallerThanOrEquals(size: Int) = data(
    data = this,
    operation = Operation.ListOperation.IsSizeSmallerThanOrEquals(size)
)

fun Data.isListSizeEqualsTo(size: Int) = data(
    data = this,
    operation = Operation.ListOperation.IsSizeEqualsTo(size)
)

fun Data.isListSizeBiggerThan(size: Int) = data(
    data = this,
    operation = Operation.ListOperation.IsSizeBiggerThan(size)
)

fun Data.isListSizeBiggerThanOrEquals(size: Int) = data(
    data = this,
    operation = Operation.ListOperation.IsSizeBiggerThanOrEquals(size)
)

fun Data.listContainsAll(items: List<AnySerializable>) = data(
    data = this,
    operation = Operation.ListOperation.ContainsAll(items)
)

fun Data.listContainsAny(items: List<AnySerializable>) = data(
    data = this,
    operation = Operation.ListOperation.ContainsAny(items)
)

fun Data.isEqualTo(dateTime: LocalDateTime) = data(
    data = this,
    operation = Operation.LocalDateTimeOperation.IsEqualTo(dateTime)
)

fun Data.isBefore(dateTime: LocalDateTime) = data(
    data = this,
    operation = Operation.LocalDateTimeOperation.IsBefore(dateTime)
)

fun Data.isAfter(dateTime: LocalDateTime) = data(
    data = this,
    operation = Operation.LocalDateTimeOperation.IsAfter(dateTime)
)

fun Data.isWeekend() = data(
    data = this,
    operation = Operation.LocalDateTimeOperation.IsWeekend
)

fun Data.isWeekday() = data(
    data = this,
    operation = Operation.LocalDateTimeOperation.IsWeekday
)


