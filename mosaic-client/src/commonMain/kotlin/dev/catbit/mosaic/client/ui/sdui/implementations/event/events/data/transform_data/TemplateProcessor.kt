package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.transform_data

/**
 * Two placeholder delimiters are supported, sharing the same path syntax (dot-notation, array
 * access via an index in brackets, empty path = the whole `data` value):
 * - `<|path|>` — "raw" placeholder. When it is the *entire* template string, the resolved value
 *   keeps its native runtime type (e.g. `Int`, `Boolean`, `Map`) — nothing is stringified.
 * - `</path/>` — "string" placeholder. Always coerces the resolved value to `String` via
 *   `.toString()`, even when it is the entire template string. Use this whenever the destination
 *   expects a `String` (e.g. a tile's `text` field) but the source value isn't naturally one —
 *   `<//>` (empty path) converts the whole incoming value to its string form.
 *
 * Both delimiters behave identically when embedded in surrounding text ("mixed content") —
 * literal text around a placeholder already forces stringification regardless of which one is
 * used.
 */
internal object TemplateProcessor {

    // Group 1 is the delimiter char ('|' or '/'), backreferenced so both sides must match; group
    // 2 is the path. Matches `<|path|>` and `</path/>`, not mixed delimiters like `<|path/>`.
    private val PLACEHOLDER_REGEX = Regex("<([|/])([^|/>]*)\\1>")

    fun applyTemplate(template: Any?, data: Any?): Any? = when (template) {
        is String -> applyToString(template, data)
        is Map<*, *> -> template.entries.associate { (k, v) -> k to applyTemplate(v, data) }
        is List<*> -> template.map { applyTemplate(it, data) }
        else -> template
    }

    private fun applyToString(template: String, data: Any?): Any? {
        val matches = PLACEHOLDER_REGEX.findAll(template).toList()
        if (matches.isEmpty()) return template

        // If the entire string is a single placeholder: `<|path|>` preserves the resolved
        // value's native type, `</path/>` always coerces it to String.
        if (matches.size == 1 && matches[0].value == template) {
            val delimiter = matches[0].groupValues[1]
            val resolved = resolvePath(data, matches[0].groupValues[2])
            return if (delimiter == "/") resolved.toString() else resolved
        }

        // Mixed content: substitute each placeholder with its string representation
        return PLACEHOLDER_REGEX.replace(template) { matchResult ->
            resolvePath(data, matchResult.groupValues[2])?.toString()
                ?: throw IllegalArgumentException(
                    "Path '${matchResult.groupValues[2]}' resolved to null in mixed-content string"
                )
        }
    }

    private fun resolvePath(data: Any?, path: String): Any? {
        if (path.isEmpty()) return data
        var current: Any? = data
        for (segment in parsePath(path)) {
            current = navigateSegment(current, segment)
        }
        return current
    }

    // Parses "people[3].names[2].name" into a flat list of navigation steps.
    // Each dot-separated part may contain an array accessor (e.g. "names[2]"),
    // which is split into two steps: the key lookup followed by the index lookup.
    private fun parsePath(path: String): List<Step> {
        val steps = mutableListOf<Step>()
        for (part in path.split(".")) {
            val bracketIdx = part.indexOf('[')
            if (bracketIdx == -1) {
                steps += Step.Key(part)
            } else {
                val key = part.substring(0, bracketIdx)
                if (key.isNotEmpty()) steps += Step.Key(key)
                val indexStr = part.substring(bracketIdx + 1, part.indexOf(']', bracketIdx))
                val index = indexStr.toIntOrNull()
                    ?: throw IllegalArgumentException("Invalid array index '$indexStr' in path '$path'")
                steps += Step.Index(index)
            }
        }
        return steps
    }

    @Suppress("UNCHECKED_CAST")
    private fun navigateSegment(current: Any?, step: Step): Any? = when (step) {
        is Step.Key -> {
            val map = current as? Map<String, *>
                ?: throw IllegalArgumentException(
                    "Expected a Map for key '${step.key}' but got ${current?.let { it::class.simpleName } ?: "null"}"
                )
            if (!map.containsKey(step.key)) {
                throw NoSuchElementException("Key '${step.key}' not found in map")
            }
            map[step.key]
        }
        is Step.Index -> {
            val list = current as? List<*>
                ?: throw IllegalArgumentException(
                    "Expected a List for index [${step.index}] but got ${current?.let { it::class.simpleName } ?: "null"}"
                )
            if (step.index < 0 || step.index >= list.size) {
                throw IndexOutOfBoundsException(
                    "Index ${step.index} out of bounds for list of size ${list.size}"
                )
            }
            list[step.index]
        }
    }

    private sealed interface Step {
        data class Key(val key: String) : Step
        data class Index(val index: Int) : Step
    }
}
