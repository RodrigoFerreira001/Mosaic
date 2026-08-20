package dev.catbit.mosaic.server.builder.event.builders.data

import dev.catbit.mosaic.core.data.schemas.event.data.AccessModeSchema
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.EvaluateDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.EvaluateDataEventSchema.Expression.DataExpression.Data
import dev.catbit.mosaic.core.data.schemas.event.events.data.EvaluateDataEventSchema.Expression.DataExpression.Operation
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDateTime

class EvaluateDataEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val expression: EvaluateDataEventSchema.Expression
) : EventSchemaBuilder<EvaluateDataEventSchema>() {

    override fun build() = EvaluateDataEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        expression = expression
    )
}

/**
 * Evaluates the boolean [expression] and branches on the result, on the IO dispatcher. An
 * expression composes [not]/`and`/`or` over leaf comparisons built by calling an operation
 * function (e.g. `isEqualsTo`, `isNull`, `containsSubstring`) on a [incomingData]/[dataSourceData]
 * value; an operation applied to a value of the wrong runtime type evaluates to `false` rather
 * than throwing. Forwards `incomingData` unchanged to whichever trigger fires; any `IncomingData`
 * leaf reads from that same value. Dispatches `onSuccess` (forwarding `incomingData`) when
 * [expression] evaluates to `true`; `onFailure` (also forwarding `incomingData`) when it evaluates
 * to `false`, or with the thrown `Throwable` instead when evaluation itself throws.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param expression Boolean expression to evaluate, built from [incomingData]/[dataSourceData] operations combined with [not]/`and`/`or`.
 */
fun EventSchemaBuilderScope.EvaluateData(
    id: String = randomId(),
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

/** Negates [expression] — `true` becomes `false` and vice versa. */
fun not(
    expression: EvaluateDataEventSchema.Expression
) = EvaluateDataEventSchema.Expression.NotExpression(expression)

/** Combines this expression with [otherExpression] — both must be `true`. */
infix fun EvaluateDataEventSchema.Expression.and(
    otherExpression: EvaluateDataEventSchema.Expression
) = EvaluateDataEventSchema.Expression.AndExpression(this, otherExpression)

/** Combines this expression with [otherExpression] — at least one must be `true`. */
infix fun EvaluateDataEventSchema.Expression.or(
    otherExpression: EvaluateDataEventSchema.Expression
) = EvaluateDataEventSchema.Expression.OrExpression(this, otherExpression)

private fun data(
    data: Data,
    operation: Operation
) = EvaluateDataEventSchema.Expression.DataExpression(data, operation)

/** References the event's own `incomingData` as the value an operation is applied to. */
fun incomingData() = Data.IncomingData

/** References a value read from [dataSource] via [accessMode] as the value an operation is applied to. */
fun dataSourceData(
    dataSource: DataSourceSchema,
    accessMode: AccessModeSchema
) = Data.DataSourceData(Data.DataSourceData.Reading(dataSource, accessMode))

/** True when this value is `null`. */
fun Data.isNull() = data(
    data = this,
    operation = Operation.NullOperation.IsNull
)

/** True when this value is not `null`. */
fun Data.isNotNull() = data(
    data = this,
    operation = Operation.NullOperation.IsNotNull
)

/** True when this string value equals [target] exactly (case-sensitive). */
fun Data.isEqualsTo(target: String) = data(
    data = this,
    operation = Operation.StringOperation.IsEqualsTo(target)
)

/** True when this string value's length is smaller than [length]. */
fun Data.isLengthSmallerThan(length: Int) = data(
    data = this,
    operation = Operation.StringOperation.IsLengthSmallerThan(length)
)

/** True when this string value's length is smaller than or equal to [length]. */
fun Data.isLengthSmallerThanOrEquals(length: Int) = data(
    data = this,
    operation = Operation.StringOperation.IsLengthSmallerThanOrEquals(length)
)

/** True when this string value's length equals [length]. */
fun Data.isLengthEqualsTo(length: Int) = data(
    data = this,
    operation = Operation.StringOperation.IsLengthEqualsTo(length)
)

/** True when this string value's length is bigger than [length]. */
fun Data.isLengthBiggerThan(length: Int) = data(
    data = this,
    operation = Operation.StringOperation.IsLengthBiggerThan(length)
)

/** True when this string value's length is bigger than or equal to [length]. */
fun Data.isLengthBiggerThanOrEquals(length: Int) = data(
    data = this,
    operation = Operation.StringOperation.IsLengthBiggerThanOrEquals(length)
)

/** True when this string value matches the [regex] pattern. */
fun Data.matchesRegex(regex: String) = data(
    data = this,
    operation = Operation.StringOperation.MatchesRegex(regex)
)

/** True when this string value contains [substring]. */
fun Data.containsSubstring(substring: String) = data(
    data = this,
    operation = Operation.StringOperation.Contains(substring)
)

/** True when this string value starts with [prefix]. */
fun Data.startsWith(prefix: String) = data(
    data = this,
    operation = Operation.StringOperation.StartsWith(prefix)
)

/** True when this string value ends with [suffix]. */
fun Data.endsWith(suffix: String) = data(
    data = this,
    operation = Operation.StringOperation.EndsWith(suffix)
)

/** True when this string value equals [target], ignoring case. */
fun Data.equalsIgnoreCase(target: String) = data(
    data = this,
    operation = Operation.StringOperation.EqualsIgnoreCase(target)
)

/** True when this string value is empty or made only of whitespace. */
fun Data.isBlank() = data(
    data = this,
    operation = Operation.StringOperation.IsBlank
)

/** True when this string value is not empty and not made only of whitespace. */
fun Data.isNotBlank() = data(
    data = this,
    operation = Operation.StringOperation.IsNotBlank
)

/** True when this int value is even. */
fun Data.isIntEven() = data(
    data = this,
    operation = Operation.IntOperation.IsEven
)

/** True when this int value is odd. */
fun Data.isIntOdd() = data(
    data = this,
    operation = Operation.IntOperation.IsOdd
)

/** True when this int value is smaller than [target]. */
fun Data.isSmallerThan(target: Int) = data(
    data = this,
    operation = Operation.IntOperation.IsSmallerThan(target)
)

/** True when this int value is smaller than or equal to [target]. */
fun Data.isSmallerThanOrEquals(target: Int) = data(
    data = this,
    operation = Operation.IntOperation.IsSmallerThanOrEquals(target)
)

/** True when this int value equals [target]. */
fun Data.isEqualsTo(target: Int) = data(
    data = this,
    operation = Operation.IntOperation.IsEqualsTo(target)
)

/** True when this int value is bigger than [target]. */
fun Data.isBiggerThan(target: Int) = data(
    data = this,
    operation = Operation.IntOperation.IsBiggerThan(target)
)

/** True when this int value is bigger than or equal to [target]. */
fun Data.isBiggerThanOrEquals(target: Int) = data(
    data = this,
    operation = Operation.IntOperation.IsBiggerThanOrEquals(target)
)

/** True when this long value is smaller than [target]. */
fun Data.isSmallerThan(target: Long) = data(
    data = this,
    operation = Operation.LongOperation.IsSmallerThan(target)
)

/** True when this long value is smaller than or equal to [target]. */
fun Data.isSmallerThanOrEquals(target: Long) = data(
    data = this,
    operation = Operation.LongOperation.IsSmallerThanOrEquals(target)
)

/** True when this long value equals [target]. */
fun Data.isEqualsTo(target: Long) = data(
    data = this,
    operation = Operation.LongOperation.IsEqualsTo(target)
)

/** True when this long value is bigger than [target]. */
fun Data.isBiggerThan(target: Long) = data(
    data = this,
    operation = Operation.LongOperation.IsBiggerThan(target)
)

/** True when this long value is bigger than or equal to [target]. */
fun Data.isBiggerThanOrEquals(target: Long) = data(
    data = this,
    operation = Operation.LongOperation.IsBiggerThanOrEquals(target)
)

/** True when this float value is smaller than [target]. */
fun Data.isSmallerThan(target: Float) = data(
    data = this,
    operation = Operation.FloatOperation.IsSmallerThan(target)
)

/** True when this float value is smaller than or equal to [target]. */
fun Data.isSmallerThanOrEquals(target: Float) = data(
    data = this,
    operation = Operation.FloatOperation.IsSmallerThanOrEquals(target)
)

/** True when this float value equals [target]. */
fun Data.isEqualsTo(target: Float) = data(
    data = this,
    operation = Operation.FloatOperation.IsEqualsTo(target)
)

/** True when this float value is bigger than [target]. */
fun Data.isBiggerThan(target: Float) = data(
    data = this,
    operation = Operation.FloatOperation.IsBiggerThan(target)
)

/** True when this float value is bigger than or equal to [target]. */
fun Data.isBiggerThanOrEquals(target: Float) = data(
    data = this,
    operation = Operation.FloatOperation.IsBiggerThanOrEquals(target)
)

/** True when this double value is smaller than [target]. */
fun Data.isSmallerThan(target: Double) = data(
    data = this,
    operation = Operation.DoubleOperation.IsSmallerThan(target)
)

/** True when this double value is smaller than or equal to [target]. */
fun Data.isSmallerThanOrEquals(target: Double) = data(
    data = this,
    operation = Operation.DoubleOperation.IsSmallerThanOrEquals(target)
)

/** True when this double value equals [target]. */
fun Data.isEqualsTo(target: Double) = data(
    data = this,
    operation = Operation.DoubleOperation.IsEqualsTo(target)
)

/** True when this double value is bigger than [target]. */
fun Data.isBiggerThan(target: Double) = data(
    data = this,
    operation = Operation.DoubleOperation.IsBiggerThan(target)
)

/** True when this double value is bigger than or equal to [target]. */
fun Data.isBiggerThanOrEquals(target: Double) = data(
    data = this,
    operation = Operation.DoubleOperation.IsBiggerThanOrEquals(target)
)

/** True when this boolean value is `false`. */
fun Data.isFalse() = data(
    data = this,
    operation = Operation.BooleanOperation.IsFalse
)

/** True when this boolean value is `true`. */
fun Data.isTrue() = data(
    data = this,
    operation = Operation.BooleanOperation.IsTrue
)

/** True when this map value has an entry with key [key]. */
fun Data.containsKey(key: String) = data(
    data = this,
    operation = Operation.MapOperation.ContainsKey(key)
)

/** True when this map value has an entry equal to [value]. */
fun Data.containsValue(value: AnySerializable) = data(
    data = this,
    operation = Operation.MapOperation.ContainsValue(value)
)

/** True when this map value has no entries. */
fun Data.isMapEmpty() = data(
    data = this,
    operation = Operation.MapOperation.IsEmpty
)

/** True when this map value has at least one entry. */
fun Data.isMapNotEmpty() = data(
    data = this,
    operation = Operation.MapOperation.IsNotEmpty
)

/** True when this map value has fewer than [size] entries. */
fun Data.isMapSizeSmallerThan(size: Int) = data(
    data = this,
    operation = Operation.MapOperation.IsSizeSmallerThan(size)
)

/** True when this map value has [size] entries or fewer. */
fun Data.isMapSizeSmallerThanOrEquals(size: Int) = data(
    data = this,
    operation = Operation.MapOperation.IsSizeSmallerThanOrEquals(size)
)

/** True when this map value has exactly [size] entries. */
fun Data.isMapSizeEqualsTo(size: Int) = data(
    data = this,
    operation = Operation.MapOperation.IsSizeEqualsTo(size)
)

/** True when this map value has more than [size] entries. */
fun Data.isMapSizeBiggerThan(size: Int) = data(
    data = this,
    operation = Operation.MapOperation.IsSizeBiggerThan(size)
)

/** True when this map value has [size] entries or more. */
fun Data.isMapSizeBiggerThanOrEquals(size: Int) = data(
    data = this,
    operation = Operation.MapOperation.IsSizeBiggerThanOrEquals(size)
)

/** True when this map value's entry at [key] equals [value]. */
fun Data.valueAtKeyEquals(key: String, value: AnySerializable) = data(
    data = this,
    operation = Operation.MapOperation.ValueAtKeyEquals(key, value)
)

/** Focuses this map value's entry at [key], so an operation (`isEqualsTo`, `isSmallerThan`, etc) chained on it validates that entry's value instead of the map itself. */
fun Data.valueAtKey(key: String) = KeyedData(this, key)

/**
 * Intermediate value produced by [Data.valueAtKey]: an operation called on it (mirroring the
 * `Data` operations above) validates the map entry at that key instead of the map as a whole.
 */
class KeyedData(
    private val data: Data,
    private val key: String
) {
    private fun build(operation: Operation): EvaluateDataEventSchema.Expression =
        EvaluateDataEventSchema.Expression.DataExpression(
            data = data,
            operation = Operation.MapOperation.ValueAtKeyValidate(key, operation)
        )

    // Null
    /** True when the keyed entry is `null`. */
    fun isNull() = build(Operation.NullOperation.IsNull)
    /** True when the keyed entry is not `null`. */
    fun isNotNull() = build(Operation.NullOperation.IsNotNull)

    // String
    /** True when the keyed string entry equals [target] exactly (case-sensitive). */
    fun isEqualsTo(target: String) = build(Operation.StringOperation.IsEqualsTo(target))
    /** True when the keyed string entry's length is smaller than [length]. */
    fun isLengthSmallerThan(length: Int) = build(Operation.StringOperation.IsLengthSmallerThan(length))
    /** True when the keyed string entry's length is smaller than or equal to [length]. */
    fun isLengthSmallerThanOrEquals(length: Int) = build(Operation.StringOperation.IsLengthSmallerThanOrEquals(length))
    /** True when the keyed string entry's length equals [length]. */
    fun isLengthEqualsTo(length: Int) = build(Operation.StringOperation.IsLengthEqualsTo(length))
    /** True when the keyed string entry's length is bigger than [length]. */
    fun isLengthBiggerThan(length: Int) = build(Operation.StringOperation.IsLengthBiggerThan(length))
    /** True when the keyed string entry's length is bigger than or equal to [length]. */
    fun isLengthBiggerThanOrEquals(length: Int) = build(Operation.StringOperation.IsLengthBiggerThanOrEquals(length))
    /** True when the keyed string entry matches the [regex] pattern. */
    fun matchesRegex(regex: String) = build(Operation.StringOperation.MatchesRegex(regex))
    /** True when the keyed string entry contains [substring]. */
    fun containsSubstring(substring: String) = build(Operation.StringOperation.Contains(substring))
    /** True when the keyed string entry starts with [prefix]. */
    fun startsWith(prefix: String) = build(Operation.StringOperation.StartsWith(prefix))
    /** True when the keyed string entry ends with [suffix]. */
    fun endsWith(suffix: String) = build(Operation.StringOperation.EndsWith(suffix))
    /** True when the keyed string entry equals [target], ignoring case. */
    fun equalsIgnoreCase(target: String) = build(Operation.StringOperation.EqualsIgnoreCase(target))
    /** True when the keyed string entry is empty or made only of whitespace. */
    fun isBlank() = build(Operation.StringOperation.IsBlank)
    /** True when the keyed string entry is not empty and not made only of whitespace. */
    fun isNotBlank() = build(Operation.StringOperation.IsNotBlank)

    // Int
    /** True when the keyed int entry is even. */
    fun isEven() = build(Operation.IntOperation.IsEven)
    /** True when the keyed int entry is odd. */
    fun isOdd() = build(Operation.IntOperation.IsOdd)
    /** True when the keyed int entry is smaller than [target]. */
    fun isSmallerThan(target: Int) = build(Operation.IntOperation.IsSmallerThan(target))
    /** True when the keyed int entry is smaller than or equal to [target]. */
    fun isSmallerThanOrEquals(target: Int) = build(Operation.IntOperation.IsSmallerThanOrEquals(target))
    /** True when the keyed int entry equals [target]. */
    fun isEqualsTo(target: Int) = build(Operation.IntOperation.IsEqualsTo(target))
    /** True when the keyed int entry is bigger than [target]. */
    fun isBiggerThan(target: Int) = build(Operation.IntOperation.IsBiggerThan(target))
    /** True when the keyed int entry is bigger than or equal to [target]. */
    fun isBiggerThanOrEquals(target: Int) = build(Operation.IntOperation.IsBiggerThanOrEquals(target))

    // Long
    /** True when the keyed long entry is smaller than [target]. */
    fun isSmallerThan(target: Long) = build(Operation.LongOperation.IsSmallerThan(target))
    /** True when the keyed long entry is smaller than or equal to [target]. */
    fun isSmallerThanOrEquals(target: Long) = build(Operation.LongOperation.IsSmallerThanOrEquals(target))
    /** True when the keyed long entry equals [target]. */
    fun isEqualsTo(target: Long) = build(Operation.LongOperation.IsEqualsTo(target))
    /** True when the keyed long entry is bigger than [target]. */
    fun isBiggerThan(target: Long) = build(Operation.LongOperation.IsBiggerThan(target))
    /** True when the keyed long entry is bigger than or equal to [target]. */
    fun isBiggerThanOrEquals(target: Long) = build(Operation.LongOperation.IsBiggerThanOrEquals(target))

    // Float
    /** True when the keyed float entry is smaller than [target]. */
    fun isSmallerThan(target: Float) = build(Operation.FloatOperation.IsSmallerThan(target))
    /** True when the keyed float entry is smaller than or equal to [target]. */
    fun isSmallerThanOrEquals(target: Float) = build(Operation.FloatOperation.IsSmallerThanOrEquals(target))
    /** True when the keyed float entry equals [target]. */
    fun isEqualsTo(target: Float) = build(Operation.FloatOperation.IsEqualsTo(target))
    /** True when the keyed float entry is bigger than [target]. */
    fun isBiggerThan(target: Float) = build(Operation.FloatOperation.IsBiggerThan(target))
    /** True when the keyed float entry is bigger than or equal to [target]. */
    fun isBiggerThanOrEquals(target: Float) = build(Operation.FloatOperation.IsBiggerThanOrEquals(target))

    // Double
    /** True when the keyed double entry is smaller than [target]. */
    fun isSmallerThan(target: Double) = build(Operation.DoubleOperation.IsSmallerThan(target))
    /** True when the keyed double entry is smaller than or equal to [target]. */
    fun isSmallerThanOrEquals(target: Double) = build(Operation.DoubleOperation.IsSmallerThanOrEquals(target))
    /** True when the keyed double entry equals [target]. */
    fun isEqualsTo(target: Double) = build(Operation.DoubleOperation.IsEqualsTo(target))
    /** True when the keyed double entry is bigger than [target]. */
    fun isBiggerThan(target: Double) = build(Operation.DoubleOperation.IsBiggerThan(target))
    /** True when the keyed double entry is bigger than or equal to [target]. */
    fun isBiggerThanOrEquals(target: Double) = build(Operation.DoubleOperation.IsBiggerThanOrEquals(target))

    // Boolean
    /** True when the keyed boolean entry is `true`. */
    fun isTrue() = build(Operation.BooleanOperation.IsTrue)
    /** True when the keyed boolean entry is `false`. */
    fun isFalse() = build(Operation.BooleanOperation.IsFalse)

    // LocalDateTime
    /** True when the keyed date-time entry equals [dateTime]. */
    fun isEqualTo(dateTime: LocalDateTime) = build(Operation.LocalDateTimeOperation.IsEqualTo(dateTime))
    /** True when the keyed date-time entry is before [dateTime]. */
    fun isBefore(dateTime: LocalDateTime) = build(Operation.LocalDateTimeOperation.IsBefore(dateTime))
    /** True when the keyed date-time entry is after [dateTime]. */
    fun isAfter(dateTime: LocalDateTime) = build(Operation.LocalDateTimeOperation.IsAfter(dateTime))
    /** True when the keyed date-time entry falls on a Saturday or Sunday. */
    fun isWeekend() = build(Operation.LocalDateTimeOperation.IsWeekend)
    /** True when the keyed date-time entry falls on a Monday through Friday. */
    fun isWeekday() = build(Operation.LocalDateTimeOperation.IsWeekday)
}

/** True when this list value contains [value]. */
fun Data.listContains(value: AnySerializable) = data(
    data = this,
    operation = Operation.ListOperation.Contains(value)
)

/** True when this value is present in [list]. */
fun Data.inList(list: List<AnySerializable>) = data(
    data = this,
    operation = Operation.ListOperation.In(list.toImmutableList())
)

/** True when this list value has no elements. */
fun Data.isListEmpty() = data(
    data = this,
    operation = Operation.ListOperation.IsEmpty
)

/** True when this list value has at least one element. */
fun Data.isListNotEmpty() = data(
    data = this,
    operation = Operation.ListOperation.IsNotEmpty
)

/** True when this list value has fewer than [size] elements. */
fun Data.isListSizeSmallerThan(size: Int) = data(
    data = this,
    operation = Operation.ListOperation.IsSizeSmallerThan(size)
)

/** True when this list value has [size] elements or fewer. */
fun Data.isListSizeSmallerThanOrEquals(size: Int) = data(
    data = this,
    operation = Operation.ListOperation.IsSizeSmallerThanOrEquals(size)
)

/** True when this list value has exactly [size] elements. */
fun Data.isListSizeEqualsTo(size: Int) = data(
    data = this,
    operation = Operation.ListOperation.IsSizeEqualsTo(size)
)

/** True when this list value has more than [size] elements. */
fun Data.isListSizeBiggerThan(size: Int) = data(
    data = this,
    operation = Operation.ListOperation.IsSizeBiggerThan(size)
)

/** True when this list value has [size] elements or more. */
fun Data.isListSizeBiggerThanOrEquals(size: Int) = data(
    data = this,
    operation = Operation.ListOperation.IsSizeBiggerThanOrEquals(size)
)

/** True when this list value contains every element of [items]. */
fun Data.listContainsAll(items: List<AnySerializable>) = data(
    data = this,
    operation = Operation.ListOperation.ContainsAll(items.toImmutableList())
)

/** True when this list value contains at least one element of [items]. */
fun Data.listContainsAny(items: List<AnySerializable>) = data(
    data = this,
    operation = Operation.ListOperation.ContainsAny(items.toImmutableList())
)

/** True when this date-time value equals [dateTime]. */
fun Data.isEqualTo(dateTime: LocalDateTime) = data(
    data = this,
    operation = Operation.LocalDateTimeOperation.IsEqualTo(dateTime)
)

/** True when this date-time value is before [dateTime]. */
fun Data.isBefore(dateTime: LocalDateTime) = data(
    data = this,
    operation = Operation.LocalDateTimeOperation.IsBefore(dateTime)
)

/** True when this date-time value is after [dateTime]. */
fun Data.isAfter(dateTime: LocalDateTime) = data(
    data = this,
    operation = Operation.LocalDateTimeOperation.IsAfter(dateTime)
)

/** True when this date-time value falls on a Saturday or Sunday. */
fun Data.isWeekend() = data(
    data = this,
    operation = Operation.LocalDateTimeOperation.IsWeekend
)

/** True when this date-time value falls on a Monday through Friday. */
fun Data.isWeekday() = data(
    data = this,
    operation = Operation.LocalDateTimeOperation.IsWeekday
)


