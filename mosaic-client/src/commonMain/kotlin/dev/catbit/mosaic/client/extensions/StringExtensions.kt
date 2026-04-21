package dev.catbit.mosaic.client.extensions

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

fun String?.textOrNull(centered: Boolean = false): (@Composable () -> Unit)? =
    this?.let { { Text(this, textAlign = if (centered) TextAlign.Center else TextAlign.Start) } }