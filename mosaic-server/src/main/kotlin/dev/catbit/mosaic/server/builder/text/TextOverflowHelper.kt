package dev.catbit.mosaic.server.builder.text

import dev.catbit.mosaic.core.data.schemas.text.TextOverflowSchema

fun clipTextOverflow() = TextOverflowSchema.CLIP
fun ellipsisTextOverflow() = TextOverflowSchema.ELLIPSIS
fun visibleTextOverflow() = TextOverflowSchema.VISIBLE
