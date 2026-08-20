package dev.catbit.mosaic.client.extensions

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import dev.catbit.mosaic.core.data.schemas.typography.TypographySchema

/** Converts the wire-format [TypographySchema] into the matching `TextStyle` off
 * `MaterialTheme.typography` — used by `SimpleText`'s `typography` to resolve the base text style,
 * which every other styling field on `SimpleText` then overrides one property of. */
@Composable
fun TypographySchema.toTextStyle() = when (this) {
    TypographySchema.DISPLAY_LARGE -> MaterialTheme.typography.displayLarge
    TypographySchema.DISPLAY_MEDIUM -> MaterialTheme.typography.displayMedium
    TypographySchema.DISPLAY_SMALL -> MaterialTheme.typography.displaySmall
    TypographySchema.HEADLINE_LARGE -> MaterialTheme.typography.headlineLarge
    TypographySchema.HEADLINE_MEDIUM -> MaterialTheme.typography.headlineMedium
    TypographySchema.HEADLINE_SMALL -> MaterialTheme.typography.headlineSmall
    TypographySchema.TITLE_LARGE -> MaterialTheme.typography.titleLarge
    TypographySchema.TITLE_MEDIUM -> MaterialTheme.typography.titleMedium
    TypographySchema.TITLE_SMALL -> MaterialTheme.typography.titleSmall
    TypographySchema.BODY_LARGE -> MaterialTheme.typography.bodyLarge
    TypographySchema.BODY_MEDIUM -> MaterialTheme.typography.bodyMedium
    TypographySchema.BODY_SMALL -> MaterialTheme.typography.bodySmall
    TypographySchema.LABEL_LARGE -> MaterialTheme.typography.labelLarge
    TypographySchema.LABEL_MEDIUM -> MaterialTheme.typography.labelMedium
    TypographySchema.LABEL_SMALL -> MaterialTheme.typography.labelSmall
}
