package dev.catbit.mosaic.core.data.schemas.event.events.data

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
    @SerialName("events") override val events: List<EventSchema>?,
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
                    @SerialName("IsNull")
                    data object IsNull : NullOperation

                    @Serializable
                    @SerialName("IsNotNull")
                    data object IsNotNull : NullOperation
                }

                @Serializable
                @SerialName("String")
                sealed interface StringOperation : Operation {
                    @Serializable
                    @SerialName("IsLengthSmallerThan")
                    data class IsEqualsTo(
                        @SerialName("target") val target: String
                    ) : StringOperation

                    @Serializable
                    @SerialName("IsLengthSmallerThanOrEquals")
                    data class IsLengthSmallerThan(
                        @SerialName("length") val length: Int
                    ) : StringOperation

                    @Serializable
                    @SerialName("IsLengthEqualsTo")
                    data class IsLengthSmallerThanOrEquals(
                        @SerialName("length") val length: Int
                    ) : StringOperation

                    @Serializable
                    @SerialName("IsLengthBiggerThan")
                    data class IsLengthEqualsTo(
                        @SerialName("length") val length: Int
                    ) : StringOperation

                    @Serializable
                    @SerialName("IsLengthBiggerThanOrEquals")
                    data class IsLengthBiggerThan(
                        @SerialName("length") val length: Int
                    ) : StringOperation

                    @Serializable
                    @SerialName("IsLengthBiggerThanOrEquals")
                    data class IsLengthBiggerThanOrEquals(
                        @SerialName("length") val length: Int
                    ) : StringOperation

                    @Serializable
                    @SerialName("MatchesRegex")
                    data class MatchesRegex(
                        @SerialName("regex") val regex: String
                    ) : StringOperation

                    @Serializable
                    @SerialName("Contains")
                    data class Contains(
                        @SerialName("substring") val substring: String
                    ) : StringOperation

                    @Serializable
                    @SerialName("StartsWith")
                    data class StartsWith(
                        @SerialName("prefix") val prefix: String
                    ) : StringOperation

                    @Serializable
                    @SerialName("EndsWith")
                    data class EndsWith(
                        @SerialName("suffix") val suffix: String
                    ) : StringOperation

                    @Serializable
                    @SerialName("EqualsIgnoreCase")
                    data class EqualsIgnoreCase(
                        @SerialName("target") val target: String
                    ) : StringOperation

                    @Serializable
                    @SerialName("IsBlank")
                    data object IsBlank : StringOperation

                    @Serializable
                    @SerialName("IsNotBlank")
                    data object IsNotBlank : StringOperation
                }

                @Serializable
                @SerialName("Int")
                sealed interface IntOperation : Operation {
                    @Serializable
                    @SerialName("IsEven")
                    data object IsEven : IntOperation

                    @Serializable
                    @SerialName("IsOdd")
                    data object IsOdd : IntOperation

                    @Serializable
                    @SerialName("IsSmallerThan")
                    data class IsSmallerThan(
                        @SerialName("target") val target: Int
                    ) : IntOperation

                    @Serializable
                    @SerialName("IsSmallerThanOrEquals")
                    data class IsSmallerThanOrEquals(
                        @SerialName("target") val target: Int
                    ) : IntOperation

                    @Serializable
                    @SerialName("IsEqualsTo")
                    data class IsEqualsTo(
                        @SerialName("target") val target: Int
                    ) : IntOperation

                    @Serializable
                    @SerialName("IsBiggerThan")
                    data class IsBiggerThan(
                        @SerialName("target") val target: Int
                    ) : IntOperation

                    @Serializable
                    @SerialName("IsBiggerThanOrEquals")
                    data class IsBiggerThanOrEquals(
                        @SerialName("target") val target: Int
                    ) : IntOperation
                }

                @Serializable
                @SerialName("Long")
                sealed interface LongOperation : Operation {

                    @Serializable
                    @SerialName("IsSmallerThan")
                    data class IsSmallerThan(
                        @SerialName("target") val target: Long
                    ) : LongOperation

                    @Serializable
                    @SerialName("IsSmallerThanOrEquals")
                    data class IsSmallerThanOrEquals(
                        @SerialName("target") val target: Long
                    ) : LongOperation

                    @Serializable
                    @SerialName("IsEqualsTo")
                    data class IsEqualsTo(
                        @SerialName("target") val target: Long
                    ) : LongOperation

                    @Serializable
                    @SerialName("IsBiggerThan")
                    data class IsBiggerThan(
                        @SerialName("target") val target: Long
                    ) : LongOperation

                    @Serializable
                    @SerialName("IsBiggerThanOrEquals")
                    data class IsBiggerThanOrEquals(
                        @SerialName("target") val target: Long
                    ) : LongOperation
                }

                @Serializable
                @SerialName("Float")
                sealed interface FloatOperation : Operation {

                    @Serializable
                    @SerialName("IsSmallerThan")
                    data class IsSmallerThan(
                        @SerialName("target") val target: Float
                    ) : FloatOperation

                    @Serializable
                    @SerialName("IsSmallerThanOrEquals")
                    data class IsSmallerThanOrEquals(
                        @SerialName("target") val target: Float
                    ) : FloatOperation

                    @Serializable
                    @SerialName("IsEqualsTo")
                    data class IsEqualsTo(
                        @SerialName("target") val target: Float
                    ) : FloatOperation

                    @Serializable
                    @SerialName("IsBiggerThan")
                    data class IsBiggerThan(
                        @SerialName("target") val target: Float
                    ) : FloatOperation

                    @Serializable
                    @SerialName("IsBiggerThanOrEquals")
                    data class IsBiggerThanOrEquals(
                        @SerialName("target") val target: Float
                    ) : FloatOperation
                }

                @Serializable
                @SerialName("Double")
                sealed interface DoubleOperation : Operation {

                    @Serializable
                    @SerialName("IsSmallerThan")
                    data class IsSmallerThan(
                        @SerialName("target") val target: Double
                    ) : DoubleOperation

                    @Serializable
                    @SerialName("IsSmallerThanOrEquals")
                    data class IsSmallerThanOrEquals(
                        @SerialName("target") val target: Double
                    ) : DoubleOperation

                    @Serializable
                    @SerialName("IsEqualsTo")
                    data class IsEqualsTo(
                        @SerialName("target") val target: Double
                    ) : DoubleOperation

                    @Serializable
                    @SerialName("IsBiggerThan")
                    data class IsBiggerThan(
                        @SerialName("target") val target: Double
                    ) : DoubleOperation

                    @Serializable
                    @SerialName("IsBiggerThanOrEquals")
                    data class IsBiggerThanOrEquals(
                        @SerialName("target") val target: Double
                    ) : DoubleOperation
                }

                @Serializable
                @SerialName("Boolean")
                sealed interface BooleanOperation : Operation {
                    @Serializable
                    @SerialName("IsFalse")
                    data object IsFalse : BooleanOperation

                    @Serializable
                    @SerialName("IsTrue")
                    data object IsTrue : BooleanOperation
                }

                @Serializable
                @SerialName("Map")
                sealed interface MapOperation : Operation {
                    @Serializable
                    @SerialName("ContainsKey")
                    data class ContainsKey(
                        @SerialName("key") val key: String
                    ) : MapOperation

                    @Serializable
                    @SerialName("ContainsValue")
                    data class ContainsValue(
                        @SerialName("value") val value: AnySerializable
                    ) : MapOperation

                    @Serializable
                    @SerialName("IsEmpty")
                    data object IsEmpty : MapOperation

                    @Serializable
                    @SerialName("IsNotEmpty")
                    data object IsNotEmpty : MapOperation

                    @Serializable
                    @SerialName("IsSizeSmallerThan")
                    data class IsSizeSmallerThan(
                        @SerialName("size") val size: Int
                    ) : MapOperation

                    @Serializable
                    @SerialName("IsSizeSmallerThanOrEquals")
                    data class IsSizeSmallerThanOrEquals(
                        @SerialName("size") val size: Int
                    ) : MapOperation

                    @Serializable
                    @SerialName("IsSizeEqualsTo")
                    data class IsSizeEqualsTo(
                        @SerialName("size") val size: Int
                    ) : MapOperation

                    @Serializable
                    @SerialName("IsSizeBiggerThan")
                    data class IsSizeBiggerThan(
                        @SerialName("size") val size: Int
                    ) : MapOperation

                    @Serializable
                    @SerialName("IsSizeBiggerThanOrEquals")
                    data class IsSizeBiggerThanOrEquals(
                        @SerialName("size") val size: Int
                    ) : MapOperation

                    @Serializable
                    @SerialName("ValueAtKeyEquals")
                    data class ValueAtKeyEquals(
                        @SerialName("key") val key: String,
                        @SerialName("value") val value: AnySerializable
                    ) : MapOperation
                }

                @Serializable
                @SerialName("List")
                sealed interface ListOperation : Operation {
                    @Serializable
                    @SerialName("Contains")
                    data class Contains(
                        @SerialName("value") val value: AnySerializable
                    ) : ListOperation

                    @Serializable
                    @SerialName("In")
                    data class In(
                        @SerialName("list") val list: List<AnySerializable>
                    ) : ListOperation

                    @Serializable
                    @SerialName("IsEmpty")
                    data object IsEmpty : ListOperation

                    @Serializable
                    @SerialName("IsNotEmpty")
                    data object IsNotEmpty : ListOperation

                    @Serializable
                    @SerialName("IsSizeSmallerThan")
                    data class IsSizeSmallerThan(
                        @SerialName("size") val size: Int
                    ) : ListOperation

                    @Serializable
                    @SerialName("IsSizeSmallerThanOrEquals")
                    data class IsSizeSmallerThanOrEquals(
                        @SerialName("size") val size: Int
                    ) : ListOperation

                    @Serializable
                    @SerialName("IsSizeEqualsTo")
                    data class IsSizeEqualsTo(
                        @SerialName("size") val size: Int
                    ) : ListOperation

                    @Serializable
                    @SerialName("IsSizeBiggerThan")
                    data class IsSizeBiggerThan(
                        @SerialName("size") val size: Int
                    ) : ListOperation

                    @Serializable
                    @SerialName("IsSizeBiggerThanOrEquals")
                    data class IsSizeBiggerThanOrEquals(
                        @SerialName("size") val size: Int
                    ) : ListOperation

                    @Serializable
                    @SerialName("ContainsAll")
                    data class ContainsAll(
                        @SerialName("items") val items: List<AnySerializable>
                    ) : ListOperation

                    @Serializable
                    @SerialName("ContainsAny")
                    data class ContainsAny(
                        @SerialName("items") val items: List<AnySerializable>
                    ) : ListOperation
                }

                @Serializable
                @SerialName("LocalDateTime")
                sealed interface LocalDateTimeOperation : Operation {
                    @Serializable
                    @SerialName("IsEqualTo")
                    data class IsEqualTo(
                        @SerialName("dateTime") val dateTime: LocalDateTime
                    ) : LocalDateTimeOperation

                    @Serializable
                    @SerialName("IsBefore")
                    data class IsBefore(
                        @SerialName("dateTime") val dateTime: LocalDateTime
                    ) : LocalDateTimeOperation

                    @Serializable
                    @SerialName("IsAfter")
                    data class IsAfter(
                        @SerialName("dateTime") val dateTime: LocalDateTime
                    ) : LocalDateTimeOperation

                    @Serializable
                    @SerialName("IsWeekend")
                    data object IsWeekend : LocalDateTimeOperation

                    @Serializable
                    @SerialName("IsWeekday")
                    data object IsWeekday : LocalDateTimeOperation
                }
            }
        }
    }
}