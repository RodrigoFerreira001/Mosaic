package dev.catbit.mosaic.server.builder

import dev.catbit.mosaic.core.extensions.immutableMapTo
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import dev.catbit.mosaic.server.builder.composition_local.BuildContext

/**
 * Base class for every DSL "scope" receiver that accumulates a list of child builders and turns
 * them into an immutable list of built schemas — `TileSchemaBuilderScope`/`EventSchemaBuilderScope`
 * both derive from this. The DSL's trailing-lambda blocks (`Column { SimpleText(...); Button(...) }`,
 * an event's `events = { ... }`) run against one of these: each nested DSL call constructs its own
 * builder and registers it via [addBuilder], and the enclosing call finishes by invoking [build] to
 * collect every registered builder's output.
 *
 * @param Model the schema type this scope's builders produce (`TileSchema`/`EventSchema`).
 * @param Builder the [GenericBuilder] subtype that produces [Model].
 */
abstract class GenericBuilderScope<Model, Builder : GenericBuilder<Model>> {

    private val builders = mutableListOf<Builder>()

    /**
     * Builds every registered [Builder] in registration order, restoring each one's own
     * [GenericBuilder.compositionLocals] snapshot (via [BuildContext.with]) for the duration of its
     * own [GenericBuilder.build] call.
     *
     * @return the built schemas, in the order their builders were registered.
     */
    fun build(): SerializableImmutableList<Model> = builders.immutableMapTo { builder ->
        BuildContext.with(builder.compositionLocals) { builder.build() }
    }

    /**
     * Registers [builder] as a child of this scope, snapshotting the [CompositionLocal] values
     * currently active (via [BuildContext.get]) into [GenericBuilder.compositionLocals] — captured
     * *now*, at DSL-call time, not later when [build] actually invokes it. Called once per nested
     * DSL call (`SimpleText(...)`, `SendNetworkRequest(...)`, etc.) by each such call's own top-level
     * builder function.
     *
     * @param builder the builder to register.
     */
    fun addBuilder(builder: Builder) {
        builders.add(builder.apply { compositionLocals = BuildContext.get() })
    }
}