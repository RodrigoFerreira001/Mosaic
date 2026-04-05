package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.transform_data

internal object TemplateProcessor {

    private val PLACEHOLDER_REGEX = Regex("<\\|([^|>]+)\\|>")

    fun applyTemplate(template: Any?, data: Any?): Any? = when (template) {
        is String -> applyToString(template, data)
        is Map<*, *> -> template.entries.associate { (k, v) -> k to applyTemplate(v, data) }
        is List<*> -> template.map { applyTemplate(it, data) }
        else -> template
    }

    private fun applyToString(template: String, data: Any?): Any? {
        val matches = PLACEHOLDER_REGEX.findAll(template).toList()
        if (matches.isEmpty()) return template

        // If the entire string is a single placeholder, preserve the native type of the resolved value
        if (matches.size == 1 && matches[0].value == template) {
            return resolvePath(data, matches[0].groupValues[1])
        }

        // Mixed content: substitute each placeholder with its string representation
        return PLACEHOLDER_REGEX.replace(template) { matchResult ->
            resolvePath(data, matchResult.groupValues[1])?.toString()
                ?: throw IllegalArgumentException(
                    "Path '${matchResult.groupValues[1]}' resolved to null in mixed-content string"
                )
        }
    }

    private fun resolvePath(data: Any?, path: String): Any? {
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
