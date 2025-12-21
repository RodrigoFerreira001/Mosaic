package dev.catbit.mosaic.client.domain.base

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val Dispatchers.IO: CoroutineContext
    get() = Dispatchers.Main