package dev.catbit.mosaic.client.exceptions

/**
 * Thrown when a `GetData` reading resolves to `null` — a `Single` access mode reading a key that
 * doesn't exist, or a `Batch` reading with `allowMissingData = false` hitting a missing key. Carried
 * as `GetData`'s `onFailure` incoming data.
 */
class DataNotFoundException(
    override val message: String?
) : Throwable()
