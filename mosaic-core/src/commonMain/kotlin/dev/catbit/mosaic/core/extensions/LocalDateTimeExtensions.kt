package dev.catbit.mosaic.core.extensions

import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun currentDateTime() = Clock.System.now().toLocalDateTime(timeZone = TimeZone.UTC)