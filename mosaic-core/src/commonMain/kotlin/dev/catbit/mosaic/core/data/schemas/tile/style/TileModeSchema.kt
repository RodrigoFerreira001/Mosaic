package dev.catbit.mosaic.core.data.schemas.tile.style

import kotlinx.serialization.Serializable

/**
 * Mirrors Compose's `TileMode` — how a gradient shader fills the region outside its own bounds.
 *
 * - [CLAMP] — repeats the edge color (default).
 * - [REPEATED] — tiles the gradient horizontally and vertically.
 * - [MIRROR] — tiles the gradient, flipping it on every repetition.
 * - [DECAL] — renders transparent pixels outside the gradient bounds.
 *
 * Only relevant when the gradient does not span the whole tile (e.g. a
 * [BackgroundSchema.HorizontalGradient] with an explicit `endX`).
 */
@Serializable
enum class TileModeSchema {
    CLAMP,
    REPEATED,
    MIRROR,
    DECAL
}
