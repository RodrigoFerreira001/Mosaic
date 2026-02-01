package dev.catbit.mosaic.core.extensions

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun currentDateTime() = Clock.System.now().toLocalDateTime(timeZone = TimeZone.UTC)