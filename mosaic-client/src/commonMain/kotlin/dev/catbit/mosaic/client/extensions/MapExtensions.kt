package dev.catbit.mosaic.client.extensions

inline fun Map<String, Any?>.getIfPresent(
    key: String,
    ifPresent: (Any) -> Unit
) {
    if (containsKey(key) && this[key] != null) ifPresent(this[key] as Any)
}

inline fun Map<String, Any?>.getOrNullIfPresent(
    key: String,
    ifPresent: (Any?) -> Unit
) {
    if (containsKey(key)) ifPresent(this[key])
}

inline fun <reified T : Any?> Map<String, Any?>.valueIfPresent(
    key: String,
    ifPresent: (T) -> Unit
) {
    if (containsKey(key) && this[key] is T) ifPresent(this[key] as T)
}

inline fun <reified T : Any?> Map<String, Any?>.valueOrNullIfPresent(
    key: String,
    ifPresent: (T?) -> Unit
) {
    if (containsKey(key)) ifPresent(this[key] as? T)
}

fun MutableMap<String, Any?>.extractAndPutIfPresent(
    key: String,
    data: Map<String, Any?>,
    ifPresent: (Any) -> Any = { it }
) {
    if (data.containsKey(key) && data[key] != null) {
        put(key, ifPresent(data[key] as Any))
    }
}

fun MutableMap<String, Any?>.extractAndPutIfPresentOrNull(
    key: String,
    data: Map<String, Any?>,
    ifPresent: (Any?) -> Any? = { it }
) {
    if (data.containsKey(key)) {
        put(key, ifPresent(data[key]))
    }
}