package dev.catbit.mosaic.core.domain.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/** Platform-specific IO dispatcher (`expect`/`actual`) — Kotlin/JVM-family targets have their own
 * `Dispatchers.IO`; other targets (e.g. JS/Wasm, which are single-threaded) provide a fallback. Used
 * by [UseCase.invoke] so every use case runs off the caller's own dispatcher without each one
 * needing its own `withContext` call. */
expect val Dispatchers.IO: CoroutineContext

/**
 * Base class for every use case (`GetScreenUseCase`, `SendNetworkRequestUseCase`,
 * the data-source ones, etc.) — a single-method, `Result`-returning unit of domain logic, always run
 * on [Dispatchers.IO] via [invoke] rather than the caller's own dispatcher.
 *
 * @param ReturnType the type this use case produces on success.
 * @param Params the input this use case needs — a dedicated `Params` data class by convention when
 * more than one value is needed, `Unit` when none is.
 */
abstract class UseCase<ReturnType, Params> {
    /** The use case's real logic — implement this, never call it directly (call [invoke] instead,
     * which adds the [Dispatchers.IO] dispatch). */
    abstract suspend fun execute(params: Params): Result<ReturnType>

    /** Runs [execute] on [Dispatchers.IO] — the only sanctioned way to run a [UseCase]. */
    suspend operator fun invoke(params: Params) = withContext(Dispatchers.IO) {
        execute(params)
    }
}

/** [UseCase.invoke] overload for a use case whose `Params` is `Unit` — lets a parameterless use case
 * be called as `useCase()` instead of `useCase(Unit)`. */
suspend operator fun <ReturnType> UseCase<ReturnType, Unit>.invoke() = invoke(Unit)