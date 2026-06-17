package dev.catbit.mosaic.server.builder.composition_local

import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import kotlin.concurrent.thread
import kotlin.test.*

// ── Test infrastructure ───────────────────────────────────────────────────────

private val LocalString = compositionLocalOf { "default" }
private val LocalInt = compositionLocalOf { 42 }

/** Captures LocalString.current() at build() time. */
private class StringCaptureBuilder(
    private val sink: MutableList<String>
) : GenericBuilder<Unit>() {
    override fun build() { sink += LocalString.current() }
}

/** Scope that adds StringCaptureBuilders. */
private class CaptureScope : GenericBuilderScope<Unit, StringCaptureBuilder>() {
    fun capture(sink: MutableList<String>) = addBuilder(StringCaptureBuilder(sink))
}

/** Captures LocalInt.current() at build() time. */
private class IntCaptureBuilder(
    private val sink: MutableList<Int>
) : GenericBuilder<Unit>() {
    override fun build() { sink += LocalInt.current() }
}

/**
 * Builder that, at build() time, creates a CaptureScope and applies a
 * captured DSL lambda — simulating how real tile builders propagate locals.
 */
private class NestingBuilder(
    private val content: CaptureScope.() -> Unit
) : GenericBuilder<Unit>() {
    override fun build() {
        CaptureScope().apply(content).build()
    }
}

private class NestingScope : GenericBuilderScope<Unit, NestingBuilder>() {
    fun nest(content: CaptureScope.() -> Unit) = addBuilder(NestingBuilder(content))
}

/** Accepts both StringCaptureBuilder and IntCaptureBuilder. */
private class MultiScope : GenericBuilderScope<Unit, GenericBuilder<Unit>>() {
    fun captureString(sink: MutableList<String>) = addBuilder(StringCaptureBuilder(sink))
    fun captureInt(sink: MutableList<Int>) = addBuilder(IntCaptureBuilder(sink))
}

// ── Tests ─────────────────────────────────────────────────────────────────────

class CompositionLocalTest {

    // ── API surface ───────────────────────────────────────────────────────────

    @Test
    fun `current returns default value when BuildContext is empty`() {
        assertEquals("default", LocalString.current())
        assertEquals(42, LocalInt.current())
    }

    @Test
    fun `current returns null for nullable local with null default`() {
        val LocalNullable = compositionLocalOf<String?> { null }
        assertNull(LocalNullable.current())
    }

    @Test
    fun `provides infix creates correct pair`() {
        val pair = LocalString provides "hello"
        assertEquals(LocalString, pair.first)
        assertEquals("hello", pair.second.provide())
    }

    @Test
    fun `two compositionLocalOf calls with same type are distinct keys`() {
        val A = compositionLocalOf { "A" }
        val B = compositionLocalOf { "B" }
        assertNotEquals(A, B)
        assertEquals("A", A.current())
        assertEquals("B", B.current())
    }

    // ── BuildContext ──────────────────────────────────────────────────────────

    @Test
    fun `BuildContext starts empty`() {
        assertTrue(BuildContext.get().isEmpty())
    }

    @Test
    fun `BuildContext with sets value during block and restores after`() {
        val captured = mutableListOf<String>()

        BuildContext.with(mapOf(LocalString to ValueProvider("inside"))) {
            captured += LocalString.current()
        }
        captured += LocalString.current()

        assertEquals(listOf("inside", "default"), captured)
    }

    @Test
    fun `BuildContext with restores previous value when nested`() {
        val captured = mutableListOf<String>()

        BuildContext.with(mapOf(LocalString to ValueProvider("outer"))) {
            captured += LocalString.current()
            BuildContext.with(mapOf(LocalString to ValueProvider("inner"))) {
                captured += LocalString.current()
            }
            captured += LocalString.current()
        }
        captured += LocalString.current()

        assertEquals(listOf("outer", "inner", "outer", "default"), captured)
    }

    @Test
    fun `BuildContext with restores value even when exception is thrown`() {
        runCatching {
            BuildContext.with(mapOf(LocalString to ValueProvider("will-throw"))) {
                error("boom")
            }
        }
        assertEquals("default", LocalString.current())
        assertTrue(BuildContext.get().isEmpty())
    }

    // ── CompositionLocalProvider ──────────────────────────────────────────────

    @Test
    fun `CompositionLocalProvider makes value visible at build time`() {
        val sink = mutableListOf<String>()
        val scope = CaptureScope()

        scope.apply {
            CompositionLocalProvider(LocalString provides "provided") {
                capture(sink)
            }
        }
        scope.build()

        assertEquals(listOf("provided"), sink)
    }

    @Test
    fun `CompositionLocalProvider restores default after block`() {
        val sink = mutableListOf<String>()
        val scope = CaptureScope()

        scope.apply {
            CompositionLocalProvider(LocalString provides "provided") {
                capture(sink)
            }
            capture(sink)
        }
        scope.build()

        assertEquals(listOf("provided", "default"), sink)
    }

    @Test
    fun `CompositionLocalProvider restores outer value after nested block`() {
        val sink = mutableListOf<String>()
        val scope = CaptureScope()

        scope.apply {
            CompositionLocalProvider(LocalString provides "outer") {
                capture(sink)
                CompositionLocalProvider(LocalString provides "inner") {
                    capture(sink)
                }
                capture(sink)
            }
            capture(sink)
        }
        scope.build()

        assertEquals(listOf("outer", "inner", "outer", "default"), sink)
    }

    @Test
    fun `CompositionLocalProvider can provide multiple locals at once`() {
        val strSink = mutableListOf<String>()
        val intSink = mutableListOf<Int>()
        val scope = MultiScope()

        scope.apply {
            CompositionLocalProvider(
                LocalString provides "hello",
                LocalInt provides 99,
            ) {
                captureString(strSink)
                captureInt(intSink)
            }
        }
        scope.build()

        assertEquals(listOf("hello"), strSink)
        assertEquals(listOf(99), intSink)
    }

    @Test
    fun `inner provider overrides only its own key — outer locals remain`() {
        val strSink = mutableListOf<String>()
        val intSink = mutableListOf<Int>()
        val scope = MultiScope()

        scope.apply {
            CompositionLocalProvider(LocalString provides "outer-str", LocalInt provides 10) {
                captureString(strSink) // "outer-str"
                captureInt(intSink)    // 10
                CompositionLocalProvider(LocalString provides "inner-str") {
                    captureString(strSink) // "inner-str"
                    captureInt(intSink)    // still 10
                }
                captureString(strSink) // "outer-str" restored
                captureInt(intSink)    // 10 restored
            }
        }
        scope.build()

        assertEquals(listOf("outer-str", "inner-str", "outer-str"), strSink)
        assertEquals(listOf(10, 10, 10), intSink)
    }

    @Test
    fun `CompositionLocalProvider with empty block does not affect subsequent builders`() {
        val sink = mutableListOf<String>()
        val scope = CaptureScope()

        scope.apply {
            CompositionLocalProvider(LocalString provides "unused") { /* no builders added */ }
            capture(sink)
        }
        scope.build()

        assertEquals(listOf("default"), sink)
    }

    @Test
    fun `providing a value then immediately re-providing on same key uses last value`() {
        val sink = mutableListOf<String>()
        val scope = CaptureScope()

        scope.apply {
            CompositionLocalProvider(LocalString provides "first") {
                CompositionLocalProvider(LocalString provides "second") {
                    capture(sink)
                }
            }
        }
        scope.build()

        assertEquals(listOf("second"), sink)
    }

    // ── Propagation through the builder pipeline ──────────────────────────────

    @Test
    fun `locals captured at DSL time propagate through builder lambdas at build time`() {
        val sink = mutableListOf<String>()
        val scope = NestingScope()

        scope.apply {
            CompositionLocalProvider(LocalString provides "propagated") {
                nest { capture(sink) }
            }
        }
        scope.build()

        assertEquals(listOf("propagated"), sink)
    }

    @Test
    fun `CompositionLocalProvider inside a nested lambda scopes correctly`() {
        val sink = mutableListOf<String>()
        val scope = NestingScope()

        scope.apply {
            CompositionLocalProvider(LocalString provides "outer") {
                nest {
                    capture(sink)
                    CompositionLocalProvider(LocalString provides "inner") {
                        capture(sink)
                    }
                    capture(sink)
                }
            }
            nest { capture(sink) }
        }
        scope.build()

        assertEquals(listOf("outer", "inner", "outer", "default"), sink)
    }

    @Test
    fun `siblings outside provider do not see the provided value`() {
        val sink = mutableListOf<String>()
        val scope = CaptureScope()

        scope.apply {
            capture(sink)
            CompositionLocalProvider(LocalString provides "provided") {
                capture(sink)
            }
            capture(sink)
        }
        scope.build()

        assertEquals(listOf("default", "provided", "default"), sink)
    }

    @Test
    fun `locals propagate through three nesting levels`() {
        val sink = mutableListOf<String>()

        class Level3Builder(
            private val content: CaptureScope.() -> Unit
        ) : GenericBuilder<Unit>() {
            override fun build() { CaptureScope().apply(content).build() }
        }

        class Level3Scope : GenericBuilderScope<Unit, Level3Builder>() {
            fun level3(content: CaptureScope.() -> Unit) = addBuilder(Level3Builder(content))
        }

        class Level2Builder(
            private val content: Level3Scope.() -> Unit
        ) : GenericBuilder<Unit>() {
            override fun build() { Level3Scope().apply(content).build() }
        }

        class Level2Scope : GenericBuilderScope<Unit, Level2Builder>() {
            fun level2(content: Level3Scope.() -> Unit) = addBuilder(Level2Builder(content))
        }

        val scope = Level2Scope()
        scope.apply {
            CompositionLocalProvider(LocalString provides "deep-value") {
                level2 {
                    level3 {
                        capture(sink)
                    }
                }
            }
        }
        scope.build()

        assertEquals(listOf("deep-value"), sink)
    }

    @Test
    fun `multiple independent scopes do not share locals`() {
        val sink1 = mutableListOf<String>()
        val sink2 = mutableListOf<String>()
        val scope1 = CaptureScope()
        val scope2 = CaptureScope()

        scope1.apply {
            CompositionLocalProvider(LocalString provides "scope1") {
                capture(sink1)
            }
        }
        scope2.apply {
            // No provider — should see default
            capture(sink2)
        }

        scope1.build()
        scope2.build()

        assertEquals(listOf("scope1"), sink1)
        assertEquals(listOf("default"), sink2)
    }

    @Test
    fun `nullable local can be overridden and restored`() {
        val LocalNullable = compositionLocalOf<String?> { null }
        val results = mutableListOf<String?>()

        class NullCaptureBuilder : GenericBuilder<Unit>() {
            override fun build() { results += LocalNullable.current() }
        }
        class NullCaptureScope : GenericBuilderScope<Unit, NullCaptureBuilder>() {
            fun capture() = addBuilder(NullCaptureBuilder())
        }

        val scope = NullCaptureScope()
        scope.apply {
            capture()
            CompositionLocalProvider(LocalNullable provides "non-null") {
                capture()
            }
            capture()
        }
        scope.build()

        assertEquals(listOf(null, "non-null", null), results)
    }

    @Test
    fun `deeply nested providers restore correctly in all levels`() {
        val sink = mutableListOf<String>()
        val scope = CaptureScope()

        scope.apply {
            capture(sink)
            CompositionLocalProvider(LocalString provides "L1") {
                capture(sink)
                CompositionLocalProvider(LocalString provides "L2") {
                    capture(sink)
                    CompositionLocalProvider(LocalString provides "L3") {
                        capture(sink)
                    }
                    capture(sink)
                }
                capture(sink)
            }
            capture(sink)
        }
        scope.build()

        assertEquals(
            listOf("default", "L1", "L2", "L3", "L2", "L1", "default"),
            sink
        )
    }

    // ── Thread safety ─────────────────────────────────────────────────────────

    @Test
    fun `concurrent builds on separate threads do not interfere with each other`() {
        val iterations = 500
        val sink1 = mutableListOf<String>()
        val sink2 = mutableListOf<String>()

        val scope1 = CaptureScope()
        val scope2 = CaptureScope()

        scope1.apply {
            CompositionLocalProvider(LocalString provides "thread-1") {
                repeat(iterations) { capture(sink1) }
            }
        }
        scope2.apply {
            CompositionLocalProvider(LocalString provides "thread-2") {
                repeat(iterations) { capture(sink2) }
            }
        }

        val t1 = thread { scope1.build() }
        val t2 = thread { scope2.build() }

        t1.join()
        t2.join()

        assertEquals(iterations, sink1.size)
        assertEquals(iterations, sink2.size)
        assertTrue(sink1.all { it == "thread-1" }, "Thread 1 captured wrong values")
        assertTrue(sink2.all { it == "thread-2" }, "Thread 2 captured wrong values")
    }

    @Test
    fun `build called from a different thread than DSL definition retains locals`() {
        val sink = mutableListOf<String>()
        val scope = CaptureScope()

        // DSL defined on the main test thread
        scope.apply {
            CompositionLocalProvider(LocalString provides "cross-thread") {
                repeat(10) { capture(sink) }
            }
        }

        // Build executed on a worker thread
        val worker = thread { scope.build() }
        worker.join()

        assertEquals(10, sink.size)
        assertTrue(sink.all { it == "cross-thread" }, "Wrong values after cross-thread build: $sink")
    }

    @Test
    fun `many concurrent threads each see only their own provided value`() {
        val threadCount = 20
        val iterationsPerThread = 200

        val LocalThreadId = compositionLocalOf { -1 }

        class IdCaptureBuilder(
            private val sink: MutableList<Int>
        ) : GenericBuilder<Unit>() {
            override fun build() { sink += LocalThreadId.current() }
        }

        class IdCaptureScope : GenericBuilderScope<Unit, IdCaptureBuilder>() {
            fun capture(sink: MutableList<Int>) = addBuilder(IdCaptureBuilder(sink))
        }

        val sinks = List(threadCount) { mutableListOf<Int>() }

        val threads = sinks.mapIndexed { index, sink ->
            thread {
                val scope = IdCaptureScope()
                scope.apply {
                    CompositionLocalProvider(LocalThreadId provides index) {
                        repeat(iterationsPerThread) { capture(sink) }
                    }
                }
                scope.build()
            }
        }

        threads.forEach { it.join() }

        sinks.forEachIndexed { index, sink ->
            assertEquals(iterationsPerThread, sink.size, "Thread $index wrong count")
            assertTrue(sink.all { it == index }, "Thread $index captured foreign value: $sink")
        }
    }

    @Test
    fun `concurrent DSL definition and build on separate threads are isolated`() {
        // Two threads each define AND build their own scope concurrently
        val iterations = 300
        val sink1 = mutableListOf<String>()
        val sink2 = mutableListOf<String>()

        val t1 = thread {
            val scope = CaptureScope()
            scope.apply {
                CompositionLocalProvider(LocalString provides "t1") {
                    repeat(iterations) { capture(sink1) }
                }
            }
            scope.build()
        }

        val t2 = thread {
            val scope = CaptureScope()
            scope.apply {
                CompositionLocalProvider(LocalString provides "t2") {
                    repeat(iterations) { capture(sink2) }
                }
            }
            scope.build()
        }

        t1.join()
        t2.join()

        assertTrue(sink1.all { it == "t1" }, "t1 saw contamination: $sink1")
        assertTrue(sink2.all { it == "t2" }, "t2 saw contamination: $sink2")
    }
}
