package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.transform_data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class TemplateProcessorTest {

    // ── <||> root passthrough ─────────────────────────────────────────────────

    @Test
    fun `root placeholder with String incomingData returns String`() {
        assertEquals("hello", TemplateProcessor.applyTemplate("<||>", "hello"))
    }

    @Test
    fun `root placeholder with Int incomingData returns Int preserving type`() {
        assertEquals(42, TemplateProcessor.applyTemplate("<||>", 42))
    }

    @Test
    fun `root placeholder with Boolean incomingData returns Boolean preserving type`() {
        assertEquals(true, TemplateProcessor.applyTemplate("<||>", true))
    }

    @Test
    fun `root placeholder with null incomingData returns null`() {
        assertNull(TemplateProcessor.applyTemplate("<||>", null))
    }

    @Test
    fun `root placeholder with List incomingData returns same List`() {
        val list = listOf(1, 2, 3)
        assertEquals(list, TemplateProcessor.applyTemplate("<||>", list))
    }

    @Test
    fun `root placeholder with Map incomingData returns same Map`() {
        val map = mapOf("a" to 1)
        assertEquals(map, TemplateProcessor.applyTemplate("<||>", map))
    }

    @Test
    fun `root placeholder embedded in string coerces incomingData to String`() {
        assertEquals("value=42", TemplateProcessor.applyTemplate("value=<||>", 42))
    }

    // ── Key path navigation ───────────────────────────────────────────────────

    @Test
    fun `single key resolves value from map`() {
        val data = mapOf("name" to "Rodrigo")
        assertEquals("Rodrigo", TemplateProcessor.applyTemplate("<|name|>", data))
    }

    @Test
    fun `nested dot path resolves deeply`() {
        val data = mapOf("user" to mapOf("address" to mapOf("city" to "SP")))
        assertEquals("SP", TemplateProcessor.applyTemplate("<|user.address.city|>", data))
    }

    @Test
    fun `key resolving to Int preserves native type when sole placeholder`() {
        val data = mapOf("age" to 30)
        assertEquals(30, TemplateProcessor.applyTemplate("<|age|>", data))
    }

    @Test
    fun `key resolving to Boolean preserves native type when sole placeholder`() {
        val data = mapOf("active" to false)
        assertEquals(false, TemplateProcessor.applyTemplate("<|active|>", data))
    }

    // ── Array index navigation ────────────────────────────────────────────────

    @Test
    fun `array index accesses correct element`() {
        val data = mapOf("items" to listOf("a", "b", "c"))
        assertEquals("b", TemplateProcessor.applyTemplate("<|items[1]|>", data))
    }

    @Test
    fun `nested array and key navigation`() {
        val data = mapOf("people" to listOf(mapOf("name" to "Alice"), mapOf("name" to "Bob")))
        assertEquals("Bob", TemplateProcessor.applyTemplate("<|people[1].name|>", data))
    }

    @Test
    fun `root list accessed directly via index`() {
        val data = listOf("x", "y", "z")
        assertEquals("z", TemplateProcessor.applyTemplate("<|[2]|>", data))
    }

    @Test
    fun `deep nested array path`() {
        val data = mapOf("a" to listOf(mapOf("b" to listOf(10, 20, 30))))
        assertEquals(20, TemplateProcessor.applyTemplate("<|a[0].b[1]|>", data))
    }

    // ── Mixed content strings ─────────────────────────────────────────────────

    @Test
    fun `mixed content string substitutes multiple placeholders`() {
        val data = mapOf("first" to "John", "last" to "Doe")
        assertEquals("John Doe", TemplateProcessor.applyTemplate("<|first|> <|last|>", data))
    }

    @Test
    fun `mixed content string coerces Int to String`() {
        val data = mapOf("score" to 99)
        assertEquals("Score: 99", TemplateProcessor.applyTemplate("Score: <|score|>", data))
    }

    // ── Recursive template structures ─────────────────────────────────────────

    @Test
    fun `map template applies placeholders to each value`() {
        val data = mapOf("a" to 1, "b" to "two")
        val template = mapOf("x" to "<|a|>", "y" to "<|b|>")
        assertEquals(mapOf("x" to 1, "y" to "two"), TemplateProcessor.applyTemplate(template, data))
    }

    @Test
    fun `list template applies placeholders to each element`() {
        val data = mapOf("n" to 7)
        val template = listOf("<|n|>", "literal", "<|n|>")
        assertEquals(listOf(7, "literal", 7), TemplateProcessor.applyTemplate(template, data))
    }

    @Test
    fun `nested map template resolves deeply`() {
        val data = mapOf("user" to mapOf("name" to "Ana", "age" to 25))
        val template = mapOf("info" to mapOf("label" to "<|user.name|>", "value" to "<|user.age|>"))
        assertEquals(mapOf("info" to mapOf("label" to "Ana", "value" to 25)), TemplateProcessor.applyTemplate(template, data))
    }

    @Test
    fun `root placeholder inside map template returns incomingData as-is`() {
        val data = listOf(1, 2, 3)
        val template = mapOf("items" to "<||>")
        assertEquals(mapOf("items" to listOf(1, 2, 3)), TemplateProcessor.applyTemplate(template, data))
    }

    // ── Non-placeholder passthrough ───────────────────────────────────────────

    @Test
    fun `number template is returned as-is`() {
        assertEquals(123, TemplateProcessor.applyTemplate(123, null))
    }

    @Test
    fun `boolean template is returned as-is`() {
        assertEquals(false, TemplateProcessor.applyTemplate(false, null))
    }

    @Test
    fun `null template is returned as-is`() {
        assertNull(TemplateProcessor.applyTemplate(null, null))
    }

    @Test
    fun `string without placeholders is returned as-is`() {
        assertEquals("static", TemplateProcessor.applyTemplate("static", null))
    }

    // ── Error scenarios ───────────────────────────────────────────────────────

    @Test
    fun `missing key throws NoSuchElementException`() {
        val data = mapOf("a" to 1)
        assertFailsWith<NoSuchElementException> {
            TemplateProcessor.applyTemplate("<|missing|>", data)
        }
    }

    @Test
    fun `key navigation on non-map throws IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            TemplateProcessor.applyTemplate("<|key|>", "not a map")
        }
    }

    @Test
    fun `index out of bounds throws IndexOutOfBoundsException`() {
        val data = mapOf("items" to listOf("a"))
        assertFailsWith<IndexOutOfBoundsException> {
            TemplateProcessor.applyTemplate("<|items[5]|>", data)
        }
    }

    @Test
    fun `index navigation on non-list throws IllegalArgumentException`() {
        val data = mapOf("items" to "not a list")
        assertFailsWith<IllegalArgumentException> {
            TemplateProcessor.applyTemplate("<|items[0]|>", data)
        }
    }

    @Test
    fun `invalid non-integer index throws IllegalArgumentException`() {
        val data = mapOf("items" to listOf("a"))
        assertFailsWith<IllegalArgumentException> {
            TemplateProcessor.applyTemplate("<|items[abc]|>", data)
        }
    }

    @Test
    fun `mixed content string with null-resolving placeholder throws IllegalArgumentException`() {
        val data = mapOf("value" to null)
        assertFailsWith<IllegalArgumentException> {
            TemplateProcessor.applyTemplate("prefix-<|value|>", data)
        }
    }
}
