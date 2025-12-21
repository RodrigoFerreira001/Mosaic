package dev.catbit.mosaic.client.domain.base

import kotlinx.coroutines.IO
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val Dispatchers.IO: CoroutineContext
    get() = Dispatchers.IO