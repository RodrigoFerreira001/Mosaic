package dev.catbit.mosaic.core.data.schemas.event.events.data

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.data.AccessModeSchema
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Evaluates a boolean [expression] tree against data and branches the event chain based on the
 * result. The expression can reference [incomingData] directly or read from any supported data
 * source, and supports logical composition via [Expression.AndExpression],
 * [Expression.OrExpression], and [Expression.NotExpression].
 *
 * **incomingData consumed:** Used as the subject of [Expression.DataExpression.Data.IncomingData]
 * nodes in the expression tree, and is forwarded unchanged as `data` on both [onSuccess] and
 * [onFailure] triggers. It is never mutated.
 *
 * **Triggers fired:**
 * - [onSuccess(incomingData)] – When the root expression evaluates to `true`. The original
 *   `incomingData` is passed through unmodified.
 * - [onFailure(incomingData or Throwable)] – When the root expression evaluates to `false`, in
 *   which case `incomingData` is passed through; OR when an exception is thrown during evaluation
 *   (e.g. a type-cast mismatch in an operation), in which case the `Throwable` is passed as data.
 *
 * **Failure scenarios:**
 * - Any uncaught exception thrown during expression evaluation (e.g. casting `incomingData` to an
 *   incompatible type, or an unsupported operation variant) fires [onFailure] with the exception.
 *   The error is also logged via `logError`.
 * - A `false` boolean result is treated as a "logical failure" and fires [onFailure] with the
 *   unmodified `incomingData`. This is not an error — it is the normal branching mechanism.
 *
 * **Notes:**
 * - Type-mismatch operations (e.g. applying [Expression.DataExpression.Operation.IntOperation] to
 *   a String value) return `false` via safe casting (`as?`), not an exception, so they do not
 *   reach [onFailure] via the catch block — they resolve as logical false.
 * - [Expression.OrExpression] uses short-circuit evaluation: the right side is only evaluated if
 *   the left returns `false`. Similarly, [Expression.AndExpression] short-circuits if the left
 *   is `false`.
 * - [Expression.DataExpression.Operation.MapOperation.ValueAtKeyValidate] is recursive: it
 *   applies a nested [Expression.DataExpression.Operation] to the value at the given key.
 * - All I/O (data source fetching) is dispatched on [Dispatchers.IO].
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("EvaluateData")
data class EvaluateDataEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("expression") val expression: Expression
) : EventSchema {

    @Serializable
    sealed interface Expression {

        @Serializable
        @SerialName("Not")
        data class NotExpression(
            @SerialName("expression") val expression: Expression
        ) : Expression

        @Serializable
        @SerialName("Or")
        data class OrExpression(
            @SerialName("leftExpression") val leftExpression: Expression,
            @SerialName("rightExpression") val rightExpression: Expression
        ) : Expression

        @Serializable
        @SerialName("And")
        data class AndExpression(
            @SerialName("leftExpression") val leftExpression: Expression,
            @SerialName("rightExpression") val rightExpression: Expression
        ) : Expression

        @Serializable
        @SerialName("Data")
        data class DataExpression(
            @SerialName("data") val data: Data,
            @SerialName("operation") val operation: Operation
        ) : Expression {

            @Serializable
            sealed interface Data {

                @Serializable
                @SerialName("IncomingData")
                data object IncomingData : Data

                @Serializable
                @SerialName("DataSourceData")
                data class DataSourceData(
                    @SerialName("reading") val reading: Reading
                ) : Data {

                    @Serializable
                    data class Reading(
                        @SerialName("dataSource") val dataSource: DataSourceSchema,
                        @SerialName("accessMode") val accessMode: AccessModeSchema
                    )
                }
            }

            @Serializable
            sealed interface Operation {

                @Serializable
                @SerialName("Null")
                sealed interface NullOperation : Operation {
                    @Serializable
                    @SerialName("Null_IsNull")
                    data object IsNull : NullOperation

                    @Serializable
                    @SerialName("Null_IsNotNull")
                    data object IsNotNull : NullOperation
                }

                @Serializable
                @SerialName("String")
                sealed interface StringOperation : Operation {
                    @Serializable
                    @SerialName("String_IsEqualsTo")
                    data class IsEqualsTo(
                        @SerialName("target") val target: String
                    ) : StringOperation

                    @Serializable
                    @SerialName("String_IsLengthSmallerThan")
                    data class IsLengthSmallerThan(
                        @SerialName("length") val length: Int
                    ) : StringOperation

                    @Serializable
                    @SerialName("String_IsLengthSmallerThanOrEquals")
                    data class IsLengthSmallerThanOrEquals(
                        @SerialName("length") val length: Int
                    ) : StringOperation

                    @Serializable
                    @SerialName("String_IsLengthEqualsTo")
                    data class IsLengthEqualsTo(
                        @SerialName("length") val length: Int
                    ) : StringOperation

                    @Serializable
                    @SerialName("String_IsLengthBiggerThan")
                    data class IsLengthBiggerThan(
                        @SerialName("length") val length: Int
                    ) : StringOperation

                    @Serializable
                    @SerialName("String_IsLengthBiggerThanOrEquals")
                    data class IsLengthBiggerThanOrEquals(
                        @SerialName("length") val length: Int
                    ) : StringOperation

                    @Serializable
                    @SerialName("String_MatchesRegex")
                    data class MatchesRegex(
                        @SerialName("regex") val regex: String
                    ) : StringOperation

                    @Serializable
                    @SerialName("String_Contains")
                    data class Contains(
                        @SerialName("substring") val substring: String
                    ) : StringOperation

                    @Serializable
                    @SerialName("String_StartsWith")
                    data class StartsWith(
                        @SerialName("prefix") val prefix: String
                    ) : StringOperation

                    @Serializable
                    @SerialName("String_EndsWith")
                    data class EndsWith(
                        @SerialName("suffix") val suffix: String
                    ) : StringOperation

                    @Serializable
                    @SerialName("String_EqualsIgnoreCase")
                    data class EqualsIgnoreCase(
                        @SerialName("target") val target: String
                    ) : StringOperation

                    @Serializable
                    @SerialName("String_IsBlank")
                    data object IsBlank : StringOperation

                    @Serializable
                    @SerialName("String_IsNotBlank")
                    data object IsNotBlank : StringOperation
                }

                @Serializable
                @SerialName("Int")
                sealed interface IntOperation : Operation {
                    @Serializable
                    @SerialName("Int_IsEven")
                    data object IsEven : IntOperation

                    @Serializable
                    @SerialName("Int_IsOdd")
                    data object IsOdd : IntOperation

                    @Serializable
                    @SerialName("Int_IsSmallerThan")
                    data class IsSmallerThan(
                        @SerialName("target") val target: Int
                    ) : IntOperation

                    @Serializable
                    @SerialName("Int_IsSmallerThanOrEquals")
                    data class IsSmallerThanOrEquals(
                        @SerialName("target") val target: Int
                    ) : IntOperation

                    @Serializable
                    @SerialName("Int_IsEqualsTo")
                    data class IsEqualsTo(
                        @SerialName("target") val target: Int
                    ) : IntOperation

                    @Serializable
                    @SerialName("Int_IsBiggerThan")
                    data class IsBiggerThan(
                        @SerialName("target") val target: Int
                    ) : IntOperation

                    @Serializable
                    @SerialName("Int_IsBiggerThanOrEquals")
                    data class IsBiggerThanOrEquals(
                        @SerialName("target") val target: Int
                    ) : IntOperation
                }

                @Serializable
                @SerialName("Long")
                sealed interface LongOperation : Operation {

                    @Serializable
                    @SerialName("Long_IsSmallerThan")
                    data class IsSmallerThan(
                        @SerialName("target") val target: Long
                    ) : LongOperation

                    @Serializable
                    @SerialName("Long_IsSmallerThanOrEquals")
                    data class IsSmallerThanOrEquals(
                        @SerialName("target") val target: Long
                    ) : LongOperation

                    @Serializable
                    @SerialName("Long_IsEqualsTo")
                    data class IsEqualsTo(
                        @SerialName("target") val target: Long
                    ) : LongOperation

                    @Serializable
                    @SerialName("Long_IsBiggerThan")
                    data class IsBiggerThan(
                        @SerialName("target") val target: Long
                    ) : LongOperation

                    @Serializable
                    @SerialName("Long_IsBiggerThanOrEquals")
                    data class IsBiggerThanOrEquals(
                        @SerialName("target") val target: Long
                    ) : LongOperation
                }

                @Serializable
                @SerialName("Float")
                sealed interface FloatOperation : Operation {

                    @Serializable
                    @SerialName("Float_IsSmallerThan")
                    data class IsSmallerThan(
                        @SerialName("target") val target: Float
                    ) : FloatOperation

                    @Serializable
                    @SerialName("Float_IsSmallerThanOrEquals")
                    data class IsSmallerThanOrEquals(
                        @SerialName("target") val target: Float
                    ) : FloatOperation

                    @Serializable
                    @SerialName("Float_IsEqualsTo")
                    data class IsEqualsTo(
                        @SerialName("target") val target: Float
                    ) : FloatOperation

                    @Serializable
                    @SerialName("Float_IsBiggerThan")
                    data class IsBiggerThan(
                        @SerialName("target") val target: Float
                    ) : FloatOperation

                    @Serializable
                    @SerialName("Float_IsBiggerThanOrEquals")
                    data class IsBiggerThanOrEquals(
                        @SerialName("target") val target: Float
                    ) : FloatOperation
                }

                @Serializable
                @SerialName("Double")
                sealed interface DoubleOperation : Operation {

                    @Serializable
                    @SerialName("Double_IsSmallerThan")
                    data class IsSmallerThan(
                        @SerialName("target") val target: Double
                    ) : DoubleOperation

                    @Serializable
                    @SerialName("Double_IsSmallerThanOrEquals")
                    data class IsSmallerThanOrEquals(
                        @SerialName("target") val target: Double
                    ) : DoubleOperation

                    @Serializable
                    @SerialName("Double_IsEqualsTo")
                    data class IsEqualsTo(
                        @SerialName("target") val target: Double
                    ) : DoubleOperation

                    @Serializable
                    @SerialName("Double_IsBiggerThan")
                    data class IsBiggerThan(
                        @SerialName("target") val target: Double
                    ) : DoubleOperation

                    @Serializable
                    @SerialName("Double_IsBiggerThanOrEquals")
                    data class IsBiggerThanOrEquals(
                        @SerialName("target") val target: Double
                    ) : DoubleOperation
                }

                @Serializable
                @SerialName("Boolean")
                sealed interface BooleanOperation : Operation {
                    @Serializable
                    @SerialName("Boolean_IsFalse")
                    data object IsFalse : BooleanOperation

                    @Serializable
                    @SerialName("Boolean_IsTrue")
                    data object IsTrue : BooleanOperation
                }

                @Serializable
                @SerialName("Map")
                sealed interface MapOperation : Operation {
                    @Serializable
                    @SerialName("Map_ContainsKey")
                    data class ContainsKey(
                        @SerialName("key") val key: String
                    ) : MapOperation

                    @Serializable
                    @SerialName("Map_ContainsValue")
                    data class ContainsValue(
                        @SerialName("value") val value: AnySerializable
                    ) : MapOperation

                    @Serializable
                    @SerialName("Map_IsEmpty")
                    data object IsEmpty : MapOperation

                    @Serializable
                    @SerialName("Map_IsNotEmpty")
                    data object IsNotEmpty : MapOperation

                    @Serializable
                    @SerialName("Map_IsSizeSmallerThan")
                    data class IsSizeSmallerThan(
                        @SerialName("size") val size: Int
                    ) : MapOperation

                    @Serializable
                    @SerialName("Map_IsSizeSmallerThanOrEquals")
                    data class IsSizeSmallerThanOrEquals(
                        @SerialName("size") val size: Int
                    ) : MapOperation

                    @Serializable
                    @SerialName("Map_IsSizeEqualsTo")
                    data class IsSizeEqualsTo(
                        @SerialName("size") val size: Int
                    ) : MapOperation

                    @Serializable
                    @SerialName("Map_IsSizeBiggerThan")
                    data class IsSizeBiggerThan(
                        @SerialName("size") val size: Int
                    ) : MapOperation

                    @Serializable
                    @SerialName("Map_IsSizeBiggerThanOrEquals")
                    data class IsSizeBiggerThanOrEquals(
                        @SerialName("size") val size: Int
                    ) : MapOperation

                    @Serializable
                    @SerialName("Map_ValueAtKeyEquals")
                    data class ValueAtKeyEquals(
                        @SerialName("key") val key: String,
                        @SerialName("value") val value: AnySerializable
                    ) : MapOperation

                    @Serializable
                    @SerialName("Map_ValueAtKeyValidate")
                    data class ValueAtKeyValidate(
                        @SerialName("key") val key: String,
                        @SerialName("validation") val validation: Operation
                    ) : MapOperation
                }

                @Serializable
                @SerialName("List")
                sealed interface ListOperation : Operation {
                    @Serializable
                    @SerialName("List_Contains")
                    data class Contains(
                        @SerialName("value") val value: AnySerializable
                    ) : ListOperation

                    @Serializable
                    @SerialName("List_In")
                    data class In(
                        @SerialName("list") val list: SerializableImmutableList<AnySerializable>
                    ) : ListOperation

                    @Serializable
                    @SerialName("List_IsEmpty")
                    data object IsEmpty : ListOperation

                    @Serializable
                    @SerialName("List_IsNotEmpty")
                    data object IsNotEmpty : ListOperation

                    @Serializable
                    @SerialName("List_IsSizeSmallerThan")
                    data class IsSizeSmallerThan(
                        @SerialName("size") val size: Int
                    ) : ListOperation

                    @Serializable
                    @SerialName("List_IsSizeSmallerThanOrEquals")
                    data class IsSizeSmallerThanOrEquals(
                        @SerialName("size") val size: Int
                    ) : ListOperation

                    @Serializable
                    @SerialName("List_IsSizeEqualsTo")
                    data class IsSizeEqualsTo(
                        @SerialName("size") val size: Int
                    ) : ListOperation

                    @Serializable
                    @SerialName("List_IsSizeBiggerThan")
                    data class IsSizeBiggerThan(
                        @SerialName("size") val size: Int
                    ) : ListOperation

                    @Serializable
                    @SerialName("List_IsSizeBiggerThanOrEquals")
                    data class IsSizeBiggerThanOrEquals(
                        @SerialName("size") val size: Int
                    ) : ListOperation

                    @Serializable
                    @SerialName("List_ContainsAll")
                    data class ContainsAll(
                        @SerialName("items") val items: SerializableImmutableList<AnySerializable>
                    ) : ListOperation

                    @Serializable
                    @SerialName("List_ContainsAny")
                    data class ContainsAny(
                        @SerialName("items") val items: SerializableImmutableList<AnySerializable>
                    ) : ListOperation
                }

                @Serializable
                @SerialName("LocalDateTime")
                sealed interface LocalDateTimeOperation : Operation {
                    @Serializable
                    @SerialName("LocalDateTime_IsEqualTo")
                    data class IsEqualTo(
                        @SerialName("dateTime") val dateTime: LocalDateTime
                    ) : LocalDateTimeOperation

                    @Serializable
                    @SerialName("LocalDateTime_IsBefore")
                    data class IsBefore(
                        @SerialName("dateTime") val dateTime: LocalDateTime
                    ) : LocalDateTimeOperation

                    @Serializable
                    @SerialName("LocalDateTime_IsAfter")
                    data class IsAfter(
                        @SerialName("dateTime") val dateTime: LocalDateTime
                    ) : LocalDateTimeOperation

                    @Serializable
                    @SerialName("LocalDateTime_IsWeekend")
                    data object IsWeekend : LocalDateTimeOperation

                    @Serializable
                    @SerialName("LocalDateTime_IsWeekday")
                    data object IsWeekday : LocalDateTimeOperation
                }
            }
        }
    }
}