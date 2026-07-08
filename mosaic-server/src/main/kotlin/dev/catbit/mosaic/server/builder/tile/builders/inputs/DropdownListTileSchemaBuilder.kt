package dev.catbit.mosaic.server.builder.tile.builders.inputs

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.DropdownListTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import kotlinx.collections.immutable.toImmutableList

internal class DropdownListTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val options: List<DropdownListTileSchema.SelectOption>,
    private val selectedOptionId: String,
    private val enabled: Boolean,
    private val kind: DropdownListTileSchema.Kind,
    private val supportingText: String?,
    private val state: DropdownListTileSchema.State,
) : TileSchemaBuilder<DropdownListTileSchema>() {

    override fun build(): DropdownListTileSchema {

        require(options.any {it.id == selectedOptionId}) {
          "The list of SelectOption must contains an option with id equals to $selectedOptionId"
        }

        return DropdownListTileSchema(
            id = id,
            events = EventSchemaBuilderScope().apply(events).build(),
            style = StyleSchemaBuilderScope().apply(style).buildStyle(),
            searchableTerms = searchableTerms?.toImmutableList(),
            visibility = visibility,
            options = options.toImmutableList(),
            expanded = false,
            selectedOptionId = selectedOptionId,
            enabled = enabled,
            kind = kind,
            supportingText = supportingText,
            state = state,
        )
    }
}

fun TileSchemaBuilderScope.DropdownList(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    searchableTerms: List<String>? = null,
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    options: List<DropdownListTileSchema.SelectOption>,
    selectedOptionId: String,
    enabled: Boolean = true,
    kind: DropdownListTileSchema.Kind = DropdownListTileSchema.Kind.OUTLINED,
    supportingText: String? = null,
    state: DropdownListTileSchema.State = normalDropdownList(),
) {
    addBuilder(
        DropdownListTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            options = options,
            selectedOptionId = selectedOptionId,
            enabled = enabled,
            kind = kind,
            supportingText = supportingText,
            state = state,
        )
    )
}

fun selectOption(id: String, label: String) = DropdownListTileSchema.SelectOption(id = id, label = label)

fun filledDropdownList() = DropdownListTileSchema.Kind.FILLED
fun outlinedDropdownList() = DropdownListTileSchema.Kind.OUTLINED
fun normalDropdownList() = DropdownListTileSchema.State.NORMAL
fun errorDropdownList() = DropdownListTileSchema.State.ERROR
