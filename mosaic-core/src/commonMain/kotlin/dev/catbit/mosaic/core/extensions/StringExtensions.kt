package dev.catbit.mosaic.core.extensions

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat

@OptIn(ExperimentalUuidApi::class)
fun randomId() = Uuid.generateV7().toString()

fun String.toSafeLocalDateTime(
    format: DateTimeFormat<LocalDateTime> = LocalDateTime.Formats.ISO
) = runCatching {
    LocalDateTime.parse(this, format)
}.getOrNull()