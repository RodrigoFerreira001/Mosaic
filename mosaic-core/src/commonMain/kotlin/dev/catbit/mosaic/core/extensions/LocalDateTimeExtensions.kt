package dev.catbit.mosaic.core.extensions

import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/** The current date/time in UTC — used wherever the framework needs "now" without depending on the
 * device's local timezone (e.g. comparing against a screen/graph's cached-until `ttl`). */
fun currentDateTime() = Clock.System.now().toLocalDateTime(timeZone = TimeZone.UTC)