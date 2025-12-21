package dev.catbit.mosaic.client.domain.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

expect val Dispatchers.IO: CoroutineContext

abstract class UseCase<ReturnType, Params> {
    abstract suspend fun execute(params: Params): Result<ReturnType>

    suspend operator fun invoke(params: Params) = withContext(Dispatchers.IO) {
        execute(params)
    }
}

suspend operator fun <ReturnType> UseCase<ReturnType, Unit>.invoke() = invoke(Unit)