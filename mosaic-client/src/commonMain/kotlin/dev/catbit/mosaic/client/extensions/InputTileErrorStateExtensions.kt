package dev.catbit.mosaic.client.extensions

import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.DatePickerTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.DropdownListTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TimePickerTileSchema

fun DatePickerTileSchema.hasErrorState() = state == DatePickerTileSchema.State.ERROR
fun TimePickerTileSchema.hasErrorState() = state == TimePickerTileSchema.State.ERROR
fun DropdownListTileSchema.hasErrorState() = state == DropdownListTileSchema.State.ERROR
