package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.transform_data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNull

@Suppress("UNCHECKED_CAST")
class TransformDataEventRunnerTest {

    // region String template — single placeholder

    @Test
    fun singlePlaceholderResolvesToString() {
        val result = TemplateProcessor.applyTemplate(
            template = "<|name|>",
            data = mapOf("name" to "Rodrigo")
        )
        assertEquals("Rodrigo", result)
    }

    @Test
    fun singlePlaceholderPreservesIntType() {
        val result = TemplateProcessor.applyTemplate(
            template = "<|age|>",
            data = mapOf("age" to 31)
        )
        assertIs<Int>(result)
        assertEquals(31, result)
    }

    @Test
    fun singlePlaceholderPreservesBooleanType() {
        val result = TemplateProcessor.applyTemplate(
            template = "<|active|>",
            data = mapOf("active" to true)
        )
        assertIs<Boolean>(result)
        assertEquals(true, result)
    }

    @Test
    fun singlePlaceholderPreservesListType() {
        val expected = listOf("a", "b", "c")
        val result = TemplateProcessor.applyTemplate(
            template = "<|items|>",
            data = mapOf("items" to expected)
        )
        assertEquals(expected, result)
    }

    @Test
    fun singlePlaceholderPreservesMapType() {
        val expected = mapOf("city" to "São Paulo")
        val result = TemplateProcessor.applyTemplate(
            template = "<|address|>",
            data = mapOf("address" to expected)
        )
        assertEquals(expected, result)
    }

    // endregion

    // region String template — mixed content

    @Test
    fun mixedContentStringWithOnePlaceholder() {
        val result = TemplateProcessor.applyTemplate(
            template = "Hello, <|name|>!",
            data = mapOf("name" to "Rodrigo")
        )
        assertEquals("Hello, Rodrigo!", result)
    }

    @Test
    fun mixedContentStringWithMultiplePlaceholders() {
        val result = TemplateProcessor.applyTemplate(
            template = "<|first|> <|last|>",
            data = mapOf("first" to "João", "last" to "Silva")
        )
        assertEquals("João Silva", result)
    }

    @Test
    fun mixedContentIntIsStringifiedInContext() {
        val result = TemplateProcessor.applyTemplate(
            template = "Age: <|age|> years",
            data = mapOf("age" to 25)
        )
        assertEquals("Age: 25 years", result)
    }

    @Test
    fun templateWithNoPlaceholdersIsReturnedAsIs() {
        val result = TemplateProcessor.applyTemplate(
            template = "static content",
            data = mapOf("name" to "Rodrigo")
        )
        assertEquals("static content", result)
    }

    // endregion

    // region Path — nested dot notation

    @Test
    fun nestedPathWithDotNotation() {
        val result = TemplateProcessor.applyTemplate(
            template = "<|user.name|>",
            data = mapOf("user" to mapOf("name" to "Rodrigo"))
        )
        assertEquals("Rodrigo", result)
    }

    @Test
    fun deeplyNestedDotPath() {
        val data = mapOf(
            "company" to mapOf(
                "department" to mapOf(
                    "lead" to mapOf("name" to "Ana")
                )
            )
        )
        val result = TemplateProcessor.applyTemplate(
            template = "<|company.department.lead.name|>",
            data = data
        )
        assertEquals("Ana", result)
    }

    // endregion

    // region Path — array index

    @Test
    fun arrayIndexFirstElement() {
        val result = TemplateProcessor.applyTemplate(
            template = "<|names[0]|>",
            data = mapOf("names" to listOf("Rodrigo", "João", "Maria"))
        )
        assertEquals("Rodrigo", result)
    }

    @Test
    fun arrayIndexLastElement() {
        val result = TemplateProcessor.applyTemplate(
            template = "<|names[2]|>",
            data = mapOf("names" to listOf("Rodrigo", "João", "Maria"))
        )
        assertEquals("Maria", result)
    }

    @Test
    fun arrayIndexThenDotPath() {
        val data = mapOf(
            "people" to listOf(
                mapOf("name" to "Rodrigo"),
                mapOf("name" to "Ana")
            )
        )
        val result = TemplateProcessor.applyTemplate(
            template = "<|people[1].name|>",
            data = data
        )
        assertEquals("Ana", result)
    }

    @Test
    fun deepComplexPathWithMultipleArraysAndKeys() {
        val data = mapOf(
            "data" to mapOf(
                "users" to listOf(
                    mapOf(
                        "addresses" to listOf(
                            mapOf("city" to "São Paulo"),
                            mapOf("city" to "Belo Horizonte")
                        )
                    )
                )
            )
        )
        val result = TemplateProcessor.applyTemplate(
            template = "<|data.users[0].addresses[1].city|>",
            data = data
        )
        assertEquals("Belo Horizonte", result)
    }

    @Test
    fun veryDeepPathWithAlternatingArraysAndKeys() {
        val data = mapOf(
            "a" to listOf(
                mapOf(
                    "b" to listOf(
                        mapOf("c" to "found it")
                    )
                )
            )
        )
        val result = TemplateProcessor.applyTemplate(
            template = "<|a[0].b[0].c|>",
            data = data
        )
        assertEquals("found it", result)
    }

    // endregion

    // region Map template

    @Test
    fun mapTemplateWithStringValues() {
        val result = TemplateProcessor.applyTemplate(
            template = mapOf("greeting" to "Hello, <|name|>!", "farewell" to "Bye, <|name|>!"),
            data = mapOf("name" to "Rodrigo")
        ) as Map<*, *>
        assertEquals("Hello, Rodrigo!", result["greeting"])
        assertEquals("Bye, Rodrigo!", result["farewell"])
    }

    @Test
    fun deeplyNestedMapTemplate() {
        val result = TemplateProcessor.applyTemplate(
            template = mapOf("outer" to mapOf("inner" to "<|value|>")),
            data = mapOf("value" to "deep")
        ) as Map<*, *>
        val inner = result["outer"] as Map<*, *>
        assertEquals("deep", inner["inner"])
    }

    @Test
    fun mapTemplateStaticKeysArePreserved() {
        val result = TemplateProcessor.applyTemplate(
            template = mapOf("staticKey" to "staticValue", "dynamic" to "<|x|>"),
            data = mapOf("x" to "resolved")
        ) as Map<*, *>
        assertEquals("staticValue", result["staticKey"])
        assertEquals("resolved", result["dynamic"])
    }

    // endregion

    // region List template

    @Test
    fun listTemplateWithStringElements() {
        val result = TemplateProcessor.applyTemplate(
            template = listOf("<|a|>", "<|b|>", "<|c|>"),
            data = mapOf("a" to "x", "b" to "y", "c" to "z")
        ) as List<*>
        assertEquals(listOf("x", "y", "z"), result)
    }

    @Test
    fun listTemplateWithMixedStaticAndDynamic() {
        val result = TemplateProcessor.applyTemplate(
            template = listOf("static", "<|name|>", 42),
            data = mapOf("name" to "Rodrigo")
        ) as List<*>
        assertEquals("static", result[0])
        assertEquals("Rodrigo", result[1])
        assertEquals(42, result[2])
    }

    @Test
    fun listOfMapsTemplate() {
        val data = mapOf(
            "users" to listOf("Rodrigo", "Ana")
        )
        val result = TemplateProcessor.applyTemplate(
            template = listOf(
                mapOf("name" to "<|users[0]|>"),
                mapOf("name" to "<|users[1]|>")
            ),
            data = data
        ) as List<*>
        assertEquals("Rodrigo", (result[0] as Map<*, *>)["name"])
        assertEquals("Ana", (result[1] as Map<*, *>)["name"])
    }

    // endregion

    // region Non-string primitive template

    @Test
    fun intTemplatePassesThroughUnchanged() {
        val result = TemplateProcessor.applyTemplate(template = 42, data = mapOf("x" to "y"))
        assertEquals(42, result)
    }

    @Test
    fun booleanTemplatePassesThroughUnchanged() {
        val result = TemplateProcessor.applyTemplate(template = false, data = mapOf("x" to "y"))
        assertEquals(false, result)
    }

    @Test
    fun nullTemplatePassesThroughAsNull() {
        val result = TemplateProcessor.applyTemplate(template = null, data = mapOf("x" to "y"))
        assertNull(result)
    }

    // endregion

    // region Failure cases

    @Test
    fun failsWhenKeyNotFoundInMap() {
        assertFailsWith<NoSuchElementException> {
            TemplateProcessor.applyTemplate(
                template = "<|missing|>",
                data = mapOf("name" to "Rodrigo")
            )
        }
    }

    @Test
    fun failsWhenIndexOutOfBounds() {
        assertFailsWith<IndexOutOfBoundsException> {
            TemplateProcessor.applyTemplate(
                template = "<|items[5]|>",
                data = mapOf("items" to listOf(1, 2))
            )
        }
    }

    @Test
    fun failsWhenExpectedMapButGotString() {
        assertFailsWith<IllegalArgumentException> {
            TemplateProcessor.applyTemplate(
                template = "<|name.first|>",
                data = mapOf("name" to "Rodrigo") // "Rodrigo" is a String, not a Map
            )
        }
    }

    @Test
    fun failsWhenExpectedListButGotMap() {
        assertFailsWith<IllegalArgumentException> {
            TemplateProcessor.applyTemplate(
                template = "<|data[0]|>",
                data = mapOf("data" to mapOf("key" to "value")) // Map, not List
            )
        }
    }

    @Test
    fun failsWhenExpectedMapButGotInt() {
        assertFailsWith<IllegalArgumentException> {
            TemplateProcessor.applyTemplate(
                template = "<|count.value|>",
                data = mapOf("count" to 10) // Int, not Map
            )
        }
    }

    @Test
    fun failsWhenIncomingDataIsNullAndPathExpectsMap() {
        assertFailsWith<IllegalArgumentException> {
            TemplateProcessor.applyTemplate(
                template = "<|name|>",
                data = null
            )
        }
    }

    @Test
    fun failsWhenNestedKeyMissingInDeepPath() {
        assertFailsWith<NoSuchElementException> {
            TemplateProcessor.applyTemplate(
                template = "<|user.address.city|>",
                data = mapOf("user" to mapOf("name" to "Rodrigo")) // address key is missing
            )
        }
    }

    // endregion
}
