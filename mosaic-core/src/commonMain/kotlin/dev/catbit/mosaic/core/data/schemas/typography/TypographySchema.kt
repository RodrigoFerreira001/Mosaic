package dev.catbit.mosaic.core.data.schemas.typography

/**
 * Maps to a Material 3 typography token from `MaterialTheme.typography`.
 *
 * Used in tiles that render text (e.g. `SimpleTextTileSchema`, `ButtonTileSchema`) to control
 * font size, weight, and line height according to the Material type scale.
 *
 * **DSL helpers (mosaic-server):**
 * ```kotlin
 * typographyDisplayLarge()   typographyDisplayMedium()   typographyDisplaySmall()
 * typographyHeadlineLarge()  typographyHeadlineMedium()  typographyHeadlineSmall()
 * typographyTitleLarge()     typographyTitleMedium()     typographyTitleSmall()
 * typographyBodyLarge()      typographyBodyMedium()      typographyBodySmall()
 * typographyLabelLarge()     typographyLabelMedium()     typographyLabelSmall()
 * ```
 *
 * **Important — client mapping quirk:** `LABEL_LARGE`, `LABEL_MEDIUM`, and `LABEL_SMALL` are
 * currently mapped to `bodyLarge`, `bodyMedium`, and `bodySmall` respectively in the client
 * extension (`TypographySchemaExtensions`). They do NOT map to the Material label styles.
 */
enum class TypographySchema {
    DISPLAY_LARGE,
    DISPLAY_MEDIUM,
    DISPLAY_SMALL,
    HEADLINE_LARGE,
    HEADLINE_MEDIUM,
    HEADLINE_SMALL,
    TITLE_LARGE,
    TITLE_MEDIUM,
    TITLE_SMALL,
    BODY_LARGE,
    BODY_MEDIUM,
    BODY_SMALL,
    LABEL_LARGE,
    LABEL_MEDIUM,
    LABEL_SMALL
}