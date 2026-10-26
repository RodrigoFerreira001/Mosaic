package dev.catbit.mosaic.client.extensions

private val insufficientDiskSpaceMarkers = listOf(
    "no space left",
    "enospc",
    "not enough space",
    "disk full",
    "quotaexceeded"
)

/**
 * Whether this error, or any error in its `cause` chain, reports that the device ran out of storage.
 * Platforms expose no common type for this, so it matches the markers each one puts in its message:
 * POSIX `ENOSPC` ("No space left on device") on Android/JVM/iOS, "There is not enough space on the
 * disk" on Windows, and OPFS's `QuotaExceededError` on the web.
 */
fun Throwable.isInsufficientDiskSpace(): Boolean =
    generateSequence(this) { it.cause }
        .take(MAX_CAUSE_DEPTH)
        .any { error ->
            val message = error.message.orEmpty().lowercase()
            insufficientDiskSpaceMarkers.any { it in message }
        }

private const val MAX_CAUSE_DEPTH = 10
