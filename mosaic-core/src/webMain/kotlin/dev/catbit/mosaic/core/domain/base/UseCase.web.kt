package dev.catbit.mosaic.core.domain.base

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val Dispatchers.IO: CoroutineContext
    get() = Dispatchers.Default