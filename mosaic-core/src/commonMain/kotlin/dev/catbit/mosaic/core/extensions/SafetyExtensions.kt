package dev.catbit.mosaic.core.extensions

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

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