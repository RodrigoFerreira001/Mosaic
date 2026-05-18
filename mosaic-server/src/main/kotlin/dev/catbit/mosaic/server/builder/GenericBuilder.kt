package dev.catbit.mosaic.server.builder

import dev.catbit.mosaic.server.builder.composition_local.CompositionLocal
import dev.catbit.mosaic.server.builder.composition_local.ValueProvider
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.graph.GraphEntryBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.menu.MenuItemSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.navigation.NavigationBarItemSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.navigation.NavigationRailItemSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.navigation.NestedNavigationGraphEntryBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.navigation.TabItemSchemaBuilderScope
import kotlin.reflect.KClass

abstract class GenericBuilder<out T> {
    internal var compositionLocals = emptyMap<CompositionLocal<*>, ValueProvider<*>>()

    operator fun TileSchemaBuilderScope.Companion.invoke() = TileSchemaBuilderScope(compositionLocals)
    operator fun EventSchemaBuilderScope.Companion.invoke() = EventSchemaBuilderScope(compositionLocals)
    operator fun StyleSchemaBuilderScope.Companion.invoke() = StyleSchemaBuilderScope(compositionLocals)
    operator fun GraphEntryBuilderScope.Companion.invoke() = GraphEntryBuilderScope(compositionLocals)

    abstract fun build(): T
}