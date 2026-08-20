package dev.catbit.mosaic.client.extensions

/**
 * Runs [ifPresent] with this map's value at [key], only when [key] is present **and** its value is
 * non-null — the common pattern for decoding an optional field out of a raw `Map<String, Any?>` (a
 * builder's `updateData`, `incomingData` treated as a map, etc.) while distinguishing "absent" from
 * "present but null".
 */
inline fun Map<String, Any?>.getIfPresent(
    key: String,
    ifPresent: (Any) -> Unit
) {
    if (containsKey(key) && this[key] != null) ifPresent(this[key] as Any)
}

/** Same as [getIfPresent], but also runs [ifPresent] (with `null`) when [key] is present with an
 * explicit `null` value — only skips it when [key] is absent entirely. */
inline fun Map<String, Any?>.getOrNullIfPresent(
    key: String,
    ifPresent: (Any?) -> Unit
) {
    if (containsKey(key)) ifPresent(this[key])
}

/**
 * Runs [ifPresent] with this map's value at [key] cast to [T], only when [key] is present **and**
 * its value is actually an instance of [T] — a value present under the wrong type is silently
 * skipped rather than throwing a `ClassCastException`.
 */
inline fun <reified T : Any?> Map<String, Any?>.valueIfPresent(
    key: String,
    ifPresent: (T) -> Unit
) {
    if (containsKey(key) && this[key] is T) ifPresent(this[key] as T)
}

/** Same as [valueIfPresent], but runs [ifPresent] even when the value isn't an instance of [T] —
 * passing `null` in that case (via `as?`) instead of skipping the call. Only skips entirely when
 * [key] is absent. */
inline fun <reified T : Any?> Map<String, Any?>.valueOrNullIfPresent(
    key: String,
    ifPresent: (T?) -> Unit
) {
    if (containsKey(key)) ifPresent(this[key] as? T)
}

/**
 * Copies the value at [key] from [data] into this mutable map under the same [key], applying
 * [ifPresent] to it first — but only when [key] is present in [data] **and** non-null. A no-op
 * otherwise, leaving this map's own entry (if any) untouched.
 *
 * @param key the key to copy.
 * @param data the source map to read from.
 * @param ifPresent transform applied to the value before storing it. Identity by default.
 */
fun MutableMap<String, Any?>.extractAndPutIfPresent(
    key: String,
    data: Map<String, Any?>,
    ifPresent: (Any) -> Any = { it }
) {
    if (data.containsKey(key) && data[key] != null) {
        put(key, ifPresent(data[key] as Any))
    }
}

/** Same as [extractAndPutIfPresent], but also copies an explicit `null` value from [data] (applying
 * [ifPresent] to it too) — only skips the copy when [key] is entirely absent from [data]. */
fun MutableMap<String, Any?>.extractAndPutIfPresentOrNull(
    key: String,
    data: Map<String, Any?>,
    ifPresent: (Any?) -> Any? = { it }
) {
    if (data.containsKey(key)) {
        put(key, ifPresent(data[key]))
    }
}