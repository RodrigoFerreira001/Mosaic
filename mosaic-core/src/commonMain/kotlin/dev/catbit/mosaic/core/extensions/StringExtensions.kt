package dev.catbit.mosaic.core.extensions

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat

@OptIn(ExperimentalUuidApi::class)
fun randomUuid() = Uuid.random().toHexDashString()

fun String.toSafeLocalDateTime(
    format: DateTimeFormat<LocalDateTime> = LocalDateTime.Formats.ISO
) = runCatching {
    LocalDateTime.parse(this, format)
}.getOrNull()