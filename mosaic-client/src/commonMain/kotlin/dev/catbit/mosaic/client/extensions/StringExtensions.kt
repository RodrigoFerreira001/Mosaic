package dev.catbit.mosaic.client.extensions

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

fun String?.textOrNull(): (@Composable () -> Unit)? = this?.let { { Text(this) } }