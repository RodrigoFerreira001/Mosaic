package dev.catbit.mosaic.client.extensions

import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

/**
 * Compose M3's `DatePickerState.selectedDateMillis` is always UTC midnight of the selected
 * date, so conversions here stick to [TimeZone.UTC] to avoid off-by-one-day shifts.
 */
/** Converts epoch millis (as produced by Compose M3's `DatePickerState`) to the ISO `yyyy-MM-dd`
 * string `DatePicker`'s schema stores/produces (`selectedDate`). */
fun epochMillisToIsoDate(millis: Long): String =
    Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.UTC).date.toString()

/**
 * Converts an ISO `yyyy-MM-dd` string (`DatePicker.selectedDate`) to epoch millis, for feeding
 * Compose M3's `DatePickerState` — the inverse of [epochMillisToIsoDate].
 *
 * @return the resolved epoch millis, or `null` if [date] isn't a valid ISO date string.
 */
fun isoDateToEpochMillis(date: String): Long? =
    runCatching { LocalDate.parse(date).atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds() }.getOrNull()

/** Formats an hour/minute pair as the zero-padded ISO `HH:mm` string `TimePicker`'s schema
 * stores/produces (`selectedTime`). */
fun hourMinuteToIsoTime(hour: Int, minute: Int): String =
    "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"

/**
 * Parses an ISO `HH:mm` string (`TimePicker.selectedTime`) back into an hour/minute pair — the
 * inverse of [hourMinuteToIsoTime].
 *
 * @return the parsed `hour to minute` pair, or `null` if [time] isn't a valid `HH:mm` string.
 */
fun isoTimeToHourMinute(time: String): Pair<Int, Int>? {
    val parts = time.split(":")
    if (parts.size != 2) return null
    val hour = parts[0].toIntOrNull() ?: return null
    val minute = parts[1].toIntOrNull() ?: return null
    return hour to minute
}
