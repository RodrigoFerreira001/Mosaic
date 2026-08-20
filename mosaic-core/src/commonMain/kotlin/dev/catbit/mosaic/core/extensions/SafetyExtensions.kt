package dev.catbit.mosaic.core.extensions

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Runs [block], routing any thrown [Throwable] to [onError] instead of letting it propagate — the
 * standard try/catch idiom every built-in `EventRunner`'s real work is wrapped in, so a runner's own
 * failure always turns into that event's own `onFailure` trigger rather than crashing the caller.
 *
 * @param onError called with the thrown value, if [block] throws. Defaults to swallowing silently.
 * @param block the work to run, at most once.
 */
@OptIn(ExperimentalContracts::class)
inline fun runSafely(
    onError: (Throwable) -> Unit = {},
    block: () -> Unit,
) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }

    try {
        block()
    } catch (e: Throwable) {
        onError(e)
    }
}