package dev.catbit.mosaic.server.builder.tile

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.tile.builders.menu.MenuItemSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.navigation.AdaptiveNavigationEntryBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.navigation.AdaptiveNavigationTileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.builders.navigation.NavigationBarItemSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.navigation.NavigationRailItemSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.navigation.NestedNavigationGraphEntryBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.navigation.TabItemSchemaBuilderScope

abstract class TileSchemaBuilder<out T : TileSchema> : GenericBuilder<T>() {
    operator fun MenuItemSchemaBuilderScope.Companion.invoke() = MenuItemSchemaBuilderScope(compositionLocals)
    operator fun NavigationBarItemSchemaBuilderScope.Companion.invoke() = NavigationBarItemSchemaBuilderScope(compositionLocals)
    operator fun NavigationRailItemSchemaBuilderScope.Companion.invoke() = NavigationRailItemSchemaBuilderScope(compositionLocals)
    operator fun NestedNavigationGraphEntryBuilderScope.Companion.invoke() = NestedNavigationGraphEntryBuilderScope(compositionLocals)
    operator fun TabItemSchemaBuilderScope.Companion.invoke() = TabItemSchemaBuilderScope(compositionLocals)
    operator fun AdaptiveNavigationEntryBuilderScope.Companion.invoke() = AdaptiveNavigationEntryBuilderScope(compositionLocals)
}

fun visible() = TileSchema.Visibility.VISIBLE
fun invisible() = TileSchema.Visibility.INVISIBLE
fun gone() = TileSchema.Visibility.GONE