package dev.catbit.mosaic.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.catbit.mosaic.client.material_symbols.MaterialSymbol
import dev.catbit.mosaic.client.material_symbols.MaterialSymbolFontsConfig
import dev.catbit.mosaic.client.material_symbols.MaterialSymbolStyle
import dev.catbit.mosaic.client.material_symbols.MaterialSymbols
import dev.catbit.mosaic.client.ui.theme.MosaicTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialSymbolsPreviewer() {
    Row(
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        var style by remember { mutableStateOf(MaterialSymbolStyle.OUTLINED) }
        var showMenu by remember { mutableStateOf(false) }

        var fill by remember { mutableStateOf(false) }
        var weight by remember { mutableStateOf(400) }
        var grade by remember { mutableStateOf(1) }
        var opticalSize by remember { mutableStateOf(1) }

        Column(
            modifier = Modifier
                .weight(0.2f)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Control(
                modifier = Modifier.fillMaxWidth(),
                title = "Style"
            ) {
                ExposedDropdownMenuBox(
                    expanded = showMenu,
                    onExpandedChange = {
                        showMenu = !showMenu
                    }
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .menuAnchor(
                                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable
                            )
                            .fillMaxWidth(),
                        readOnly = true,
                        value = style.name,
                        onValueChange = {},
                    )
                    ExposedDropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                    ) {
                        MaterialSymbolStyle.entries.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption.name) },
                                onClick = {
                                    style = selectionOption
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            }
            Control(
                modifier = Modifier.fillMaxWidth(),
                title = "Fill"
            ) {
                Switch(
                    checked = fill,
                    onCheckedChange = {
                        fill = it
                    }
                )
            }
            Control(
                modifier = Modifier.fillMaxWidth(),
                title = "Weight"
            ) {
                SliderController(
                    modifier = Modifier.fillMaxWidth(),
                    value = weight,
                    step = 100,
                    min = 100,
                    max = 700,
                    startLabel = "100",
                    endLabel = "700",
                    onValueChange = {
                        weight = it
                    }
                )
            }
            Control(
                modifier = Modifier.fillMaxWidth(),
                title = "Grade"
            ) {
                SliderController(
                    modifier = Modifier.fillMaxWidth(),
                    value = grade,
                    step = 1,
                    min = 0,
                    max = 2,
                    startLabel = "-25 (low)",
                    endLabel = "200 (high emphasis)",
                    onValueChange = {
                        grade = it
                    }
                )
            }
            Control(
                modifier = Modifier.fillMaxWidth(),
                title = "Optical Size"
            ) {
                SliderController(
                    modifier = Modifier.fillMaxWidth(),
                    value = opticalSize,
                    step = 1,
                    min = 0,
                    max = 3,
                    startLabel = "20px",
                    endLabel = "48px",
                    onValueChange = {
                        opticalSize = it
                    }
                )
            }
        }

        val config = remember(fill, weight, grade, opticalSize) {
            MaterialSymbolFontsConfig(
                filled = fill,
                weight = FontWeight(weight),
                grade = when (grade) {
                    0 -> -25
                    1 -> 0
                    else -> 200
                },
                opticalSize = when (opticalSize) {
                    0 -> 20.sp
                    1 -> 24.sp
                    2 -> 40.sp
                    else -> 48.sp
                },
            )
        }

        MosaicTheme(
            materialSymbolFontsConfig = config
        ) {
            LazyVerticalStaggeredGrid(
                modifier = Modifier.weight(1f),
                columns = StaggeredGridCells.Adaptive(48.dp),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(count = MaterialSymbols.entries.size) {
                    MaterialSymbol(
                        iconName = MaterialSymbols.entries[it].name,
                        style = style,
                        size = 48.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun Control(
    modifier: Modifier = Modifier,
    title: String,
    controller: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        controller()
    }
}

@Composable
private fun SliderController(
    modifier: Modifier = Modifier,
    value: Int,
    step: Int,
    min: Int,
    max: Int,
    startLabel: String,
    endLabel: String,
    onValueChange: (Int) -> Unit
) {
    Column(
        modifier = modifier,
    ) {
        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = value.toFloat(),
            valueRange = min.toFloat()..max.toFloat(),
            onValueChange = {
                onValueChange(it.toInt())
            },
            steps = ((max - min) / step) - 1,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = startLabel, style = MaterialTheme.typography.bodySmall)
            Text(text = endLabel, style = MaterialTheme.typography.bodySmall)
        }
    }
}