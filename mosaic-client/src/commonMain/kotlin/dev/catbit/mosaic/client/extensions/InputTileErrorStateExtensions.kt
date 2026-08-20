package dev.catbit.mosaic.client.extensions

import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.DatePickerTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.DropdownListTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TimePickerTileSchema

/** Whether this `DatePicker`'s `state` is `ERROR` — used by its renderer to switch the underlying
 * Material field into error styling. */
fun DatePickerTileSchema.hasErrorState() = state == DatePickerTileSchema.State.ERROR

/** Whether this `TimePicker`'s `state` is `ERROR` — used by its renderer to switch the underlying
 * Material field into error styling. */
fun TimePickerTileSchema.hasErrorState() = state == TimePickerTileSchema.State.ERROR

/** Whether this `DropdownList`'s `state` is `ERROR` — used by its renderer to switch the underlying
 * Material field into error styling. */
fun DropdownListTileSchema.hasErrorState() = state == DropdownListTileSchema.State.ERROR
