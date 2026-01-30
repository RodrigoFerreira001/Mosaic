package dev.catbit.mosaic.core.domain.base

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers

actual val Dispatchers.IO: CoroutineContext
    get() = Dispatchers.IO