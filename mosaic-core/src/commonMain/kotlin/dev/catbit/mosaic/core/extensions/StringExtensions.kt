package dev.catbit.mosaic.core.extensions

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat

/** Generates a random UUID string — the default every schema's `id` field falls back to in the
 * server DSL when a call site doesn't pass one explicitly, and the pattern `AGENTS.md` recommends for
 * any programmatically-generated event id in a reusable composition. */
@OptIn(ExperimentalUuidApi::class)
fun randomId() = Uuid.random().toString()

/**
 * Parses this string as a [LocalDateTime] using [format], returning `null` instead of throwing on a
 * malformed string.
 *
 * @param format the format to parse against. Defaults to ISO 8601.
 * @return the parsed [LocalDateTime], or `null` if this string doesn't match [format].
 */
fun String.toSafeLocalDateTime(
    format: DateTimeFormat<LocalDateTime> = LocalDateTime.Formats.ISO
) = runCatching {
    LocalDateTime.parse(this, format)
}.getOrNull()