package dev.catbit.mosaic.core.domain.base

import kotlinx.coroutines.IO
import kotlinx.coroutines.Dispatchers as KotlinDispatchers
import kotlin.coroutines.CoroutineContext

actual val KotlinDispatchers.IO: CoroutineContext
    get() = KotlinDispatchers.IO