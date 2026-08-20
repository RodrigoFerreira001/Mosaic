package dev.catbit.mosaic.core.extensions

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Runs [scopeBlock] with [receiver] as its own receiver, only if [receiver] is non-null — a
 * `let`-style scope function whose Kotlin contract makes the smart-cast explicit at the call site.
 * Used, for instance, by `TextFieldTileSchema.keyboardOptions()` to build a `KeyboardOptions` only
 * when the schema's own nested `keyboardOptions` field is non-null.
 *
 * @param receiver the value to check.
 * @param scopeBlock run with [receiver] as its receiver, at most once.
 * @return the result of [scopeBlock], or `null` if [receiver] was `null`.
 */
@OptIn(ExperimentalContracts::class)
inline fun <reified T, R> withNotNull(receiver: T?, scopeBlock: T.() -> R): R? {
    contract {
        callsInPlace(scopeBlock, InvocationKind.AT_MOST_ONCE)
        returnsNotNull() implies (receiver is T)
    }
    return receiver?.scopeBlock()
}