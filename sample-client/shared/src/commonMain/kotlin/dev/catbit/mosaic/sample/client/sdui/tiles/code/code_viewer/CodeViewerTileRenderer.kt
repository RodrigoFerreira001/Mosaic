package dev.catbit.mosaic.sample.client.sdui.tiles.code.code_viewer

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeBlock
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeFence
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.sample.core.schemas.tiles.code.CodeViewerTileSchema
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxLanguage
import dev.snipme.highlights.model.SyntaxThemes

private fun CodeViewerTileSchema.Language.toSyntaxLanguage(): SyntaxLanguage = when (this) {
    CodeViewerTileSchema.Language.DEFAULT -> SyntaxLanguage.DEFAULT
    CodeViewerTileSchema.Language.C -> SyntaxLanguage.C
    CodeViewerTileSchema.Language.CPP -> SyntaxLanguage.CPP
    CodeViewerTileSchema.Language.DART -> SyntaxLanguage.DART
    CodeViewerTileSchema.Language.JAVA -> SyntaxLanguage.JAVA
    CodeViewerTileSchema.Language.KOTLIN -> SyntaxLanguage.KOTLIN
    CodeViewerTileSchema.Language.RUST -> SyntaxLanguage.RUST
    CodeViewerTileSchema.Language.CSHARP -> SyntaxLanguage.CSHARP
    CodeViewerTileSchema.Language.COFFEESCRIPT -> SyntaxLanguage.COFFEESCRIPT
    CodeViewerTileSchema.Language.JAVASCRIPT -> SyntaxLanguage.JAVASCRIPT
    CodeViewerTileSchema.Language.PERL -> SyntaxLanguage.PERL
    CodeViewerTileSchema.Language.PYTHON -> SyntaxLanguage.PYTHON
    CodeViewerTileSchema.Language.RUBY -> SyntaxLanguage.RUBY
    CodeViewerTileSchema.Language.SHELL -> SyntaxLanguage.SHELL
    CodeViewerTileSchema.Language.SWIFT -> SyntaxLanguage.SWIFT
    CodeViewerTileSchema.Language.TYPESCRIPT -> SyntaxLanguage.TYPESCRIPT
    CodeViewerTileSchema.Language.GO -> SyntaxLanguage.GO
    CodeViewerTileSchema.Language.PHP -> SyntaxLanguage.PHP
}

private fun CodeViewerTileSchema.Theme.highlightsKey(): String = when (this) {
    CodeViewerTileSchema.Theme.DARCULA -> "darcula"
    CodeViewerTileSchema.Theme.MONOKAI -> "monokai"
    CodeViewerTileSchema.Theme.NOTEPAD -> "notepad"
    CodeViewerTileSchema.Theme.MATRIX -> "matrix"
    CodeViewerTileSchema.Theme.PASTEL -> "pastel"
    CodeViewerTileSchema.Theme.ATOM_ONE -> "atomone"
}

object CodeViewerTileRenderer : TileRenderer<CodeViewerTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: CodeViewerTileSchema,
    ) {
        with(tileSchema) {
            val isDarkTheme = isSystemInDarkTheme()

            val highlightsBuilder = remember(language, theme, isDarkTheme) {
                Highlights.Builder()
                    .language(language.toSyntaxLanguage())
                    .theme(
                        SyntaxThemes.getByName(theme.highlightsKey(), darkMode = isDarkTheme)
                            ?: SyntaxThemes.default(isDarkTheme)
                    )
            }

            val fenceLanguage = language.name.lowercase()

            SelectionContainer {
                Markdown(
                    content = "```$fenceLanguage\n$code\n```",
                    modifier = Modifier
                        .visible(isVisible())
                        .styledWith(style),
                    colors = markdownColor(),
                    typography = markdownTypography(),
                    components = markdownComponents(
                        codeBlock = {
                            MarkdownHighlightedCodeBlock(
                                content = it.content,
                                node = it.node,
                                highlightsBuilder = highlightsBuilder,
                            )
                        },
                        codeFence = {
                            MarkdownHighlightedCodeFence(
                                content = it.content,
                                node = it.node,
                                highlightsBuilder = highlightsBuilder,
                            )
                        }
                    )
                )
            }
        }
    }
}
