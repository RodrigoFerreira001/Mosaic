package dev.catbit.mosaic.server.builder.text

import dev.catbit.mosaic.core.data.schemas.text.TextOverflowSchema

/** Cuts off overflowing text at the tile's bounds with no visual indicator. */
fun clipTextOverflow() = TextOverflowSchema.CLIP

/** Truncates overflowing text and appends an ellipsis ("…") at the cut point. */
fun ellipsisTextOverflow() = TextOverflowSchema.ELLIPSIS

/** Lets overflowing text spill outside the tile's bounds instead of being cut off. */
fun visibleTextOverflow() = TextOverflowSchema.VISIBLE
