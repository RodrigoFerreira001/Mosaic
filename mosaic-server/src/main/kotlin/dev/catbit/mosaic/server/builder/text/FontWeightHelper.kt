package dev.catbit.mosaic.server.builder.text

import dev.catbit.mosaic.core.data.schemas.text.FontWeightSchema

/** Thin font weight (100) — the lightest available stroke. */
fun thinFontWeight() = FontWeightSchema.THIN

/** Extra-light font weight (200). */
fun extraLightFontWeight() = FontWeightSchema.EXTRA_LIGHT

/** Light font weight (300). */
fun lightFontWeight() = FontWeightSchema.LIGHT

/** Normal/regular font weight (400) — the default text weight. */
fun normalFontWeight() = FontWeightSchema.NORMAL

/** Medium font weight (500). */
fun mediumFontWeight() = FontWeightSchema.MEDIUM

/** Semi-bold font weight (600). */
fun semiBoldFontWeight() = FontWeightSchema.SEMI_BOLD

/** Bold font weight (700). */
fun boldFontWeight() = FontWeightSchema.BOLD

/** Extra-bold font weight (800). */
fun extraBoldFontWeight() = FontWeightSchema.EXTRA_BOLD

/** Black font weight (900) — the heaviest available stroke. */
fun blackFontWeight() = FontWeightSchema.BLACK
